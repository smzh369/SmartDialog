# SmartDialog [![](https://jitpack.io/v/com.gitee.Zerlings/SmartDialog.svg)](https://jitpack.io/#com.gitee.Zerlings/SmartDialog)

SmartDialog是一款简单灵活的弹窗生成框架，用于快速创建弹窗，相比原生Dialog能减少大量代码。

支持DataBinding。

Requirement
-----------
Kotlin / Java(需开启kotlin插件支持)

Android API 19+

Usage
-------

### 1.快速使用
```kotlin
//Kotlin
SmartDialog.Builder(this)
            .setLayoutResId(R.layout.dialog_test)
            .setFullScreen(true)
            .setGravity(Gravity.CENTER)
            .bind { dialog, view ->
                view.tv_test.text = "test";
            }
            .refresh { dialog, view ->
                i++
                view.tv_test.text = "${view.tv_test.text}+$i"
            }.build().show()
        
//Kotlin with DataBinding
SmartDialog.Builder<DialogTestBinding>(this)
            .setLayoutResId(R.layout.dialog_test)
            .setFullScreen(true)
            .setGravity(Gravity.CENTER)
            .bind { dialog, binding ->
                binding.tvTest.text = "test"
            }
            .refresh { dialog, binding ->
                i++
                binding.setVariable(BR.test, "${binding.tvTest.text}+$i")
            }.build().show()
```
* Note: 和DataBinding一起使用时需要传入layout对应的绑定类

### 3.API
| name                      | info                                                   |
|------------------------   |--------------------------------------------------------|
| setLayoutResId(layoutResId: Int)| 设置布局文件                  |
| setAnimStyle(animStyle: Int)| 设置动画样式                  |
| setCancelable(cancelable: Boolean)| 是否能取消                  |
| setGravity(gravity: Int)| 弹窗位置           |
| setFullScreen(fullScreen: Boolean)| 是否全屏             |
| setBackGroundTransparent(backGroundTransparent: Boolean)| 背景是否透明             |
| bind(block: (dialog: SmartDialog, view: View) -> Unit)| 用于界面初始化和绑定监听器，只在dialog创建时执行一次             |
| refresh(block: (dialog: SmartDialog, view: View) -> Unit)| 用于刷新界面，dialog每次show()时都会执行             |

Include
-------
首先在项目根目录下的`build.gradle`中加入（已有则忽略）:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
然后在app文件夹下的`build.gradle`中引入：
```
dependencies {
    implementation 'com.gitee.Zerlings:SmartDialog:0.1.0'
}
```

License
-------
    MIT License

    Copyright (c) 2020 Zerlings
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial    Copyright (c) 2020 Zerlings
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE. portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.