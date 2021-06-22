package kr.rowan.digital_museum_gyeonggi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kr.rowan.digital_museum_gyeonggi.R


class ScreenSlidePagerAdapter() : RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slide_page, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = 19
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imgView: AppCompatImageView = itemView.findViewById(R.id.contentImgView)

    fun bind(position: Int) {
        imgView.setImageResource(getImg(position))
    }

    private fun getImg(num:Int): Int{
        when(num){
            0 -> {
                return R.drawable.history_img_01
            }
            1 -> {
                return R.drawable.history_img_02
            }
            2 -> {
                return R.drawable.history_img_03
            }
            3 -> {
                return R.drawable.history_img_04
            }
            4 -> {
                return R.drawable.history_img_05
            }
            5 -> {
                return R.drawable.history_img_06
            }
            6 -> {
                return R.drawable.history_img_07
            }
            7 -> {
                return R.drawable.history_img_08
            }
            8 -> {
                return R.drawable.history_img_09
            }
            9 -> {
                return R.drawable.history_img_10
            }
            10 -> {
                return R.drawable.history_img_11
            }
            11 -> {
                return R.drawable.history_img_12
            }
            12 -> {
                return R.drawable.history_img_13
            }
            13 -> {
                return R.drawable.history_img_14
            }
            14 -> {
                return R.drawable.history_img_15
            }
            15 -> {
                return R.drawable.history_img_16
            }
            16 -> {
                return R.drawable.history_img_17
            }
            17 -> {
                return R.drawable.history_img_18
            }
            18 -> {
                return R.drawable.history_img_19
            }
            else -> {
                return R.drawable.history_img_01
            }
        }
    }
}