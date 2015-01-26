Author
----------------------
Praveen Kumar Pendyala <<praveen@praveenkumar.co.in>><br>
http://praveenkumar.co.in


License
----------------------
Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 
Unported license.

Read the complete license at,
http://creativecommons.org/licenses/by-nc-sa/3.0/


Docs
----------------------
The docs for MDroid can be found at http://mdroid.praveenkumar.co.in/ which are mirrored from the ```docs``` directory of this respository.


Dependencies
-----------------
- [ViewPagerIndicator](https://github.com/JakeWharton/ViewPagerIndicator)
- Google Playservices Library - Install Google Play Services Android SDK Manager
- Android support v7 Appcompact - Usually comes with latest Android Development Kit
- Other dependencies are included as jar files in ```libs/``` directory


Setting up MDroid
----------------------
1. Fork MDroid repository
2. Clone your fork to your machine
3. Import MDroid project to eclipse
4. Go to project properties
   - Choose ```Google APIs``` (instead of ```Android Open Source Project```) for build target (Recommend ```API level 19```)
   - Add dependencies listed above


Code contributions
--------------------
- Setup MDroid using above instructions
- Code ninja code!
- Push your commits to your fork
- Make a pull request 


Translating
---------------
- Download the strings file from ```res/values/string.xml``` (direct link)[https://github.com/praveendath92/MDroid/blob/master/res/values/strings.xml]
- All you need to do is translate words in this file as shown in the below example,
   English (untranslated) ```<string name="activity_tutorial_beautiful">Hello Beautiful!</string>```
   Spanish (translated)   ```<string name="activity_tutorial_beautiful">Hello compañero!</string>```
- Send me the new file with translated strings and done!

If you are familiar with Github, you may send a pull request with the new strings file placed in appropriate directory. Example, the Spanish translated strings would be at ```res/values-es/strings.xml``` You may find (this)[http://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android] helpful


Older versions
-----------------------
The earlier versions of MDroid, v1.0 - v5.x, were all built using parsing as a method to get data from Moodle. This could vary from each Moodle setup and is definitely not a recommended approach. Since, MDroid started of as an IIT Bombay Moodle support application I had nothing but that approach to take. I moved on to APIs approach now, which means web services are required for the current version of MDroid to work. You can still find the older versions code on other branches of this repository but it is highly recommended that you take the APIs approach as well.
