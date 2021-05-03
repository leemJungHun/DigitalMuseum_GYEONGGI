package kr.rowan.digital_museum_gyeonggi.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.adapter.ScreenSlidePagerAdapter
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentAlbumBinding
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentDigitalMuseumBinding
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentHistoryBinding
import java.util.*


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@HistoryFragment
            fragment = this@HistoryFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = ScreenSlidePagerAdapter()
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    fun onClick(view: View) {
        val result = Bundle()
        when(view.id){
            R.id.backImgView -> {
                activity.setStartFragment(SubFragment(),true,null)
            }
        }
    }

}