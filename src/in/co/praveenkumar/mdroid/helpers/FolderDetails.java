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

package in.co.praveenkumar.mdroid.helpers;

import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.models.Course;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class FolderDetails {
	private final String DEBUG_TAG = "HELPERS_FOLDER_DETAILS";
	private ArrayList<String> sDirNames = new ArrayList<String>();
	private ArrayList<String> sDirTimes = new ArrayList<String>();
	private ArrayList<String> sDirFileCounts = new ArrayList<String>();
	private ArrayList<String> fileNames = new ArrayList<String>();
	private ArrayList<String> fileTimes = new ArrayList<String>();
	private ArrayList<String> fileSizes = new ArrayList<String>();
	private ArrayList<Course> courses = new ArrayList<Course>();
	private int sDirCount = 0;
	private int fileCount = 0;
	private Boolean exist = false;
	private String fileId = ""; // If file exists then its path

	@SuppressLint("SimpleDateFormat")
	public FolderDetails(File f) {
		// Check if file exists. This is needed for FilesActivity class.
		// Mostly the file doesn't exist due to extension differences.
		if (f.exists()) {
			findFileDetails(f);
		} else {
			checkFileNoExtension(f);
		}
	}

	// For creating folders for course in CourseActivity
	public FolderDetails(ArrayList<Course> courses) {
		this.courses = courses;
	}

	public ArrayList<Course> createCourseFolders() {
		for (int i = 0; i < courses.size(); i++) {
			String tempCourseName = "";
			tempCourseName = courses.get(i).getName().replaceAll(":", "-");
			tempCourseName = android.text.Html.fromHtml(tempCourseName)
					.toString();

			courses.get(i).setName(tempCourseName);
			File file = new File(Environment.getExternalStorageDirectory(),
					"/MDroid/" + tempCourseName + "/");
			if (!file.exists()) {
				if (!file.mkdirs()) {
					Log.d(DEBUG_TAG, "Problem creating course folder");
					MainActivity.toaster.showToast("failed to create folder "
							+ file);
				}
			}
		}
		return courses;
	}

	private void findFileDetails(File f) {
		// An instance of Relative data modified finder
		RelativeLastModified rlm = new RelativeLastModified();

		File[] files = f.listFiles();
		for (File file : files) {
			int startIndex;
			int endIndex;
			String fileName = file.toString();
			startIndex = fileName.lastIndexOf("/") + 1;
			endIndex = fileName.length();
			fileName = fileName.substring(startIndex, endIndex);
			File filePath = new File(f + "/" + fileName);

			// Isolate unnecessary files/folders
			if (fileName != ".android_secure" && fileName != "LOST.DIR") {

				// Get data modified for File/Folder. Needed in both cases.
				Date lastModDate = new Date(filePath.lastModified());
				String lastModDateformatted = rlm.getRelativeTime(lastModDate);

				// Folder case
				if (file.isDirectory()) {
					// Getting # files in that folder.
					File filesInFolder = new File(filePath + "/");
					int filesCountInFolder = filesInFolder.listFiles().length;

					sDirNames.add(fileName);
					sDirTimes.add(lastModDateformatted);
					sDirFileCounts.add(filesCountInFolder + "");
					sDirCount++;
				}

				// File case
				else {
					// Getting file size
					long fileSize = filePath.length();
					String fileSizeInfo;
					if (fileSize > (1024 * 1024))
						fileSizeInfo = fileSize / (1024 * 1024) + " MB";
					else if (fileSize > 1024)
						fileSizeInfo = fileSize / 1024 + " KB";
					else
						fileSizeInfo = fileSize + " Bytes";
					fileNames.add(fileName);
					fileTimes.add(lastModDateformatted);
					fileSizes.add(fileSizeInfo);
					fileCount++;
				}
			}
		}
	}

	// This trims the extensions of each file in the directory
	// Then checks if a file exists
	private void checkFileNoExtension(File f) {
		// parse out directory name from f.
		int startIndex = 0;
		int endIndex;
		String filePath = f.toString();
		endIndex = filePath.lastIndexOf("/") + 1;
		String dirPath = filePath.substring(startIndex, endIndex);

		// Parse file name without extension for checking match.
		startIndex = endIndex;
		endIndex = filePath.length();
		String fNameWOExt = filePath.substring(startIndex, endIndex);

		// Fetch all files in dir and trim their extensions
		File dir = new File(dirPath);
		ArrayList<String> fileNames = new ArrayList<String>();
		ArrayList<String> fileExts = new ArrayList<String>();
		File[] files = dir.listFiles();
		if (files != null)
			for (File file : files) {
				if (!(file.isDirectory())) {
					startIndex = 0;
					endIndex = 0;
					String dFileName = file.toString();

					// Extract just the file name - trim the path.
					startIndex = dFileName.lastIndexOf("/") + 1;
					endIndex = dFileName.length();
					dFileName = dFileName.substring(startIndex, endIndex);

					// Separate name and Extension
					startIndex = 0;
					endIndex = dFileName.lastIndexOf(".");
					String dFileNameWOExt = dFileName.substring(startIndex,
							endIndex);
					startIndex = endIndex + 1;
					endIndex = dFileName.length();
					String dFileExt = dFileName.substring(startIndex, endIndex);
					if (dFileNameWOExt.equals("") == false
							&& dFileExt.equals("") == false) {
						fileNames.add(dFileNameWOExt);
						fileExts.add(dFileExt);
					}

				}
			}

		// Check for matches in trimmed names
		for (int i = 0; i < fileNames.size(); i++) {
			if (fileNames.get(i).equals(fNameWOExt) == true) {
				fileId = dirPath + fileNames.get(i) + "." + fileExts.get(i);

				exist = true;
				break;
			}
		}

	}

	public ArrayList<String> getSubFolderNames() {
		return sDirNames;
	}

	public ArrayList<String> getSubFolderDateModified() {
		return sDirTimes;
	}

	public ArrayList<String> getSubFolderFileCount() {
		return sDirFileCounts;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public ArrayList<String> getFilesDateModified() {
		return fileTimes;
	}

	public ArrayList<String> getFilesSize() {
		return fileSizes;
	}

	public int folderCount() {
		return sDirCount;
	}

	public int fileCount() {
		return fileCount;
	}

	public Boolean existWOExtCheck() {
		return exist;
	}

	public String getFileSize() {
		// Getting file size
		File f = new File(fileId);
		long fileSize = f.length();
		String fileSizeInfo;
		if (fileSize > (1024 * 1024))
			fileSizeInfo = fileSize / (1024 * 1024) + " MB";
		else if (fileSize > 1024)
			fileSizeInfo = fileSize / 1024 + " KB";
		else
			fileSizeInfo = fileSize + " Bytes";

		return fileSizeInfo;
	}

	public String getFileRelDate() {
		File f = new File(fileId);
		Date lastModDate = new Date(f.lastModified());
		RelativeLastModified rlm = new RelativeLastModified();
		String lastModDateformatted = rlm.getRelativeTime(lastModDate);
		return lastModDateformatted;
	}

	public String getFileId() {
		return fileId;
	}
}
