package kr.rowan.digital_museum_gyeonggi.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.adapter.MuseumCategoryAdapter
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentMuseumCategoryBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MuseumCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMuseumCategoryBinding
    private lateinit var activity: MainActivity
    private val categoryList = ArrayList<CategoryVO>()
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
        binding = FragmentMuseumCategoryBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@MuseumCategoryFragment
            fragment = this@MuseumCategoryFragment
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
                        val categoryVO: CategoryVO =
                            gson.fromJson(array, CategoryVO::class.java)
                        categoryList.add(categoryVO)
                        Log.e("categoryVO.name:", "${categoryVO.name}")
                    }
                    val mLayoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    val mAdapter = MuseumCategoryAdapter(
                        activity,
                        arguments!!.getString("preUuid").toString(),
                        uuid,
                        arguments!!.getString("name").toString()
                    )
                    binding.museumListView.apply {
                        layoutManager = mLayoutManager
                        adapter = mAdapter
                    }
                    mAdapter.update(categoryList)
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
                activity.setStartFragment(MuseumFragment(), true, result)
            }
            R.id.homeImgView -> {
                activity.setStartFragment(SubFragment(), true, null)
            }
        }
    }
}