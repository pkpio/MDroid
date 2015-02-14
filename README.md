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
- ```ViewPagerIndicator``` from [here][1]
- ```Google Playservices Library``` - Get Playservices from SDK Manager
- ```Android support v7 Appcompact``` - Usually comes with latest Android Development Kit
- Other dependencies are included as jar files in ```libs/``` directory


Setting up MDroid
----------------------
1. Fork MDroid repository
2. Clone your fork to your machine
3. Import MDroid project to eclipse
4. Go to project properties
   - Choose ```Google APIs``` with target ```API level 19```
   - Add dependencies listed above


Code contributions
--------------------
- Setup MDroid using above instructions
- Code ninja code!
- Push your commits to your fork
- Make a pull request 


Translating
--------------------
MDroid Translation page: https://crowdin.com/project/mdroid


Older versions
-----------------------
The earlier versions of MDroid, v1.0 - v5.x, were all built using parsing as a method to get data from Moodle. This could vary from each Moodle setup and is definitely not a recommended approach. Since, MDroid started of as an IIT Bombay Moodle support application I had nothing but that approach to take. I moved on to APIs approach now, which means web services are required for the current version of MDroid to work. You can still find the older versions code on other branches of this repository but it is highly recommended that you take the APIs approach as well.

[1]: https://github.com/JakeWharton/ViewPagerIndicator
[2]: https://github.com/praveendath92/MDroid/blob/master/res/values/strings.xml
[3]: http://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android
