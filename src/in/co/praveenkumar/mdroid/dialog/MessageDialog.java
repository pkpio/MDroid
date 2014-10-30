package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContact;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleMessage;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestMessage;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageDialog extends Dialog implements
		android.view.View.OnClickListener {
	Context context;
	MoodleContact contact = new MoodleContact();
	EditText messageET;

	public MessageDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);

		// Setup widget values
		TextView userImage = (TextView) findViewById(R.id.dialog_contact_image);
		TextView userName = (TextView) findViewById(R.id.dialog_contact_name);
		LinearLayout headerLine = (LinearLayout) findViewById(R.id.dialog_header_line);
		TextView unreadCount = (TextView) findViewById(R.id.dialog_unread_count);
		ImageView sendButton = (ImageView) findViewById(R.id.dialog_message_sendbutton);
		messageET = (EditText) findViewById(R.id.dialog_message_text);

		// Set values
		char letter = (contact.getFullname().length() > 0) ? contact
				.getFullname().charAt(0) : '0';
		userImage.setText(letter + "");
		userName.setText(contact.getFullname());
		userImage.setBackgroundColor(LetterColor.of(letter));
		headerLine.setBackgroundColor(LetterColor.of(letter));
		if (contact.getUnread() > 0)
			unreadCount.setText(contact.getUnread() + "");
		else
			unreadCount.setVisibility(TextView.GONE);

		// Send button listener
		sendButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_message_sendbutton:
			SessionSetting session = new SessionSetting(context);
			new AsyncMessageSender(session.getmUrl(), session.getToken(),
					contact.getContactid(), messageET.getText().toString())
					.execute("");
			break;
		}
		dismiss();
	}

	public void setContact(MoodleContact contact) {
		this.contact = contact;
	}

	private class AsyncMessageSender extends
			AsyncTask<String, Integer, Boolean> {
		String mUrl;
		String token;
		int userid;
		String message;
		MoodleRestMessage mrm;

		public AsyncMessageSender(String mUrl, String token, int userid,
				String message) {
			this.mUrl = mUrl;
			this.token = token;
			this.userid = userid;
			this.message = message;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			mrm = new MoodleRestMessage(mUrl, token);
			MoodleMessage mMessage = new MoodleMessage(userid, message);
			return mrm.sendMessage(mMessage);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result)
				Toast.makeText(context,
						"Message sending failed. Error: " + mrm.getError(),
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT)
						.show();
		}

	}

}
