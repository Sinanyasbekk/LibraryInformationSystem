import java.io.*;

public class SaveAndLoad {

   public static void saveObject(Object object, String fileName) {
      if(object instanceof Serializable) {
         try {
            FileOutputStream fileOutput = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutput);
            oos.writeObject(object);
         } catch (IOException e) {
            e.printStackTrace();
         }
      } else {
         System.out.println("Object is not serializable");
      }
   }

   public static Object loadUsers() throws Exception {
      Object object;
      FileInputStream fileInput = new FileInputStream(AppManager.USER_SAVING_LOCATION);
      ObjectInputStream ois = new ObjectInputStream(fileInput);
      object = ois.readObject();
      return object;
   }

   public static Object loadIssues() throws Exception {
      Object object;
      FileInputStream fileInput = new FileInputStream(AppManager.ISSUES_SAVING_LOCATION);
      ObjectInputStream ois = new ObjectInputStream(fileInput);
      object = ois.readObject();
      return object;
   }
}
