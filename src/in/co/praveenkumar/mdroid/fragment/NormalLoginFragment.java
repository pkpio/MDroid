package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.helper.FormValidate;
import in.co.praveenkumar.mdroid.legacy.R;
import in.co.praveenkumar.mdroid.task.LoginTask;
import in.co.praveenkumar.mdroid.view.LoginStatusViewHolder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NormalLoginFragment extends Fragment {
	EditText usernameET;
	EditText passwordET;
	EditText murlET;
	LoginStatusViewHolder progressViews = new LoginStatusViewHolder();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_login_normal, container,
				false);
		setUpWidgets(rootView);

		progressViews.loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				doNormalLogin();
			}
		});

		progressViews.retryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				usernameET.setEnabled(true);
				passwordET.setEnabled(true);
				murlET.setEnabled(true);
				progressViews.loginButton.setEnabled(true);
				progressViews.statusLayout.setVisibility(LinearLayout.GONE);
				progressViews.loginButton.setText("Login");
			}
		});

		// Demo setup
		RelativeLayout demoButton = (RelativeLayout) rootView
				.findViewById(R.id.login_demo);
		demoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				usernameET.setText("demo");
				passwordET.setText("demo");
				murlET.setText("http://moodle.praveenkumar.co.in");
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
		progressViews.loginButton = (Button) rootView
				.findViewById(R.id.login_normal_login);

		// Progress views
		progressViews.statusLayout = (RelativeLayout) rootView
				.findViewById(R.id.login_progress_layout);
		progressViews.progressTitle = (TextView) rootView
				.findViewById(R.id.login_progress_title);
		progressViews.progressBar = (ProgressBar) rootView
				.findViewById(R.id.login_progress_progressbar);
		progressViews.progressText = (TextView) rootView
				.findViewById(R.id.login_progress_message);
		progressViews.retryButton = (Button) rootView
				.findViewById(R.id.login_progress_retry);
	}

	private void doNormalLogin() {
		String username = usernameET.getText().toString();
		String password = passwordET.getText().toString();
		String mUrl = murlET.getText().toString();
		FormValidate fv = new FormValidate();
		if (!fv.valid(username, password, mUrl)) {
			usernameET.setError(fv.getUsernameError(username));
			passwordET.setError(fv.getPasswordError(password));
			murlET.setError(fv.getUrlError(mUrl));
			return;
		}

		usernameET.setEnabled(false);
		passwordET.setEnabled(false);
		murlET.setEnabled(false);
		new LoginTask(username, password, mUrl, progressViews, getActivity())
				.execute("");
	}

}
