package com.zerlings.smartdialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zerlings.library.binding.SmartDialog
import com.zerlings.smartdialog.databinding.DialogTestBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SmartDialog.Builder<DialogTestBinding>(this).setLayoutResId(R.layout.dialog_test).setBind { dialog, binding ->
            binding.tvTest.text = "success";
        }.setIsFullScreen(false).build().show()
    }
}
