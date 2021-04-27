package kr.rowan.digital_museum_gyeonggi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentDigitalMuseumBinding

class MuseumFragment : Fragment() {
    private lateinit var binding: FragmentDigitalMuseumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        binding = FragmentDigitalMuseumBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@MuseumFragment
            fragment = this@MuseumFragment
        }
        return binding.root
    }
}