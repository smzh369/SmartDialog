package com.zerlings.library

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.support.v4.view.ViewCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

class SmartDialog private constructor(val builder: Builder) : Dialog(builder.context, R.style.DialogStyle) {
    //判断对就的activity是否销毁
    val isActivityDestroy: Boolean
        get() {
            if (builder.context is Activity) {
                return builder.context.isFinishing||builder.context.isDestroyed
            }
            return false
        }

    override fun onStart() {
        super.onStart()
        if (builder.isFullScreen) {
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            window!!.decorView.systemUiVisibility = uiOptions
        }
    }

    init {
        setContentView(builder.layoutRes!!)
        val params = window!!.attributes
        params.gravity = builder.gravity
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        if (builder.isFullScreen) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            window!!.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            // android 5.0 需要做一下
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //给 dialog 的顶层view 添加一个状态栏收起监听，让状态栏收起
                ViewCompat.setOnApplyWindowInsetsListener(window!!.decorView) { p0, insets -> insets.consumeSystemWindowInsets() }
            }
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        if (builder.isBackGroundTransparent){
            window!!.setDimAmount(0f)
//            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        window!!.attributes = params
        builder.animStyle?.let { window!!.setWindowAnimations(it) }
        builder.bindListener?.invoke(this@SmartDialog, window!!.decorView)
        builder.bind?.bind(this@SmartDialog, window!!.decorView)
        setCancelable(builder.cancelable)//外部和返回键不可点击
    }


    class Builder(val context: Context) {
        var layoutRes: Int? = null
            private set
        var animStyle: Int? = null
            private set
        var bindListener: ((dialog: SmartDialog, view: View) -> Unit)? = null
            private set
        var bind: BindListener? = null
            private set
        var cancelable: Boolean = true
            private set
        var gravity: Int = Gravity.CENTER
            private set
        var isFullScreen: Boolean = false

        var isBackGroundTransparent = false

        fun setLayoutRes(layoutRes: Int): Builder {
            this.layoutRes = layoutRes
            return this
        }

        fun setAnimStyle(animStyle: Int): Builder {
            this.animStyle = animStyle
            return this
        }

        fun setBindListener(block: (dialog: SmartDialog, view: View) -> Unit): Builder {
            this.bindListener = block
            return this
        }

        fun setBindListener(bindListener: BindListener): Builder {
            this.bind = bindListener
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun setIsFullScreen(isFullScreen: Boolean): Builder {
            this.isFullScreen = isFullScreen
            return this
        }

        fun setIsBackGroundTransparent(isBackGroundTransparent: Boolean): Builder {
            this.isBackGroundTransparent = isBackGroundTransparent
            return this
        }

        fun build(): SmartDialog {

            return SmartDialog(this)
        }
    }

    interface BindListener {
        fun bind(dialog: SmartDialog, view: View)
    }

}
