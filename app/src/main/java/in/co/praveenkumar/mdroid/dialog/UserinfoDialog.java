package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.model.MoodleContact;
import in.co.praveenkumar.mdroid.model.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.model.MoodleUser;
import in.co.praveenkumar.mdroid.model.MoodleUserCourse;
import in.co.praveenkumar.mdroid.task.ContactSyncTask;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserinfoDialog extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	SessionSetting session;
	MoodleSiteInfo siteinfo;
	MoodleUser user;
	List<MoodleUserCourse> mCourses;
	CourseListAdapter userCourseListAdapter;
	ListView userCourseList;
	Boolean isContact;

	// Widgets
	ImageView contactIcon;
	TextView userEmail;
	TextView userSkype;
	TextView userUrl;
	TextView userCity;

	public UserinfoDialog(Context context, long siteid, int userid) {
		super(context);
		this.context = context;
		this.session = new SessionSetting(context);
		siteinfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, siteid);
		List<MoodleUser> mUsers = MoodleUser.find(MoodleUser.class,
				"userid = ? and siteid = ?", userid + "", siteid + "");
		if (mUsers != null && mUsers.size() > 0)
			user = mUsers.get(0);
		mCourses = MoodleUserCourse
				.find(MoodleUserCourse.class, "userid = ? and siteid = ?",
						user.getUserid() + "", siteid + "");

		// Check if this user is a contact
		List<MoodleContact> mContacts = MoodleContact.find(MoodleContact.class,
				"contactid = ? and siteid = ?", userid + "", siteid + "");
		isContact = (mContacts != null && mContacts.size() != 0);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_userinfo);
		TextView userImage = (TextView) findViewById(R.id.dialog_userinfo_user_image);
		TextView userFullname = (TextView) findViewById(R.id.dialog_userinfo_user_fullname);
		ImageView messageIcon = (ImageView) findViewById(R.id.dialog_userinfo_message_icon);
		contactIcon = (ImageView) findViewById(R.id.dialog_userinfo_contact_icon);

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
		userEmail = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_email);
		userSkype = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_skype);
		userUrl = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_url);
		userCity = (TextView) infoHeaderView
				.findViewById(R.id.dialog_userinfo_user_city);

		if (user == null)
			return;

		// Set OnClickListeners
		userEmailLayout.setOnClickListener(this);
		userSkypeLayout.setOnClickListener(this);
		userUrlLayout.setOnClickListener(this);
		userCityLayout.setOnClickListener(this);
		messageIcon.setOnClickListener(this);
		contactIcon.setOnClickListener(this);

		// Set values
		// Name and Image
		String name = user.getFullname();
		char firstChar = 0;
		if (name.length() != 0)
			firstChar = name.charAt(0);
		userImage.setText(firstChar + "");
		userImage.setBackgroundColor(LetterColor.of(firstChar));
		userFullname.setText(user.getFullname());

		// Contact icon
		if (isContact)
			contactIcon.setImageResource(R.drawable.icon_contact_remove);
		else
			contactIcon.setImageResource(R.drawable.icon_contact_add);

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
		Intent DefaultIntent = new Intent(android.content.Intent.ACTION_SEND);
		DefaultIntent.setType("text/plain");

		switch (v.getId()) {
		case R.id.dialog_userinfo_message_icon:
			MoodleContact contact = new MoodleContact(user.getUserid(),
					user.getFullname());
			MessageDialog md = new MessageDialog(context);
			md.setContact(contact);
			md.show();
			break;
		case R.id.dialog_userinfo_contact_icon:
			new contactStateChangeBg().execute("");
			break;
		case R.id.dialog_userinfo_layout_email:
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", user.getEmail(), null));
			context.startActivity(Intent.createChooser(intent, "Send Email"));
			break;
		case R.id.dialog_userinfo_layout_skype:
			DefaultIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					userSkype.getText());
			context.startActivity(Intent.createChooser(DefaultIntent,
					"Choose action for Skype"));
			break;
		case R.id.dialog_userinfo_layout_url:
			DefaultIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					userUrl.getText());
			context.startActivity(Intent.createChooser(DefaultIntent,
					"Choose action for URL"));
			break;
		case R.id.dialog_userinfo_layout_location:
			DefaultIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					userCity.getText());
			context.startActivity(Intent.createChooser(DefaultIntent,
					"Choose action for Location"));
			break;
		}

	}

	private class contactStateChangeBg extends
			AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			if (isContact)
				contactIcon.setImageResource(R.drawable.icon_contact_add);
			else
				contactIcon.setImageResource(R.drawable.icon_contact_remove);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			ContactSyncTask cst = new ContactSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			Boolean status = isContact ? cst.RemoveContact(user) : cst
					.AddContact(user);
			if (status) {
				/**
				 * Delete contact if it has been removed. Removed => currently a
				 * contact.
				 */
				if (isContact)
					MoodleContact.deleteAll(MoodleContact.class,
							"siteid = ? and contactid = ?",
							session.getCurrentSiteId() + "", user.getUserid()
									+ "");
				cst.syncAllContacts();
			}
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
				isContact = !isContact;
			else
				Toast.makeText(context, "Action failed!", Toast.LENGTH_LONG)
						.show();

			if (isContact)
				contactIcon.setImageResource(R.drawable.icon_contact_remove);
			else
				contactIcon.setImageResource(R.drawable.icon_contact_add);
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
