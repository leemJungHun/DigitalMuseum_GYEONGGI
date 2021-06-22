package kr.rowan.digital_museum_gyeonggi.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.adapter.DetailImgAdapter
import kr.rowan.digital_museum_gyeonggi.adapter.MuseumCategoryAdapter
import kr.rowan.digital_museum_gyeonggi.databinding.CustomDialogBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.vo.DetailsVO

class CustomDialog(
    context: Context?, private val detailsVO: DetailsVO?,
    activity: MainActivity,
    singleListener: View.OnClickListener?
) :
    Dialog(context!!) {

    private lateinit var binding: CustomDialogBinding
    var URL: String = HttpRequestService.URL
    private val mBtnClickListener: View.OnClickListener = singleListener!!
    private val mActivity = activity
    private var pageCount = 0
    private var lastPage = 0
    private var prePage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val amount: String = detailsVO!!.quantity.toString() + "개"
        var date = "날짜 미상"
        if (detailsVO.year != null) {
            date = "날짜 ${detailsVO.year}년"
        } else if (detailsVO.year != null && detailsVO.month != null) {
            date = "날짜 ${detailsVO.year}년 ${detailsVO.month}월"
        } else if (detailsVO.year != null && detailsVO.month != null && detailsVO.date != null) {
            date = "날짜 ${detailsVO.year}년 ${detailsVO.month}월 ${detailsVO.date}일"
        }
        binding.producted.text = date
        binding.title.text = detailsVO.title
        binding.amountValue.text = amount
        binding.sizeValue.text = detailsVO.size
        binding.materialValue.text = detailsVO.texture
        binding.productionValue.text = detailsVO.reference
        binding.providerValue.text = detailsVO.contributor
        binding.sourceValue.text = detailsVO.origin
        //binding.contentText.text = detailsVO.content
        //Glide.with(context).load(URL + detailsVO.path).thumbnail(0.01f).into(binding.contentImage)
        val pathList = ArrayList<String>()
        for (i in 0 until detailsVO.images.size) {
            pathList.add(detailsVO.images[i].path)
        }
        lastPage = pathList.size - 1
        if (lastPage >= 1) {
            binding.nextImgView.visibility = View.VISIBLE
        } else {
            binding.nextImgView.visibility = View.INVISIBLE
        }
        val mAdapter = DetailImgAdapter(mActivity)
        binding.contentImgListView.apply {
            adapter = mAdapter
            val recyclerView = (getChildAt(0) as RecyclerView)
            recyclerView.overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Log.e("pagePosition", "$position")
                    if (prePage < position) {
                        pageCount++
                        if (pageCount == lastPage) {
                            binding.preImgView.visibility = View.VISIBLE
                            binding.nextImgView.visibility = View.GONE
                        } else {
                            binding.preImgView.visibility = View.VISIBLE
                            binding.nextImgView.visibility = View.VISIBLE
                        }
                    } else if (prePage > position) {
                        pageCount--
                        if (pageCount == 0) {
                            binding.preImgView.visibility = View.GONE
                            binding.nextImgView.visibility = View.VISIBLE
                            binding.contentImgListView.currentItem = pageCount
                        } else {
                            binding.preImgView.visibility = View.VISIBLE
                            binding.nextImgView.visibility = View.VISIBLE
                            binding.contentImgListView.currentItem = pageCount
                        }
                    }
                    prePage = position
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

        }
        mAdapter.update(pathList)
        binding.closeBtn.setOnClickListener(mBtnClickListener)
        binding.contentImgListView.setOnClickListener(mBtnClickListener)
        binding.producted.setOnClickListener(mBtnClickListener)
        binding.amountValue.setOnClickListener(mBtnClickListener)
        binding.sizeValue.setOnClickListener(mBtnClickListener)
        binding.materialValue.setOnClickListener(mBtnClickListener)
        binding.productionValue.setOnClickListener(mBtnClickListener)
        binding.providerValue.setOnClickListener(mBtnClickListener)
        binding.sourceValue.setOnClickListener(mBtnClickListener)
        binding.contentText.setOnClickListener(mBtnClickListener)
        binding.customBack.setOnClickListener(mBtnClickListener)
        binding.nextImgView.setOnClickListener(pageBtnClickListener)
        binding.preImgView.setOnClickListener(pageBtnClickListener)
    }

    private val pageBtnClickListener = View.OnClickListener { view ->
        when (view.id) {
            binding.preImgView.id -> {
                binding.contentImgListView.currentItem = pageCount - 1
            }
            binding.nextImgView.id -> {
                binding.contentImgListView.currentItem = pageCount + 1
            }
        }
    }

}
