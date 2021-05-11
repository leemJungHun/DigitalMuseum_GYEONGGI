package kr.rowan.digital_museum_gyeonggi.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.JsonArray
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.VisiblePositionChangeListener
import kr.rowan.digital_museum_gyeonggi.adapter.ItemAdapter
import kr.rowan.digital_museum_gyeonggi.adapter.ProudItemAdapter
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentProudSeniorsBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
import kr.rowan.digital_museum_gyeonggi.network.vo.ItemVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class ProudSeniorsFragment : Fragment() {
    private lateinit var binding: FragmentProudSeniorsBinding
    private lateinit var activity: MainActivity
    private lateinit var itemAdapter: ItemAdapter
    private val itemList = ArrayList<ItemVO>()
    private var uuid = ""
    var isSeekBar = false
    var prePosition = 0
    private var itemTouch = true

    private var httpRequestService: HttpRequestService? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        container?.removeAllViews()
        binding = FragmentProudSeniorsBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@ProudSeniorsFragment
            fragment = this@ProudSeniorsFragment
        }
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(HttpRequestService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        httpRequestService = retrofit.create(HttpRequestService::class.java)
        uuid = arguments!!.getString("uuid").toString()
        binding.titleTxtView.text = arguments!!.getString("name")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        httpRequestService!!.getItems(UuidRequest(uuid))!!.enqueue(object : Callback<JsonArray?> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<JsonArray?>,
                response: Response<JsonArray?>
            ) {
                Log.e("httpRequestService:", "OK")
                Log.e("response:", "$response ")
                if (response.body() != null) {
                    val gson = Gson()
                    Log.e("Success response:", "${response.body()}")
                    val jsonArray = response.body()
                    for (array in jsonArray!!) {

                        val itemVO: ItemVO =
                            gson.fromJson(array, ItemVO::class.java)
                        itemList.add(itemVO)
                        Log.e("itemVO.etc:", "${itemVO.etc}")
                    }
                    Glide.with(activity).load(HttpRequestService.URL + itemList[0].images!![0].path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f).into(binding.selectImgView)
                    val startTextList = itemList[0].etc!!.split("\\n")
                    var startContent = "-"
                    for(i in startTextList.indices){
                        if(i>0){
                            if(i==1){
                                binding.startYearTxtView.text = startTextList[i].split(":")[0]
                                startContent += startTextList[i].split(":")[1]
                            }
                            startContent += "\n- ${startTextList[i]}"
                        }
                    }
                    binding.contentTxtView.text = startContent
                    binding.nameTxtView.text = itemList[0].title
                    Log.e("itemList.size", "${itemList.size}")
                    binding.rvSeekBar.max = itemList.size / 3

                    val mLayoutManager =
                        GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
                    val mAdapter = ProudItemAdapter(activity)
                    binding.seniorsListView.apply {
                        layoutManager = mLayoutManager
                        adapter = mAdapter
                        setOnScrollChangeListener { _, _, _, _, _ ->
                            itemTouch = false }
                        addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                                if (e.action == MotionEvent.ACTION_UP && itemTouch) {
                                    val child = rv.findChildViewUnder(e.x, e.y)
                                    if(child !=null){
                                        val position = rv.getChildAdapterPosition(child)
                                        val item = mAdapter.getItem(position)
                                        Glide.with(activity).load(HttpRequestService.URL + item.images!![0].path)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .thumbnail(0.5f).into(binding.selectImgView)
                                        val textList = item.etc!!.split("\\n")
                                        var content = "-"
                                        for(i in textList.indices){
                                            if(i>0){
                                                if(i==1){
                                                    binding.startYearTxtView.text = textList[i].split(":")[0]
                                                    content += textList[i].split(":")[1]
                                                }
                                                content += "\n- ${textList[i]}"
                                            }
                                        }
                                        binding.contentTxtView.text = content
                                        binding.nameTxtView.text = item.title
                                    }

                                }
                                else if (MotionEvent.ACTION_DOWN == e.action) {
                                    itemTouch = true
                                }
                                return false
                            }

                            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                            }

                            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                            }

                        })
                        addOnScrollListener(
                            VisiblePositionChangeListener(
                                mLayoutManager,
                                object : VisiblePositionChangeListener.OnChangeListener {
                                    override fun onFirstVisiblePositionChanged(position: Int) {
                                        Log.d("현재보이는 포지션", " $position")
                                        if (!isSeekBar) {
                                            binding.rvSeekBar.progress = (position) / 2
                                        }
                                    }

                                    override fun onLastVisiblePositionChanged(position: Int) {
                                    }

                                    override fun onFirstInvisiblePositionChanged(position: Int) {
                                        Log.d("가려지는 포지션", " $position")
                                        if (!isSeekBar) {
                                            binding.rvSeekBar.progress = (position + 2) / 2
                                        }
                                    }

                                    override fun onLastInvisiblePositionChanged(position: Int) {
                                    }

                                })
                        )
                    }
                    mAdapter.update(itemList)
                    binding.rvSeekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                            if (isSeekBar) {
                                if (prePosition < i) {
                                    binding.seniorsListView.smoothScrollToPosition(i * 2 + 8)
                                    Log.e("scrollToPosition", "${i * 2} ")
                                } else {
                                    binding.seniorsListView.smoothScrollToPosition(i * 2)
                                }
                                prePosition = i
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {
                            isSeekBar = true
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar) {
                            isSeekBar = false
                        }
                    })
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                Log.e("Failed uuid:", "${t.message}")
            }

        })

    }

    fun onClick(view: View) {
        val result = Bundle()
        when (view.id) {
            R.id.backImgView -> {
                result.putString("uuid", arguments!!.getString("preUuid"))
                activity.setStartFragment(SeniorsFragment(), true, result)
            }
            R.id.homeImgView -> {
                activity.setStartFragment(SubFragment(), true, null)
            }
        }
    }
}