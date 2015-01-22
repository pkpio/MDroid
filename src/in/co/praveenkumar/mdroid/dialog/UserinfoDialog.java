package in.co.praveenkumar.mdroid.dialog;

import java.io.File;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ImageDecoder;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserinfoDialog extends Dialog {
	Context context;
	MoodleSiteInfo siteinfo;

	public UserinfoDialog(Context context, long siteid) {
		super(context);
		this.context = context;
		siteinfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, siteid);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_userinfo);

		// Get account info for dialog
		TextView userfullname = (TextView) findViewById(R.id.dialog_logout_user_fullname);
		TextView sitename = (TextView) findViewById(R.id.dialog_logout_sitename);
		ImageView userimage = (ImageView) findViewById(R.id.dialog_logout_user_image);
		Button confirmbutton = (Button) findViewById(R.id.dialog_logout_confirm);
		Button cancelbutton = (Button) findViewById(R.id.dialog_logout_cancel);
	}

}
