package kr.rowan.digital_museum_gyeonggi.dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kr.rowan.digital_museum_gyeonggi.databinding.CustomDialog2Binding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService

class CustomDialog2     // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    (
    context: Context?, var imgUrl: String,
    private val mBtnClickListener: View.OnClickListener?
) :
    Dialog(context!!, R.style.Theme_Translucent_NoTitleBar) {

    private lateinit var binding: CustomDialog2Binding
    var URL: String = HttpRequestService.URL + "/kyodong" //
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        binding = CustomDialog2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(context).load(URL + imgUrl).thumbnail(0.01f).into(binding.contentImage)
        if (mBtnClickListener != null) {
            binding.contentImage.setOnClickListener(mBtnClickListener)
            binding.customBack.setOnClickListener(mBtnClickListener)
        }
    }
}
