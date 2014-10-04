package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.helper.FormValidate;
import in.co.praveenkumar.mdroid.legacy.R;
import in.co.praveenkumar.mdroid.task.LoginTask;
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

public class ParanoidLoginFragment extends Fragment {
	EditText tokenET;
	EditText murlET;
	Button loginButton;
	Button retryButton;
	ScrollView loginProgressSV;
	TextView loginProgressTV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_login_paranoid,
				container, false);
		setUpWidgets(rootView);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doParanoidLogin();
			}
		});

		retryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginProgressSV.setVisibility(ScrollView.GONE);
				loginButton.setText("Login");
				loginButton.setEnabled(true);
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
		retryButton = (Button) rootView.findViewById(R.id.login_paranoid_retry);
	}

	private void doParanoidLogin() {
		String token = tokenET.getText().toString();
		String mUrl = murlET.getText().toString();
		FormValidate fv = new FormValidate();
		if (!fv.valid(token, mUrl)) {
			tokenET.setError(fv.getTokenError(token));
			murlET.setError(fv.getUrlError(mUrl));
			return;
		}

		new LoginTask(token, mUrl, loginButton, loginProgressSV,
				loginProgressTV, getActivity()).execute("");
	}
}
