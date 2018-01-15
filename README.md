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
    List<String> urlList = new ArrayList<>();       urlList.add("http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg");       urlList.add("http://img.youguoquan.com/uploads/magazine/content/7b2a0fdbb23c9e63586b7ff6798dbebb_magazine_web_m.jpg");       urlList.add("http://img.youguoquan.com/uploads/magazine/content/c9c47160b46fceab5afd24dea7f216e6_magazine_web_m.jpg");       urlList.add("http://img.youguoquan.com/uploads/magazine/content/fd986a6e0d5fa3a4485e5ce28f40b2ad_magazine_web_m.jpg");
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

* `CheckedTextView`

  > 可选中的`TextView`

  * 布局文件
  * 设置事件

* `ExpandTextView`

* `SquareImageView`

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