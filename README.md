# Kongzue TitleBar
Kongzue TitleBar 是一款导航栏组件，它可以帮您快速配置界面顶部的标题栏，并提供丰富可选的功能，而无需手动费时费力的创建导航栏布局。
同时 TitleBar 还支持沉浸式适配，只需要一个开关，即可完全搞定沉浸式！

<a href="https://github.com/kongzue/TitleBar/">
<img src="https://img.shields.io/badge/TitleBar-1.2.5-green.svg" alt="Kongzue TitleBar">
</a>
<a href="https://bintray.com/myzchh/maven/TitleBar/1.2.4/link">
<img src="https://img.shields.io/badge/Support-1.2.4-red.svg" alt="Support">
</a>
<a href="https://bintray.com/myzchh/maven/TitleBar/1.2.5/link">
<img src="https://img.shields.io/badge/AndroidX-1.2.5-green.svg" alt="AndroidX">
</a>
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License">
</a>
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Homepage">
</a>

Demo预览图如下：

![Tabbar](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/titlebar_demo.png)

Demo下载地址：http://beta.kongzue.com/titlebar

## 优势
- 轻松易配置，主体功能都可在XML中直接完成设置。

- 易于上手，快速创建，满足绝大多数标题栏使用场景。

## 引入

请注意，Support 版本的 Titlebar 最后维护至版本 1.2.4，AndroidX 版本请更新换用 1.2.5 版。

### Support Version

1) 从 Maven 仓库或 jCenter 引入：
Maven仓库：
```
<dependency>
  <groupId>com.kongzue.titlebar</groupId>
  <artifactId>titlebar</artifactId>
  <version>1.2.4</version>
  <type>pom</type>
</dependency>
```
Gradle：
在dependencies{}中添加引用：
```
implementation 'com.kongzue.titlebar:titlebar:1.2.4'
```

### AndroidX Version

1) 在 project 的 build.gradle 文件中找到 `allprojects{}` 代码块添加以下代码：

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }      //增加 jitPack Maven 仓库
    }
}
```

2) 在 app 的 build.gradle 文件中找到 `dependencies{}` 代码块，并在其中加入以下语句：

```
implementation 'com.github.kongzue:TitleBar:1.2.5'
```

## 使用方法

2) 从XML布局文件创建：
```
<com.kongzue.titlebar.TitleBar xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/titleBar"
    android:layout_width="match_parent"
    android:layout_height="55dp"
    android:background="#3F51B5"
    app:gravity="left"
    app:noBackButton="true"
    app:statusBarTransparent="true"
    app:title="TitleBarDemo" />
```

其中支持的自定义属性解释如下：

字段 | 含义 | 类型
---|---|---
app:mainColor  | 主色调，影响左右按钮（含文字）、标题的颜色  | ColorInt
app:title  | 标题文字  | String
app:tip  | 副标题文字  | String
app:titleColor  | 标题文字颜色（优先于mainColor）  | ColorInt
app:titleBold  | 标题文字是否粗体  | boolean
app:tipColor  | 副标题文字颜色  | ColorInt
app:titleSize  | 标题文字大小  | int(像素)
app:tipSize  | 副标题文字大小  | int(像素)
app:leftButtonImage  | 返回按钮（左侧按钮）图标  | DrawableResId
app:rightButtonImage  | 右侧按钮图标，默认不设置即不显示  | DrawableResId
app:statusBarTransparent  | 是否沉浸式当前Activity  | boolean
app:statusBarTransparentOnlyPadding | 只使用上方 Padding 位移以隔开状态栏区域的方法  | boolean
app:splitLineColor  | TitleBar底部分割线颜色，默认不设置即不显示  | ColorInt
app:backText  | 返回按钮文字，默认不设置即不显示  | String
app:rightText  | 右侧按钮文字，默认不设置即不显示  | String
app:noBackButton  | 不显示返回按钮  | boolean
app:backgroundAlpha  | 背景透明度  | float(范围0f~1f)
app:gravity  | 文字排版样式  | 可选left;center;right
app:buttonTextSize | 按钮文字字号 | int(像素)

也可通过set方法设置：
```
titleBar = findViewById(R.id.titleBar);

titleBar.setMainColor(Color.rgb(62, 120, 238));                             //设置主色调颜色
titleBar.setTitleSize(dp2px(12));                                           //设置标题字号
titleBar.setNoBackButton(true);                                             //不显示返回按钮
titleBar.setGravity(TitleBar.GravityValue.LEFT.ordinal());                  //设置排版方式
titleBar.setBackgroundAlpha(0.5f);                                          //设置背景透明度50%
titleBar.setButtonTextSize(dp2px(12));                                      //设置按钮文字字号
//...
```

3) 特性：

请注意若您在 XML 中设置 android:background 为一个颜色，且在不额外单独设置 mainColor、titleColor 等影响颜色相关属性时，那么在开启沉浸式时，TitleBar 会根据背景颜色的深浅自动判断图标、文字会自动应用深色（黑）、浅色（白）的主题，且您的设备状态栏主题也会自动根据背景色的明暗程度变化。

即，当您的背景色设置为深色时，一般开启沉浸式的情况下，系统状态栏以及 TitleBar 上的图标、文字会以白色显示，相反，则以黑色显示。

另外请注意，TitleBar 的沉浸式本质上是对自身设置了一部分顶端距离，这段距离等于状态栏高度，因此请勿将 TitleBar 放置于“安全区”内（即，设置有android:fitsSystemWindows="true"属性的 ViewGroup 内）。

以上！

## 开源协议
```
Copyright Kongzue TitleBar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 更新日志
v1.2.4:
- 修复了 title、tip 等属性引用 stringResId 可能出现无效的问题；
- 可直接使用 setOnClickListener 来设置点击事件（若设置了 onTitleBarDoubleClick 则会延迟500毫秒执行）；

v1.2.3:
- 修复 bug；

v1.2.2:
- 修复 bug；
- OnRightButtonPressed 和 OnBackPressed 新增回传参数 view，方便需要使用 PopupMenu 的场景；
- 新增属性 statusBarTransparentOnlyPadding 只设置状态栏高度上方偏移，不执行沉浸式代码，方便有其他沉浸式适配代码的应用。

v1.2：
- 修复当开启 noBackButton 时 title 不居中的问题；

v1.1:
- 修复 title 不可以为空文本的 bug；
- 新增属性 buttonTextSize 控制左右按钮文本字号；

v1.0:
- 全新发布；

