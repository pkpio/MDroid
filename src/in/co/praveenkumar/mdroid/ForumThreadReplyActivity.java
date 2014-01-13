/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	14-01-2014
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */


package in.co.praveenkumar.mdroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.networking.ForumThreadReplier;

public class ForumThreadReplyActivity extends BaseActivity {
	private String threadReplyId = "";
	private EditText replyContentET;
	private ForumThreadReplier FTR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum_reply);

		// Get threadId and thread subject
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		threadReplyId = extras.getString("threadReplyId");
		FTR = new ForumThreadReplier(threadReplyId);

		replyContentET = (EditText) findViewById(R.id.forum_thread_reply_reply_text);
		Button replyBtn = (Button) findViewById(R.id.forum_thread_reply_reply_button);

		// Set OnClickListener
		replyBtn.setOnClickListener(ReplyBtnListen);

	}

	private OnClickListener ReplyBtnListen = new OnClickListener() {
		public void onClick(View v) {
			AsyncThreadReply ATR = new AsyncThreadReply();
			ATR.execute();
		}
	};

	private class AsyncThreadReply extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		protected Long doInBackground(String... values) {
			FTR.postReply();
			return null;
		}

		protected void onPostExecute(Long result) {

		}
	}

}
