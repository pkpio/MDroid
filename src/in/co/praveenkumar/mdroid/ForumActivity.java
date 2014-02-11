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
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.models.Course;
import in.co.praveenkumar.mdroid.models.ForumThread;
import in.co.praveenkumar.mdroid.networking.FetchForum;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ForumActivity extends BaseActivity {
	ArrayList<ForumThread> threads = new ArrayList<ForumThread>();
	Course course = new Course();
	FetchForum FF = new FetchForum();
	LinearLayout loadingMsgLL;
	ProgressBar progBar;
	TextView progMsgTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum);

		// Get html data and extract courses
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		course.setId(extras.getString("cId"));
		course.setName(extras.getString("cName"));

		// Set course name
		TextView cNameTV = (TextView) findViewById(R.id.forum_course_name);
		cNameTV.setText(course.getName());

		// For progress message hide, unhide and messages
		loadingMsgLL = (LinearLayout) findViewById(R.id.forum_loading_message);
		progBar = (ProgressBar) findViewById(R.id.forum_progress_bar);
		progMsgTV = (TextView) findViewById(R.id.forum_progress_msg);

		// Fetch data from server and update UI
		AsyncForumFetch AFF = new AsyncForumFetch();
		AFF.execute();
	}

	private class AsyncForumFetch extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			loadingMsgLL.setVisibility(LinearLayout.VISIBLE);
			super.onPreExecute();
		}

		protected Long doInBackground(String... values) {
			FF.fetchForum(course.getId());
			return null;
		}

		protected void onPostExecute(Long result) {
			loadingMsgLL.setVisibility(LinearLayout.GONE);
			threads = FF.getThreads();
			listForumsInListView();
		}
	}

	private void listForumsInListView() {
		if (!threads.isEmpty()) {
			// Set title
			setTitle("Forum (" + threads.size() + ")");

			ListView listView = (ListView) findViewById(R.id.forum_list);
			MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
					threads);

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
		private ArrayList<ForumThread> threads;

		public MySimpleArrayAdapter(Context context,
				ArrayList<ForumThread> threads) {
			super(context, R.layout.forum_listview_layout);
			this.context = context;
			this.threads = threads;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.forum_listview_layout,
					parent, false);
			// Because position can't be used for it is not final
			final int pos = position;

			// Set forum details
			final TextView fSubView = (TextView) rowView
					.findViewById(R.id.forum_thread_subject);
			final TextView fRepCountView = (TextView) rowView
					.findViewById(R.id.forum_reply_count);
			final TextView fAuthView = (TextView) rowView
					.findViewById(R.id.forum_author);
			final TextView fDateView = (TextView) rowView
					.findViewById(R.id.forum_date_posted);
			fSubView.setText(threads.get(position).getSubject());
			fRepCountView.setText("(" + threads.get(position).getReplyCount()
					+ ")");
			fAuthView.setText(threads.get(position).getAuthor());
			fDateView.setText(threads.get(position).getDate());

			// Set onClickListeners on buttons
			final LinearLayout forumLL = (LinearLayout) rowView
					.findViewById(R.id.forum_thread);

			// textView.setHint(courseID.get(position));
			forumLL.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Open thread
					openThread(threads.get(pos).getId(), threads.get(pos)
							.getSubject());
				}
			});

			return rowView;
		}
	}

	private void openThread(String threadId, String threadSub) {
		Intent i = new Intent(this, ForumThreadActivity.class);
		i.putExtra("threadId", threadId + "&mode=1");
		i.putExtra("threadSub", threadSub);
		i.putExtra("cName", course.getName());
		startActivityForResult(i, 4);
	}
}
