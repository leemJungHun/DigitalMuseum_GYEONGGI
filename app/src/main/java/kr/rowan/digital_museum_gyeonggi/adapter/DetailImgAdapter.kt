package kr.rowan.digital_museum_gyeonggi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.databinding.ItemDatailImgBinding
import kr.rowan.digital_museum_gyeonggi.network.HttpRequestService
import java.util.*
import kotlin.collections.ArrayList

/**

 *@param uuid1 가장 첫 프래그먼트 uuid
 *@param uuid2 직전 프래그먼트 uuid
 */
class DetailImgAdapter(val context: MainActivity) : RecyclerView.Adapter<DetailImgAdapter.DetailImgViewHolder>() {

    private var pathList = ArrayList<String>()
    var URL: String = HttpRequestService.URL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImgViewHolder {
        val binding = ItemDatailImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailImgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailImgViewHolder, position: Int) {
        val binding = holder.binding
        val path = pathList[position]
        Glide.with(context).load(URL + path).thumbnail(0.01f).into(binding.contentImgView)
    }

    fun update(pathList: ArrayList<String>) {
        this.pathList = pathList
        notifyDataSetChanged()
    }
    override fun getItemCount() = pathList.size

    class DetailImgViewHolder(val binding: ItemDatailImgBinding) : RecyclerView.ViewHolder(binding.root)
}