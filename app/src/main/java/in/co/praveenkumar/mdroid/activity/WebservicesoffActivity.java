package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class WebservicesoffActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webservices_off);

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_WEBSERVICESS_OFF);
	}

	public void tryLegacyVersion(View v) {
		Intent i = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("market://details?id=in.co.praveenkumar.mdroid.legacy"));
		startActivity(i);
	}

	public void webservicesHelp(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://mdroid.praveenkumar.co.in/#!moodle-setup.md"));
		startActivity(i);
	}

}
