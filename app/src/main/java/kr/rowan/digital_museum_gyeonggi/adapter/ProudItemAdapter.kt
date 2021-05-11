package kr.rowan.digital_museum_gyeonggi.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.databinding.ItemMuseumCategoryBinding
import kr.rowan.digital_museum_gyeonggi.databinding.ItemProudCardBinding
import kr.rowan.digital_museum_gyeonggi.fragment.ItemFragment
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import kr.rowan.digital_museum_gyeonggi.network.vo.ItemVO
import java.util.*
import kotlin.collections.ArrayList

class ProudItemAdapter(val context: MainActivity) :
    RecyclerView.Adapter<ProudItemAdapter.ProudItemViewHolder>() {

    private var itemList = ArrayList<ItemVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProudItemViewHolder {
        val binding =
            ItemProudCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProudItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProudItemViewHolder, position: Int) {
        val binding = holder.binding
        val item = itemList[position]
        if (item.images != null) {
            Glide.with(context).load(HttpRequestService.URL + item.images[0].path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f).into(binding.contentImgView)
        }
        binding.nameTxtView.text = item.title

    }


    fun update(itemList: ArrayList<ItemVO>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun getItem(position: Int):ItemVO{
        return itemList[position]
    }

    private val onClickListener = View.OnClickListener { view ->
        val result = Bundle()
        val eventBtn = view as AppCompatButton

    }

    override fun getItemCount() = itemList.size

    class ProudItemViewHolder(val binding: ItemProudCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}