package library.model;

/**
 * Represents a single book in the Digital Library.
 *
 * Purpose:
 * Stores the metadata of each book loaded from the TXT files.
 *
 * Fields:
 * - Book Type
 * - Title
 * - Author
 * - ISBN
 * - Category
 */
public class Book {

    // ==========================================
    // Book Attributes
    // ==========================================

    private String bookType;
    private String title;
    private String author;
    private String isbn;
    private String category;

    // ==========================================
    // Constructor
    // ==========================================

    /**
     * Creates a Book object with all required information.
     */
    public Book(String bookType, String title, String author,
                String isbn, String category) {

        this.bookType = bookType;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
    }

    // ==========================================
    // Getters
    // ==========================================

    public String getBookType() {
        return bookType;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    // ==========================================
    // String Representation
    // ==========================================

    /**
     * Returns the book information in readable format.
     */
    @Override
    public String toString() {
        return "Book Type : " + bookType + "\n"
             + "Title     : " + title + "\n"
             + "Author    : " + author + "\n"
             + "ISBN      : " + isbn + "\n"
             + "Category  : " + category;
    }
}