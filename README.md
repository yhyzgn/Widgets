# Widgets
![widget](https://img.shields.io/badge/widget-1.0.21-brightgreen.svg)

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

  |                    名称                    |               描述                |
  | :--------------------------------------: | :-----------------------------: |
  |   [`PreImgActivity`](#PreImgActivity)    |           点击图片查看大图控件            |
  |          [`AdvView`](#AdvView)           |            滚动广告轮播控件             |
  |  [`CheckedTextView`](#CheckedTextView)   |         可选中的`TextView`          |
  |   [`ExpandTextView`](#ExpandTextView)    |        可展开收起的`TextView`         |
  |  [`SquareImageView`](#SquareImageView)   |  正方形`ImageView`，右上角可设置按钮及点击事件   |
  |  [`CircleImageView`](#CircleImageView)   |              圆形图片               |
  |   [`RoundImageView`](#RoundImageView)    |              圆角图片               |
  |   [`HackyViewPager`](#HackyViewPager)    |    多点触摸滑动时防止内存溢出的`ViewPager`    |
  |       [`PickerView`](#PickerView)        |           上下滚动数据选取控件            |
  | [`RecyclerScrollView`](#RecyclerScrollView) | 用来嵌套`RecyclerView`的`ScrollView` |
  |        [`RvDivider`](#RvDivider)         |       `RecyclerView`的分割线        |
  | [`SettingsItemView`](#SettingsItemView)  |          常用设置布局中的条目控件           |
  |         [`TitleBar`](#TitleBar)          |             常用标题栏控件             |
  |     [`SwitchButton`](#SwitchButton)      |              开关控件               |

* `layout`控件

  > 布局控件

  |                    名称                    |           描述            |
  | :--------------------------------------: | :---------------------: |
  | [`CheckedFrameLayout`](#CheckedFrameLayout) |    可选中的`FrameLayout`    |
  |    [`CheckedLayout`](#CheckedLayout)     |     可选中的`ViewGroup`     |
  | [`CheckedLinearLayout`](#CheckedLinearLayout) |   可选中的`LinearLayout`    |
  | [`CheckedRelativeLayout`](#CheckedRelativeLayout) |  可选中的`RelativeLayout`   |
  |       [`FlowLayout`](#FlowLayout)        |     流式布局，标签流式布局的基类      |
  |    [`TagFlowLayout`](#TagFlowLayout)     |         标签流式布局          |
  |      [`SlideLayout`](#SlideLayout)       |          侧滑布局           |
  |     [`StatusLayout`](#StatusLayout)      | 状态管理页面布局【加载中，空数据，错误，成功】 |

## 使用说明

#### `core`控件

* <a name = "PreImgActivity">`PreImgActivity`</a>

  > 点击查看大图功能

  * `Application`中初始化

    > 需要在`Application`中初始化，并通过`ImgPreHelper`设置图片加载器、图片下载器等相关配置

    ```java
    ImgPreHelper.getInstance().init(this).setLoader(new ImgPreHelper.ImgLoader() {
        @Override
        public <T> void load(ImageView iv, T model, ProgressBar pbLoading) {
            Glide.with(iv.getContext()).load(model).into(iv);
        }
    }).setOnDownloadListener(new ImgPreHelper.OnDownloadListener() {
        @Override
        public void onProgress(float progress, long current, long total) {
            Log.i("ImgDownloader", "下载进度：" + (progress * 100F) + "%，总大小：" + total + " bytes, 已下载：" + current + " bytes.");
        }

        @Override
        public void onSuccess(File img, String msg) {
            ToastUtils.shortT(msg);
        }

        @Override
        public void onError(String error) {
            ToastUtils.shortT(error);
        }
    });
    ```

  * 设置`ImgPreCfg`参数

    > 多张图

    ```java
    // 多张图
    List<String> urlList = new ArrayList<>();
    urlList.add("http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg");
    urlList.add("http://img.youguoquan.com/uploads/magazine/content/7b2a0fdbb23c9e63586b7ff6798dbebb_magazine_web_m.jpg");
    urlList.add("http://img.youguoquan.com/uploads/magazine/content/c9c47160b46fceab5afd24dea7f216e6_magazine_web_m.jpg");
    urlList.add("http://img.youguoquan.com/uploads/magazine/content/fd986a6e0d5fa3a4485e5ce28f40b2ad_magazine_web_m.jpg");
    // 参数1为点击的ImageView；参数3为当前要预览的图片索引。
    ImgPreCfg cfg = new ImgPreCfg(iv, urlList, 1);
    ```

    > 一张图

    ```java
    // 参数1为点击的ImageView；参数2为当前要预览的图片地址。
    ImgPreCfg cfg = new ImgPreCfg(iv, url);

    // x, y, width, height 分别为图片x, y坐标和宽高度。
    ImgPreCfg cfg = new ImgPreCfg(x, y, width, height, url);
    ```

  * 开始预览

    ```java
    PreImgActivity.preview(this, cfg);
    ```

* <a name = "AdvView">`AdvView`</a>

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
    |   `av_interval`    |  动画定时，单位`ms`  |      `3000`      |
    | `av_anim_duration` | 动画执行时间，单位`ms` |      `800`       |
    |    `av_anim_in`    |    入场动画资源     | `R.anim.adv_in`  |
    |   `av_anim_out`    |    出场动画资源     | `R.anim.adv_out` |

* <a name = "CheckedTextView">`CheckedTextView`</a>

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

* <a name = "ExpandTextView">`ExpandTextView`</a>

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

    |            属性             |      说明       |  默认值   |
    | :-----------------------: | :-----------: | :----: |
    | `etv_max_collapsed_lines` |  收缩时显示的最大行数   |  `2`   |
    |    `etv_anim_duration`    | 动画执行时间，单位`ms` | `400m` |
    |  `etv_anim_alpha_start`   |  透明度动画开始时的值   | `0.6`  |

* <a name = "SquareImageView">`SquareImageView`</a>

  > 正方形`ImageView`

  * 布局文件

    ```xml
    <com.yhy.widget.core.img.SquareImageView 
      android:id="@+id/siv_test"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      app:siv_btn_color="#f20"
      app:siv_btn_img="@drawable/ic_delete_white" />
    ```

  * 获取控件

    ```java
    SquareImageView sivTest = findViewById(R.id.siv_test);
    ```

  * 设置数据

    ```java
    Glide.with(ctx).load(url).into(sivTest);
    ```

  * 设置事件

    ```java
    sivTest.setOnBtnClickListener(new SquareImageView.OnBtnClickListener() {
      @Override
      public void onClick(SquareImageView siv) {
        Toast.makeText(SquareIVActivity.this, "删除第" + position + "张图片", Toast.LENGTH_SHORT).show();
      }
    });
    ```

  * 自定义属性

    |        属性         |       说明        |    默认值     |
    | :---------------: | :-------------: | :--------: |
    |   `siv_btn_img`   |     右上角按钮图片     |     无      |
    |  `siv_btn_size`   | 右上角按钮大小，单位`dp`  | 控件大小的`1/6` |
    |  `siv_btn_color`  |     右上角按钮颜色     |     透明     |
    | `siv_btn_padding` | 右上角按钮内边距，单位`dp` |    `2`     |

* <a name = "CircleImageView">`CircleImageView`</a>

  > 圆形图片

  * 布局文件

    ```xml
    <com.yhy.widget.core.img.corner.CircleImageView
      android:id="@+id/civ_test"
      android:layout_width="100dp"
      android:layout_height="100dp"
      android:src="@mipmap/ic_avatar"
      app:civ_border_color="#f40"
      app:civ_border_overlay="true"
      app:civ_border_width="2dp"
      app:civ_fill_color="#0f0"/>
    ```

  * 获取控件

    ```java
    CircleImageView civTest = findViewById(R.id.civ_test);
    ```

  * 设置数据

    ```java
    Glide.with(ctx).load(url).into(civTest);
    ```

  * 自定义属性

    |          属性          |     说明      |    默认值    |
    | :------------------: | :---------: | :-------: |
    |  `civ_border_width`  | 边框宽度，单位`dp` |    `0`    |
    |  `civ_border_color`  |    边框颜色     | `#000000` |
    | `civ_border_overlay` |   是否悬浮边框    |  `false`  |
    |   `civ_fill_color`   |    填充颜色     |    透明     |

* <a name = "RoundImageView">`RoundImageView`</a>

  > 圆角图片

  * 布局文件

    ```xml
    <com.yhy.widget.core.img.corner.RoundImageView
      android:id="@+id/riv_test"
      android:layout_width="100dp"
      android:layout_height="100dp"
      android:layout_marginTop="24dp"
      android:src="@mipmap/ic_avatar"
      app:civ_border_color="#f40"
      app:civ_border_overlay="true"
      app:civ_border_width="2dp"
      app:civ_fill_color="#0f0"
      app:riv_radius="6dp"/>
    ```

  * 获取控件

    ```java
    CircleImageView rivTest = findViewById(R.id.riv_test);
    ```

  * 设置数据

    ```java
    Glide.with(ctx).load(url).into(rivTest);
    ```

  * 自定义属性

    |          属性          |     说明      |    默认值    |
    | :------------------: | :---------: | :-------: |
    |  `civ_border_width`  | 边框宽度，单位`dp` |    `0`    |
    |  `civ_border_color`  |    边框颜色     | `#000000` |
    | `civ_border_overlay` |   是否悬浮边框    |  `false`  |
    |   `civ_fill_color`   |    填充颜色     |    透明     |
    |     `riv_radius`     | 圆角半径，单位`dp` |    `0`    |

* <a name = "HackyViewPager">`HackyViewPager`</a>

  > 多点触摸滑动时防止内存溢出的`ViewPager`
  >
  > 当成普通`ViewPager`使用即可

* <a name = "PickerView">`PickerView`</a>

  > 上下滚动数据选取控件

  * 布局文件

    ```xml
    <com.yhy.widget.core.picker.PickerView
      android:id="@+id/pv_test"
      android:layout_width="match_parent"
      android:layout_height="200dp"
      app:pv_text_color="#f20"/>
    ```

  * 获取控件

    ```java
    // // 泛型表示该控件中数据源的数据类型
    PickerView<TestEntity> pvText = findViewById(R.id.pv_test);
    ```

  * 设置数据及事件

    ```java
    List<TestEntity> testList = new ArrayList<>();
    for (int i = 1; i <= 40; i++) {
      testList.add(new TestEntity(i, "Data " + i));
    }

    pvTest.setData(testList, new PickerView.ItemProvider<TestEntity>() {
      @Override
      public String getItem(TestEntity data, int position) {
        return data.name;
      }
    }).setOnSelectListener(new PickerView.OnSelectListener<TestEntity>() {
      @Override
      public void onSelect(TestEntity data) {
        toast(data.name);
      }
    });
    ```

  * 自定义属性

    |         属性         |      说明       |    默认值    |
    | :----------------: | :-----------: | :-------: |
    | `pv_max_text_size` | 字体最大尺寸，单位`dp` |   `20`    |
    | `pv_min_text_size` | 字体最小尺寸，单位`dp` |   `14`    |
    |  `pv_text_color`   |     字体颜色      | `#e84c3d` |

* <a name = "RecyclerScrollView">`RecyclerScrollView`</a>

  > 用来嵌套`RecyclerView`的`ScrollView`
  >
  > 当成普通`ScrollView`使用即可

  * 设置滚动监听事件

    ```java
    rsvTest.setOnScrollListener(new OnScrollListener() {
      @Override
      public void onScroll(RecyclerScrollView view, int x, int y, int oldX, int oldY){
        toast("当前滚动条y坐标为：" + y);
      }
    });
    ```

* <a name = "RvDivider">`RvDivider`</a>

  > `RecyclerView`的分割线
  >
  > 目前只针对`LinearLayoutManager`和`GridLayoutManager`两种布局
  >
  > 注意：一定要在设置适配器后添加分割线

  * 创建分割线

    ```java
    RvDivider mDivider = new RvDivider.Builder(this)
      .widthDp(30)
      .color(getResources().getColor(R.color.colorPrimary))
      .type(RvDivider.DividerType.TYPE_WITH_START_END)
      .build();
    ```


  * `LinearLayoutManager`

    ```java
    rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    // 先设置适配器，再添加分割线
    rvContent.addItemDecoration(mDivider);
    ```

  * `GridLayoutManager`

    ```java
    rvContent.setLayoutManager(new GridLayoutManager(this, 4));
    // 先设置适配器，再添加分割线
    rvContent.addItemDecoration(mDivider);
    ```

* <a name = "SettingsItemView">`SettingsItemView`</a>

  > 常用设置布局中的条目控件

  * 布局文件

    ```xml
    <com.yhy.widget.core.settings.SettingsItemView
      android:id="@+id/siv_test"
      android:layout_width="match_parent"
      android:layout_height="48dp"
      android:background="#fff"
      android:paddingLeft="8dp"
      android:paddingRight="8dp"
      app:siv_name="设置1"
      app:siv_text="值1" />
    ```

  * 获取控件

    ```java
    SettingsItemView sivTest = findViewById(R.id.siv_test);
    ```

  * 设置数据

    ```java
    sivTest.setName("左边文字").setText("右边文字");
    // ... 还有设置图标之类的各种方法，如下
    sivTest.setIcon(mIcon)
      .showIcon(mShowIcon)
      .setArrow(mArrow)
      .showArrow(mShowArrow)
      .setName(mName)
      .setNameWidth(mNameWidth)
      .setNameColor(mNameColor)
      .setNameSize(mNameSize)
      .setText(mText)
      .setHint(mHint)
      .setEditable(mEditable)
      .setTextColor(mTextColor)
      .setTextSize(mTextSize)
      .onSwitch(mSwitchOn)
      .showSwitch(mShowSwitch)
      .setNameGravity(mNameGravity)
      .setTextGravity(mTextGravity)
      .setCursorDrawableRes(mCursorDrawableRes);

    // 如果有开关控件，还需要设置开关监听事件
    sivTest.setOnSwitchStateChangeListener(new SettingsItemView.OnSwitchStateChangeListener() {
      @Override
      public void onStateChanged(SettingsItemView siv, SwitchButton sb, boolean isOn) {
        toast(siv.getName() + " :: isOn = " + isOn);
      }
    });
    ```

  * 自定义属性

    |          属性           |                说明                 |    默认值    |
    | :-------------------: | :-------------------------------: | :-------: |
    |      `siv_icon`       |               左边图标                |     无     |
    |    `siv_show_icon`    |             是否显示左边图标              |  `false`  |
    |      `siv_arrow`      |               右边箭头                |     无     |
    |   `siv_show_arrow`    |             是否显示右边箭头              |  `false`  |
    |      `siv_name`       |               左边文本                |     空     |
    |   `siv_name_width`    |           左边文本宽度，单位`dp`           |    自适应    |
    |  `siv_name_gravity`   | 左边文本对其方式【`left`、`center`、`right`】 |`center`|
    |    `siv_name_size`    |           左边文本大小，单位`sp`           |   `14`    |
    |   `siv_name_color`    |              左边文本颜色               | `#000000` |
    |      `siv_text`       |               右边内容                |     空     |
    |  `siv_text_gravity`   | 右边内容对齐方式【`left`、`center`、`right`】 |`center`|
    |      `siv_hint`       |              右边提示文本               |     空     |
    |    `siv_text_size`    |           右边字体大小，单位`sp`           |   `14`    |
    |   `siv_text_color`    |              右边字体颜色               | `#000000` |
    |    `siv_switch_on`    |              是否打开开关               |  `false`  |
    |  `siv_switch_width`   |           开关控件宽度，单位`dp`           |   `48`    |
    |  `siv_switch_height`  |           开关控件高度，单位`dp`           |   `28`    |
    |   `siv_show_switch`   |             是否显示开关控件              |  `false`  |
    |    `siv_editable`     |               是否可编辑               |  `false`  |
    | `siv_cursor_drawable` |              光标颜色资源               |   系统默认    |
    | `siv_input_type` | 输入类型【`text`、`phone`、`email`、`password`】 | `text` |
    | `siv_max_length` | 可输入最大长度 | 不限 |

* <a name = "TitleBar">`TitleBar`</a>

  > 常用标题栏控件

  * 布局文件

    ```xml
    <com.yhy.widget.core.title.TitleBar
      android:id="@+id/tb_test"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:background="#7ad"
      android:elevation="8dp"
      app:tb_title="测试标题" />
    ```

  * 获取控件

    ```java
    TitleBar tbTest = findViewById(R.id.tb_test);
    ```

  * 设置数据及事件

    ```java
    tbTest.setTitle("标题");

    // 事件，该监听器不是接口或者抽象类，只需要重写对应的方法即可
    tbTest2.setOnTitleBarListener(new TitleBar.OnTitleBarListener() {
      @Override
      public void titleClick(View view) {
        toast("点击了标题");
      }

      @Override
      public void leftIconClick(View view) {
        toast("返回");
        finish();
      }

      @Override
      public void leftTextClick(View view) {
        toast("左边文本");
      }

      @Override
      public void rightIconClick(View view) {
        toast("右边图标");
      }

      @Override
      public void rightTextClick(View view) {
        toast("右边文本");
      }
    });
    }
    ```

  * 自定义属性

    |       属性        |  说明  |    默认值    |
    | :-------------: | :--: | :-------: |
    |   `tb_title`    | 标题文本 |     空     |
    | `tb_left_text`  | 左边文本 |     空     |
    | `tb_right_text` | 右边文本 |     空     |
    | `tb_left_icon`  | 左边图标 |     无     |
    | `tb_right_icon` | 右边图标 |     无     |
    | `tb_font_color` | 字体颜色 | `#ffffff` |

* <a name = "SwitchButton">`SwitchButton`</a>

  > 常用开关控件

  * 布局文件

    ```xml
    <com.yhy.widget.core.toggle.SwitchButton
      android:id="@+id/switch_button"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginTop="10dp"/>
    ```

  * 获取控件

    ```java
    SwitchButton switchButton = findViewById(R.id.switch_button);
    ```

  * 设置数据事件

    ```java
    switchButton.onOrOff(true);
    switchButton.isOn();
    switchButton.toggle();     //switch state
    switchButton.toggle(false);//switch without animation
    switchButton.setShadowEffect(true);//disable shadow effect
    switchButton.setEnabled(true);//disable button
    switchButton.setEnableEffect(false);//disable the switch animation

    switchButton.setOnStateChangeListener(new SwitchButton.OnStateChangeListener() {
      @Override
      public void onCheckedChanged(SwitchButton view, boolean isOn) {
        toast("isOn = " + isOn);
      }
    });
    ```

  * 自定义属性

    |           属性           |        说明         |     默认值     |
    | :--------------------: | :---------------: | :---------: |
    |   `sb_shadow_radius`   |    阴影半径，单位`dp`    |    `2.5`    |
    |   `sb_shadow_offset`   |   阴影偏移值，单位`dp`    |    `1.5`    |
    |   `sb_shadow_color`    |       阴影颜色        | `#33000000` |
    |     `sb_off_color`     |       关闭时颜色       |  `#dddddd`  |
    |     `sb_on_color`      |       打开时颜色       |  `#51d367`  |
    |   `sb_border_width`    |    边框宽度，单位`dp`    |     `1`     |
    |   `sb_on_line_color`   |    打开状态中短竖线颜色     |  `#ffffff`  |
    |   `sb_on_line_width`   | 打开状态中短竖线宽度，单位`dp` |     `1`     |
    | `sb_off_circle_color`  |     关闭状态中圆圈颜色     |  `#aaaaaa`  |
    | `sb_off_circle_width`  | 关闭状态中圆圈宽度，单位`dp`  |    `1.5`    |
    | `sb_off_circle_radius` | 关闭状态中圆圈半径，单位`dp`  |     `4`     |
    |        `sb_on`         |       是否打开        |   `false`   |
    |   `sb_shadow_effect`   |     是否支持阴影效果      |   `true`    |
    |  `sb_effect_duration`  |   效果显示时间，单位`ms`   |    `300`    |
    |   `sb_button_color`    |       按钮颜色        |  `#ffffff`  |
    |  `sb_show_indicator`   |      是否显示指示器      |   `true`    |
    |    `sb_background`     |       背景颜色        |  `#ffffff`  |
    |   `sb_enable_effect`   |      是否开启特效       |   `true`    |

#### `layout`控件

* <a name = "CheckedFrameLayout">`CheckedFrameLayout`</a>

  > 可选中的`FrameLayout`

  * 布局文件

    ```xml
    <com.yhy.widget.layout.checked.CheckedRelativeLayout
      android:id="@+id/crl_test"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content">

      <TextView
        android:id="@+id/tv_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/bg_checked_ctv_selector"
        android:padding="8dp"
        android:text="CheckedRelativeLayout"
        android:textColor="#fff"
        android:textSize="14sp" />

      <TextView
        android:layout_below="@id/tv_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="48dp"
        android:background="@drawable/bg_checked_ctv_selector"
        android:padding="8dp"
        android:text="CheckedRelativeLayout"
        android:textColor="#fff"
        android:textSize="14sp" />
    </com.yhy.widget.layout.checked.CheckedRelativeLayout>
    ```

  * 获取控件

    ```java
    CheckedRelativeLayout crlTest = $(R.id.crl_test);
    CheckedRelativeLayout tvTest = $(R.id.tv_test);
    ```

  * 设置事件

    ```java
    tvTest.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击了TextView");
      }
    });

    crlTest.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击");
      }
    });

    crlTest.setOnCheckedChangeListener(new CheckedRelativeLayout.OnCheckedChangeListener() {
      @Override
      public void onChanged(CheckedRelativeLayout crl, boolean isChecked) {
        toast("isChecked = " + isChecked);
      }
    });
    ```

* <a name = "CheckedLayout">`CheckedLayout`</a>

  > 可选中的`ViewGroup`
  >
  > 用法同[`CheckedFrameLayout`](#CheckedFrameLayout)

* <a name = "CheckedLinearLayout">`CheckedLinearLayout`</a>

  > 可选中的`LinearLayout`
  >
  > 用法同[`CheckedFrameLayout`](#CheckedFrameLayout)

* <a name = "CheckedRelativeLayout">`CheckedRelativeLayout`</a>

  > 可选中的`RelativeLayout`
  >
  > 用法同[`CheckedFrameLayout`](#CheckedFrameLayout)

* <a name = "FlowLayout">`FlowLayout`</a>

  > 流式布局
  >
  > 一般不用这个布局，只在定义其他流式布局时使用，继承`FlowLayout`即可，详情请参照[`TagFlowLayout`](#TagFlowLayout)源码

  * 布局文件

    ```xml
    <com.yhy.widget.layout.flow.FlowLayout
      android:layout_width="wrap_content"
      android:layout_height="wrap_content">

      <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="8dp"
        android:background="#666"
        android:padding="4dp"
        android:text="哈哈哈哈"
        android:textColor="#fff"
        android:textSize="14sp" />

      <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="8dp"
        android:background="#666"
        android:padding="4dp"
        android:text="呵哈呵"
        android:textColor="#fff"
        android:textSize="14sp" />

      <!-- 多个子控件... -->
    </com.yhy.widget.layout.flow.FlowLayout>
    ```

  * 自定义属性

    |      属性      |   说明   |  默认值   |
    | :----------: | :----: | :----: |
    | `fl_gravity` | 布局对齐方式 | `left` |

* <a name = "TagFlowLayout">`TagFlowLayout`</a>

  > 标签流式布局
  >
  > 继承于[`FlowLayout`](#FlowLayout)

  * 布局文件

    ```xml
    <com.yhy.widget.layout.flow.tag.TagFlowLayout
      android:id="@+id/tfl_def"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content" />
    ```

  * 获取控件

    ```java
    // 泛型表示该控件中数据源的数据类型
    TagFlowLayout<TestEntity> tflDef = findViewById(R.id.tfl_def);
    ```

  * 设置数据和事件

    ```java
    // 模拟数据
    List<TestEntity> mTestList = new ArrayList<>();
    mTestList.add(new TestEntity(0, "张三张三张三张三张三"));
    mTestList.add(new TestEntity(1, "李四"));
    mTestList.add(new TestEntity(2, "大胖子"));
    mTestList.add(new TestEntity(3, "尼古拉斯"));
    mTestList.add(new TestEntity(4, "哈"));
    mTestList.add(new TestEntity(5, "大胖子"));
    mTestList.add(new TestEntity(6, "尼古拉斯"));
    mTestList.add(new TestEntity(7, "哈"));

    // 设置适配器
    tflDef.setAdapter(new TagFlowAdapter<TestEntity> {
      public Adapter(List<TestEntity> dataList) {
        super(dataList);
      }

      @Override
      public View getView(TagFlowLayout parent, int position, TestEntity data) {
        TextView tv = (TextView) LayoutInflater.from(TagFlowActivity.this).inflate(R.layout.item_tag_flow, null);
        tv.setText(data.name);
        return tv;
      }
    });

    // 设置条目点击事件
    tflDef.setOnCheckChangedListener(new TagFlowLayout.OnCheckChangedListener<TestEntity>() {
      @Override
      public void onChanged(boolean checked, int position, TestEntity data, List<TestEntity> dataList) {
        toast(dataList);
        // 动态添加元素
        mTestList.add(new TestEntity(9, "嘻嘻嘻" + position));
        mAdapter.notifyDataChanged();
      }
    });
    ```

  * 自定义属性

    |       属性        |         说明         |   默认值   |
    | :-------------: | :----------------: | :-----: |
    |  `fl_gravity`   |       布局对齐方式       | `left`  |
    | `tfl_max_count` | 允许选中的最大数量，`-1`表示无限 |  `-1`   |
    | `tfl_is_single` |       是否是单选        | `false` |

* <a name = "SlideLayout">`SlideLayout`</a>

  > 侧滑菜单布局

  * 布局文件

    ```xml
    <com.yhy.widget.layout.slider.SlideLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      android:id="@+id/sl_slide"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="@mipmap/bg_main"
      app:sl_anim_alpha_color="#f60"
      app:sl_main_alpha_enable="true">

      <LinearLayout
        android:layout_width="240dp"
        android:layout_height="match_parent"
        android:background="#66000000"
        android:gravity="center"
        android:orientation="vertical">

        <TextView
          android:id="@+id/tv_menu"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="菜单"
          android:textColor="#f40"
          android:textSize="24sp" />
      </LinearLayout>

      <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff"
        android:orientation="vertical">

        <android.support.v4.view.ViewPager
          android:id="@+id/vp_content"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content" />
      </LinearLayout>
    </com.yhy.widget.layout.slider.SlideLayout>
    ```

  * 获取控件

    ```java
    SlideLayout slSlide = findViewById(R.id.sl_slide);
    TextView tvMenu = findViewById(R.id.tv_menu);
    ViewPager vpContent = findViewById(R.id.vp_content);
    ```

  * 设置数据和事件

    ```java
    // 为ViewPager设置适配器
    vpContent.setAdapter(new PagerAdapter() {
      @Override
      public int getCount() {
        return 4;
      }

      @Override
      public boolean isViewFromObject(View view, Object object) {
        return view == object;
      }

      @Override
      public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = new ImageView(SliderActivity.this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (position == 0) {
          iv.setImageResource(R.mipmap.img_pager_1);
        } else if (position == 1) {
          iv.setImageResource(R.mipmap.img_pager_2);
        } else if (position == 2) {
          iv.setImageResource(R.mipmap.img_pager_3);
        } else {
          iv.setImageResource(R.mipmap.img_pager_4);
        }
        container.addView(iv);
        return iv;
      }

      @Override
      public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
      }
    });

    // 设置事件
    // 当菜单按钮点击时关闭菜单
    tvMenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        slSlide.close();
      }
    });

    // 监听内容，根据需要改变侧滑菜单的可用性
    slSlide.setOnSlideEnableWatcher(new SlideLayout.OnSlideEnableWatcher() {
      @Override
      public boolean shouldEnable() {
        //第二页禁用侧边栏
        return vpContent.getCurrentItem() != 1;
      }
    });

    // 菜单滑动状态监听
    slSlide.setOnStateChangeListener(new SlideLayout.OnStateChangeListener() {
      @Override
      public void onOpened() {
        tvMenu.setText("已打开");
      }

      @Override
      public void onClosed() {
        tvMenu.setText("已关闭");
      }

      @Override
      public void onDragging(float percent, int dx, int total) {
        tvMenu.setText("比例：" + percent);
      }
    });
    ```

* <a name = "StatusLayout">`StatusLayout`</a>

  > 状态管理页面布局【加载中，空数据，错误，成功】
  >
  > 共有三种使用方法，分别是【默认，布局文件中配置，使用外部助手配置】
  >
  > 多种使用方式的优先级关系为：布局文件中配置 `>` 使用外部助手配置 `>` 默认

  * 注意

    > 四种状态均用`tag`来做区分，所以必须要给各种页面设置对应的`tag`

    |  状态  |  `tag`值   |
    | :--: | :-------: |
    | 加载中  | `loading` |
    | 空数据  |  `empty`  |
    |  错误  |  `error`  |
    |  成功  | `success` |

  * 默认方式

    * 布局文件

      ```xml
      <com.yhy.widget.layout.status.StatusLayout
        android:id="@+id/sl_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff">

        <!--当只有一个子控件时，该子控件会被当作[成功]状态的界面，此时无需指定tag为success-->
        <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:gravity="center"
          android:orientation="vertical"
          android:tag="success">

          <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Success"
            android:textColor="#2f0"
            android:textSize="20sp" />
        </LinearLayout>
      </com.yhy.widget.layout.status.StatusLayout>
      ```

  * 布局文件中配置

    * 布局文件

      ```xml
      <com.yhy.widget.layout.status.StatusLayout
        android:id="@+id/sl_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff">

        <!-- 默认的加载中页面 -->
        <com.yhy.widget.layout.status.view.StaLoadingView
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:tag="loading" />

        <!-- 默认的错误页面 -->
        <com.yhy.widget.layout.status.view.StaErrorView
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:tag="error" />

        <!-- 默认的空数据页面 -->
        <com.yhy.widget.layout.status.view.StaEmptyView
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:tag="empty" />

        <!-- 成功页面 -->
        <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:gravity="center"
          android:orientation="vertical"
          android:tag="success">

          <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Success"
            android:textColor="#2f0"
            android:textSize="20sp" />
        </LinearLayout>
      </com.yhy.widget.layout.status.StatusLayout>
      ```

  * 使用外部助手配置

    * 布局文件

      ```xml
      <com.yhy.widget.layout.status.StatusLayout
        android:id="@+id/sl_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff">

        <!-- 使用外部助手配置时，也可以在布局中配置，布局中优先使用 -->
        <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:gravity="center"
          android:orientation="vertical"
          android:tag="error">

          <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:tag="retry"
            android:text="布局中定义的错误页面"
            android:textColor="#246854"
            android:textSize="20sp" />
        </LinearLayout>

        <!-- 成功页面 -->
        <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:gravity="center"
          android:orientation="vertical"
          android:tag="success">

          <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Success"
            android:textColor="#2f0"
            android:textSize="20sp" />
        </LinearLayout>
      </com.yhy.widget.layout.status.StatusLayout>
      ```

    * 获取控件

      ```java
      StatusLayout slContent = findViewById(R.id.sl_content);
      ```

    * 外部助手配置

      ```java
      StaLayoutHelperBuilder builder = new StaLayoutHelperBuilder.Builder(slContent).setLoadingLayout(getLoadingView()).build();
      // builder 还可以设置各种状态的view
      slContent.setHelper(builder.getHelper());

      // ...
      private View getLoadingView() {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setText("Helper中定义的加载中");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tv.setTextColor(Color.RED);
        // 设置tag为loading
        tv.setTag(StatusLayout.Status.LOADING.getStatus());
        return tv;
      }
      ```

  * 切换页面状态

    ```java
    // 加载中
    slContent.showLoading();
    // 成功
    slContent.showSuccess();
    // 错误
    slContent.showError();
    // 空数据
    slContent.showEmpty();
    ```

> `That's all, enjoy it !!!`

_______________

## License

```tex
Copyright 2018 yhyzgn

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

