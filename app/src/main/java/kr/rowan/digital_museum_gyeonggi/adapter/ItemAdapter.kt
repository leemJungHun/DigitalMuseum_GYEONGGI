package kr.rowan.digital_museum_gyeonggi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.databinding.ItemCardBinding
import kr.rowan.digital_museum_gyeonggi.network.vo.ItemVO

class ItemAdapter(var context: MainActivity, var item: ArrayList<ItemVO>) :
    RecyclerView.Adapter<ItemAdapter.MainViewHolder>() {

    private lateinit var binding: ItemCardBinding
    private var changedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        binding = holder.binding
        val unit = item[position]
       /* Glide.with(context).load(unit.path).diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(0.5f).into(binding.contentImgView)*/

        binding.contentText.text = item[position].title
    }

    override fun getItemCount() = item.size

    fun getItem(position: Int) = item[position]

    inner class MainViewHolder(var binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            /*binding.startBtn.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(view: View) {
                    Log.e("startBtn", "onItemClickListener position = $bindingAdapterPosition")
                    context.startAssignment(
                        bindingAdapterPosition,
                        binding.thumbnailImgView,
                        binding.categoryTxtView,
                        item[bindingAdapterPosition].progress
                    )
                }
            })*/
        }
    }
}