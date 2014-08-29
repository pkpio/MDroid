package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.task.Login;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class ParanoidLogin extends Fragment {
	EditText tokenET;
	EditText murlET;
	Button loginButton;
	ScrollView loginProgressSV;
	TextView loginProgressTV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_paranoid_login, container,
				false);
		setUpWidgets(rootView);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doParanoidLogin();
			}
		});

		return rootView;
	}

	private void setUpWidgets(View rootView) {
		tokenET = (EditText) rootView.findViewById(R.id.login_paranoid_token);
		murlET = (EditText) rootView.findViewById(R.id.login_paranoid_url);
		loginButton = (Button) rootView.findViewById(R.id.login_paranoid_login);
		loginProgressSV = (ScrollView) rootView
				.findViewById(R.id.login_progress_layout);
		loginProgressTV = (TextView) rootView
				.findViewById(R.id.login_progress_message);
	}

	private void doParanoidLogin() {
		String token = tokenET.getText().toString();
		String mUrl = murlET.getText().toString();

		new Login(token, mUrl, loginButton, loginProgressSV, loginProgressTV)
				.execute("");
	}
}
