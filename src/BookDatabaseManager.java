
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;


public class BookDatabaseManager {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:9898/books";
    static Scanner sc =new Scanner(System.in);

    // Database credentials
    static final String USER = "root";
    static final String PASS = "password";
    // connection
    static Connection conn = null;

    public BookDatabaseManager() {
    }

        //method to load data from tables
    private void loadDataBase(String query, ArrayList<String> array, int i) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);

            while(rs.next())
                if (i==1){
                array.add(rs.getString(1)+"@"+rs.getString(2)+"@"+rs.getInt(3)+"@"+rs.getString(4));
                }else  {
                    array.add(rs.getString(1) + "@" + rs.getString(2) + "@" + rs.getString(3));

                }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBooks() {
        String query="select title ,isbn ,editionNumber ,copyright from titles ;";
        ArrayList<String> BooksArray = new ArrayList<>();
//        pass the query and empty array also a number to verify which method is calling to the loadDataBase method and get the result
        loadDataBase(query, BooksArray,1);
        LinkedList<Books >bookObjects = new LinkedList<>();
        String[] book;
        for (String x :BooksArray) {
            book=x.split("@",4);
//            split the query result into 4 strings
            LinkedList<Author> AuthorsArray = new LinkedList<>();
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select a.authorID,a.firstName , a.lastName from authors a join  authorisbn c on a.authorID =c.authorID join titles b on b.isbn = c.isbn  where b.title='"+book[0]+"' ");
                while (rs.next()) {
                    Author Author=new Author(rs.getInt(1),rs.getString(2),rs.getString(3));
                    AuthorsArray.add(Author);
                }
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
//            create book objects and pass the authors array to them
            Books b=new Books("","",0,"",AuthorsArray);
            b.setTitle(book[0]);
            b.setISBN(book[1]);
            b.setEdition(Integer.parseInt(book[2]));
            b.setCopyright(book[3]);
            bookObjects.add(b);
        }
//        print each book and its authors
        for(Books o:bookObjects){
            System.out.println(String.format("%-15s","Book Name")+": "+o.getTitle()+"\n"+String.format("%-15s","ISBN")+": "+o.getISBN()
            + "\n"+String.format("%-15s","Edition No")+": "+o.getEdition()+"\n"+String.format("%-15s","Copy Right")+ ": "+o.getCopyright());

            int i=1;
            for (Author author :o.getAuthorList()){
                System.out.println(String.format("%-15s", ("Author"+ i ))+String.format("%-62s",(": "+ author.getAuthorFName()+", "+ author.getAuthorLName()))+ "Author ID:" + author.getID());
                i++;
            }
            for (String s : Arrays.asList("\n","******************************************************************************************", "\n")) {
                System.out.println(s);
            }
        }
    }

    public void loadAuthors() {
        String query="select firstName,lastName,authorID from authors;";
        ArrayList<String> authorsArray = new ArrayList<>();
        loadDataBase(query, authorsArray,2);
        LinkedList<Author >authorObjects = new LinkedList<>();
        String fName,lName;
        int id;
        for(String x:authorsArray){
            //        split the result from the authors table int fname , lname and id

            String[] author=x.split("@",3);
            fName=author[0];
            lName=author[1];
            id=Integer.parseInt(author[2]);
            LinkedList<Books> booksList = new LinkedList<>();

            try {
                conn = DriverManager.getConnection(
                        DB_URL, USER, PASS);
                Statement stmt;
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select b.title ,b.isbn,b.editionNumber,b.copyright from titles b join authorisbn a on b.isbn = a.isbn join authors a2 on a.authorID = a2.authorID  where a2.firstName='"+fName+"'and a2.lastName='"+lName+"' ");

                while (rs.next()) {
                    Books book=new Books(rs.getString(1),rs.getString(2),Integer.parseInt(rs.getString(3)),rs.getString(4));
                    booksList.add(book);
                }
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
//create an author object and pass the list of books objects to it
            Author a=new Author(id,fName,lName, booksList);
            authorObjects.add(a);
        }
//        here comes the print statements
        for(Author o:authorObjects){
            System.out.println(String.format("%-15s","Author Name ")+String.format("%-62s",": "+o.getAuthorFName()+", "+o.getAuthorLName())+"Author ID "+": "+o.getID());
            int i=1;
            for (Books book :o.getBooksList()){
                System.out.println(String.format("%-15s","Book "+i+" Title")+": "+String.format("%-60s",book.getTitle())+
                        String.format("%-22s","ISBN : "+book.getISBN())
                        +String.format("%-20s","Edition No : "+book.getEdition())+"\tCopy Right : "+book.getCopyright());
                i++;
            }
            for (String s : Arrays.asList("\n","*****************************************************************************************************************************************", "\n")) {
                System.out.println(s);
            }
        }
    }

//    method to get a new author data
    public void addNewAuthor() {
        System.out.println("Enter Author first Name:\t");
        String fName=sc.nextLine();
        System.out.println("Enter Author last Name:\t");
        String lName=sc.nextLine();
        AuthorInsertion(fName, lName);
    }

//    method to add a new book
    public void addNewBook() {
        System.out.println("Enter ISBN:\t");
        String ISBN=sc.nextLine();
        System.out.println("Enter Book title:\t");
        String bookTitle=String.valueOf(sc.nextLine());
        System.out.println("Enter Edition Number:\t");
        int editionNumber=sc.nextInt();
        System.out.println("Enter Copyright:\t");
        String copyRight=sc.next();
        int ExistingOrNew;
        do {
            System.out.println("""

                    Please Choose the matching number from Below:
                    For Existing Author Please Enter 1
                    For adding a new author please enter 2\t""");
            ExistingOrNew = sc.nextInt();
            if (ExistingOrNew == 1) {
                String query = "select firstName,lastName,authorID from authors;";
                ArrayList<String> authorsArray = new ArrayList<>();
                loadDataBase(query, authorsArray, 2);
                String fName, lName, id;
                System.out.printf("%s%s%s%n", String.format("%-5s", "ID "), String.format("%-13s", "First Name "), String.format("%-10s", "Last Name "));
                String[] author;
                for (String x : authorsArray) {
                    author = x.split("@", 3);
                    fName = author[0];
                    lName = author[1];
                    id = author[2];
                    System.out.printf("%s%s%s%n", String.format("%-5s", id), String.format("%-13s", fName), String.format("%-10s", lName));
                }
                System.out.println("Enter the Author ID from the above list");
                int idSelection = sc.nextInt();
                authorIsbnInsertion( idSelection,ISBN);

                BookToBeInserted(ISBN, bookTitle, editionNumber, copyRight);

            } else {
                System.out.println("Author First Name:\t");
                String AuthFName = sc.next();

                System.out.println("Author Last Name :\t");
                String AuthLName = sc.next();

                AuthorInsertion(AuthFName, AuthLName);
                BookToBeInserted(ISBN, bookTitle, editionNumber, copyRight);

                int idToBeInsertedIntoAuthorIsbn = 0;

                try {

                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    Statement stmt = conn.createStatement();
                    String query = ("select  authorID  from authors where firstName='" + AuthFName + "' and lastName='" + AuthLName + "'");
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next()){
                        idToBeInsertedIntoAuthorIsbn = rs.getInt(1);
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println(idToBeInsertedIntoAuthorIsbn);
                authorIsbnInsertion(idToBeInsertedIntoAuthorIsbn, ISBN);

            }
        } while (ExistingOrNew<1||ExistingOrNew>2);
    }

//    method to insert the book object into titles table
    private void BookToBeInserted(String ISBN, String bookTitle, int editionNumber, String copyRight) {

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String bookQuery =("insert into titles(isbn ,title,editionNumber,copyright) values (?,?,?,?)");
            PreparedStatement preparedStmt = conn.prepareStatement(bookQuery);
            preparedStmt.setString(1,ISBN);
            preparedStmt.setString(2,bookTitle);
            preparedStmt.setInt(3,editionNumber);
            preparedStmt.setString(4,copyRight);

            preparedStmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    method to connect book with author by inserting them into the author isbn table
    public void authorIsbnInsertion( int idToBeInsertedIntoAuthorIsbn,String ISBN) {
        System.out.println(ISBN);
        System.out.println(idToBeInsertedIntoAuthorIsbn);
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query1 = ("INSERT INTO authorisbn VALUES(?,?)");
            PreparedStatement preparedStmt = conn.prepareStatement(query1);
            preparedStmt.setInt(1,idToBeInsertedIntoAuthorIsbn);
            preparedStmt.setString(2,ISBN);
            preparedStmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    method to insert the author into authors table
    private void AuthorInsertion(String authFName, String authLName) {
        try {
            conn = DriverManager.getConnection(
                    DB_URL, USER, PASS);
            String query = ("insert into authors  values (default,'"+ authFName +"','"+ authLName +"')");
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




