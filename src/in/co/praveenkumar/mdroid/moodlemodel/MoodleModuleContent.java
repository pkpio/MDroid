package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MoodleModuleContent extends SugarRecord<MoodleModuleContent> {
	
	@SerializedName("type")
	String type;
	
	@SerializedName("filename")
	String filename;
	
	@SerializedName("filepath")
	String filepath;
	
	@SerializedName("filesize")
	int filesize;
	
	@SerializedName("fileurl")
	String fileurl;
	
	@SerializedName("content")
	String content;
	
	@SerializedName("timecreated")
	int timecreated;
	
	@SerializedName("timemodified")
	int timemodified;
	
	@SerializedName("sortorder")
	int sortorder;
	
	@SerializedName("userid")
	int userid;
	
	@SerializedName("author")
	String author;
	
	@SerializedName("license")
	String license;

}
