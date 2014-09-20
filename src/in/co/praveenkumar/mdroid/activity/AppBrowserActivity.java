package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.adapter.NavigationDrawer;
import in.co.praveenkumar.mdroid.apis.R;
import android.os.Bundle;
import android.webkit.WebView;

public class AppBrowserActivity extends NavigationDrawer {
	WebView mBrowser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appbrowser);
		setUpDrawer();
		mBrowser = (WebView) findViewById(R.id.webview);
		mBrowser.loadUrl("http://moodle.praveenkumar.co.in");
	}

}
