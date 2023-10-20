import java.io.Serializable;

public class Book implements Serializable {
   private final String TITLE;
   private final String AUTHOR;

   public Book(String title, String author) {
      TITLE = title;
      AUTHOR = author;
   }

   public String getTITLE() {
      return TITLE;
   }

   public String getAUTHOR() {
      return AUTHOR;
   }

   public void printBook(int index) {
      System.out.println("\n" + (index+1) + "-)");
      System.out.println("Title: " + TITLE);
      System.out.println("Author: " + AUTHOR);
   }
}
