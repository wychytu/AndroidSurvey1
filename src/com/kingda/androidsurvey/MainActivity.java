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
	private String errorHtml = "<html><body><h1>���������~~~</h1><p>���粻ͨ�������������~~</p></body></html>";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);
		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		// �����һ�仰�Ǳ���ģ�����Ҫ��javaScript��Ȼ����һ�ж���ͽ�͵�
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);

		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());

		// �������õ��� addJavascriptInterface ��������ǵ��ص��е��ص�
		// �����ٿ�����DemoJavaScriptInterface����ࡣ��Ҫ�����һ��Ҫ�����߳���
		mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "asdasd");
		mWebView.addJavascriptInterface(new myHellowWorld(), "my");
		mWebView.addJavascriptInterface(new getRs(), "my");

		mWebView.loadUrl("file:///android_asset/main.html");
	}

	//���ĵ��ֻ��ķ��ذ�ť�¼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���¼����Ϸ��ذ�ť 
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//����������Է�����һ��ҳ��ʱ
			if (mWebView.canGoBack()) {       
				mWebView.goBack();       
	            return true;       
	        } 
			//����һ����ʾ��ѯ���Ƿ��˳�ϵͳ
			new AlertDialog.Builder(this).setTitle("�� ʾ").setMessage("ȷ��Ҫ�˳���")
				.setNegativeButton("��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
			}).setPositiveButton("��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//�˳�����¼�
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

	// ������������ addJavascriptInterface �ṩ��һ��Object
	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		}

		/**
		 * This is not called on the UI thread. Post a runnable to invoke
		 * �ⲻ�Ǻ��������̡߳�����һ�����е��� loadUrl on the UI thread. loadUrl��UI�̡߳�
		 */
		@JavascriptInterface
		public void clickOnAndroid() { // ע����������ơ���ΪclickOnAndroid(),ע�⣬ע�⣬����ע��
			mHandler.post(new Runnable() {
				public void run() {
					// �˴����� HTML �е�javaScript ����
					mWebView.loadUrl("javascript:wave()");
				}
			});
		}
	}

	// ���µĴ�����Բ����������õ�
	/**
	 * Provides a hook for calling "alert" from javascript. Useful for
	 * ��javascript���ṩ��һ���С���ʾ�� �����Ǻ����õ� debugging your javascript.
	 * �������javascript��
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

	// WebView�¼�
	private class MyWebViewClient extends WebViewClient {
		//
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// super.onPageFinished(view, url);
		// }
		// ������ҳ����
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			// �˴����д�����
			view.loadData(errorHtml, "text/html; charset=UTF-8", null);
		}

		// ��WebView�ж�����Ĭ�����������ʾҳ��,������д�˷���
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
	}

}