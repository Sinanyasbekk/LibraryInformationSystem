/**
 * Application runs in this class. It manages the loops properly for
 * providing good experience for users.
 *
 * Application terminates if isProgramRunning is false.
 */

public class RunApp {
   public static void main(String[] args) {
      AppManager appManager = new AppManager();
      int userType = -1;
      while(AppManager.isProgramRunning) {
         while(AppManager.isLogin) {
            appManager.setCurrentUser(null);
            appManager.systemStartupMenu();
            userType = appManager.getUserInput();
            appManager.loginRouter(userType);
         }
         // After login process is done, this part runs.

         // Admin operations
         if(userType == 1) {
            int input;
            AdminManager.showAdminMenu();
            AdminManager adminManager = new AdminManager(
               (Admin) appManager.getCurrentUser()
            );

            input = appManager.getUserInput();
            adminManager.handleUserInput(input);
         }
         // standard user operations
         else if(userType == 2) {
            int input;
            StandardUserManager.showMenu();
            StandardUserManager standardUserManager = new StandardUserManager(
               (StandardUser) appManager.getCurrentUser());

            input = appManager.getUserInput();
            standardUserManager.handleUserInput(input);
         }
      }
   }
}
