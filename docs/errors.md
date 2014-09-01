This is a list of known errors (not bugs) - explaination for the error or errorcode that you may have encountered in the application

- Login errors
  * web services not enabled: 
    Occurs when web services are not enabled on your site. 
    It is also possible that the web services but tokens for the particular user. If you are the Moodle admin too, check webservices -> tokens
    and make sure that the user you are trying to login as has a token generated.

  * access restricted: 
    Occurs when there is token generated for the user but the webservice being used requires more capabilities than what the user have.
    You may try adding those missing capabilities to the user or removing those functions from webservices. Missing capabilities are listed
    when you access webservices -> tokens section and look for the user

  * No host found:
    Well, there is an issue with your internet connection or the moodle url you gave

- Other errors
  * Permission denied:
    The user does not have access to that particular section. Example: User may have access to My Courses but not all Courses in the Moodle site.


While attempts have been made to list all non-trivial errors, it is possible that some of them may have missed my notice. If you encounter an
error which is not abvious, please write to me. I will try to fix it, if it can be or document it on this page.

PS: Credits will be given :P
