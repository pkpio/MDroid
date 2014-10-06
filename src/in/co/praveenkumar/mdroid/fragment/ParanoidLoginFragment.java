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

public class ParanoidLoginFragment extends Fragment {
	EditText tokenET;
	EditText murlET;
	LoginStatusViewHolder progressViews = new LoginStatusViewHolder();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_login_paranoid,
				container, false);
		setUpWidgets(rootView);

		progressViews.loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doParanoidLogin();
			}
		});

		progressViews.retryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tokenET.setEnabled(true);
				murlET.setEnabled(true);
				progressViews.loginButton.setEnabled(true);
				progressViews.statusLayout.setVisibility(LinearLayout.GONE);
				progressViews.loginButton.setText("Login");
			}
		});

		return rootView;
	}

	private void setUpWidgets(View rootView) {
		tokenET = (EditText) rootView.findViewById(R.id.login_paranoid_token);
		murlET = (EditText) rootView.findViewById(R.id.login_paranoid_url);
		progressViews.loginButton = (Button) rootView
				.findViewById(R.id.login_paranoid_login);

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

	private void doParanoidLogin() {
		String token = tokenET.getText().toString();
		String mUrl = murlET.getText().toString();
		FormValidate fv = new FormValidate();
		if (!fv.valid(token, mUrl)) {
			tokenET.setError(fv.getTokenError(token));
			murlET.setError(fv.getUrlError(mUrl));
			return;
		}

		new LoginTask(token, mUrl, progressViews, getActivity()).execute("");
	}
}
