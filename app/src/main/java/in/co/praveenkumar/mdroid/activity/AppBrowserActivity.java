package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class AppBrowserActivity extends BaseNavigationActivity {
    final int MAX_LOGIN_ATTEMPTS = 2; // Since page load callback occurs during redirects too.

	WebView mBrowser;
	String DEFAULT_URL = "http://mdroid.praveenkumar.co.in";
	String DEFAULT_TITLE = "MDroid browser";
    int loginAttempts = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appbrowser);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_BROWSER);

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
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("Page loaded!");
                if (loginAttempts < MAX_LOGIN_ATTEMPTS) {
                    loginAttempts++;
                    String uname = "demo";
                    String password = "demo";
                    view.loadUrl("javascript: {" +
                            "document.getElementById('username').value = '" + uname + "';" +
                            "document.getElementById('password').value = '" + password + "';" +
                            "document.forms[0].submit(); };");
                    System.out.println("Login tried");
                }
            }
        });

        loginAttempts = 0;
        mBrowser.loadUrl(url);
	}

}
