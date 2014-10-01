###1. Multiple accounts
  <b>Status:</b> Implemented<br/>
  <b>Description:</b> Support multiple accounts at the same time in the app. Accounts can be on the same Moodle server or on different moodle servers

2. Paranoid login
  Status: Implemented
  Description: Login without password. Authenticated using a token given by Moodle site. This way your password is safe.

3. Intelligent file download
  Status: Implemented
  Description: Intelligent file download using system downloader. Your file downloads are persistant across network failures, switching networks (between Mobile data and Wifi) and even device reboots!

4. Calender
  Status: Partially implemented
  Description: Moodle site calender. Includes a calender in the courses for course specific events. Adding events to be implemented.

5. Messaging
  Status: Partially implemented
  Description: Lists unread message counts. Indications of sender status - Online, Offline, Stranger. Option to send messages to be implemented. Reading messages in not supported in Moodle APIs but we will be using webview in the app to read messages.

6. Course participants
  Status: Not implemented
  Description: Adds a section in the course to show the list of participants in that course. Will also have options to add a participant as a contact, view profile and send messages

7. New content notifications
  Status: Not implemented
  Description: Periodic checking for new contents and sending notification to user. Options will given to set the frequency of checking and more.

8. Uploading files
  Status: Not implemented
  Description: User will be able to upload files to the Moodle server to their private files section. Depending on the support in Moodle APIs, this will be extended to other sections.

9. Forums
  Status: Implemented
  Description: Listing forums, topics inside a forum and post in a topic implemented. Reply option is not supported by Moodle APIs so, webview is used to fulfil that.

10. Notes
  Status: Not implemented
  Description: Viewing notes created by the user. Will also be having an option to create notes for a user, course or personal note.
