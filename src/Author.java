import java.util.List;

public class Author {
    private final String authorFName;
    public String getAuthorFName() {return authorFName;}

    private final String authorLName;
    public String getAuthorLName() {return authorLName;}

    public List<Books> getBooksList() {return booksList;}

    private List <Books> booksList;

    private final int ID;
    public int getID() {return ID;}

    public Author(int ID,String authorFName, String authorLName) {
        this.authorFName = authorFName;
        this.authorLName = authorLName;
        this.ID=ID;
    }

    public Author(int ID,String authorFName, String authorLName, List<Books> booksList) {
        this.authorFName = authorFName;
        this.authorLName = authorLName;
        this.booksList = booksList;
        this.ID=ID;
    }


}
