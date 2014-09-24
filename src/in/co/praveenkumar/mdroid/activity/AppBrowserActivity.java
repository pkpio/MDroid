package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
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

		mBrowser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		mBrowser.loadUrl(url);
	}

}
