package com.zerlings.library.common

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import androidx.core.view.ViewCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zerlings.library.R

class SmartDialog private constructor(private val builder: Builder) : Dialog(builder.context, R.style.DialogStyle) {

    private val variableMap = HashMap<String, Any>()
    //判断对应的activity是否销毁
    val isActivityDestroyed: Boolean
        get() {
            if (builder.context is Activity) {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    builder.context.isFinishing||builder.context.isDestroyed
                } else {
                    builder.context.isFinishing
                }
            }
            return false
        }

    override fun onStart() {
        super.onStart()
        if (builder.fullScreen) {
            val uiOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        //or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
            } else {
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LOW_PROFILE
                        //or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
            window!!.decorView.systemUiVisibility = uiOptions
        }
        builder.refreshView?.invoke(this, window!!.decorView)
    }

    init {
        setContentView(builder.layoutResId!!)
        val params = window!!.attributes
        params.gravity = builder.gravity
        if (builder.fullScreen) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
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
        if (builder.backGroundTransparent){
            window!!.setDimAmount(0f)
//            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        window!!.attributes = params
        builder.animStyle?.let { window!!.setWindowAnimations(it) }
        builder.bindView?.invoke(this, window!!.decorView)
        setCancelable(builder.cancelable)//外部和返回键不可点击
    }

    fun setVariable(key: String, variable: Any) {
        variableMap[key] = variable
    }

    @Suppress("UNCHECKED_CAST")
    fun <T>getVariable(key: String): T = variableMap[key] as T

    class Builder(val context: Context) {

        var layoutResId: Int? = null
            private set
        var animStyle: Int? = null
            private set
        var bindView: ((dialog: SmartDialog, view: View) -> Unit)? = null
            private set
        var refreshView: ((dialog: SmartDialog, view: View) -> Unit)? = null
            private set
        var cancelable: Boolean = true
            private set
        var gravity: Int = Gravity.CENTER
            private set
        var fullScreen: Boolean = false
            private set
        var backGroundTransparent = false
            private set

        fun setLayoutResId(layoutResId: Int): Builder {
            this.layoutResId = layoutResId
            return this
        }

        fun setAnimStyle(animStyle: Int): Builder {
            this.animStyle = animStyle
            return this
        }

        fun bind(block: (dialog: SmartDialog, view: View) -> Unit): Builder {
            this.bindView = block
            return this
        }

        fun refresh(block: (dialog: SmartDialog, view: View) -> Unit): Builder {
            this.refreshView = block
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

        fun setFullScreen(fullScreen: Boolean): Builder {
            this.fullScreen = fullScreen
            return this
        }

        fun setBackGroundTransparent(backGroundTransparent: Boolean): Builder {
            this.backGroundTransparent = backGroundTransparent
            return this
        }

        fun build(): SmartDialog {
            return SmartDialog(this)
        }
    }

}
