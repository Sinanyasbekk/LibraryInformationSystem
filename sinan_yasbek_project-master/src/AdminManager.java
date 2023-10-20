import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * When the user access to the system as admin, this class provides the admin
 * operations to the user.
 */

public class AdminManager {
   private Admin currentAdmin;

   // Constructor loads the issues and assign the currentAdmin.
   public AdminManager(Admin currentAdmin) {
      this.currentAdmin = currentAdmin;
      if(User.issues == null) {
         try {
            User.issues = (ArrayList<String>) SaveAndLoad.loadIssues();
         } catch (Exception e) {
            User.issues = new ArrayList<>();
         }
      }
   }

   // This method only take care of executing corresponding method
   // according to user's choice from admin menu.
   public void handleUserInput(int input) {
      Scanner scanner = new Scanner(System.in);
      if(input == 1){
         addNewBook();
      } else if(input == 2) {
         while(true) {
            System.out.println("\n1- Add new user");
            System.out.println("2- Delete user");
            System.out.println("3- Back");

            try {
               int selection = scanner.nextInt();
               if (selection == 1) {
                  addNewUser();
               } else if (selection == 2) {
                  deleteUser();
               } else if (selection == 3) {
                  break;
               } else {
                  AppManager.printErrorMessage();
               }
            } catch(InputMismatchException ex) {
               AppManager.printErrorMessage();
            }
         }
      } else if(input == 3) {
         while(true) {
            System.out.println("\n1- Add new admin");
            System.out.println("2- Delete admin");
            System.out.println("3- Back");
            try {
               int selection = scanner.nextInt();
               if (selection == 1) {
                  addNewAdmin();
               } else if (selection == 2) {
                  deleteAdmin();
               } else if (selection == 3){
                  break;
               } else {
                  AppManager.printErrorMessage();
               }
            } catch(InputMismatchException ex) {
               AppManager.printErrorMessage();
            }
         }
      } else if(input == 4) {
         showIssues();
      } else if(input == 5) {
         AppManager.isLogin = true;
      } else {
         AppManager.printErrorMessage();
      }
   }

   // Takes information about new book to be added.
   private void addNewBook() {
      Scanner scanner = new Scanner(System.in);

      System.out.print("\nTitle: ");
      String title = scanner.nextLine();
      System.out.print("Description: ");
      String description = scanner.nextLine();
      System.out.print("Author: ");
      String author = scanner.nextLine();
      System.out.print("Genres: ");
      String genres = scanner.nextLine();

      insert(author, description, title, genres);
   }

   // Takes the new book information as parameter, and makes database connection
   // to add new book into database.
   private void insert(String author, String description, String title, String genres) {
      if(DBConnection.connection == null) {
         DBConnection.getConnection();
      }
      String query = "INSERT INTO books(book_authors,book_desc,book_title,genres,image_url) VALUES(?,?,?,?,?)";

      try {
         PreparedStatement pstmt = DBConnection.connection.prepareStatement(query);
         pstmt.setString(1, author);
         pstmt.setString(2, description);
         pstmt.setString(3, title);
         pstmt.setString(4, genres);
         pstmt.setString(5, "empty");
         pstmt.executeUpdate();
      } catch(SQLException ex) { }
   }

   // Scans the user in users array, deletes user according to admin's choice
   // and saves it.
   private void deleteUser() {
      Scanner scanner = new Scanner(System.in);
      int i = 0;
      for(User user : AppManager.users) {
         if(user instanceof StandardUser) {
            i++;
            System.out.println("\n" + i + "-)");
            System.out.println(user.getName() + " " + user.getSurname());
         }
      }
      if(i != 0) {
         System.out.print("\nEnter the number of user you want to delete: ");

         try {
            int selection = scanner.nextInt();
            if (selection > 0 && selection <= AppManager.users.size()) {
               System.out.println("\n" + AppManager.users.get(selection - 1).getName() + " "
                  + AppManager.users.get(selection - 1).getSurname() + " is deleted.");
               AppManager.users.remove(selection - 1);
               SaveAndLoad.saveObject(AppManager.users, AppManager.USER_SAVING_LOCATION);
            } else {
               AppManager.printErrorMessage();
            }
         } catch (InputMismatchException ex) {
            AppManager.printErrorMessage();
         }
      } else {
         System.out.println("No user exist on system.");
      }
   }

   // Executes the signUp method which is in AppManager class to add new user.
   private void addNewUser() {
      AppManager manager = new AppManager();
      manager.signUp();
   }

   // Shows all admins but current and default admin. Deletes admin.
   private void deleteAdmin() {
      Scanner scanner = new Scanner(System.in);
      ArrayList<Admin> admins = new ArrayList<>();
      int j = 0;
      int i = 0;
      for(User user : AppManager.users) {
         if(user instanceof Admin) {
            if(user != currentAdmin) {
               i++;
               System.out.println("\n" + i + "-)");
               System.out.println(user.getName() + " " + user.getSurname());
               admins.add((Admin) user);
            }
         }
      }
      if(admins.size() != 0) {
         System.out.print("\nEnter the number of admin you want to delete: ");
         try {
            int number = scanner.nextInt();
            if (number > 0 && number <= AppManager.users.size()) {
               AppManager.users.remove(admins.get(number - 1));
               SaveAndLoad.saveObject(AppManager.users, AppManager.USER_SAVING_LOCATION);
               System.out.println("\n" + admins.get(number - 1).getName() + " " +
                  admins.get(number - 1).getSurname() + " is deleted.");
            } else {
               AppManager.printErrorMessage();
            }
         } catch (InputMismatchException ex) {
            AppManager.printErrorMessage();
         }
      } else {
         System.out.println("\nNo additional admin exist on system.");
      }
   }

   // Adds new admin to the system.
   private void addNewAdmin() {
      Scanner scanner = new Scanner(System.in);

      System.out.print("\nEnter name: ");
      String name = scanner.nextLine();
      System.out.print("Enter surname: ");
      String surname = scanner.nextLine();
      System.out.print("Enter username: ");
      String username = scanner.nextLine();
      System.out.print("Enter password: ");
      String password = scanner.nextLine();

      if(AppManager.validateInfo(name, surname, username, password)) {
         AppManager.users.add(new Admin(name, surname, username, password));
         SaveAndLoad.saveObject(AppManager.users, AppManager.USER_SAVING_LOCATION);
      } else {
         AppManager.printErrorMessage();
      }
   }

   // Provides the admin to see the user's issues.
   private void showIssues() {
      for(String i : User.issues) {
         System.out.println(i);
      }
   }

   // Prints the menu on screen.
   public static void showAdminMenu() {
      System.out.println("\n1- Add New Book");
      System.out.println("2- User add-drop");
      System.out.println("3- Admin add-edit");
      System.out.println("4- Book Reports");
      System.out.println("5- Exit");
   }
}
