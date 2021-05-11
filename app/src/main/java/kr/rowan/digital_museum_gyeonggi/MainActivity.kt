package kr.rowan.digital_museum_gyeonggi

import android.graphics.Point
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kr.rowan.digital_museum_gyeonggi.databinding.ActivityMainBinding
import kr.rowan.digital_museum_gyeonggi.dialog.CustomDialog
import kr.rowan.digital_museum_gyeonggi.dialog.CustomDialog2
import kr.rowan.digital_museum_gyeonggi.fragment.MainFragment
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    var fragmentManager: FragmentManager? = null
    var mainFragment: Fragment? = null
    var nowFragment: Fragment? = null
    var back = false
    var dialog: CustomDialog? = null
    var dialog2: CustomDialog2? = null
    private var decorView: View? = null
    private var uiOption = 0
    var addTask: TimerTask? = null
    var mediaPlayer: MediaPlayer? = null
    var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        Log.d("해상도", ">>> size.x : " + size.x + ", size.y : " + size.y)
       /* mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic)
        mediaPlayer!!.isLooping = false //무한재생 방지
        mediaPlayer!!.start()*/
        decorView = window.decorView
        uiOption = window.decorView.systemUiVisibility
        uiOption = uiOption or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        uiOption = uiOption or View.SYSTEM_UI_FLAG_FULLSCREEN
        uiOption = uiOption or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView!!.systemUiVisibility = uiOption
        fragmentManager = supportFragmentManager
        binding.mainContainer.setOnClickListener(this)
        val transaction = fragmentManager!!.beginTransaction()
        mainFragment = MainFragment()
        nowFragment = MainFragment()
        transaction
                .replace(R.id.main_container, MainFragment()).commitAllowingStateLoss()
    }

    fun setStartFragment(fragment: Fragment, isBack: Boolean, result: Bundle?) {
        val transaction = fragmentManager!!.beginTransaction()
        nowFragment = fragment
        if (!isBack) {
            transaction.setCustomAnimations(
                R.anim.pull_in_right,
                R.anim.push_out_left,
                R.anim.pull_in_right,
                R.anim.push_out_left
            )
        } else {
            transaction.setCustomAnimations(
                R.anim.pull_in_left,
                R.anim.push_out_right,
                R.anim.pull_in_left,
                R.anim.push_out_right
            )
        }
        if (result != null) {
            fragment.arguments = result
        }
        if (nowFragment !== mainFragment) {
            Restart_Period()
        }
        transaction
                .replace(R.id.main_container, fragment).addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (back) {
        }
    }

    fun Media_Start() {
        if (mediaPlayer != null) {
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()
            }
        } else {
            //mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic)
            mediaPlayer!!.isLooping = false //무한재생 방지
            mediaPlayer!!.start()
        }
    }

    fun Media_Stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }
        }
    }

    fun Restart_Period() {
        Log.d("Restart", "Task")
        Stop_Period()
        Start_Period()
    }

    fun Restart_Period(dialog: CustomDialog?) {
        Log.d("Restart", "Task")
        this.dialog = dialog
        Stop_Period()
        Start_Period()
    }

    fun Restart_Period(dialog2: CustomDialog2?) {
        Log.d("Restart", "Task")
        this.dialog2 = dialog2
        Stop_Period()
        Start_Period()
    }

    fun Start_Period() {
        if (nowFragment !== mainFragment) {
            SetAddTask()
            timer = Timer()
            timer!!.schedule(addTask, (210 * 1000).toLong())
        }
    }

    fun Stop_Period() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private fun SetAddTask() {
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
        val transaction = fragmentManager!!.beginTransaction()
        nowFragment = MainFragment()
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        } else if (dialog2 != null) {
            if (dialog2!!.isShowing) {
                dialog2!!.dismiss()
            }
        }
        transaction
                .replace(R.id.main_container, MainFragment()).commitAllowingStateLoss()
        false
    }

    override fun onClick(view: View) {
        Restart_Period()
    }
}