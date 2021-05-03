package kr.rowan.digital_museum_gyeonggi.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kr.rowan.digital_museum_gyeonggi.MainActivity
import kr.rowan.digital_museum_gyeonggi.R
import kr.rowan.digital_museum_gyeonggi.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    var timer: Timer? = null
    var forwardDrawable = ArrayList<Drawable>()
    var backendDrawable = ArrayList<Drawable>()
    var backendNum = 0
    var forwardNum = 0
    var isForward = true
    var addTask: TimerTask? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        binding = FragmentMainBinding.inflate(layoutInflater)
        forwardDrawable.add(resources.getDrawable(R.drawable.gyeonggi_school_bg_01))
        forwardDrawable.add(resources.getDrawable(R.drawable.gyeonggi_school_bg_03))
        backendDrawable.add(resources.getDrawable(R.drawable.gyeonggi_school_bg_02))
        backendDrawable.add(resources.getDrawable(R.drawable.gyeonggi_school_bg_04))
        //(Objects.requireNonNull(activity) as MainActivity).Media_Start()
        stopPeriod()
        startPeriod()
        binding.mainScreen.setOnClickListener {
            stopPeriod()
            (Objects.requireNonNull(activity) as MainActivity).Media_Stop()
            (Objects.requireNonNull(activity) as MainActivity).setStartFragment(
                SubFragment(),
                false,
                null
            )
        }
        return binding.root
    }

    private fun fadeOutAndHideImage(outImg: ImageView) {
        val fadeOut: Animation = AlphaAnimation(1.0f, 0.0f).apply {
            interpolator = AccelerateInterpolator()
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    when (outImg.id) {
                        R.id.backend_img -> {
                            if (backendNum < 1) {
                                backendNum++
                            } else {
                                backendNum = 0
                            }
                            Glide.with(Objects.requireNonNull(activity)!!).load(
                                backendDrawable[backendNum]
                            ).into(outImg)
                        }
                        R.id.forward_img -> {
                            if (forwardNum < 1) {
                                forwardNum++
                            } else {
                                forwardNum = 0
                            }
                            Glide.with(Objects.requireNonNull(activity)!!).load(
                                forwardDrawable[forwardNum]
                            ).into(outImg)
                        }
                    }
                    outImg.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationStart(animation: Animation) {}
            })
        }
        outImg.startAnimation(fadeOut)
    }

    private fun fadeInAndShowImage(inImg: ImageView) {
        val fadeIn: Animation = AlphaAnimation(0.0f, 1.0f).apply {
            interpolator = AccelerateInterpolator()
            duration= 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    inImg.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationStart(animation: Animation) {}
            })
        }
        inImg.startAnimation(fadeIn)
    }

    private fun startPeriod() {
        setImageTask()
        timer = Timer()
        timer!!.schedule(addTask, 0, (3 * 1000).toLong())
    }

    private fun stopPeriod() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private fun setImageTask() {
        try {
            addTask = object : TimerTask() {
                override fun run() {
                    val msg = handler.obtainMessage()
                    handler.sendMessage(msg)
                }
            }
        } catch (ignored: Exception) {
        }
    }

    val handler = Handler { // 원래 하려던 동작 (UI변경 작업 등)
        binding.backendImg.clearAnimation()
        binding.forwardImg.clearAnimation()
        isForward = if (isForward) {
            fadeInAndShowImage(binding.backendImg)
            fadeOutAndHideImage(binding.forwardImg)
            false
        } else {
            fadeInAndShowImage(binding.forwardImg)
            fadeOutAndHideImage(binding.backendImg)
            true
        }
        false
    }
}