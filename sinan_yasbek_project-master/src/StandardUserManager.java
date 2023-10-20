import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class provides the operation for standard users.
 */
public class StandardUserManager {

   private StandardUser currentUser;
   private Scanner scanner;

   // Constructor assigns the current user and loads the issues.
   public StandardUserManager(StandardUser currentUser) {
      this.currentUser = currentUser;

      if(User.issues == null) {
         try {
            User.issues = (ArrayList<String>) SaveAndLoad.loadIssues();
         } catch (Exception e) {
            User.issues = new ArrayList<>();
         }
      }
   }

   // Directs the user to the proper method according to user's choice
   // from the menu.
   public void handleUserInput(int input) {
      if(input == 1) {
         printReadBooks();
      } else if (input == 2) {
         showAccount();
      } else if (input == 3) {
         changePassword();
      } else if(input == 4) {
         searchBooks();
      } else if(input == 5) {
         bookAddDrop();
      } else if(input == 6) {
         issueBookReport();
      } else if(input == 7){
         AppManager.isLogin = true;
      } else {
         AppManager.printErrorMessage();
      }
   }

   // Users can add new issue and can see the previous reports.
   // After process is done, it saves the issues.
   private void issueBookReport() {
      scanner = new Scanner(System.in);

      System.out.println("\nPrevious reports: ");

      int i = 0;
      for(String report : currentUser.getBookReports()) {
         i++;
         System.out.println("\n" + i + "-)");
         System.out.println(report);
      }

      System.out.println("\nEnter your new report here:");
      String bookReport = scanner.nextLine();
      if(bookReport.length() != 0) {
         currentUser.getBookReports().add(currentUser.getName() + ": " + bookReport);
         User.issues.add(currentUser.getName() + ": " + bookReport);
         SaveAndLoad.saveObject(User.issues, AppManager.ISSUES_SAVING_LOCATION);
      }
      SaveAndLoad.saveObject(AppManager.users, AppManager.USER_SAVING_LOCATION);
   }

   // Provides the user to add or delete the book that the user reads.
   private void bookAddDrop() {
      scanner = new Scanner(System.in);
      while(true) {
         System.out.println("\n1- Add read book");
         System.out.println("2- Delete read book");
         System.out.println("3- Back");
         int input = -1;
         try {
            input = scanner.nextInt();
         } catch(InputMismatchException ex) {
            AppManager.printErrorMessage();
         }

         if(input == 1) {
            ArrayList<Book> results = searchBooks();
            if(results.size() != 0) {
               System.out.print("\nEnter the number of book you want to add: ");
               try {
                  int bookNumber = scanner.nextInt();
                  currentUser.setReadBooks(results.get(bookNumber - 1));
                  System.out.println("\nNew book is added.");
               } catch(InputMismatchException ex) {
                  AppManager.printErrorMessage();
               } catch(IndexOutOfBoundsException ex) {
                  AppManager.printErrorMessage();
               }
            } else {
               System.out.println("\nBook can not be found!");
            }
         } else if (input == 2) {
            printReadBooks();
            System.out.print("\nEnter the number of book you want to delete: ");
            try {
               int choice = scanner.nextInt();
               System.out.println("\n" + currentUser.getReadBooks().get(choice-1).getTITLE() + " is deleted.");
               currentUser.getReadBooks().remove(choice-1);
            } catch(InputMismatchException ex) {
               AppManager.printErrorMessage();
            }
         } else if (input == 3){
            break;
         } else {
            AppManager.printErrorMessage();
         }
         SaveAndLoad.saveObject(AppManager.users, AppManager.USER_SAVING_LOCATION);
      }
   }

   // Search the book in database by using the book title.
   // If the title entered by user is exactly the same with the
   // book's title, then it is shown firstly. Other results contain the
   // word's entered by the user.
   private ArrayList<Book> searchBooks() {
      scanner = new Scanner(System.in);
      ArrayList<Book> results = new ArrayList<>();

      System.out.print("Book title: ");
      String bookTitle = scanner.nextLine();

      String[] splittedTitle = bookTitle.split(" ");
      String[] queries = new String[splittedTitle.length];
      for(int i = 0; i < queries.length; i++) {
         queries[i] = "%" + splittedTitle[i] + "%";
      }

      int size = 0;
      try {
         if (DBConnection.connection == null) {
            DBConnection.getConnection();
         }
         Statement statement = DBConnection.connection.createStatement();
         ResultSet resultSet = statement.executeQuery(
            "SELECT book_title, book_authors FROM books WHERE book_title LIKE '" + bookTitle + "';");
         while(resultSet.next()) {
            results.add(new Book(
               resultSet.getString("book_title"),
               resultSet.getString("book_authors")
            ));
            size++;
         }

         for(int i = 0; i < queries.length; i++) {
            resultSet = statement.executeQuery(
               "SELECT book_title, book_authors FROM books WHERE book_title LIKE '" + queries[i] + "';");
            while(resultSet.next()) {
               results.add(new Book(
                  resultSet.getString("book_title"),
                  resultSet.getString("book_authors")
               ));
               size++;
               if(size > 19)
                  break;
            }
         }

      } catch(SQLException ex) {

      }
      results = reverseArray(results);
      for(int i = 0; i < results.size(); i++) {
         results.get(i).printBook(i);
      }

      return results;
   }

   // Just for user can see the perfect match firstly.
   private ArrayList<Book> reverseArray(ArrayList<Book> list) {
      ArrayList<Book> newArray = new ArrayList<>();
      for(int i = list.size()-1; i >= 0; i--) {
         newArray.add(list.get(i));
      }
      return newArray;
   }

   // Prints the book which are read by user.
   private void printReadBooks() {
      int i = 0;
      for(Book book : currentUser.getReadBooks()) {
         i++;
         System.out.println("\n" + i + "-)");
         System.out.println("Title: " + book.getTITLE());
         System.out.println("Author: " + book.getAUTHOR());
      }
   }

   // Prints user's account information.
   private void showAccount() {
      System.out.println("\nName: " + currentUser.getName());
      System.out.println("Surname: " + currentUser.getSurname());
      System.out.println("Username: " + currentUser.getUsername());
      System.out.println("Password: " + currentUser.getPassword());
      System.out.println("Number of books I read: " + currentUser.getReadBooks().size());
   }

   // Changes user's password. It asks for old password and second time
   // entering the new password. If all inputs are match, then password
   // changes.
   private void changePassword() {
      scanner = new Scanner(System.in);
      while(true) {
         System.out.print("Old Password: ");
         String oldPassword = scanner.nextLine();
         System.out.print("New Password: ");
         String newPassword = scanner.nextLine();
         System.out.print("New Password again: ");
         String newPasswordAgain = scanner.nextLine();

         if(currentUser.getPassword().equals(oldPassword)) {
            if(newPassword.equals(newPasswordAgain)) {
               currentUser.setPassword(newPassword);
               SaveAndLoad.saveObject(AppManager.users,AppManager.USER_SAVING_LOCATION);
               System.out.println("\nPassword is updated.");
               break;
            }
         } else {
            System.out.println("\nWrong password!");
         }
      }
   }

   // Prints the standard user menu.
   public static void showMenu() {
      System.out.println("\n1- Show Books I Read");
      System.out.println("2- My Account");
      System.out.println("3- Change Password");
      System.out.println("4- Search Books");
      System.out.println("5- Book Management");
      System.out.println("6- Issue Book Report");
      System.out.println("7- Back");
   }
}
