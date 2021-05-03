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
import kr.rowan.digital_museum_gyeonggi.network.vo.ItemVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SeniorsFragment : Fragment() {
    private lateinit var binding: FragmentSeniorsBinding
    private lateinit var activity: MainActivity
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
    }

    fun onClick(view: View) {
        val result = Bundle()
        Log.e("onClick:", "onClick")
        when(view.id){
            R.id.backImgView -> {
                activity.setStartFragment(SubFragment(),true,null)
            }
            else -> {
                Log.e("onClick:", "else")
                httpRequestService!!.getItems(UuidRequest(uuid))!!.enqueue(object : Callback<JsonArray?> {
                    override fun onResponse(
                        call: Call<JsonArray?>,
                        response: Response<JsonArray?>
                    ) {
                        if (response.body() != null) {
                            val gson = Gson()
                            Log.e("Success response:", "${response.body()}")
                            val jsonArray = response.body()
                            for(array in jsonArray!!){
                                val itemVO: ItemVO =
                                    gson.fromJson(array, ItemVO::class.java)
                                itemList.add(itemVO)
                                Log.e("itemVO.title:", "${itemVO.title}")
                            }
                            var fragment = Fragment()
                            when (view.id) {
                                binding.headmasterImgView.id -> {
                                    result.putBoolean("direct", true)
                                    fragment = ItemFragment()
                                    result.putParcelableArrayList("itemList", itemList)
                                }
                            }
                            activity.setStartFragment(
                                fragment,
                                false,
                                result
                            )
                        }
                    }

                    override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                        Log.e("Failed uuid:", "${t.message}")
                    }

                })
            }
        }
    }
}