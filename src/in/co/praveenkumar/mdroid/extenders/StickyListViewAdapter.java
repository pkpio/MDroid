/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 29th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.extenders;

import in.co.praveenkumar.mdroid.apis.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class StickyListViewAdapter extends BaseAdapter implements
		StickyListHeadersAdapter, SectionIndexer {

	protected ArrayList<String> mDataSet;
	protected int[] mSectionIndices;
	protected Character[] mSectionLetters;
	protected LayoutInflater mInflater;
	
	public StickyListViewAdapter(Context context, ArrayList<String> dataSet) {
		if (dataSet != null) {
			mDataSet = dataSet;
			mInflater = LayoutInflater.from(context);
			mSectionIndices = getSectionIndices();
			mSectionLetters = getSectionLetters();
		}
	}

	private int[] getSectionIndices() {
		ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
		char lastFirstChar = mDataSet.get(0).charAt(0);
		sectionIndices.add(0);
		for (int i = 1; i < mDataSet.size(); i++) {
			if (mDataSet.get(i).charAt(0) != lastFirstChar) {
				lastFirstChar = mDataSet.get(i).charAt(0);
				sectionIndices.add(i);
			}
		}
		int[] sections = new int[sectionIndices.size()];
		for (int i = 0; i < sectionIndices.size(); i++) {
			sections[i] = sectionIndices.get(i);
		}
		return sections;
	}

	private Character[] getSectionLetters() {
		Character[] letters = new Character[mSectionIndices.length];
		for (int i = 0; i < mSectionIndices.length; i++) {
			letters[i] = mDataSet.get(mSectionIndices[i]).charAt(0);
		}
		return letters;
	}

	@Override
	public int getCount() {
		return mDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_left_drawer, parent,
					false);
			holder.text = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(mDataSet.get(position));

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.sticky_section_header,
					parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		// set header text as first char in name
		CharSequence headerChar = mDataSet.get(position).subSequence(0, 1);
		holder.text.setText(headerChar);

		return convertView;
	}

	/**
	 * Remember that these have to be static, postion=1 should always return the
	 * same Id that is.
	 */
	@Override
	public long getHeaderId(int position) {
		// return the first character of the country as ID because this is what
		// headers are based upon
		return mDataSet.get(position).subSequence(0, 1).charAt(0);
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}

	class HeaderViewHolder {
		TextView text;
	}

	public class ViewHolder {
		public TextView text;
	}

}
