package in.co.praveenkumar.mdroid.helper;

import in.co.praveenkumar.R;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Work arounds implemented in the application
 * 
 * @author praveen
 * 
 */
public class Workarounds {

	/**
	 * Links the SwipeRefreshLayout and the ListView so that refresh gesture
	 * won't happen while attempting to scroll the ListView
	 * 
	 * @param swipeLayout
	 *            SwipeRefreshLayout
	 * @param listView
	 *            ListView
	 */
	public static void linkSwipeRefreshAndListView(
			final SwipeRefreshLayout swipeLayout, final ListView listView) {
		if (swipeLayout == null || listView == null)
			return;

		swipeLayout.setColorSchemeResources(R.color.refresh_yellow,
				R.color.refresh_green, R.color.refresh_red,
				R.color.refresh_blue);

		// Link swipeLayout with underlying listview
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = (listView == null || listView
						.getChildCount() == 0) ? 0 : listView.getChildAt(0)
						.getTop();
				swipeLayout.setEnabled(topRowVerticalPosition >= 0
						&& firstVisibleItem == 0);
			}
		});
	}

}
