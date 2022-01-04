package com.yongjincompany.sexapplicationform

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import com.yongjincompany.sexapplicationform.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.dialog_view.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 클릭 리스너 구현
        binding.sharebutton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.sharebutton.visibility = View.INVISIBLE
                binding.explainbtn.visibility = View.INVISIBLE
                ScreenShot()
                binding.sharebutton.visibility = View.VISIBLE
                binding.explainbtn.visibility = View.VISIBLE
            }
        })

        binding.explainbtn.setOnClickListener {
            val view = View.inflate(this, R.layout.dialog_view, null)

            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)

            view.gomain.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
    var Time = 0L
    override fun onBackPressed() {
        if(System.currentTimeMillis() - Time >=1500 ) {
            Time = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }

    //화면 캡쳐하기
    fun ScreenShot() {
        val view: View = window.decorView.rootView
        view.setDrawingCacheEnabled(true) //화면에 뿌릴때 캐시 사용

        //캐시 -> 비트맵 변환
        val screenBitmap: Bitmap = Bitmap.createBitmap(view.getDrawingCache())
        try {
            val cachePath = File(applicationContext.cacheDir, "images")
                cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream(cachePath.toString() + "/image.png") // overwrites this image every time
                screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()
            val newFile = File(cachePath, "image.png")
            val contentUri: Uri = FileProvider.getUriForFile(
                applicationContext,
                "com.yongjincompany.sexapplicationform", newFile
            )
            val Sharing_intent = Intent(Intent.ACTION_SEND)
                Sharing_intent.setType("image/png")
                Sharing_intent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    startActivity(Intent.createChooser(Sharing_intent, "Share image"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}