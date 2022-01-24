import java.util.Scanner;

public class BookApplication {
    public static void main(String[] args) {
//        call the BookDataBaseManager constructor and create an object
        BookDatabaseManager store =new BookDatabaseManager();
//create a scanner object
        Scanner sc =new Scanner(System.in);
        int choice;
//create the main menu
        do {
            System.out.println("""
                                    
                    Select a number to continue

                    1. Print all the books from the database (showing the authors)\s
                    2. Print all the authors from the database (showing the books)\s
                    3. Add a book to the database for an existing author \s
                    4. Add a new author\s
                    5. Quit
                    """);
            choice=sc.nextInt();
            if (choice==1) store.loadBooks();
            else if(choice==2) store.loadAuthors();
            else if (choice==3) store.addNewBook();
            else if(choice==4) store.addNewAuthor();
            else {
                break;
            }
    }while (true);
        System.out.println("\nBye!");
}}
