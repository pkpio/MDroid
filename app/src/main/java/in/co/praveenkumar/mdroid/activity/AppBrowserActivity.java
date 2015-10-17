package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.model.MoodleSiteInfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class AppBrowserActivity extends BaseNavigationActivity {
    static final String TAG = "AppBrowserActivity";
    final int MAX_LOGIN_ATTEMPTS = 2; // Since page load callback occurs during redirects too.

    MoodleSiteInfo mSiteInfo;
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


        // Setup title and url
        String url = DEFAULT_URL;
		String title = DEFAULT_TITLE;
        try {
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
		} catch (Exception e) {
			e.printStackTrace();
		}
        title = (title == null || title.contentEquals("")) ? DEFAULT_TITLE : title;
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setIcon(R.drawable.icon_public_white);

        // Get siteinfo for auto login
        SessionSetting session = new SessionSetting(this);
        mSiteInfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, session.getCurrentSiteId());

		// Init browser with cookies enabled and synced from last session
        mBrowser = (WebView) findViewById(R.id.webview);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();

		// Enable Javascript and DOM storage
        mBrowser.getSettings().setJavaScriptEnabled(true);
		mBrowser.getSettings().setDomStorageEnabled(true);
		mBrowser.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

		// Override the WebViewClient for auto login on load
        mBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Page load finished");
                if (loginAttempts < MAX_LOGIN_ATTEMPTS && mSiteInfo != null) {
                    loginAttempts++;
                    tryAutoLogin();
                }
            }
        });

        loginAttempts = 0;
        mBrowser.loadUrl(url);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_browser, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                tryAutoLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void tryAutoLogin(){
        Toast.makeText(this, getString(R.string.activity_appbrowser_autologin_info),
                Toast.LENGTH_SHORT).show();
        String uname = mSiteInfo.getUsername();
        String password = mSiteInfo.getLoginPassword();
        mBrowser.loadUrl("javascript: {" +
                "document.getElementById('username').value = '" + uname + "';" +
                "document.getElementById('password').value = '" + password + "';" +
                "document.forms[0].submit(); };");
        Log.d(TAG, "Login attempted");
    }

}
