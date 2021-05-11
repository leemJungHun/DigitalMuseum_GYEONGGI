package kr.rowan.digital_museum_gyeonggi.adapter

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.adapters.TextViewBindingAdapter.setTextSize
import androidx.recyclerview.widget.RecyclerView
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.databinding.ItemMuseumCategoryBinding
import kr.rowan.digital_museum_gyeonggi.databinding.ItemYearBinding
import kr.rowan.digital_museum_gyeonggi.fragment.ItemFragment
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import kotlin.collections.ArrayList

/**

 *@param uuid1 가장 첫 프래그먼트 uuid
 *@param uuid2 직전 프래그먼트 uuid
 */
class YearAdapter(val context: MainActivity) : RecyclerView.Adapter<YearAdapter.YearViewHolder>() {

    private var categoryList = ArrayList<CategoryVO>()
    var nowYear = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val binding = holder.binding
        val category = categoryList[position]
        if (position!=0){
            binding.startMargin.visibility = View.GONE
        }
        if(category.name==nowYear){
            binding.contentTxtView2.text = category.name
            binding.contentTxtView2.visibility = View.VISIBLE
            binding.contentTxtView.visibility = View.GONE
            if (position!=0){
                binding.startMargin2.visibility = View.GONE
            }
        }else{
            binding.contentTxtView.text = category.name
            binding.contentTxtView2.visibility = View.GONE
            binding.contentTxtView.visibility = View.VISIBLE
        }
        binding.contentTxtView.tag = category.uuid
    }

    fun update(categoryList: ArrayList<CategoryVO>, nowYear:String) {
        this.categoryList = categoryList
        this.nowYear = nowYear
        notifyDataSetChanged()
    }

    fun getUuid(position: Int):String{
        return categoryList[position].uuid!!
    }

    fun setText(position: Int){
        nowYear = categoryList[position].name!!
        notifyDataSetChanged()
    }
    private val onClickListener = View.OnClickListener { view ->
        val result = Bundle()
        val eventBtn = view as AppCompatButton

    }

    override fun getItemCount() = categoryList.size

    class YearViewHolder(val binding: ItemYearBinding) : RecyclerView.ViewHolder(binding.root)
}