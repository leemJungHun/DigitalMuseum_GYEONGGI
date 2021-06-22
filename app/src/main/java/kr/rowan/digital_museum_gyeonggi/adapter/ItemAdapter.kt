package kr.rowan.digital_museum_gyeonggi.adapter

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.JsonObject
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.databinding.ItemCardBinding
import kr.rowan.digital_museum_gyeonggi.dialog.CustomDialog
import kr.rowan.digital_museum_gyeonggi.dialog.CustomDialog2
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import kr.rowan.digital_museum_gyeonggi.network.vo.DetailsVO
import kr.rowan.digital_museum_gyeonggi.network.vo.ItemVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ItemAdapter(var context: MainActivity, var item: ArrayList<ItemVO>, var fragmentName:String) :
    RecyclerView.Adapter<ItemAdapter.MainViewHolder>() {
    var itemList = item
    private lateinit var binding: ItemCardBinding
    private var changedPosition = -1
    var dialog: CustomDialog? = null
    var dialog2: CustomDialog2? = null
    var isClick = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(context), parent, false)


        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        binding = holder.binding
        val unit = itemList[position]
        Log.e("image Path", HttpRequestService.URL + unit.images)
        Log.e("position","$position ")
        Log.e("fragmentName","$fragmentName ")
        if (unit.images != null) {
            when (fragmentName) {
                "seniors" -> {
                    binding.contentImgView.visibility = View.INVISIBLE
                    binding.contentImgView2.visibility = View.VISIBLE
                    Glide.with(context).load(HttpRequestService.URL + unit.images[0].path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f).into(binding.contentImgView2)
                }
                "album" -> {
                    binding.contentText.visibility = View.GONE
                    binding.contentImgView2.visibility = View.GONE
                    binding.contentImgView.visibility = View.VISIBLE
                    Glide.with(context).load(HttpRequestService.URL + unit.images[0].path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f).into(binding.contentImgView)
                }
                else -> {
                    binding.contentImgView2.visibility = View.GONE
                    binding.contentImgView.visibility = View.VISIBLE
                    Glide.with(context).load(HttpRequestService.URL + unit.images[0].path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f).into(binding.contentImgView)
                }
            }
        }


        var year = unit.year
        if (unit.year == null) {
            binding.yearText.visibility = View.INVISIBLE
        } else if (unit.year != "미정") {
            year = unit.year + "년"
        }

        binding.yearText.text = year
        binding.contentText.text = unit.title
    }

    override fun getItemCount() = itemList.size

    fun getItem(position: Int) = itemList[position]

    fun update(item: ArrayList<ItemVO>) {
        this.itemList.clear()
        this.itemList.addAll(item)
        Log.e("update","update")
        Log.e("item","$item")
        Log.e("this.item","${this.itemList}")
        notifyDataSetChanged()
    }

    inner class MainViewHolder(var binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemLayout.setOnClickListener {
                if (!isClick&&fragmentName=="museum") {
                    isClick = true
                    val retrofit = Retrofit.Builder()
                        .baseUrl(HttpRequestService.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val httpRequestService = retrofit.create(HttpRequestService::class.java)

                    httpRequestService!!.getDetail(UuidRequest(item[bindingAdapterPosition].uuid.toString()))!!
                        .enqueue(object : Callback<JsonObject?> {
                            override fun onResponse(
                                call: Call<JsonObject?>,
                                response: Response<JsonObject?>
                            ) {
                                if (response.isSuccessful && response.body() != null) {
                                    val gson = Gson()
                                    val detailsVO: DetailsVO = gson.fromJson(
                                        response.body()!!.toString(),
                                        DetailsVO::class.java
                                    )
                                    Log.d("detailsVo", " " + detailsVO.title)
                                    dialog(detailsVO)
                                    isClick = false
                                } else {
                                    isClick = false
                                }
                            }

                            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                                isClick = false
                            }
                        })
                }else if(!isClick&&fragmentName=="album"){
                    dialog2(item[bindingAdapterPosition].images!![0].path)
                    isClick = false
                }
            }
        }

        fun dialog(detailsVO: DetailsVO?) {
            Log.e("context", "$context")
            Log.e("detailsVO", "$detailsVO")
            Log.e("okListener", "$okListener")
            dialog = CustomDialog(
                context,
                detailsVO!!,  // 내용
                context,
                okListener
            )
            context.Restart_Period(dialog)
            //요청 이 다이어로그를 종료할 수 있게 지정함
            dialog!!.setCancelable(true)
            Objects.requireNonNull(dialog!!.window)!!.setGravity(Gravity.CENTER)
            dialog!!.show()
        }

        fun dialog2(imgUrl: String?) {
            Log.e("context", "$context")
            Log.e("okListener", "$okListener")
            dialog2 = CustomDialog2(
                context,
                imgUrl!!,  // 내용
                okListener
            )
            context.Restart_Period(dialog2)
            //요청 이 다이어로그를 종료할 수 있게 지정함
            dialog2!!.setCancelable(true)
            Objects.requireNonNull(dialog2!!.window)!!.setGravity(Gravity.CENTER)
            dialog2!!.show()
        }

        //다이얼로그 클릭이벤트
        private val okListener = View.OnClickListener { v ->
            if (v.id == R.id.close_btn) {
                dialog!!.dismiss()
                isClick = false
            } else {
                context.Restart_Period(dialog)
            }
        }
    }
}