package in.co.praveenkumar.mdroid.activities;

import in.co.praveenkumar.mdroid.apis.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LoginActivity extends Activity {
	final String DEBUG_TAG = "LoginActivity";
	Button switchNormal;
	Button switchParanoid;
	LinearLayout normalLayout;
	LinearLayout paranoidLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		setUpWidgets();
	}

	private void setUpWidgets() {
		switchNormal = (Button) findViewById(R.id.switch_normal);
		switchParanoid = (Button) findViewById(R.id.switch_paranoid);
		normalLayout = (LinearLayout) findViewById(R.id.normal_layout);
		paranoidLayout = (LinearLayout) findViewById(R.id.paranoid_layout);
		View v = null;
		setToNormal(v);
	}

	public void setToNormal(View v) {
		switchNormal.setEnabled(true);
		switchParanoid.setEnabled(false);

		normalLayout.setVisibility(LinearLayout.VISIBLE);
		paranoidLayout.setVisibility(LinearLayout.GONE);
	}

	public void setToParanoid(View v) {
		switchNormal.setEnabled(false);
		switchParanoid.setEnabled(true);

		normalLayout.setVisibility(LinearLayout.GONE);
		paranoidLayout.setVisibility(LinearLayout.VISIBLE);

	}

}
