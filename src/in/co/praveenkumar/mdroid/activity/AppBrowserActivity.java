package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import in.co.praveenkumar.mdroid.legacy.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AppBrowserActivity extends AppNavigationDrawer {
	WebView mBrowser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appbrowser);
		setUpDrawer();
		String url = getIntent().getStringExtra("url");

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

			int count = 0;

			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:"
						+ "document.getElementById('password').value='lola';");
				count++;
				System.out.println("Page loaded!");
			}
		});

		mBrowser.loadUrl(url);
	}

}
