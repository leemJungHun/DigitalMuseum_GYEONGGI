package kr.rowan.digital_museum_gyeonggi.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentSubBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.vo.MediumVO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SubFragment : Fragment() {
    private lateinit var binding: FragmentSubBinding
    private var httpRequestService: HttpRequestService? = null
    private val mediumVOS: ArrayList<MediumVO> = ArrayList<MediumVO>()
    var LCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    fun onClick(view: View) {
        val result = Bundle()
        when (view.id) {
            binding.categoryImgView2.id -> {
                result.putString("LCode","Code")
                (Objects.requireNonNull(activity) as MainActivity).setStartFragment(
                    MuseumFragment(),
                    false,
                    result
                )
            }
        }
        /*httpRequestService.getMediumList(LCode).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful() && response.body() != null){
                    Gson gson = new Gson();
                    if(!response.body().get("data").toString().equals("null")){
                        JsonObject jsonObject = response.body().getAsJsonObject("data");
                        JsonArray jsonArray = jsonObject.getAsJsonArray("mediums");
                        for (JsonElement element : jsonArray) {
                            MediumVO mediumVO = gson.fromJson(element, MediumVO.class);
                            Log.d("getTitle", " " + mediumVO.getTitle());
                            Log.d("getImage", " " + mediumVO.getImage());
                            Log.d("getCode", " " + mediumVO.getCode());
                            mediumVOS.add(mediumVO);
                        }

                        Bundle result = new Bundle();
                        result.putParcelableArrayList("mediumVOS", mediumVOS);
                        result.putString("LCode", LCode);

                        ((MainActivity) Objects.requireNonNull(getActivity())).setStartFragment(new MediumFragment(),false, result);
                    }
                    else{
                        Log.d("data","null");
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });*/
    }
}
