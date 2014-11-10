package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.BaseNavigationActivity;
import in.co.praveenkumar.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class AppBrowserActivity extends BaseNavigationActivity {
	WebView mBrowser;
	String DEFAULT_URL = "http://mdroid.praveenkumar.co.in";
	String DEFAULT_TITLE = "MDroid browser";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appbrowser);
		setUpDrawer();

		String url = DEFAULT_URL;
		String title = DEFAULT_TITLE;
		try {
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
		} catch (Exception e) {
			e.printStackTrace();
		}
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setIcon(R.drawable.icon_public_white);

		mBrowser = (WebView) findViewById(R.id.webview);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();

		mBrowser.getSettings().setJavaScriptEnabled(true);
		mBrowser.getSettings().setDomStorageEnabled(true);
		mBrowser.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

		mBrowser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				// Login user if not logged in
				// if (view.getUrl() == "") {

				// }
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// view.loadUrl("javascript:"
				// + "document.getElementById('password').value='lola';");
				System.out.println("Page loaded!");
			}
		});

		mBrowser.loadUrl(url);
	}

}
