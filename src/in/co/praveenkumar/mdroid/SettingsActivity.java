/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid;

import java.util.Calendar;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.helpers.FrequencyIndexConvertor;
import in.co.praveenkumar.mdroid.services.StartServiceReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	final String DEBUG_TAG = "MDROID SETTINGS";
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

		// set notifications state
		setNotificationsState();

		// Add choices to frequency
		addChoiceToFrequency();

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

	private void setNotificationsState() {
		Boolean state = db.getNotificationsState();
		RadioButton rb;
		// 0 - only uname; 1 - both; 2 - none
		if (state) {
			rb = (RadioButton) findViewById(R.id.notifications_yes);
			rb.setChecked(true);
		} else {
			rb = (RadioButton) findViewById(R.id.notifications_no);
			rb.setChecked(true);
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

	public void onNotificationStateChange(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		// 0 - yes; 1 - no
		switch (view.getId()) {
		case R.id.notifications_yes:
			if (checked) {
				updateNotificationsState(true);
			}
			break;
		case R.id.notifications_no:
			if (checked) {
				updateNotificationsState(false);
			}
			break;
		}
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

	// Add data to Frequency spinner
	public void addChoiceToFrequency() {
		Spinner spinner = (Spinner) findViewById(R.id.menu_frequency_choices);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.frequency_array,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		// Set the current setting for spinner
		int index = FrequencyIndexConvertor.getIndex(db.getServiceFrequency());
		spinner.setSelection(index);

		// Add an onselect listener
		spinner.setOnItemSelectedListener(SpinnerListener);
	}

	private OnItemSelectedListener SpinnerListener = new OnItemSelectedListener() {
		Boolean isSettingForFirstTime = true;

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			Log.d(DEBUG_TAG, index + "");
			// Update frequency to db
			db.setServiceFrequency(FrequencyIndexConvertor.getValue(index));

			// Enable notifications.
			updateNotificationsState(false);
			updateNotificationsState(true);
			setNotificationsState();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	private void updateNotificationsState(Boolean state) {
		// NOTE: changing frequency would also enable the notifications
		Log.d(DEBUG_TAG, "notifications update requested !");

		// Enable notifications and set time
		if (state) {
			// Update state to db
			db.setNotificationsState(true);

			// Enable services scheduling
			Database db = new Database(this);
			final int REPEAT_TIME = 1000 * 60 * 60 * db.getServiceFrequency();

			AlarmManager service = (AlarmManager) this
					.getSystemService(Context.ALARM_SERVICE);

			Intent i = new Intent(this, StartServiceReceiver.class);
			PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT);

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 30);

			long startTime = cal.getTimeInMillis() + REPEAT_TIME;
			service.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime,
					REPEAT_TIME, pending);

			// Enable on BOOT re-triggering
			ComponentName receiver = new ComponentName(this,
					in.co.praveenkumar.mdroid.services.ScheduleReceiver.class);
			PackageManager pm = this.getPackageManager();

			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		}
		// Disable notifications
		else {
			// Update state to db
			db.setNotificationsState(false);

			// Disable scheduled services
			AlarmManager service = (AlarmManager) this
					.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(this, StartServiceReceiver.class);
			PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT);
			service.cancel(pending);

			// Disable on BOOT re-triggering
			ComponentName receiver = new ComponentName(this,
					in.co.praveenkumar.mdroid.services.ScheduleReceiver.class);
			PackageManager pm = this.getPackageManager();

			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
		}

	}
}
