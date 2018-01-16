# Widgets
![widget](https://img.shields.io/badge/widget-1.0.16-brightgreen.svg)

> `widget`是一个`Android`自定义控件库。包含多种常用控件。

## 效果展示

> 控件展示较多，更新也较频繁，不方便录制`gif`图片，请下载`demo`体验

* [`Demo`](https://fir.im/zufy)下载

  ![download](imgs/download_qr.png)

## 基本使用

* 引入`jitpack`

  > 由于引用了`PhotoView`，而其中使用了`jitpack`中的库，所以需要在项目根目录下的`build.gradle`中引入`jitpack`

  ```groovy
  allprojects {
    repositories {
      // ...
      maven { url "https://jitpack.io" }
    }
  }
  ```

* 添加依赖

  > 在项目主模块中添加如下依赖

  ```groovy
  dependencies {
    compile 'com.yhy.widget:view-core:latestVersion'
  }
  ```

## 控件总览

> 简要罗列各种控件

* `core`控件

  > 核心控件，即各种独立使用的控件

  |          名称          |               描述                |
  | :------------------: | :-----------------------------: |
  |   `PreImgActivity`   |           点击图片查看大图控件            |
  |      `AdvView`       |            滚动广告轮播控件             |
  |  `CheckedTextView`   |         可选中的`TextView`          |
  |   `ExpandTextView`   |        可展开收起的`TextView`         |
  |  `SquareImageView`   |  正方形`ImageView`，右上角可设置按钮及点击事件   |
  |  `CircleImageView`   |              圆形图片               |
  |   `RoundImageView`   |              圆角图片               |
  |    `LoadingView`     |             加载状态控件              |
  |   `HackyViewPager`   |       防止内存溢出的`ViewPager`        |
  |     `PickerView`     |           上下滚动数据选取控件            |
  | `RecyclerScrollView` | 用来嵌套`RecyclerView`的`ScrollView` |
  |     `RvDivider`      |       `RecyclerView`的分割线        |
  |  `SettingsItemView`  |          常用设置布局中的条目控件           |
  |      `TitleBar`      |             常用标题栏控件             |
  |    `SwitchButton`    |              开关控件               |

* `layout`控件

  > 布局控件

  |           名称            |          描述           |
  | :---------------------: | :-------------------: |
  |  `CheckedFrameLayout`   |   可选中的`FrameLayout`   |
  |     `CheckedLayout`     |    可选中的`ViewGroup`    |
  |  `CheckedLinearLayout`  |  可选中的`LinearLayout`   |
  | `CheckedRelativeLayout` | 可选中的`RelativeLayout`  |
  |      `FlowLayout`       |    流式布局，标签流式布局的基类     |
  |     `TagFlowLayout`     |        标签流式布局         |
  |      `SlideLayout`      |         侧滑布局          |
  |     `StatusLayout`      | 状态页面布局【加载中，空数据，错误，成功】 |

## 使用说明

#### `core`控件

* `PreImgActivity`

  > 点击查看大图功能

  * 设置`ImgPreCfg`参数

    > 多张图

    ```java
    // 多张图
    List<String> urlList = new ArrayList<>();      urlList.add("http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg");      urlList.add("http://img.youguoquan.com/uploads/magazine/content/7b2a0fdbb23c9e63586b7ff6798dbebb_magazine_web_m.jpg");      urlList.add("http://img.youguoquan.com/uploads/magazine/content/c9c47160b46fceab5afd24dea7f216e6_magazine_web_m.jpg");      urlList.add("http://img.youguoquan.com/uploads/magazine/content/fd986a6e0d5fa3a4485e5ce28f40b2ad_magazine_web_m.jpg");
    // 参数1为点击的ImageView；参数3为当前要预览的图片索引。
    ImgPreCfg cfg = new ImgPreCfg(iv, urlList, 1);
    ```

    > 一张图

    ```java
    // 参数1为点击的ImageView；参数2为当前要预览的图片地址。
    ImgPreCfg cfg = new ImgPreCfg(iv, url);
    ```

  * 开始预览

    ```java
    PreImgActivity.preview(this, cfg);
    ```

* `AdvView`

  > 广告轮播展示栏

  * 布局文件

    ```xml
    <com.yhy.widget.core.adv.AdvView
      android:id="@+id/av_view_multiple"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:layout_marginTop="48dp"
      android:background="#ac0"
      app:av_anim_duration="1000"
      app:av_interval="4000" />
    ```

  * 获取控件

    ```java
    AdvView avMultiple = findViewById(R.id.av_view_multiple);
    ```

  * 设置数据和事件

    > 设置数据

    ```java
    List<String> mItems = new ArrayList<>();
    mItems.add("这是第1个");
    mItems.add("这是第2个");
    mItems.add("这是很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的第3个");
    mItems.add("这是第4个");
    mItems.add("这是第5个");
    mItems.add("这是第6个");
    mItems.add("这是第7个");

    // 第3个参数为每页展示广告条数，默认为1，即单条展示
    SimpleAdvAdapter<String> avAdapter = new SimpleAdvAdapter<String>(this, mItems, 3) {
      @Override
      protected View getItemView(int position, String data) {
        TextView tv = new TextView(mCtx);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setText(data);
        tv.setTextColor(Color.DKGRAY);
        tv.setTextSize(14);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        return tv;
      }
    };

    // 设置适配器到AdvView
    avMultiple.setAdapter(avAdapter);
    ```

    > 设置条目点击事件

    ```java
    avAdapter.setOnItemClickListener(new SimpleAdvAdapter.OnItemClickListener<String>() {
      @Override
      public void onItemClick(SimpleAdvAdapter adapter, int position, String data) {
        toast("position = " + position + ", " + data);
      }
    });
    ```

  * 自定义属性

    |         属性         |      说明       |       默认值        |
    | :----------------: | :-----------: | :--------------: |
    |   `av_interval`    |  动画定时，单位`ms`  |     `3000ms`     |
    | `av_anim_duration` | 动画执行时间，单位`ms` |     `800ms`      |
    |    `av_anim_in`    |    入场动画资源     | `R.anim.adv_in`  |
    |   `av_anim_out`    |    出场动画资源     | `R.anim.adv_out` |

* `CheckedTextView`

  > 可选中的`TextView`

  * 布局文件

    > 背景选择器`bg_checked_ctv_selector`

    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
      <item android:state_checked="true">
        <shape android:shape="rectangle">
          <solid android:color="@color/colorAccent" />
          <corners android:radius="4dp" />
        </shape>
      </item>
      <item>
        <shape android:shape="rectangle">
          <solid android:color="#666" />
          <corners android:radius="4dp" />
        </shape>
      </item>
    </selector>
    ```

    > 布局

    ```xml
    <com.yhy.widget.core.checked.CheckedTextView
       android:id="@+id/ctv_def"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:background="@drawable/bg_checked_ctv_selector"
       android:padding="8dp"
       android:text="阻止了Click和LongClick事件"
       android:textColor="#fff"
       android:textSize="14sp" />
    <!-- 默认阻止了click和longClick事件，如果需要添加这些事件，请添加属性：app:ctv_prevent="false" -->
    ```

  * 获取控件

    ```java
    CheckedTextView ctvDef = findViewById(R.id.ctv_def);
    ```

  * 设置事件

    ```java
    ctvDef.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("我的click被阻止了");
      }
    });

    ctvDef.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        toast("我的longClick被阻止了");
        return true;
      }
    });

    ctvDef.setOnCheckedChangeListener(new CheckedTextView.OnCheckedChangeListener() {
      @Override
      public void onChanged(CheckedTextView ctv, boolean isChecked) {
        toast("isChecked = " + isChecked);
      }
    });
    ```

  * 自定义属性

    |      属性       |            说明             |  默认值   |
    | :-----------: | :-----------------------: | :----: |
    | `ctv_prevent` | 是否阻止`click`和`longClick`事件 | `true` |

* `ExpandTextView`

  > 可展开收起的`TextView`

  * 布局文件

    ```xml
    <com.yhy.widget.core.exptext.ExpandTextView
      android:id="@+id/etv_content"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:background="#fff"
      android:orientation="vertical"
      app:etv_anim_alpha_start="0.2"
      app:etv_anim_duration="800"
      app:etv_max_collapsed_lines="4">

      	<!-- 显示文本内容的TextView -->
        <TextView
          android:id="@+id/tv_content"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_marginLeft="10dp"
          android:layout_marginRight="10dp"
          android:layout_marginTop="8dp"
          android:ellipsize="end"
          android:textColor="#666666"
          android:textSize="16sp"/>
    	
      	<!--展开和收起的点击按钮-->
        <TextView
          android:id="@+id/tv_expand"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_gravity="end|bottom"
          android:padding="16dp"
          android:text="窝草"/>
    </com.yhy.widget.core.exptext.ExpandTextView>
    ```

  * 获取控件

    ```java
    ExpandTextView etvContent = findViewById(R.id.etv_content);
    // 配置内容控件和按钮控件
    etvContent.mapViewId(R.id.tv_content, R.id.tv_expand);
    ```

  * 设置数据

    ```java
    etvContent.setText("哈哈哈哈哈哈啊哈哈哈哈");
    ```

  * 设置事件

    ```java
    etvContent.setOnExpandStateChangeListener(new ExpandTextView.OnExpandStateChangeListener() {
      @Override
      public void onExpandStateChanged(TextView textView, boolean isExpanded) {
        toast(isExpanded ? "展开了" : "收起了");
      }
    });
    ```

  * 自定义属性

    |            属性             |      说明       |   默认值   |
    | :-----------------------: | :-----------: | :-----: |
    | `etv_max_collapsed_lines` |  收缩时显示的最大行数   |   `2`   |
    |    `etv_anim_duration`    | 动画执行时间，单位`ms` | `400ms` |
    |  `etv_anim_alpha_start`   |  透明度动画开始时的值   | `0.6f`  |

* `SquareImageView`

* `CircleImageView`

* `RoundImageView`

* `LoadingView`

* `HackyViewPager`

* `PickerView`

* `RecyclerScrollView`

* `RvDivider`

* `SettingsItemView`

* `TitleBar`

* `SwitchButton`

#### `layout`控件

* `CheckedFrameLayout`
* `CheckedLayout`
* `CheckedLinearLayout`
* `CheckedRelativeLayout`
* `FlowLayout`
* `TagFlowLayout`
* `SlideLayout`
* `StatusLayout`