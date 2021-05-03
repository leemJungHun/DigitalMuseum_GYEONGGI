package kr.rowan.digital_museum_gyeonggi.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.JsonArray
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.adapter.ItemAdapter
import kr.rowan.digital_museum_gyeonggi.adapter.decoration.MainStack
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentItemBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
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
    private var uuid = ""

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    binding.rvSeekBar.max = itemList.size
                    itemAdapter = ItemAdapter(activity, itemList)
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
        when(view.id){
            R.id.backImgView -> {
                if (arguments!!.getBoolean("direct", true)) {
                    val fragment: Fragment =
                        when (arguments!!.getString("fragmentName").toString()) {
                            "museum" -> {
                                MuseumFragment()
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
                    activity.setStartFragment(MuseumCategoryFragment(), true, result)
                }
            }
            R.id.homeImgView -> {
                activity.setStartFragment(SubFragment(), true, null)
            }
        }
    }
}