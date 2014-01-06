package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_error);

		// Get error code
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		int code = extras.getInt("errCode");
		TextView eCodeTV = (TextView) findViewById(R.id.error_code);
		eCodeTV.setText("Error code: " + code);
	}

}
