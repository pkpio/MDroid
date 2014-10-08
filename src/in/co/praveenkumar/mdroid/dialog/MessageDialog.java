package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.legacy.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContact;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageDialog extends Dialog {
	Context context;
	MoodleContact contact = new MoodleContact();

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
	}

	public void setContact(MoodleContact contact) {
		this.contact = contact;
	}

}
