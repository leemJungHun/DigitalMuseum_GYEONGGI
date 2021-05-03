package kr.rowan.digital_museum_gyeonggi.adapter.decoration

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import kr.rowan.digital_museum_gyeonggi.R
import kotlin.math.abs


class MainStack(var context: Context): ViewPager2.PageTransformer {
    /*companion object {
        private const val MIN_SCALE = 0.55f
        private const val MIN_Y_SCALE = 0.85f
        private const val MAX_SCALE = 0.33f
        private const val MAX_Y_SCALE = 1f
    }
    override fun transformPage(view: View, position: Float) {
        val child = view.findViewById<ConstraintLayout>(R.id.layout)
        val pageWidth: Int = child.width / 10
        when {
            position < 0 -> {
                ViewCompat.setTranslationZ(view, position)
                view.translationX = pageWidth * position * 0.6f
                child.layoutParams.width = child.width
                *//*val scaleXFactor = (MIN_SCALE
                        + (MAX_SCALE - MIN_SCALE) * (1 - abs(position)))
                val scaleYFactor = (MIN_Y_SCALE
                        + (MAX_Y_SCALE - MIN_Y_SCALE) * (1 - abs(position)))
                view.scaleX = scaleXFactor
                view.scaleY = scaleYFactor*//*
                view.scaleX = 0.9f
                view.scaleY = 0.9f
            }
            position == 0.0f -> { //현재 페이지
                view.scaleX = 1f
                view.scaleY = 1f
                view.translationX = 0.0f
                ViewCompat.setTranslationZ(view, 0f)
            }
            position <= 2 -> {
                Log.e("x", "${position}")
                ViewCompat.setTranslationZ(view, -position)
                view.translationX = pageWidth * position * 0.6f
                *//*
                val scaleXFactor = (MIN_SCALE
                        + (MAX_SCALE - MIN_SCALE) * (1 - abs(position)))
                val scaleYFactor = (MIN_Y_SCALE
                        + (MAX_Y_SCALE - MIN_Y_SCALE) * (1 - abs(position)))
                view.scaleX = scaleXFactor
                view.scaleY = scaleYFactor*//*
                view.scaleX = 0.9f
                view.scaleY = 0.9f
            }
        }
    }*/
    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_Y_SCALE = 0.85f
        private const val MAX_SCALE = 1.0f
        private const val MAX_Y_SCALE = 1.0f
        private const val MIN_FADE = 0.8f
        private const val MIN_FADE2 = 0.3f
    }
    override fun transformPage(view: View, position: Float) {
        val pageWidth: Int = view.width
        when {
            position < -2 -> {
            }
            position < 0 -> {
                ViewCompat.setTranslationZ(view, position)
                view.translationX = -pageWidth * 0.67f * position
                val scaleXFactor = (MIN_SCALE
                        + (MAX_SCALE - MIN_SCALE) * (1 - abs(position)))
                val scaleYFactor = (MIN_Y_SCALE
                        + (MAX_Y_SCALE - MIN_Y_SCALE) * (1 - abs(position)))
                view.scaleX = scaleXFactor
                view.scaleY = scaleYFactor
            }
            position == 0.0f -> { //현재 페이지
                view.translationX = 0.0f
                view.scaleX = MAX_SCALE
                ViewCompat.setTranslationZ(view, 0f)
                view.scaleY = MAX_Y_SCALE
            }
            position <= 2 -> {
                ViewCompat.setTranslationZ(view, -position)
                view.translationX = pageWidth * 0.65f * -position
                val scaleXFactor = (MIN_SCALE
                        + (MAX_SCALE - MIN_SCALE) * (1 - abs(position)))
                val scaleYFactor = (MIN_Y_SCALE
                        + (MAX_Y_SCALE - MIN_Y_SCALE) * (1 - abs(position)))
                view.scaleX = scaleXFactor
                view.scaleY = scaleYFactor
            }
        }
    }
}