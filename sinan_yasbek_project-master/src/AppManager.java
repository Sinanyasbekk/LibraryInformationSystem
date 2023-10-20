import java.util.*;

/**
 * This class contains the admin information and saving locations as final variables.
 * Also contains the global operations and variables.
 */
public class AppManager {
   public static final String USER_SAVING_LOCATION = "users.dat";
   public static final String ISSUES_SAVING_LOCATION = "issues.dat";
   public static final String ERROR_MESSAGE = "\nPlease enter valid value.";
   private final String ADMIN_USERNAME = "sinanyasbek";
   private final String ADMIN_PASSWORD = "admin";

   public static boolean isProgramRunning = true;
   public static boolean isLogin = true;
   private Scanner scanner;
   // admin key=1, standard user key=2
   public static ArrayList<User> users;
   private User currentUser;

   // Loads the users if exists, otherwise creates the new array list.
   public AppManager() {
      try {
         users = (ArrayList<User>) SaveAndLoad.loadUsers();
      } catch(Exception ex) {
         users = new ArrayList<>();
      }
   }

   public void setCurrentUser(User currentUser) {
      this.currentUser = currentUser;
   }

   public User getCurrentUser() {
      return currentUser;
   }

   // Prints the menu when the application starts.
   public void systemStartupMenu() {
      System.out.println("\n1- Login as admin");
      System.out.println("2- Login as standard user");
      System.out.println("3- Terminate System");
   }

   // handles the exception when getting integer value from user.
   public int getUserInput() {
      scanner = new Scanner(System.in);
      int input = -1;
      try {
         input = scanner.nextInt();
         return input;
      } catch(InputMismatchException ex) {
         scanner = null;
         printErrorMessage();
      }
      scanner = null;
      return input;
   }

   // Directs to the correct method for accessing the system by standard users.
   public void loginRouter(int userInput) {
      if(userInput == 1) {
         adminLogin();
      } else if (userInput == 2) {
         System.out.println("1- Sign Up");
         System.out.println("2- Log In");
         System.out.println("3- Back");

         int input = getUserInput();
         if (input == 1) {
            signUp();
         } else if (input == 2) {
            standardUserLogin();
         } else if (input == 3) {
            isLogin = false;
         } else {
            printErrorMessage();
         }
      } else if(userInput == 3) {
         System.out.println("\nBye");
         SaveAndLoad.saveObject(users, USER_SAVING_LOCATION);
         System.exit(0);
      } else {
         printErrorMessage();
      }
   }

   public void signUp() {
      scanner = new Scanner(System.in);

      System.out.print("Enter name: ");
      String name = scanner.nextLine();
      System.out.print("Enter surname: ");
      String surname = scanner.nextLine();
      System.out.print("Enter username: ");
      String username = scanner.nextLine();
      System.out.print("Enter password: ");
      String password = scanner.nextLine();

      if(validateInfo(name, surname, username, password)) {

         StandardUser newUser = new StandardUser(name, surname, username, password);
         currentUser = newUser;
         users.add(newUser);
         SaveAndLoad.saveObject(users, USER_SAVING_LOCATION);

         System.out.println("\nNew Account!");
         System.out.println("Welcome " + name + " " + surname + "!");
         isLogin = false;
      } else {
         printErrorMessage();
      }
   }

   public static boolean validateInfo(
      String name,
      String surname,
      String username,
      String password)
   {
      return (name.length() != 0 &&
         surname.length() != 0 &&
         username.length() != 0 &&
         password.length() != 0);
   }

   private void standardUserLogin() {
      scanner = new Scanner(System.in);
      System.out.print("Username: ");
      String username = scanner.nextLine();
      System.out.print("Password: ");
      String password = scanner.nextLine();

      boolean userFound = false;
      for(User user : users) {
         if(user instanceof StandardUser) {
            StandardUser standardUser = (StandardUser) user;
            if(standardUser.getUsername().equals(username)) {

               if(!standardUser.getPassword().equals(password)) {
                  System.out.println("\nWrong password!");
                  userFound = true;
               } else {
                  currentUser = standardUser;
                  System.out.println("\nWelcome " + currentUser.getName() + " " + currentUser.getSurname() + "!");

                  userFound = true;
                  isLogin = false;
                  SaveAndLoad.saveObject(users, USER_SAVING_LOCATION);
               }
            }
         }
      }

      if(!userFound  ) {
         System.out.println("\nUser can not be found.");
      }
   }

   private void adminLogin() {
      scanner = new Scanner(System.in);
      System.out.print("Admin username: ");
      String username = scanner.nextLine();
      System.out.print("Admin password: ");
      String password = scanner.nextLine();

      if(username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
         isLogin = false;
         System.out.println("\nWelcome Sinan Yasbek!");
      } else {
         boolean userFound = false;
         for(User user : users) {
            if(user instanceof Admin) {
               if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                  System.out.println("\nWelcome " + user.getName() + " " + user.getSurname() + "!");
                  currentUser = user;
                  userFound = true;
                  isLogin = false;
               }
            }
         }
         if(!userFound) {
            System.out.println("\nWrong name and surname for admin");
         }
      }
   }

   public static void printErrorMessage() {
      System.out.println(ERROR_MESSAGE);
   }
}
