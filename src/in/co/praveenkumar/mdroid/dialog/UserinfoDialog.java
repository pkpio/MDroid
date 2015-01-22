package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleUser;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserinfoDialog extends Dialog {
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
		TextView userImage = (TextView) findViewById(R.id.dialog_userinfo_user_image);
		TextView userFullname = (TextView) findViewById(R.id.dialog_userinfo_user_fullname);
		TextView userEmail = (TextView) findViewById(R.id.dialog_userinfo_user_email);
		TextView userSkype = (TextView) findViewById(R.id.dialog_userinfo_user_skype);
		TextView userUrl = (TextView) findViewById(R.id.dialog_userinfo_user_url);
		TextView userCity = (TextView) findViewById(R.id.dialog_userinfo_user_city);

		if (user == null)
			return;

		// Set values
		String name = user.getFullname();
		char firstChar = 0;
		if (name.length() != 0)
			firstChar = name.charAt(0);
		userImage.setText(firstChar + "");
		userImage.setBackgroundColor(LetterColor.of(firstChar));
		userFullname.setText(user.getFullname());
		userEmail.setText(user.getEmail());
		userSkype.setText(user.getSkype());
		userUrl.setText(user.getUrl());
		userCity.setText(user.getCity());
	}

}
