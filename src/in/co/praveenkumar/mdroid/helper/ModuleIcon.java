package in.co.praveenkumar.mdroid.helper;

import java.util.ArrayList;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleModule;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleModuleContent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ModuleIcon {

	public static Bitmap of(MoodleModule module, Context context) {
		String modname = module.getModname();

		if (modname.contentEquals("forum"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_messages);

		if (modname.contentEquals("assign"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_note_taking);

		// -TODO- Icon
		if (modname.contentEquals("label"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_note_taking);

		// -TODO- Icon
		if (modname.contentEquals("page"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_note_taking);

		// -TODO- Icon
		if (modname.contentEquals("url"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.module_book);

		// -TODO- Icon
		if (modname.contentEquals("glossary"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_note_taking);

		// -TODO- Icon
		if (modname.contentEquals("book"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.module_book);

		// -TODO- Icon
		if (modname.contentEquals("quiz"))
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.module_book);

		if (modname.contentEquals("resource")) {
			ArrayList<MoodleModuleContent> contents = module.getContents();

			if (contents == null)
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_unknown);

			if (contents.size() == 0)
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_unknown);

			String filename = contents.get(0).getFilename();
			int posDot = filename.indexOf(".");
			String fileExt = filename.substring(posDot + 1, filename.length());

			if (fileExt.contentEquals("pdf"))
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_pdf);

			// -TODO- More doc formats
			if (fileExt.contentEquals("doc") || fileExt.contentEquals("docx"))
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_excel);

			// -TODO- More excel formats
			if (fileExt.contentEquals("xls"))
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_video);

			// -TODO- More zip formats
			if (fileExt.contentEquals("zip") || fileExt.contentEquals("rar"))
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_zip);

			if (fileExt.contentEquals("mp4"))
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_video);

			if (fileExt.contentEquals("swf"))
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.format_video);
		}

		return BitmapFactory.decodeResource(context.getResources(),
				R.drawable.format_unknown);
	}

}
