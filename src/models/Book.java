package models;

/**
 * Book model representing a book in the library
 */
public class Book {
    private final String bookId;  // Unique, cannot be modified
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private int quantity;
    private int totalBorrowed;  // Track popularity

    /**
     * Constructor for Book
     */
    public Book(String bookId, String title, String author, String genre, 
                int publicationYear, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.totalBorrowed = 0;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    /**
     * Decrease stock when a book is borrowed
     */
    public void decreaseStock() {
        if (this.quantity > 0) {
            this.quantity--;
            this.totalBorrowed++;
        }
    }

    /**
     * Increase stock when a book is returned
     */
    public void increaseStock() {
        this.quantity++;
    }

    /**
     * Check if book is available
     */
    public boolean isAvailable() {
        return this.quantity > 0;
    }

    @Override
    public String toString() {
        return String.format("Book{ID='%s', title='%s', author='%s', genre='%s', year=%d, quantity=%d, borrowed=%d}",
                bookId, title, author, genre, publicationYear, quantity, totalBorrowed);
    }
}
