package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleUser;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserinfoDialog extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	MoodleSiteInfo siteinfo;
	MoodleUser user;

	public UserinfoDialog(Context context, long siteid, int userid) {
		super(context);
		this.context = context;
		siteinfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, siteid);
		List<MoodleUser> mUsers = MoodleUser.find(MoodleUser.class,
				"userid = ? and siteid = ?", userid + "", siteid + "");
		if (mUsers != null && mUsers.size() > 0)
			user = mUsers.get(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_userinfo);

		// Get views
		LinearLayout userEmailLayout = (LinearLayout) findViewById(R.id.dialog_userinfo_layout_email);
		LinearLayout userSkypeLayout = (LinearLayout) findViewById(R.id.dialog_userinfo_layout_skype);
		LinearLayout userUrlLayout = (LinearLayout) findViewById(R.id.dialog_userinfo_layout_url);
		LinearLayout userCityLayout = (LinearLayout) findViewById(R.id.dialog_userinfo_layout_location);
		TextView userImage = (TextView) findViewById(R.id.dialog_userinfo_user_image);
		TextView userFullname = (TextView) findViewById(R.id.dialog_userinfo_user_fullname);
		TextView userEmail = (TextView) findViewById(R.id.dialog_userinfo_user_email);
		TextView userSkype = (TextView) findViewById(R.id.dialog_userinfo_user_skype);
		TextView userUrl = (TextView) findViewById(R.id.dialog_userinfo_user_url);
		TextView userCity = (TextView) findViewById(R.id.dialog_userinfo_user_city);

		if (user == null)
			return;

		// Set OnClickListeners
		userEmailLayout.setOnClickListener(this);
		userSkypeLayout.setOnClickListener(this);
		userUrlLayout.setOnClickListener(this);
		userCityLayout.setOnClickListener(this);

		// Set values
		// Name and Image
		String name = user.getFullname();
		char firstChar = 0;
		if (name.length() != 0)
			firstChar = name.charAt(0);
		userImage.setText(firstChar + "");
		userImage.setBackgroundColor(LetterColor.of(firstChar));
		userFullname.setText(user.getFullname());

		// Email
		if (user.getEmail() != null && !user.getEmail().contentEquals(""))
			userEmail.setText(user.getEmail());
		else
			userEmailLayout.setVisibility(LinearLayout.GONE);

		// Skype
		if (user.getSkype() != null && !user.getSkype().contentEquals(""))
			userSkype.setText(user.getSkype());
		else
			userSkypeLayout.setVisibility(LinearLayout.GONE);

		// Url
		if (user.getUrl() != null && !user.getUrl().contentEquals(""))
			userUrl.setText(user.getUrl());
		else
			userUrlLayout.setVisibility(LinearLayout.GONE);

		// City
		if (user.getCity() != null && !user.getCity().contentEquals(""))
			userCity.setText(user.getCity());
		else
			userCityLayout.setVisibility(LinearLayout.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_userinfo_layout_email:
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", user.getEmail(), null));
			context.startActivity(Intent.createChooser(intent, "Send Email"));

			break;
		case R.id.dialog_userinfo_user_email:
			Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", user.getEmail(), null));
			context.startActivity(Intent.createChooser(intent2, "Send Email"));

			break;
		}

	}

}
