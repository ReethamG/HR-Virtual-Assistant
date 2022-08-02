import java.io.*;
import javax.swing.*;
import java.util.*;

public class HRManager {
   public static void main(String[] args) throws IOException {
      if (args.length > 0) {
         run(args[0]);
      } else {
         run(null);
      }
   }

   public static void create(String[] info, String pword) { // Can be used to create a new employee, will make all the
                                                            // files for that employee and add the data to the .dat and
                                                            // information to the .txt
      String fname = info[0] + ".txt";
      try {
         FileOutputStream fos = new FileOutputStream(fname);
         PrintWriter pw = new PrintWriter(fname);
         pw.println("Username: " + info[0]);
         pw.println("Position: " + info[1]);
         pw.println("Monthly salary: " + info[2]);
         pw.println("Remaining sick days: " + info[3]);
         pw.println("Admin access: " + info[4]);
         pw.close();
         FileWriter fw = new FileWriter("LoginInfo.dat", true);
         pw = new PrintWriter(fw);
         pw.println(info[0] + ":" + pword.hashCode());
         pw.close();
         fw.close();
         fos = new FileOutputStream(info[0] + "_accepted.txt");
         fos = new FileOutputStream(info[0] + "_denied.txt");
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "The file either could not be created or could not be edited");
      }
   }

   public static String[][] getDatInfo() throws IOException { // Will be used to get the information within the .dat
                                                              // files
      ArrayList<String> unames = new ArrayList<String>(0);
      ArrayList<String> pwords = new ArrayList<String>(0);
      File f = new File("LoginInfo.dat");
      Scanner s = new Scanner(f);
      String temp;
      int colon;
      while (s.hasNext()) {
         temp = s.nextLine();
         colon = temp.indexOf(":");
         unames.add(temp.substring(0, colon));
         pwords.add(temp.substring(colon + 1));
      }
      String[][] uandps = new String[2][unames.size()];
      for (int i = 0; i < unames.size(); i++) {
         uandps[0][i] = unames.get(i);
         uandps[1][i] = pwords.get(i);
      }
      return uandps;
   }

   public static boolean validCreds(String uname, String pword, String[][] uandps) { // Checks if username and password
                                                                                     // match
      for (int i = 0; i < uandps[0].length; i++) {
         if (uname.equals(uandps[0][i])) {
            if (pword.hashCode() == Integer.parseInt(uandps[1][i])) {
               return true;
            }
         }
      }
      return false;
   }

   public static String[][] getUserInfo(String uname) throws IOException { // Returns all the information from a user's
                                                                           // file
      File f = new File(uname + ".txt");
      Scanner s = new Scanner(f);
      String[][] retvals = new String[2][5];
      String temp;
      for (int i = 0; i < 5; i++) {
         temp = s.nextLine();
         retvals[0][i] = temp.substring(temp.indexOf(":") + 2);
         retvals[1][i] = temp;
      }
      return retvals;
   }

   public static String[][] login(String[][] usandpss) throws IOException { // Calls valid creds, and calls the userinfo
                                                                            // which will be used for the login system
      boolean valid = false;
      String user, pw;
      do {
         user = JOptionPane.showInputDialog("Username");
         if (user.equals("/kill")) {
            System.exit(0);
         }
         pw = JOptionPane.showInputDialog("Password");
         valid = validCreds(user, pw, usandpss);
      } while (!valid);
      String[][] retvals = getUserInfo(user);
      return retvals;
   }

   public static String[][] minusSickDay(String[][] info1) throws IOException { // When requesting for a sick day from
                                                                                // an employee, this method will a
                                                                                // subtract one sick day from the user's
                                                                                // .txt file
      info1[1][3] = info1[1][3].replace(info1[0][3], String.valueOf(Integer.parseInt(info1[0][3]) - 1));
      info1[0][3] = info1[0][3].replace(info1[0][3], String.valueOf(Integer.parseInt(info1[0][3]) - 1));
      PrintWriter pw = new PrintWriter(info1[0][0] + ".txt");
      for (int i = 0; i < info1[0].length; i++) {
         pw.println(info1[1][i]);
      }
      pw.close();
      return info1;
   }

   public static String[][] fire(String uname, String[][] uandps) throws IOException { // Fires the employee in the
                                                                                       // sense of deleting all the
                                                                                       // files for the employee
      boolean flag = false;
      ArrayList<String> preserve = new ArrayList<String>(0);
      for (int i = 0; i < uandps[0].length; i++) {
         if (uname.equals(uandps[0][i])) {
            flag = true;
         }
      }
      if (flag) {
         File f = new File(uname + ".txt");
         f.delete();
         f = new File(uname + "_accepted.txt");
         f.delete();
         f = new File(uname + "_denied.txt");
         f.delete();
         f = new File("LoginInfo.dat");
         Scanner s = new Scanner(f);
         String sval;
         while (s.hasNext()) {
            sval = s.nextLine();
            if (sval.substring(0, sval.indexOf(":")).equals(uname)) {
               continue;
            }
            preserve.add(sval);
         }
         PrintWriter pw = new PrintWriter(f);
         for (int i = 0; i < preserve.size(); i++) {
            pw.println(preserve.get(i));
         }
         pw.close();
         String[][] newuandps = getDatInfo();
         return newuandps;
      } else {
         JOptionPane.showMessageDialog(null, "Error: user not found");
         return uandps;
      }
   }

   public static void raise(String uname, double raiseval, String[][] validate, String sudopword) throws IOException { // Raises
                                                                                                                       // a
                                                                                                                       // user's
                                                                                                                       // salar,
                                                                                                                       // also
                                                                                                                       // updates
                                                                                                                       // on
                                                                                                                       // the
                                                                                                                       // .txt
                                                                                                                       // file
      boolean flag1 = false;
      for (int i = 0; i < validate[0].length; i++) {
         if (uname.equals(validate[0][i])) {
            flag1 = true;
         }
      }
      if (!flag1) {
         JOptionPane.showMessageDialog(null, "User not found");
      }
      boolean flag2 = false;
      if (sudopword == null) {
         JOptionPane.showMessageDialog(null, "No sudopassword provided");
      } else {
         for (int i = 0; i < validate[0].length; i++) {
            if (sudopword.hashCode() == Integer.parseInt(validate[1][i])) {
               flag2 = true;
            }
         }
      }
      if ((flag1) && (flag2)) {
         File f = new File(uname + ".txt");
         Scanner s = new Scanner(f);
         String[] temparr = new String[5];
         for (int i = 0; i < 5; i++) {
            if (i == 2) {
               String temp = s.nextLine();
               temp = temp.substring(temp.indexOf("$") + 1);
               double salary = Double.parseDouble(temp);
               salary += raiseval;
               temparr[i] = "Monthly salary: $" + salary + "0";
               continue;
            }
            temparr[i] = s.nextLine();
         }
         PrintWriter pw = new PrintWriter(f);
         for (int i = 0; i < temparr.length; i++) {
            pw.println(temparr[i]);
         }
         pw.close();
      }
   }

   public static String inputDialogWithSelectionForManager() { // The graphics for this software
      String[] choices = { "Do you want to see your information?", "Do you want to see the pending requests file?",
            "Do you want to see an employee's file?", "Do you want to fire an employee?",
            "Do you want to grant a raise?", "Do you want to logout?" };
      String input = (String) JOptionPane.showInputDialog(null, "What do you want to do?", "Manager Panel",
            JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
      return input;
   }

   public static String inputDialogWithSelectionForEmployee() {
      String[] choices = { "Do you want to make a request to the manager?", "Do you want to see your information?",
            "Do you want to logout?", "Do you want to use an allocated sick day?",
            "Do you want to view approved requests?", "Do you want to view denied requests?" };
      String input = (String) JOptionPane.showInputDialog(null, "What do you want to do?", "Employee Panel",
            JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
      return input;
   }

   public static void run(String sudo) throws IOException { // This is the method that will be called in the main method
      String[][] loginInfo = login(getDatInfo());

      if (loginInfo[0][1].equals("Manager")) {
         String inputForManager = inputDialogWithSelectionForManager();
         afterDialogForManager(inputForManager, loginInfo, sudo);
      } else {
         String inputForEmployee = inputDialogWithSelectionForEmployee();
         afterDialogForEmployee(inputForEmployee, loginInfo, sudo);
      }
   }

   public static void afterDialogForManager(String input, String[][] login, String sudo) throws IOException { // This
                                                                                                              // method
                                                                                                              // will be
                                                                                                              // shown
                                                                                                              // after
                                                                                                              // the
                                                                                                              // user
                                                                                                              // logs in
                                                                                                              // as a
                                                                                                              // Manager
      if (input.equals("Do you want to fire an employee?")) {
         String employeeToFire = JOptionPane.showInputDialog("Enter the username of the employee you want to fire?");
         fire(employeeToFire, getDatInfo());
         afterDialogForManager(inputDialogWithSelectionForManager(), login, sudo);
      } else if (input.equals("Do you want to grant a raise?")) {
         String uName = "";
         String[][] datinfo = getDatInfo();
         boolean flag = false;
         while (!flag) {
            uName = JOptionPane.showInputDialog("Which employee do you want to give a raise to?");
            for (int i = 0; i < datinfo[0].length; i++) {
               if (uName.equals(datinfo[0][i])) {
                  flag = true;
               }
            }
            if (!flag) {
               JOptionPane.showMessageDialog(null, "User not found");
            }
         }
         flag = false;
         double amountToRaise = 0.0;
         while (!flag) {
            try {
               amountToRaise = Double
                     .parseDouble(
                           JOptionPane.showInputDialog("How much do you want to raise " + uName + "'s salary by?"));
               flag = true;
            } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Invalid input");
            }
         }
         raise(uName, amountToRaise, getDatInfo(), sudo);
         afterDialogForManager(inputDialogWithSelectionForManager(), login, sudo);
      } else if (input.equals("Do you want to see your information?")) {
         showOwnInfo(login);
         afterDialogForManager(inputDialogWithSelectionForManager(), login, sudo);
      } else if (input.equals("Do you want to see an employee's file?")) {
         String uname;
         boolean flag = false;
         String[][] datinfo = getDatInfo();
         do {
            uname = JOptionPane.showInputDialog("What is the username of the employee's file you want to see?");
            for (int i = 0; i < datinfo[0].length; i++) {
               if (uname.equals(datinfo[0][i])) {
                  flag = true;
               }
            }
            if (!flag) {
               JOptionPane.showMessageDialog(null, "User not found");
            }
         } while (!flag);
         showOwnInfo(getUserInfo(uname));
         afterDialogForManager(inputDialogWithSelectionForManager(), login, sudo);
      } else if (input.equals("Do you want to logout?")) {
         run(sudo);
      } else if (input.equals("Do you want to see the pending requests file?")) {
         resolveRequests(getDatInfo());
         afterDialogForManager(inputDialogWithSelectionForManager(), login, sudo);
      }
   }

   public static void afterDialogForEmployee(String input, String[][] login, String sudo) throws IOException { // This
                                                                                                               // method
                                                                                                               // will
                                                                                                               // be
                                                                                                               // shown
                                                                                                               // after
                                                                                                               // the
                                                                                                               // user
                                                                                                               // logs
                                                                                                               // in as
                                                                                                               // a
                                                                                                               // Employee
      if (input.equals("Do you want to make a request to the manager?")) {
         makeRequest(login);
         afterDialogForEmployee(inputDialogWithSelectionForEmployee(), login, sudo);
      } else if (input.equals("Do you want to logout?")) {
         run(sudo);
      } else if (input.equals("Do you want to use an allocated sick day?")) {
         if (Integer.parseInt(login[0][3]) > 0) {
            login = minusSickDay(login);
         } else {
            JOptionPane.showMessageDialog(null, "You have no more sick days");
         }
         afterDialogForEmployee(inputDialogWithSelectionForEmployee(), login, sudo);
      } else if (input.equals("Do you want to view approved requests?")) {
         viewResolvedRequests(login, "_accepted.txt");
         afterDialogForEmployee(inputDialogWithSelectionForEmployee(), login, sudo);
      } else if (input.equals("Do you want to view denied requests?")) {
         viewResolvedRequests(login, "_denied.txt");
         afterDialogForEmployee(inputDialogWithSelectionForEmployee(), login, sudo);
      } else {
         showOwnInfo(login);
         afterDialogForEmployee(inputDialogWithSelectionForEmployee(), login, sudo);
      }
   }

   public static void showOwnInfo(String[][] uinfo) throws IOException { // Shows the user's info in a systematic manner
      String allInfo = "";
      for (int i = 0; i < 5; i++) {
         if (i == 2) {
            allInfo = allInfo + "\n"
                  + String.format("Monthly salary: $%,.2f", Double.parseDouble(uinfo[0][i].substring(1)));
            continue;
         }
         allInfo = allInfo + "\n" + uinfo[1][i];
      }
      JOptionPane.showMessageDialog(null, allInfo);
   }

   public static void resolveRequests(String[][] uandps) throws IOException { // This method will be called when the
                                                                              // manager wants to approve or deny the
                                                                              // pending requests from employees
      File f = new File("pendingRequests.txt");
      Scanner s = new Scanner(f);
      FileWriter fw = new FileWriter("defaultfile.txt");
      PrintWriter pw = new PrintWriter(fw);
      pw.close();
      fw.close();
      String user, request;
      int rnum = 0;
      boolean flag = false;
      while (s.hasNext()) {
         request = s.nextLine();
         while (!flag) {
            try {
               rnum = Integer.parseInt(JOptionPane.showInputDialog(request + "\n1 to accept, 2 to deny"));
               switch (rnum) {
                  case 1:
                  case 2:
                     flag = true;
                     break;
                  default:
                     JOptionPane.showMessageDialog(null, "Invalid input");
               }
               // if ((rnum == 1) || (rnum == 2)) {
               // flag = true;
               // } else {
               // JOptionPane.showMessageDialog(null, "Invalid input");
               // }
            } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Invalid input");
            }
         }
         flag = false;
         user = request.substring(0, request.indexOf(" "));
         for (int i = 0; i < uandps[0].length; i++) {
            if (user.equals(uandps[0][i])) {
               flag = true;
            }
         }
         if ((rnum == 1) && (flag)) {
            fw = new FileWriter(user + "_accepted.txt", true);
            pw = new PrintWriter(fw);
            pw.println(request);
         } else if ((rnum == 2) && (flag)) {
            fw = new FileWriter(user + "_denied.txt", true);
            pw = new PrintWriter(fw);
            pw.println(request);
         }
         pw.close();
         fw.close();
         pw = new PrintWriter("pendingRequests.txt");
         pw.close();
      }
   }

   public static void viewResolvedRequests(String[][] uinfo, String aord) throws IOException { // This method will be
                                                                                               // used by the employee
                                                                                               // to see their approved
                                                                                               // or denied requests
      File f = new File(uinfo[0][0] + aord);
      Scanner s = new Scanner(f);
      String showval = "";
      while (s.hasNext()) {
         showval = showval + s.nextLine() + "\n";
      }
      JOptionPane.showMessageDialog(null, showval);
      s.close();
   }

   public static void makeRequest(String[][] uinfo) throws IOException { // Method will be called whenever an employee
                                                                         // wants to make a request to the manager to
                                                                         // approve or deny
      int rnum = 0;
      boolean flag = false;
      while (!flag) {
         try {
            rnum = Integer.parseInt(JOptionPane.showInputDialog("1 for extra sick day, 2 for vacation day"));
            if ((rnum == 1) || (rnum == 2)) {
               flag = true;
            } else {
               JOptionPane.showMessageDialog(null, "Invalid input");
            }
         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input");
         }
      }
      FileWriter fw = new FileWriter("pendingRequests.txt", true);
      PrintWriter pw = new PrintWriter(fw);
      if (rnum == 1) {
         pw.println(uinfo[0][0] + " requested an extra sick day");
      } else if (rnum == 2) {
         pw.println(uinfo[0][0] + " requested a vacation day");
      }
      pw.close();
      fw.close();
   }
}