package kr.rowan.digital_museum_gyeonggi.dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kr.rowan.digital_museum_gyeonggi.databinding.CustomDialogBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.vo.DetailsVO

class CustomDialog(
    context: Context?, private val detailsVO: DetailsVO,
    singleListener: View.OnClickListener?
) :
    Dialog(context!!, R.style.Theme_Translucent_NoTitleBar) {

    private lateinit var binding: CustomDialogBinding
    var URL: String = HttpRequestService.URL + "/kyodong" //
    private val mBtnClickListener: View.OnClickListener? = singleListener


    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val amount: String = detailsVO.amount.toString() + "개"
        binding.classified.text = detailsVO.code
        binding.producted.text = detailsVO.productedAt.substring(0, 4)
        binding.title.text = detailsVO.title.replace("\\?", "")
        binding.amountValue.text = amount
        binding.sizeValue.text = detailsVO.size
        binding.materialValue.text = detailsVO.material
        binding.productionValue.text = detailsVO.production
        binding.providerValue.text = detailsVO.production
        binding.sourceValue.text = detailsVO.source
        binding.originValue.text = detailsVO.origin
        binding.storageValue.text = detailsVO.storage
        binding.storeNumValue.text = detailsVO.storeNum
        binding.contentText.text = detailsVO.content
        Glide.with(context).load(URL + detailsVO.imgUrl).thumbnail(0.01f).into(binding.contentImage)
        if (mBtnClickListener != null) {
            binding.closeBtn.setOnClickListener(mBtnClickListener)
            binding.contentImage.setOnClickListener(mBtnClickListener)
            binding.classified.setOnClickListener(mBtnClickListener)
            binding.producted.setOnClickListener(mBtnClickListener)
            binding.amountValue.setOnClickListener(mBtnClickListener)
            binding.sizeValue.setOnClickListener(mBtnClickListener)
            binding.materialValue.setOnClickListener(mBtnClickListener)
            binding.productionValue.setOnClickListener(mBtnClickListener)
            binding.providerValue.setOnClickListener(mBtnClickListener)
            binding.sourceValue.setOnClickListener(mBtnClickListener)
            binding.originValue.setOnClickListener(mBtnClickListener)
            binding.storageValue.setOnClickListener(mBtnClickListener)
            binding.storeNumValue.setOnClickListener(mBtnClickListener)
            binding.contentText.setOnClickListener(mBtnClickListener)
            binding.customBack.setOnClickListener(mBtnClickListener)
        }
    }

}
