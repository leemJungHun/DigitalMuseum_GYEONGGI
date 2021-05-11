package kr.rowan.digital_museum_gyeonggi.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.databinding.ItemAlbumCategoryBinding
import kr.rowan.digital_museum_gyeonggi.databinding.ItemMuseumCategoryBinding
import kr.rowan.digital_museum_gyeonggi.fragment.ItemFragment
import kr.rowan.digital_museum_gyeonggi.network.vo.CategoryVO
import java.util.*
import kotlin.collections.ArrayList

/**

 *@param uuid1 가장 첫 프래그먼트 uuid
 *@param uuid2 직전 프래그먼트 uuid
 */
class AlbumCategoryAdapter(val context: MainActivity, private val uuid1:String, private val uuid2:String, private val preName:String) : RecyclerView.Adapter<AlbumCategoryAdapter.AlbumCategoryViewHolder>() {

    private var categoryList = ArrayList<CategoryVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCategoryViewHolder {
        val binding = ItemAlbumCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumCategoryViewHolder, position: Int) {
        val binding = holder.binding
        val category = categoryList[position]

        binding.albumBtn.text = category.name
        binding.albumBtn.tag = category.uuid
        binding.albumBtn.setOnClickListener(onClickListener)
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
        result.putString("fragmentName", "album")
        result.putParcelableArrayList("categoryList", categoryList)
        result.putBoolean("direct", false)
        context.setStartFragment(
            ItemFragment(),
            false,
            result
        )
    }

    override fun getItemCount() = categoryList.size

    class AlbumCategoryViewHolder(val binding: ItemAlbumCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}