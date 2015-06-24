package com.kingda.androidsurvey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class MainActivity extends Activity {

	private static final String LOG_TAG = "WebViewDemo";
	private WebView mWebView;
	private Handler mHandler = new Handler();
	private String errorHtml = "<html><body><h1>好像出错了~~~</h1><p>网络不通，请检查下网络吧~~</p></body></html>";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);
		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		// 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);

		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());

		// 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
		// 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中
		mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "asdasd");
		mWebView.addJavascriptInterface(new myHellowWorld(), "my");
		mWebView.addJavascriptInterface(new getRs(), "my");

		mWebView.loadUrl("file:///android_asset/main.html");
	}

	//消耗掉手机的返回按钮事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//当浏览器可以返回上一个页面时
			if (mWebView.canGoBack()) {       
				mWebView.goBack();       
	            return true;       
	        } 
			//弹出一个提示框，询问是否退出系统
			new AlertDialog.Builder(this).setTitle("提 示").setMessage("确定要退出吗？")
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
			}).setPositiveButton("是", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//退出软件事件
					finish();
				}
			}).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class getRs{
		getRs(){}
		@JavascriptInterface
		public void getrs(String t) {
			System.out.println(t);
			final String ft = t;
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, ft,
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	class myHellowWorld {
		myHellowWorld() {}
		@JavascriptInterface
		public void show() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, "HELLOW WORLD",
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	// 这是他定义由 addJavascriptInterface 提供的一个Object
	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		}

		/**
		 * This is not called on the UI thread. Post a runnable to invoke
		 * 这不是呼吁界面线程。发表一个运行调用 loadUrl on the UI thread. loadUrl在UI线程。
		 */
		@JavascriptInterface
		public void clickOnAndroid() { // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
			mHandler.post(new Runnable() {
				public void run() {
					// 此处调用 HTML 中的javaScript 函数
					mWebView.loadUrl("javascript:wave()");
				}
			});
		}
	}

	// 线下的代码可以不看，调试用的
	/**
	 * Provides a hook for calling "alert" from javascript. Useful for
	 * 从javascript中提供了一个叫“提示框” 。这是很有用的 debugging your javascript.
	 * 调试你的javascript。
	 */
	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.d(LOG_TAG, message);
			result.confirm();
			return true;
		}
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		}
	}

	// WebView事件
	private class MyWebViewClient extends WebViewClient {
		//
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// super.onPageFinished(view, url);
		// }
		// 加载网页出错
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			// 此处进行错误处理
			view.loadData(errorHtml, "text/html; charset=UTF-8", null);
		}

		// 在WebView中而不是默认浏览器中显示页面,必须重写此方法
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
	}

}