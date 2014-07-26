/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 31st May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdriod.parsers;

import in.co.praveenkumar.mdroid.models.MoodleCourseContent;
import in.co.praveenkumar.mdroid.models.MoodleCourseModule;
import in.co.praveenkumar.mdroid.models.MoodleCourseSection;
import in.co.praveenkumar.mdroid.models.MoodleFile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseContentParser {
	private MoodleCourseContent courseContent = new MoodleCourseContent();
	private String error;

	// json tags
	private static final String TAG_ERROR = "error";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_SUMMARY = "summary";
	private static final String TAG_SUMMARY_FORMAT = "summaryformat";
	private static final String TAG_MODULES = "modules";
	private static final String TAG_URL = "url";
	private static final String TAG_MOD_NAME = "modname";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CONTENTS = "contents";
	private static final String TAG_FILE_NAME = "filename";
	private static final String TAG_FILE_SIZE = "filesize";
	private static final String TAG_FILE_URL = "fileurl";
	private static final String TAG_FILE_CREATED = "timecreated";
	private static final String TAG_FILE_MODIFIED = "timemodified";
	private static final String TAG_FILE_AUTHOR = "author";

	// Expected values in JSON
	private static final String VALUE_RESOURCE = "resource";

	public CourseContentParser(String json) {

		// The response will be a JSONArray while error gives a JSONObject
		// So, we first see if there are any contents, else we shall look for
		// error by taking it as a JSONObject.
		try {
			JSONArray jArray = new JSONArray(json);

			// Check if it has courses
			if (jArray.length() > 0)
				parseContents(jArray);

			// No contents
			else if (jArray.length() == 0)
				error += "\n No contents found";

			// An unknown error
			else
				error += "\n Unknown error";

		} catch (JSONException e) {
			e.printStackTrace();

			// Course reading failed. Check if there was an error
			// thrown by Moodle.
			try {
				JSONObject jObj = new JSONObject(json);

				// Error thrown by Moodle
				if (jObj.has(TAG_ERROR))
					error += "\n" + jObj.getString(TAG_ERROR);

				// Unknown error.
				else
					error += "\n Unknown error";

			} catch (JSONException e1) {
				// Decode failed.
				error += "\n json decode error";

				e1.printStackTrace();
			}

		}
	}

	// Recursively parse each section and build contents
	private void parseContents(JSONArray jContentArray) {
		JSONObject jSectionObj;
		for (int i = 0; i < jContentArray.length(); i++) {
			try {
				jSectionObj = jContentArray.getJSONObject(i);
				MoodleCourseSection section = new MoodleCourseSection();

				section.setId(jSectionObj.getString(TAG_ID));
				section.setName(jSectionObj.getString(TAG_NAME));
				section.setSummary(jSectionObj.getString(TAG_SUMMARY));
				section.setSummaryformat(jSectionObj
						.getString(TAG_SUMMARY_FORMAT));
				section.setModules(parseModules(
						jSectionObj.getJSONArray(TAG_MODULES),
						jSectionObj.getString(TAG_ID),
						jSectionObj.getString(TAG_NAME)));

				courseContent.addSection(section);
			} catch (JSONException e) {
				error += "\n error parsing sections in course";
				e.printStackTrace();
			}
		}
	}

	// Recursively parse Module for Modules ArrayList
	private ArrayList<MoodleCourseModule> parseModules(JSONArray jModulesArray,
			String sectionid, String sectionname) {
		ArrayList<MoodleCourseModule> modules = new ArrayList<MoodleCourseModule>();
		JSONObject jModuleObj;

		for (int i = 0; i < jModulesArray.length(); i++) {
			try {
				jModuleObj = jModulesArray.getJSONObject(i);
				MoodleCourseModule module = new MoodleCourseModule();

				module.setId(jModuleObj.getString(TAG_ID));
				module.setUrl(jModuleObj.getString(TAG_URL));
				module.setName(jModuleObj.getString(TAG_NAME));
				module.setSectionid(sectionid);
				module.setSectionname(sectionname);

				// Module may or may not have a description
				if (jModuleObj.has(TAG_DESCRIPTION))
					module.setDescription(jModuleObj.getString(TAG_DESCRIPTION));

				// Is this is a resource? If so, get files. Nothing otherwise.
				if (jModuleObj.getString(TAG_MOD_NAME).contentEquals(
						VALUE_RESOURCE)) {
					module.setHasFiles(true);
					module.setFiles(parseFiles(jModuleObj
							.getJSONArray(TAG_CONTENTS)));
				} else {
					module.setHasFiles(false);
				}

				modules.add(module);
			} catch (JSONException e) {
				error += "\n error parsing sections in course";
				e.printStackTrace();
			}
		}
		return modules;
	}

	private ArrayList<MoodleFile> parseFiles(JSONArray jFilesArray) {
		ArrayList<MoodleFile> files = new ArrayList<MoodleFile>();
		JSONObject jFileObj;

		for (int i = 0; i < jFilesArray.length(); i++) {
			try {
				jFileObj = jFilesArray.getJSONObject(i);
				MoodleFile file = new MoodleFile();

				file.setFilename(jFileObj.getString(TAG_FILE_NAME));
				file.setFilesize(jFileObj.getLong(TAG_FILE_SIZE));
				file.setFileurl(jFileObj.getString(TAG_FILE_URL));
				file.setTimecreated(jFileObj.getLong(TAG_FILE_CREATED));
				file.setTimemodified(jFileObj.getLong(TAG_FILE_MODIFIED));
				file.setAuthor(jFileObj.getString(TAG_FILE_AUTHOR));

				files.add(file);
			} catch (JSONException e) {
				error += "\n error parsing files in a module";
				e.printStackTrace();
			}
		}

		return files;
	}

	public MoodleCourseContent getCourseContents() {
		return courseContent;
	}

	public String getError() {
		return error;
	}

}
