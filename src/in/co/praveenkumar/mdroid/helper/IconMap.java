package in.co.praveenkumar.mdroid.helper;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.model.MoodleModule;
import in.co.praveenkumar.mdroid.model.MoodleModuleContent;

import java.util.ArrayList;

public class IconMap {

	/**
	 * Finds the icon for a given module
	 * 
	 * @param module
	 *            MoodleModule
	 * @return Icon resource
	 */
	public static int moduleIcon(MoodleModule module) {
		String modname = module.getModname();

		if (modname.contentEquals("forum"))
			return R.drawable.icon_messages;

		if (modname.contentEquals("assign"))
			return R.drawable.icon_note_taking;

		if (modname.contentEquals("label"))
			return R.drawable.icon_label;

		// -TODO- Icon
		if (modname.contentEquals("page"))
			return R.drawable.icon_file;

		// -TODO- Icon
		if (modname.contentEquals("choice"))
			return R.drawable.icon_file;

		// -TODO- Icon
		if (modname.contentEquals("url"))
			return R.drawable.icon_explorer;

		// -TODO- Icon
		if (modname.contentEquals("glossary"))
			return R.drawable.icon_glossary;

		// -TODO- Icon
		if (modname.contentEquals("book"))
			return R.drawable.format_book;

		// -TODO- Icon
		if (modname.contentEquals("quiz"))
			return R.drawable.icon_quiz;

		if (modname.contentEquals("resource")) {
			ArrayList<MoodleModuleContent> contents = module.getContents();

			if (contents == null)
				return R.drawable.format_unknown;

			if (contents.size() == 0)
				return R.drawable.format_unknown;

			String filename = contents.get(0).getFilename();
			int posDot = filename.indexOf(".");
			String fileExt = filename.substring(posDot + 1, filename.length());

			if (fileExt.contentEquals("pdf"))
				return R.drawable.format_pdf;

			// -TODO- More doc formats
			if (fileExt.contentEquals("doc") || fileExt.contentEquals("docx"))
				return R.drawable.format_excel;

			// -TODO- More excel formats
			if (fileExt.contentEquals("xls") || fileExt.contentEquals("xlsx"))
				return R.drawable.format_excel;

			// -TODO- More zip formats
			if (fileExt.contentEquals("zip") || fileExt.contentEquals("rar"))
				return R.drawable.format_zip;

			if (fileExt.contentEquals("mp4") || fileExt.contentEquals("swf"))
				return R.drawable.format_video;
		}

		return R.drawable.format_unknown;
	}

	/**
	 * Finds the icon for a given notification type
	 * 
	 * @param type
	 *            Notification type
	 * @return Icon resource
	 */
	public static int notificationIcon(int type) {
		switch (type) {
		case MDroidNotification.TYPE_CONTACT:
			return R.drawable.icon_people_color;

		case MDroidNotification.TYPE_PARTICIPANT:
			return R.drawable.icon_people_color;

		case MDroidNotification.TYPE_COURSE_CONTENT:
			return R.drawable.icon_inbox_download;

		case MDroidNotification.TYPE_EVENT:
			return R.drawable.icon_event_color;

		case MDroidNotification.TYPE_FORUM:
			return R.drawable.icon_forum_color;

		case MDroidNotification.TYPE_FORUM_REPLY:
			return R.drawable.icon_inbox_download;

		case MDroidNotification.TYPE_FORUM_TOPIC:
			return R.drawable.icon_inbox_download;

		case MDroidNotification.TYPE_MESSAGE:
			return R.drawable.icon_message_color;

		default:
			return R.drawable.icon_notifications_color;
		}
	}

}
