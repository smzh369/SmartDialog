package com.zerlings.library.binding

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.ArrayMap
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zerlings.library.R

class SmartDialog<TBinding: ViewDataBinding> private constructor(private val builder: Builder<TBinding>) : Dialog(builder.context, R.style.DialogStyle) {

    private val variableMap = ArrayMap<String, Any>()
    //判断对就的activity是否销毁
    val isActivityDestroy: Boolean
        get() {
            if (builder.context is Activity) {
                return builder.context.isFinishing||builder.context.isDestroyed
            }
            return false
        }

    private val binding: TBinding

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
        builder.refreshView?.invoke(this, binding)
    }

    init {
        binding = DataBindingUtil.inflate(layoutInflater, builder.layoutResId!!, null, false)
        setContentView(binding.root)
        val params = window!!.attributes
        params.gravity = builder.gravity
        if (builder.isFullScreen) {
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
        if (builder.isBackGroundTransparent){
            window!!.setDimAmount(0f)
//            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        window!!.attributes = params
        builder.animStyle?.let { window!!.setWindowAnimations(it) }
        builder.bindView?.invoke(this@SmartDialog, binding)
        setCancelable(builder.cancelable)//外部和返回键不可点击
    }

    fun setVariable(key: String, variable: Any) {
        variableMap[key] = variable
    }

    @Suppress("UNCHECKED_CAST")
    fun <T>getVariable(key: String): T = variableMap[key] as T

    class Builder<TBinding: ViewDataBinding>(val context: Context) {

        var layoutResId: Int? = null
            private set
        var animStyle: Int? = null
            private set
        var bindView: ((dialog: SmartDialog<TBinding>, binding: TBinding) -> Unit)? = null
            private set
        var refreshView: ((dialog: SmartDialog<TBinding>, binding: TBinding) -> Unit)? = null
            private set
        var cancelable: Boolean = true
            private set
        var gravity: Int = Gravity.CENTER
            private set
        var isFullScreen: Boolean = false

        var isBackGroundTransparent = false

        fun setLayoutResId(layoutResId: Int): Builder<TBinding> {
            this.layoutResId = layoutResId
            return this
        }

        fun setAnimStyle(animStyle: Int): Builder<TBinding> {
            this.animStyle = animStyle
            return this
        }

        fun bind(block: (dialog: SmartDialog<TBinding>, binding: TBinding) -> Unit): Builder<TBinding> {
            this.bindView = block
            return this
        }

        fun refresh(block: (dialog: SmartDialog<TBinding>, binding: TBinding) -> Unit): Builder<TBinding> {
            this.refreshView = block
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder<TBinding> {
            this.cancelable = cancelable
            return this
        }

        fun setGravity(gravity: Int): Builder<TBinding> {
            this.gravity = gravity
            return this
        }

        fun setIsFullScreen(isFullScreen: Boolean): Builder<TBinding> {
            this.isFullScreen = isFullScreen
            return this
        }

        fun setIsBackGroundTransparent(isBackGroundTransparent: Boolean): Builder<TBinding> {
            this.isBackGroundTransparent = isBackGroundTransparent
            return this
        }

        fun build(): SmartDialog<TBinding> {
            return SmartDialog(this)
        }
    }

}
