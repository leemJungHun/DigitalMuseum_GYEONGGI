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
import com.google.gson.JsonArray
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentAlbumBinding
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentDigitalMuseumBinding
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentSeniorsBinding
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
import kotlin.collections.ArrayList

class SeniorsFragment : Fragment() {
    private lateinit var binding: FragmentSeniorsBinding
    private lateinit var activity: MainActivity
    private lateinit var categories: ArrayList<AppCompatImageView>
    private val categoryList = ArrayList<CategoryVO>()
    private var httpRequestService: HttpRequestService? = null
    private var uuid = ""
    private val itemList = ArrayList<ItemVO>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        binding = FragmentSeniorsBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@SeniorsFragment
            fragment = this@SeniorsFragment
        }
        uuid = arguments!!.getString("uuid").toString()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(HttpRequestService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        httpRequestService = retrofit.create(HttpRequestService::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories = ArrayList()
        categories.add(binding.clubMasterImgView)
        categories.add(binding.headmasterImgView)
        categories.add(binding.proudSeniorsImgView)
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
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                Log.e("Failed uuid:", "${t.message}")
            }

        })
    }

    fun onClick(view: View) {
        val result = Bundle()
        Log.e("onClick:", "onClick")
        when(view.id){
            R.id.backImgView -> {
                activity.setStartFragment(SubFragment(),true,null)
            }
            binding.headmasterImgView.id, binding.clubMasterImgView.id -> {
                val vo = view.getTag(R.string.category_vo) as CategoryVO
                result.putBoolean("direct", true)
                if(view.id==binding.clubMasterImgView.id){
                    result.putString("fragmentName", "seniors")
                }else{
                    result.putString("fragmentName", "head")
                }
                result.putString("name", vo.name)
                result.putString("uuid", vo.uuid)
                result.putString("preUuid", uuid)
                activity.setStartFragment(
                    ItemFragment(),
                    false,
                    result
                )
            }
            binding.proudSeniorsImgView.id -> {
                val vo = view.getTag(R.string.category_vo) as CategoryVO
                result.putBoolean("direct", true)
                result.putString("name", vo.name)
                result.putString("uuid", vo.uuid)
                result.putString("preUuid", uuid)
                activity.setStartFragment(
                    ProudSeniorsFragment(),
                    false,
                    result
                )
            }
        }
    }
}