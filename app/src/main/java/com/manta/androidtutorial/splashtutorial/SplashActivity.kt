package com.manta.androidtutorial.splashtutorial

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.animation.AlphaAnimation
import com.manta.androidtutorial.databinding.ActivityMainBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    val mediaPlayer : MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareVideo()
    }

    fun prepareVideo(){
        //텍스쳐가 준비되면 불리는 리스너. 시스템에 의해 불림
        binding.video.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return true // 자동으로 surface 릴리즈
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                try{
                    //surface 객체를 생성해서 넣어줌
                    mediaPlayer.setSurface(Surface(surface))
                    //res>raw 폴더에 동영상파일을 넣고, android.resource 스키마로 uri를 설정해준다.
                    mediaPlayer.setDataSource(this@SplashActivity, Uri.parse("android.resource://$packageName/raw/example"))
                    //비동기적으로 데이터소스를 준비한다.
                    mediaPlayer.prepareAsync()
                    //준비되면 동영상을 시작한다.
                    mediaPlayer.setOnPreparedListener{
                        mediaPlayer.start()
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }
        //동영상 끝났을때 fadeout처리
        mediaPlayer.setOnCompletionListener {
            //더미에 동영상의 현재 장면(마지막 장면)을 띄운다.
            binding.dummy.setImageBitmap(binding.video.bitmap)
            binding.video.visibility = View.GONE
            binding.dummy.visibility = View.VISIBLE
            binding.dummy.startAnimation(AlphaAnimation(1F, 0F).apply{
                duration = 1000
                fillAfter = true
            })

        }

    }
}