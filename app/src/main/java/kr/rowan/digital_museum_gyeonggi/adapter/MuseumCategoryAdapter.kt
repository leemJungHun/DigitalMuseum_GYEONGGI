package kr.rowan.digital_museum_gyeonggi.adapter

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
import kr.rowan.digital_museum_gyeonggi.fragment.ItemFragment
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import kotlin.collections.ArrayList

/**

 *@param uuid1 가장 첫 프래그먼트 uuid
 *@param uuid2 직전 프래그먼트 uuid
 */
class MuseumCategoryAdapter(val context: MainActivity, private val uuid1:String, private val uuid2:String, private val preName:String) : RecyclerView.Adapter<MuseumCategoryAdapter.MuseumCategoryViewHolder>() {

    private var categoryList = ArrayList<CategoryVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuseumCategoryViewHolder {
        val binding = ItemMuseumCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MuseumCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MuseumCategoryViewHolder, position: Int) {
        val binding = holder.binding
        val category = categoryList[position]
        if(position%3==0){
            binding.patternLayout1.visibility = View.VISIBLE
            binding.patternLayout2.visibility = View.GONE
            binding.pattenBtn1.text = category.name
            if(category.path!=null){

                Glide.with(context).load(HttpRequestService.URL + category.path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f).into(binding.pattenImgView1)
            }
            binding.pattenBtn1.tag = category.uuid
            binding.pattenBtn1.setOnClickListener(onClickListener)
        }else if(position%3==1){
            binding.patternLayout2.visibility = View.VISIBLE
            binding.patternLayout1.visibility = View.GONE
            binding.pattenBtn2.text = category.name
            binding.pattenBtn2.tag = category.uuid
            binding.pattenBtn2.setOnClickListener(onClickListener)
            if(category.path!=null){
                Glide.with(context).load(HttpRequestService.URL + category.path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f).into(binding.pattenImgView2)
            }
            if(position+1<=categoryList.size-1){
                val category2 = categoryList[position+1]
                binding.pattenBtn3.text = category2.name
                binding.pattenBtn3.tag = category2.uuid
                binding.pattenBtn3.setOnClickListener(onClickListener)
                if(category2.path!=null){
                    Glide.with(context).load(HttpRequestService.URL + category2.path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f).into(binding.pattenImgView3)
                }
            }else{
                binding.pattenBtn3.visibility = View.GONE
            }
        }
    }

    fun update(categoryList: ArrayList<CategoryVO>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    private val onClickListener = View.OnClickListener { view ->
        val result = Bundle()
        val eventBtn = view as AppCompatButton
        result.putString("name", eventBtn.text.toString())
        result.putString("uuid", eventBtn.tag.toString())
        result.putString("preUuid", uuid2)
        result.putString("firstUuid", uuid1)
        result.putString("preName", preName)
        result.putString("fragmentName", "museum")
        result.putBoolean("direct", false)
        context.setStartFragment(
            ItemFragment(),
            false,
            result
        )
    }

    override fun getItemCount() = categoryList.size

    class MuseumCategoryViewHolder(val binding: ItemMuseumCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}