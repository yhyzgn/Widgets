package com.yhy.widget.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.widget.Button;
import android.widget.Toast;

import com.yhy.widget.core.web.HybridWebView;
import com.yhy.widget.core.web.bridge.HybridBridge;
import com.yhy.widget.core.web.listener.SimpleOnWebEventListener;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 15:26
 * version: 1.0.0
 * desc   :
 */
public class WebHybridActivity extends BaseActivity {

    private HybridWebView hwvContent;
    private Button btnTest;

    @Override
    protected int getLayout() {
        return R.layout.activity_web_hybrid;
    }

    @Override
    protected void initView() {
        hwvContent = $(R.id.hwv_content);
        btnTest = $(R.id.btn_test);
    }

    @Override
    protected void initData() {
        // 注册交互桥梁
        hwvContent.register(new TestBridge());
        // 加载页面
        hwvContent.loadUrl("file:///android_asset/index.html");
//        hwvContent.loadUrl("http://huantuo.fnlxlife.com/phone/android/news");
    }

    @Override
    protected void initEvent() {
        hwvContent.setOnWebEventListener(new SimpleOnWebEventListener() {
            @Override
            public boolean onJsAlert(HybridWebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WebHybridActivity.this);
                builder
                        .setTitle("标题")
                        .setMessage(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .show();
                //返回true表示不再往下传递弹窗事件，即不再使用原本WebView的弹窗，否则会弹出两次弹窗
                return true;
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用js中的test函数
//                hwvContent.js("test", "小姐姐");
                hwvContent.js("noArg");
            }
        });
    }

    /**
     * 交互桥梁，必须是HybridBridge的子类
     */
    public class TestBridge extends HybridBridge {

        @JavascriptInterface
        public String test(String json) {
            Toast.makeText(WebHybridActivity.this, json, Toast.LENGTH_LONG).show();
            return "Android接收到数据啦";
        }
    }
}
