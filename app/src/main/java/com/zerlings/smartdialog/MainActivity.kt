package com.zerlings.smartdialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zerlings.library.binding.SmartDialog
import com.zerlings.smartdialog.databinding.DialogTestBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var i = 0
        val testDialog = SmartDialog.Builder<DialogTestBinding>(this)
            .setLayoutResId(R.layout.dialog_test)
            .setIsFullScreen(false)
            .bind { dialog, binding ->
                binding.tvTest.text = "test";
            }
            .refresh { dialog, binding ->
                i++
                binding.tvTest.text = "${binding.tvTest.text}+$i"
            }.build()
        btn_show.setOnClickListener {
            testDialog.show()
        }
    }
}
