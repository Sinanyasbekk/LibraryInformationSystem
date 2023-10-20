import java.util.ArrayList;

public class StandardUser extends User {
   private ArrayList<Book> readBooks;
   private String password;
   private ArrayList<String> bookReports;

   public StandardUser(String name, String surname, String username, String password) {
      super(name, surname, username, password);
      readBooks = new ArrayList<>();
      bookReports = new ArrayList<>();
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setReadBooks(Book book) {
      readBooks.add(book);
   }

   public ArrayList<Book> getReadBooks() {
      return readBooks;
   }

   public ArrayList<String> getBookReports() {
      return bookReports;
   }

   public void setBookReports(ArrayList<String> bookReports) {
      this.bookReports = bookReports;
   }
}
