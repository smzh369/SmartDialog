package com.zerlings.smartdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
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
            .setFullScreen(true)
            .setGravity(Gravity.CENTER)
            .bind { dialog, binding ->
                binding.tvTest.text = "test"
            }
            .refresh { dialog, binding ->
                i++
                binding.setVariable(BR.test, "${binding.tvTest.text}+$i")
            }.build()
        btn_show.setOnClickListener {
            testDialog.show()
        }
    }
}
