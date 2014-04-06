package in.co.praveenkumar.mdroid.activities;

import in.co.praveenkumar.mdroid.apis.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.andreabaccega.widget.FormEditText;

public class LoginActivity extends Activity {
	final String DEBUG_TAG = "LoginActivity";
	Button switchNormal;
	Button switchParanoid;
	LinearLayout normalLayout;
	LinearLayout paranoidLayout;
	FormEditText mNormUrl;
	FormEditText mParaUrl;
	FormEditText unameView;
	FormEditText pswdView;

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
		mNormUrl = (FormEditText) findViewById(R.id.norm_url);
		mParaUrl = (FormEditText) findViewById(R.id.para_url);
		unameView = (FormEditText) findViewById(R.id.username);
		pswdView = (FormEditText) findViewById(R.id.password);

		View v = null;
		setToNormal(v);
	}

	public void doNormLogin(View v) {
		FormEditText[] allFields = { mNormUrl, unameView, pswdView };

		boolean allValid = true;
		for (FormEditText field : allFields) {
			allValid = field.testValidity() && allValid;
		}

		if (allValid) {
			// YAY
		} else {
			// EditText are going to appear with an exclamation mark and an
			// explicative message.
		}
	}

	public void setToNormal(View v) {
		Log.d(DEBUG_TAG, "Set to normal");

		switchNormal.setActivated(true);
		switchParanoid.setActivated(false);

		normalLayout.setVisibility(LinearLayout.VISIBLE);
		paranoidLayout.setVisibility(LinearLayout.GONE);
	}

	public void setToParanoid(View v) {
		Log.d(DEBUG_TAG, "Set to paranoid");

		switchNormal.setActivated(false);
		switchParanoid.setActivated(true);

		normalLayout.setVisibility(LinearLayout.GONE);
		paranoidLayout.setVisibility(LinearLayout.VISIBLE);

	}

}
