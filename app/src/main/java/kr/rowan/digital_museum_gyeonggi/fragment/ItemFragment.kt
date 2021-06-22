package kr.rowan.digital_museum_gyeonggi.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.JsonArray
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.adapter.ItemAdapter
import kr.rowan.digital_museum_gyeonggi.adapter.YearAdapter
import kr.rowan.digital_museum_gyeonggi.adapter.decoration.MainStack
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentItemBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import kr.rowan.digital_museum_gyeonggi.network.vo.ItemVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private lateinit var activity: MainActivity
    private lateinit var itemAdapter: ItemAdapter
    private val itemList = ArrayList<ItemVO>()
    private var categoryList = ArrayList<CategoryVO>()
    private var uuid = ""
    private var isItemClick = false
    private var itemTouch = false

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
        binding = FragmentItemBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@ItemFragment
            fragment = this@ItemFragment
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!!.getString("fragmentName") != "album") {
            binding.yearListView.visibility = View.GONE
            binding.yearListBackImgView.visibility = View.GONE
        } else {
            categoryList = arguments!!.getParcelableArrayList("categoryList")!!
            Log.e("categoryList", "$categoryList ")
            binding.titleTxtView.text = "졸업 앨범"
            val mLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            val mAdapter = YearAdapter(
                activity
            )
            binding.yearListView.apply {
                layoutManager = mLayoutManager
                adapter = mAdapter

                setOnScrollChangeListener { _, _, _, _, _ -> itemTouch = false }

                addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        if (e.action == MotionEvent.ACTION_UP && !isItemClick && itemTouch) {
                            isItemClick = true
                            val child = rv.findChildViewUnder(e.x, e.y)
                            if(child !=null){
                                val position = rv.getChildAdapterPosition(child)
                                Log.e("child uuid", mAdapter.getUuid(position))
                                Log.e("child position", "$position")
                                httpRequestService!!.getItems(UuidRequest(mAdapter.getUuid(position)))!!
                                    .enqueue(object : Callback<JsonArray?> {
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
                                                val newItemList = ArrayList<ItemVO>()
                                                for (array in jsonArray!!) {
                                                    val itemVO: ItemVO =
                                                        gson.fromJson(array, ItemVO::class.java)
                                                    newItemList.add(itemVO)
                                                    Log.e("itemVO.title:", "${itemVO.title}")
                                                }
                                                Log.e("itemList.size", "${newItemList.size}")
                                                binding.rvSeekBar.max = itemList.size - 1
                                                itemAdapter.update(newItemList)
                                                mAdapter.setText(position)
                                                binding.assignmentListView.apply {
                                                    adapter = itemAdapter
                                                }
                                                isItemClick = false
                                            } else {
                                                isItemClick = false
                                            }
                                        }

                                        override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                                            Log.e("Failed uuid:", "${t.message}")
                                            isItemClick = false
                                        }

                                    })
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
            }
            mAdapter.update(
                categoryList,
                arguments!!.getString("name")!!
            )
        }
        httpRequestService!!.getItems(UuidRequest(uuid))!!.enqueue(object : Callback<JsonArray?> {
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
                        Log.e("itemVO.title:", "${itemVO.title}")
                    }
                    Log.e("itemList.size", "${itemList.size}")
                    binding.rvSeekBar.max = itemList.size - 1
                    itemAdapter = ItemAdapter(
                        activity,
                        itemList,
                        arguments!!.getString("fragmentName").toString()
                    )
                    binding.assignmentListView.apply {
                        //isUserInputEnabled = false
                        setPageTransformer(MainStack(activity))
                        offscreenPageLimit = 3
                        adapter = itemAdapter
                        val recyclerView = (getChildAt(0) as RecyclerView)
                        recyclerView.overScrollMode =
                            RecyclerView.OVER_SCROLL_NEVER
                        registerOnPageChangeCallback(object :
                            ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                Log.e("pagePosition", "$position")
                                binding.rvSeekBar.progress = position
                            }

                            override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                            ) {
                            }

                            override fun onPageScrollStateChanged(state: Int) {
                            }
                        })

                        binding.rvSeekBar.setOnSeekBarChangeListener(object :
                            OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                                binding.assignmentListView.currentItem = i
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar) {
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar) {
                            }
                        })

                        /*binding.view.setOnClickListener { }
                        binding.assignmentIndicator.setViewPager2(binding.assignmentListView)*/
                    }
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
                if (arguments!!.getBoolean("direct", true)) {
                    val fragment: Fragment =
                        when (arguments!!.getString("fragmentName").toString()) {
                            "museum" -> {
                                MuseumFragment()
                            }
                            "seniors", "head" -> {
                                SeniorsFragment()
                            }
                            else -> {
                                SubFragment()
                            }
                        }
                    result.putString("uuid", arguments!!.getString("preUuid"))
                    activity.setStartFragment(fragment, true, result)
                } else {
                    result.putString("name", arguments!!.getString("preName"))
                    result.putString("uuid", arguments!!.getString("preUuid"))
                    result.putString("preUuid", arguments!!.getString("firstUuid"))
                    val fragment: Fragment =
                        when (arguments!!.getString("fragmentName").toString()) {
                            "museum" -> {
                                MuseumCategoryFragment()
                            }
                            "album" -> {
                                AlbumCategoryFragment()
                            }
                            else -> {
                                SubFragment()
                            }
                        }
                    activity.setStartFragment(fragment, true, result)
                }
            }
            R.id.homeImgView -> {
                activity.setStartFragment(SubFragment(), true, null)
            }
        }
    }
}