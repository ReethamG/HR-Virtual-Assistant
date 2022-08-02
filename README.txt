HOW TO RUN:
  Type in the terminal:
   - javac HRManager.java
   - java HRManager <sudo password> (if you don't have sudo access, don't put anything there)

When you run the program, you will be brought to a login system, which will recognize whether you are a employee (no admin access), or a manager (has admin access).
  -Enter your username, then your password (Check passwords through the text files, those are our "databases" for this demo project of HR Virtual Assitant)


If you log in as a manager:
  You will see a dropdown panel, with all the options you can do. Select the option you want, then hit OK or enter.
    -Do you want to see your information?
    -Do you want to see the pending requests file?
    -Do you want to see an employee's file?
    -Do you want to fire an employee?
    -Do you want to grant a raise?
    -Do you want to log out?

  Most of these are self-explanatory
    -Viewing your information requires no further input
    -Viewing pending requests will display each request one at a time; choose 1 to approve the request or 2 to deny it
    -Viewing an employee's file will prompt for the username of the employee you want to view, simply type this into the input box
    -Firing an employee will prompt for a username, again, type the username and that employee will have all of their files deleted
    -Granting a raise will prompt for both a username and a number, again, type the username, then the value of the raise, and if the sudo password has been provided, the employee will get that raise
    -Logging out requires no further input


If you log in as a employee:
  You will see a dropdown panel, with all the options you can do. Select the option you want, then hit OK or enter.
    -Do you want to make a request to the manager?
    -Do you want to see your information?
    -Do you want to use an allocated sick day?
    -Do you want to view approved requests?
    -Do you want to view denied requests?
    -Do you want to log out?

  Most of these are self-explanatory
    -Making a request will prompt you for a number; type the one that corresponds to what you want (e.g. 1 for an extra sick day)
    -Viewing your information requires no further input
    -Using an allocated sick day requires no further input
    -Viewing approved requests requires no further input
    -Viewing denied requests requires no further input
    -Logging out requires no further input


EXTRA NOTES:
  -To end the program, logout, then when it prompts for a username, type /kill
  -The create(String[] info, String pword) function is never used by default in the program. However, if you would like to create a new user, add the following lines to the top of the main method (replacing everything in <> with the appropriate value):

    String[] newuser = {"<username>","<position>","$<monthly salary>","<remaining sick days>","<admin access (true/false)>"};
    create(newuser,"<password>");

  -Be warned that forgetting to delete the above after it runs the first time may cause potentially fatal issues to the program, as creating multiple users with the same username may change data in certain files that could break the program
  -All passwords are hashed, but default users' passwords are provided in a nonhashed.txt file
