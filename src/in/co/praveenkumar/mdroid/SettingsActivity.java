package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
	private Database db;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		db = new Database(getApplicationContext());
		dialog = new Dialog(this);

		// Set URL to current value in db and then add textchange listener
		EditText urlET = (EditText) findViewById(R.id.settings_url);
		urlET.setText(db.getURL());
		urlET.addTextChangedListener(new addListenerOnTextChange(urlET));

		// set current auto save state. Listener is set from XML.
		setAutoSaveState();

		// set current auto login state. Listener is set from XML.
		setAutoLoginState();

		// OnClick listeners for About and Change log set from XML.
	}

	private class addListenerOnTextChange implements TextWatcher {
		// private Context mContext;
		EditText mEdittextview;

		public addListenerOnTextChange(EditText edittextview) {
			super();
			// this.mContext = context;
			this.mEdittextview = edittextview;
		}

		@Override
		public void afterTextChanged(Editable s) {
			String mURL = mEdittextview.getText().toString();
			db.setURL(mURL);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	private void setAutoSaveState() {
		int state = db.getAutoSaveState();
		RadioButton rb;
		// 0 - only uname; 1 - both; 2 - none
		switch (state) {
		case 0:
			rb = (RadioButton) findViewById(R.id.credentials_uname);
			rb.setChecked(true);
			break;
		case 1:
			rb = (RadioButton) findViewById(R.id.credentials_both);
			rb.setChecked(true);
			break;
		case 2:
			rb = (RadioButton) findViewById(R.id.credentials_none);
			rb.setChecked(true);
			break;
		}
	}

	private void setAutoLoginState() {
		Boolean state = db.getAutoLoginState();
		RadioButton rb;
		if (state)
			rb = (RadioButton) findViewById(R.id.auto_login_yes);
		else
			rb = (RadioButton) findViewById(R.id.auto_login_no);
		rb.setChecked(true);
	}

	public void onCredentialsStateChange(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		// 0 - only uname; 1 - both; 2 - none
		switch (view.getId()) {
		case R.id.credentials_uname:
			if (checked) {
				db.setAutoSaveState(0);
				db.setPswd("");
			}
			break;
		case R.id.credentials_both:
			if (checked) {
				db.setAutoSaveState(1);
			}
			break;
		case R.id.credentials_none:
			if (checked) {
				db.setAutoSaveState(2);
				db.setLDAP("");
				db.setPswd("");
			}
			break;
		}
	}

	public void onAutoLoginStateChange(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.auto_login_yes:
			if (checked)
				db.setAutoLoginState(true);
			break;
		case R.id.auto_login_no:
			if (checked)
				db.setAutoLoginState(false);
			break;
		}
	}

	public void changeLogOnClick(View view) {
		dialog.setTitle("Change log");
		dialog.setContentView(R.layout.change_log);
		dialog.show();
	}

	public void aboutOnClick(View view) {
		dialog.setTitle("About");
		dialog.setContentView(R.layout.about);
		dialog.show();
	}
}
