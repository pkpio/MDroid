package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.asynctask.Login;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class NormalLogin extends Fragment {
	Boolean DEBUGGING_MODE = false;
	EditText usernameET;
	EditText passwordET;
	EditText murlET;
	Button loginButton;
	ScrollView loginProgressSV;
	TextView loginProgressTV;
	Context ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.normal_login, container,
				false);
		setUpWidgets(rootView);
		ctx = getActivity();

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doNormalLogin();
			}
		});

		return rootView;
	}

	private void setUpWidgets(View rootView) {
		usernameET = (EditText) rootView
				.findViewById(R.id.login_normal_username);
		passwordET = (EditText) rootView
				.findViewById(R.id.login_normal_password);
		murlET = (EditText) rootView.findViewById(R.id.login_normal_url);
		loginButton = (Button) rootView.findViewById(R.id.login_normal_login);
		loginProgressSV = (ScrollView) rootView
				.findViewById(R.id.login_progress_layout);
		loginProgressTV = (TextView) rootView
				.findViewById(R.id.login_progress_message);
	}

	private void doNormalLogin() {
		String username = usernameET.getText().toString();
		String password = passwordET.getText().toString();
		String mUrl = murlET.getText().toString();

		new Login(username, password, mUrl, loginButton, loginProgressSV,
				loginProgressTV).execute("");
	}

}
