package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.mdroid.activity.CourseActivity;
import in.co.praveenkumar.mdroid.activity.LoginActivity;
import in.co.praveenkumar.mdroid.helper.ImageDecoder;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.model.MoodleContact;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleDiscussion;
import in.co.praveenkumar.mdroid.model.MoodleEvent;
import in.co.praveenkumar.mdroid.model.MoodleForum;
import in.co.praveenkumar.mdroid.model.MoodleMessage;
import in.co.praveenkumar.mdroid.model.MoodleModule;
import in.co.praveenkumar.mdroid.model.MoodleModuleContent;
import in.co.praveenkumar.mdroid.model.MoodlePost;
import in.co.praveenkumar.mdroid.model.MoodleSection;
import in.co.praveenkumar.mdroid.model.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.model.MoodleUser;
import in.co.praveenkumar.mdroid.model.MoodleUserCourse;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LogoutDialog extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	MoodleSiteInfo siteinfo;

	public LogoutDialog(Context context, long siteid) {
		super(context);
		this.context = context;
		siteinfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, siteid);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_logout);

		// Get account info for dialog
		TextView userfullname = (TextView) findViewById(R.id.dialog_logout_user_fullname);
		TextView sitename = (TextView) findViewById(R.id.dialog_logout_sitename);
		ImageView userimage = (ImageView) findViewById(R.id.dialog_logout_user_image);
		Button confirmbutton = (Button) findViewById(R.id.dialog_logout_confirm);
		Button cancelbutton = (Button) findViewById(R.id.dialog_logout_cancel);

		// Set values
		userfullname.setText(siteinfo.getFullname());
		sitename.setText(siteinfo.getSitename());
		Bitmap userImage = ImageDecoder
				.decodeImage(new File(Environment.getExternalStorageDirectory()
						+ "/MDroid/." + siteinfo.getId()));
		if (userImage != null)
			userimage.setImageBitmap(userImage);
		confirmbutton.setOnClickListener(this);
		cancelbutton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_logout_confirm:
			performLogout(siteinfo.getId());
			break;
		case R.id.dialog_logout_cancel:
			break;
		}
		dismiss();
	}

	public void performLogout(long siteid) {
		// Delete siteinfo
		MoodleSiteInfo.deleteAll(MoodleSiteInfo.class, "id = ?", siteid + "");

		// set a new site from db as current site. The below call will pick a
		// new site from db as current site
		SessionSetting session = new SessionSetting(context);

		// Now delete all other info related to that site
		MoodleContact.deleteAll(MoodleContact.class, "siteid = ?", siteid + "");
		MoodleCourse.deleteAll(MoodleCourse.class, "siteid = ?", siteid + "");
		MoodleDiscussion.deleteAll(MoodleDiscussion.class, "siteid = ?", siteid
				+ "");
		MoodleEvent.deleteAll(MoodleEvent.class, "siteid = ?", siteid + "");
		MoodleForum.deleteAll(MoodleForum.class, "siteid = ?", siteid + "");
		MoodleMessage.deleteAll(MoodleMessage.class, "siteid = ?", siteid + "");
		MoodleModule.deleteAll(MoodleModule.class, "siteid = ?", siteid + "");
		MoodleModuleContent.deleteAll(MoodleModuleContent.class, "siteid = ?",
				siteid + "");
		MoodlePost.deleteAll(MoodlePost.class, "siteid = ?", siteid + "");
		MoodleSection.deleteAll(MoodleSection.class, "siteid = ?", siteid + "");
		MoodleUser.deleteAll(MoodleUser.class, "siteid = ?", siteid + "");
		MoodleUserCourse.deleteAll(MoodleUserCourse.class, "siteid = ?", siteid + "");

		Intent i;
		if (session.getCurrentSiteId() == SessionSetting.NO_SITE_ID)
			i = new Intent(context, LoginActivity.class); // No more sites in db
		else
			i = new Intent(context, CourseActivity.class); // New site from db

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(i);
	}

}
