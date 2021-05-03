package kr.rowan.digital_museum_gyeonggi.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentDigitalMuseumBinding
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

class MuseumFragment : Fragment() {
    private lateinit var binding: FragmentDigitalMuseumBinding
    private lateinit var activity: MainActivity
    private lateinit var categories: ArrayList<AppCompatImageView>
    private var httpRequestService: HttpRequestService? = null
    private var uuid = ""
    private val itemList = ArrayList<ItemVO>()
    private val categoryList = ArrayList<CategoryVO>()
    private var categoryOff = intArrayOf(
        R.drawable.btn_books,
        R.drawable.btn_document,
        R.drawable.btn_teachware,
        R.drawable.btn_clothes,
        R.drawable.btn_history,
        R.drawable.btn_instrument,
        R.drawable.btn_etc,
        R.drawable.btn_alumni
    )
    private var categoryOn = intArrayOf(
        R.drawable.btn_book_hover,
        R.drawable.btn_document_hover,
        R.drawable.btn_teachware_hover,
        R.drawable.btn_clothes_hover,
        R.drawable.btn_history_hover,
        R.drawable.btn_instrument_hover,
        R.drawable.btn_etc_hover,
        R.drawable.btn_alumni_hover
    )

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
        binding = FragmentDigitalMuseumBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@MuseumFragment
            fragment = this@MuseumFragment
        }
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(HttpRequestService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        httpRequestService = retrofit.create(HttpRequestService::class.java)
        uuid = arguments!!.getString("uuid").toString()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories = ArrayList()
        categories.add(binding.bookImgView)
        categories.add(binding.documentImgView)
        categories.add(binding.teachwareImgView)
        categories.add(binding.clothesImgView)
        categories.add(binding.historyImgView)
        categories.add(binding.instrumentImgView)
        categories.add(binding.etcImgView)
        categories.add(binding.alumniImgView)
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
                    for(array in jsonArray!!){
                        val categoryVO: CategoryVO =
                            gson.fromJson(array, CategoryVO::class.java)
                        categoryList.add(categoryVO)
                        for(category in categories){
                            if(categoryVO.name!! == category.tag.toString()){
                                category.setTag(R.string.category_vo, categoryVO)
                            }
                        }
                    }
                    for(i in 0 until categories.size){
                        categories[i].setOnTouchListener(touch)
                        categories[i].setTag(R.string.category_num_key, i)
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                Log.e("Failed uuid:", "${t.message}")
            }

        })
    }

    @SuppressLint("ClickableViewAccessibility")
    val touch = View.OnTouchListener { view: View?, evt: MotionEvent ->
        val action = evt.action
        if (action == MotionEvent.ACTION_DOWN) {
            if (view != null) {
                val tag = view.getTag(R.string.category_num_key) as Int
                categories[tag].setImageResource(categoryOn[tag])
            }
        }else if(action == MotionEvent.ACTION_UP) {
            Log.e("action","UP")
            if (view != null) {
                Log.e("view","$view  ")
                val tag = view.getTag(R.string.category_num_key) as Int
                categories[tag].setImageResource(categoryOff[tag])
            }
        }
        false
    }

    fun onClick(view: View) {
        val result = Bundle()
        when(view.id){
            R.id.backImgView -> {
                activity.setStartFragment(SubFragment(),true,null)
            }
            else -> {
                Log.e("onClick:", "else")
                val vo = view.getTag(R.string.category_vo) as CategoryVO
                val fragment: Fragment = if(view.id == binding.alumniImgView.id){
                    result.putBoolean("direct", true)
                    result.putString("fragmentName", "museum")
                    ItemFragment()
                }else{
                    MuseumCategoryFragment()
                }
                result.putString("name", vo.name)
                result.putString("uuid", vo.uuid)
                result.putString("preUuid", uuid)
                activity.setStartFragment(
                    fragment,
                    false,
                    result
                )

                /*httpRequestService!!.getItems(UuidRequest(vo.uuid!!))!!.enqueue(object : Callback<JsonArray?> {
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
                            var fragment = Fragment()
                            if(view.id == binding.alumniImgView.id){
                                for(array in jsonArray!!){
                                    val jsonObject = array.asJsonObject
                                    val itemVO: ItemVO =
                                        gson.fromJson(jsonObject, ItemVO::class.java)
                                    itemList.add(itemVO)
                                    Log.e("jsonObject:", "$jsonObject ")
                                    Log.e("itemVO.uuid:", "${itemVO.uuid}")
                                    Log.e("itemVO.title:", "${itemVO.title}")
                                }
                                fragment = ItemFragment()
                                result.putParcelableArrayList("itemList",itemList)
                                result.putString("name", vo.name)
                                result.putString("preUuid", uuid)
                            }else{
                                for(array in jsonArray!!){
                                    val categoryVO: CategoryVO =
                                        gson.fromJson(array, CategoryVO::class.java)
                                    categoryList.add(categoryVO)
                                    Log.e("categoryVO.name:", "${categoryVO.name}")
                                }
                                fragment = MuseumCategoryFragment()
                                result.putParcelableArrayList("categoryList", categoryList)
                                result.putString("name", vo.name)
                                result.putString("preUuid", uuid)
                            }

                        }
                    }

                    override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                        Log.e("Failed uuid:", "${t.message}")
                    }

                })*/
            }
        }
    }
}