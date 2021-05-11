package kr.rowan.digital_museum_gyeonggi.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentSubBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SubFragment : Fragment() {
    private lateinit var binding: FragmentSubBinding
    private lateinit var activity: MainActivity
    private var httpRequestService: HttpRequestService? = null
    private lateinit var categories: ArrayList<AppCompatImageView>
    private val uuidList = ArrayList<String>()
    private var categoryOff = intArrayOf(
        R.drawable.btn_01,
        R.drawable.btn_02,
        R.drawable.btn_03,
        R.drawable.btn_04
    )
    private var categoryOn = intArrayOf(
        R.drawable.btn_01_on,
        R.drawable.btn_02_on,
        R.drawable.btn_03_on,
        R.drawable.btn_04_on
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
        binding = FragmentSubBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@SubFragment
            fragment = this@SubFragment
        }
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(HttpRequestService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        httpRequestService = retrofit.create(HttpRequestService::class.java)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories = ArrayList()
        categories.add(binding.categoryImgView1)
        categories.add(binding.categoryImgView2)
        categories.add(binding.categoryImgView3)
        categories.add(binding.categoryImgView4)



        for(i in 0 until categories.size){
            categories[i].setOnTouchListener(touch)
            categories[i].tag = i
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    val touch = View.OnTouchListener { view: View?, evt: MotionEvent ->
        val action = evt.action
        if (action == MotionEvent.ACTION_DOWN) {
            if (view != null) {
                val tag = view.tag as Int
                categories[tag].setImageResource(categoryOn[tag])
            }
        }else if(action == MotionEvent.ACTION_UP) {
            if (view != null) {
                val tag = view.tag as Int
                categories[tag].setImageResource(categoryOff[tag])
            }
        }
        false
    }

    fun onClick(view: View) {
        val result = Bundle()

        when (view.id) {
            binding.categoryImgView1.id -> {
                activity.setStartFragment(
                    HistoryFragment(),
                    false,
                    result
                )
            }
            else -> {
                httpRequestService!!.getCategory()!!.enqueue(object : Callback<JsonArray?> {
                    override fun onResponse(
                        call: Call<JsonArray?>,
                        response: Response<JsonArray?>
                    ) {
                        if (response.body() != null) {
                            Log.e("Success response:", "${response.body()}")
                            Log.e("Success uuid:", "")
                            //Toast.makeText(context,"${response.body()}",Toast.LENGTH_LONG).show()
                            val jsonArray = response.body()
                            for(array in jsonArray!!){
                                val jsonObject = array.asJsonObject
                                uuidList.add(jsonObject["uuid"].asString)
                                Log.e("uuid:", jsonObject["uuid"].asString)
                            }
                            var fragment = Fragment()
                            when (view.id) {
                                binding.categoryImgView2.id -> {
                                    fragment = MuseumFragment()
                                    result.putString("uuid", uuidList[0])
                                }
                                binding.categoryImgView3.id -> {
                                    fragment = AlbumFragment()
                                    result.putString("uuid", uuidList[1])
                                }
                                binding.categoryImgView4.id -> {
                                    fragment = SeniorsFragment()
                                    result.putString("uuid", uuidList[2])
                                }
                            }
                            activity.setStartFragment(
                                fragment,
                                false,
                                result
                            )
                        }else{
                            Toast.makeText(context,"body null",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                        Log.e("Failed uuid:", "${t.message}")
                        Toast.makeText(context,"${t.message}",Toast.LENGTH_LONG).show()
                    }

                })
            }
           /* binding.categoryImgView2.id -> {
                result.putString("uuid", "Code")
                activity.setStartFragment(
                    MuseumFragment(),
                    false,
                    result
                )
            }
            binding.categoryImgView3.id -> {
                result.putString("uuid", "Code")
                activity.setStartFragment(
                    AlbumFragment(),
                    false,
                    result
                )
            }
            binding.categoryImgView4.id -> {
                result.putString("uuid", "Code")
                activity.setStartFragment(
                    SeniorsFragment(),
                    false,
                    result
                )
            }*/

            /*httpRequestService.getMediumList(LCode).enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    if (response.isSuccessful && response.body() != null) {
                        val gson = Gson()
                        if (response.body()!!["data"].toString() != "null") {
                            val jsonObject = response.body()!!.getAsJsonObject("data")
                            val jsonArray = jsonObject.getAsJsonArray("mediums")
                            for (element in jsonArray) {
                                val mediumVO: MediumVO =
                                    gson.fromJson(element, MediumVO::class.java)
                                Log.d("getTitle", " " + mediumVO.getTitle())
                                Log.d("getImage", " " + mediumVO.getImage())
                                Log.d("getCode", " " + mediumVO.getCode())
                                mediumVOS.add(mediumVO)
                            }
                            val result = Bundle()
                            result.putParcelableArrayList("mediumVOS", mediumVOS)
                            result.putString("LCode", LCode)
                            (Objects.requireNonNull(getActivity()) as MainActivity).setStartFragment(
                                MediumFragment(),
                                false,
                                result
                            )
                        } else {
                            Log.d("data", "null")
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {}
            })*/
        }
    }
}
