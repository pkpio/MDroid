/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
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

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseFTActivity;
import in.co.praveenkumar.mdroid.helpers.Colors.Color;
import in.co.praveenkumar.mdroid.networking.FetchForumThread;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ForumThreadActivity extends BaseFTActivity {
	private ArrayList<String> threadPostSubs = new ArrayList<String>();
	private ArrayList<String> threadPostAuthors = new ArrayList<String>();
	private ArrayList<String> threadPostDates = new ArrayList<String>();
	private ArrayList<String> threadPostContent = new ArrayList<String>();
	private int nThreadPosts;
	private String threadId = "";
	private String threadReplyId = "";
	FetchForumThread FFT = new FetchForumThread();
	LinearLayout loadingMsgLL;
	ProgressBar progBar;
	TextView progMsgTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum_thread);

		// Get threadId and thread subject
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		threadId = extras.getString("threadId");
		// String threadSub = extras.getString("threadSub");
		String cName = extras.getString("cName");

		// Set course name
		TextView cNameTV = (TextView) findViewById(R.id.forum_thread_course_name);
		cNameTV.setText(cName);

		// For progress message hide, unhide and messages
		loadingMsgLL = (LinearLayout) findViewById(R.id.forum_thread_loading_message);
		progBar = (ProgressBar) findViewById(R.id.forum_thread_progress_bar);
		progMsgTV = (TextView) findViewById(R.id.forum_thread_progress_msg);

		// Fetch data from server and update UI
		AsyncThreadFetch ATF = new AsyncThreadFetch();
		ATF.execute();
	}

	private class AsyncThreadFetch extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			loadingMsgLL.setVisibility(LinearLayout.VISIBLE);
			super.onPreExecute();
		}

		protected Long doInBackground(String... values) {
			FFT.fetchThread(threadId);
			return null;
		}

		protected void onPostExecute(Long result) {
			loadingMsgLL.setVisibility(LinearLayout.GONE);
			threadPostSubs = FFT.getPostSubject();
			threadPostAuthors = FFT.getPostAuthors();
			threadPostDates = FFT.getPostDates();
			threadPostContent = FFT.getPostContent();
			nThreadPosts = FFT.getPostsCount();
			threadReplyId = FFT.getReplyId();
			listPostsInListView();
		}
	}

	private void listPostsInListView() {
		if (!threadPostSubs.isEmpty()) {
			// Set title
			setTitle("Forum thread (" + nThreadPosts + ")");

			ListView listView = (ListView) findViewById(R.id.forum_thread_list);
			MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
					threadPostSubs, threadPostAuthors, threadPostDates,
					threadPostContent);

			// Assign adapter to ListView
			listView.setAdapter(adapter);
		} else {
			loadingMsgLL.setVisibility(LinearLayout.VISIBLE);
			progBar.setVisibility(ProgressBar.GONE);
			progMsgTV.setText("No posts.");
			MainActivity.toaster.showToast("No posts found.");
		}
	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private ArrayList<String> threadPostSubs;
		private ArrayList<String> threadPostAuthors;
		private ArrayList<String> threadPostDates;
		private ArrayList<String> threadPostContent;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> threadPostSubs,
				ArrayList<String> threadPostAuthors,
				ArrayList<String> threadPostDates,
				ArrayList<String> threadPostContent) {
			super(context, R.layout.forum_thread_listview_layout,
					threadPostSubs);
			this.context = context;
			this.threadPostSubs = threadPostSubs;
			this.threadPostAuthors = threadPostAuthors;
			this.threadPostDates = threadPostDates;
			this.threadPostContent = threadPostContent;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.forum_thread_listview_layout, parent, false);

			// Set post details
			final TextView pSubView = (TextView) rowView
					.findViewById(R.id.forum_thread_post_subject);
			final TextView pAuthView = (TextView) rowView
					.findViewById(R.id.forum_thread_post_author);
			final TextView pDateView = (TextView) rowView
					.findViewById(R.id.forum_thread_post_time);
			final TextView pContentView = (TextView) rowView
					.findViewById(R.id.forum_thread_post_content);
			final TextView pAuthImgView = (TextView) rowView
					.findViewById(R.id.forum_thread_author_img);
			pSubView.setText(threadPostSubs.get(position));
			pAuthView.setText(threadPostAuthors.get(position));
			pDateView.setText(threadPostDates.get(position));
			pContentView.setText(threadPostContent.get(position));

			// Set author Image text and color
			try {
				char FL = threadPostAuthors.get(position).toUpperCase()
						.charAt(0);
				String cs = Color.getColor(FL);
				pAuthImgView.setText(FL + "");
				pAuthImgView.setBackgroundColor(android.graphics.Color
						.parseColor(cs));
			} catch (StringIndexOutOfBoundsException e) {

			}

			return rowView;
		}
	}

	@Override
	public void replyInThread() {
		if (!threadReplyId.contentEquals("")) {
			MainActivity.toaster
					.showToast("This feature will be available soon!");
			// Intent i = new Intent(this, ForumThreadReplyActivity.class);
			// i.putExtra("threadReplyId", threadReplyId);
			// startActivityForResult(i, 13);
		} else
			MainActivity.toaster.showToast("Error fetching reply id");
	}
}
