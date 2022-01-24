import java.util.List;

public class Books {

    private String title;

    public Books(String title, String ISBN, int edition, String copyright) {
        this.title = title;
        this.ISBN = ISBN;
        this.edition = edition;
        this.copyright = copyright;
    }


    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    private List<Author> authorList;
    public List<Author> getAuthorList() {return authorList;}
    public void setAuthorList(List<Author> authorList) {this.authorList = authorList;}

    private String ISBN;
    public String getISBN() {return ISBN;}
    public void setISBN(String ISBN) {this.ISBN = ISBN;}

    private int edition;
    public int getEdition() {return edition;}
    public void setEdition(int edition) {this.edition = edition;}


    private String copyright;
    public String getCopyright() {return copyright;}
    public void setCopyright(String copyright) {this.copyright = copyright;}

    public Books(String title,String ISBN,int edition,String copyright, List<Author> authorList) {
        this.title = title;
        this.authorList = authorList;
    }

    public Books(String title) {
        this.title = title;
    }
}
