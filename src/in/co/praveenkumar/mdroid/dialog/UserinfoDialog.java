package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleUser;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleUserCourse;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UserinfoDialog extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	MoodleSiteInfo siteinfo;
	MoodleUser user;
	List<MoodleUserCourse> mCourses;
	CourseListAdapter userCourseListAdapter;
	ListView userCourseList;

	public UserinfoDialog(Context context, long siteid, int userid) {
		super(context);
		this.context = context;
		siteinfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, siteid);
		List<MoodleUser> mUsers = MoodleUser.find(MoodleUser.class,
				"userid = ? and siteid = ?", userid + "", siteid + "");
		if (mUsers != null && mUsers.size() > 0)
			user = mUsers.get(0);
		mCourses = MoodleUserCourse
				.find(MoodleUserCourse.class, "userid = ? and siteid = ?",
						user.getUserid() + "", siteid + "");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_userinfo);
		TextView userImage = (TextView) findViewById(R.id.dialog_userinfo_user_image);
		TextView userFullname = (TextView) findViewById(R.id.dialog_userinfo_user_fullname);

		// Set Info Header
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout infoHeaderView = (LinearLayout) inflater.inflate(
				R.layout.list_header_userinfo, null);

		// Get views
		LinearLayout userEmailLayout = (LinearLayout) infoHeaderView
				.findViewById(R.id.dialog_userinfo_layout_email);
		LinearLayout userSkypeLayout = (LinearLayout) infoHeaderView
				.findViewById(R.id.dialog_userinfo_layout_skype);
		LinearLayout userUrlLayout = (LinearLayout) infoHeaderView
				.findViewById(R.id.dialog_userinfo_layout_url);
		LinearLayout userCityLayout = (LinearLayout) infoHeaderView
				.findViewById(R.id.dialog_userinfo_layout_location);
		TextView userEmail = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_email);
		TextView userSkype = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_skype);
		TextView userUrl = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_url);
		TextView userCity = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_city);

		if (user == null)
			return;

		// Set OnClickListeners
		userEmailLayout.setOnClickListener(this);
		userEmail.setOnClickListener(this);
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

		// Set course list
		userCourseList = (ListView) findViewById(R.id.dialog_userinfo_user_infolist);
		userCourseList.addHeaderView(infoHeaderView);
		userCourseListAdapter = new CourseListAdapter(context);
		userCourseList.setAdapter(userCourseListAdapter);
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

	public class CourseListAdapter extends BaseAdapter {
		private final Context context;

		public CourseListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_usercourse,
						parent, false);

				viewHolder.shortname = (TextView) convertView
						.findViewById(R.id.list_course_shortname);
				viewHolder.fullname = (TextView) convertView
						.findViewById(R.id.list_course_fullname);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			final MoodleUserCourse mCourse = mCourses.get(position);
			viewHolder.shortname.setText(mCourse.getShortname());
			viewHolder.fullname.setText(mCourse.getFullname());

			return convertView;
		}

		@Override
		public int getCount() {
			return mCourses.size();
		}

		@Override
		public Object getItem(int position) {
			return mCourses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView shortname;
		TextView fullname;
	}

}
