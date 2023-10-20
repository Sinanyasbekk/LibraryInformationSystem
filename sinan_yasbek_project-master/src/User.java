import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
   private String name;
   private String surname;
   private String username;
   private String password;
   public static ArrayList<String> issues;

   public User(String name, String surname, String username, String password) {
      this.name = name;
      this.surname = surname;
      this.username = username;
      this.password = password;
   }

   public String getName() {
      return name;
   }

   public String getSurname() {
      return surname;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }
}
