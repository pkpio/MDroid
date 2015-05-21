package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.DonationActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WorkInProgressFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final Context context = getActivity();
		View rootView = inflater.inflate(R.layout.frag_workinprogress,
				container, false);
		Button requestFeature = (Button) rootView
				.findViewById(R.id.request_feature);

		// Request features listener
		requestFeature.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context,
						DonationActivity.class));
			}
		});

		return rootView;
	}

}
