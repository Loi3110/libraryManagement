package services;

import models.Book;
import exceptions.BookNotFoundException;
import exceptions.ValidationException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BookService manages all book-related operations
 */
public class BookService {
    private Map<String, Book> books;

    public BookService() {
        this.books = new HashMap<>();
    }

    /**
     * Add a new book
     */
    public void addBook(Book book) throws ValidationException {
        if (book == null) {
            throw new ValidationException("Book cannot be null");
        }
        if (book.getBookId() == null || book.getBookId().isEmpty()) {
            throw new ValidationException("Book ID cannot be empty");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new ValidationException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new ValidationException("Book author cannot be empty");
        }
        if (book.getGenre() == null || book.getGenre().isEmpty()) {
            throw new ValidationException("Book genre cannot be empty");
        }
        if (books.containsKey(book.getBookId())) {
            throw new ValidationException("Book with ID '" + book.getBookId() + "' already exists");
        }
        books.put(book.getBookId(), book);
    }

    /**
     * Update an existing book
     */
    public void updateBook(Book book) throws BookNotFoundException, ValidationException {
        if (book == null || !books.containsKey(book.getBookId())) {
            throw new BookNotFoundException("Book with ID '" + book.getBookId() + "' not found");
        }
        books.put(book.getBookId(), book);
    }

    /**
     * Remove a book (only if not currently borrowed)
     */
    public void removeBook(String bookId) throws BookNotFoundException, ValidationException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID '" + bookId + "' not found");
        }
        if (book.getQuantity() < book.getTotalBorrowed()) {
            throw new ValidationException("Cannot remove book '" + bookId + "' - some copies are currently borrowed");
        }
        books.remove(bookId);
    }

    /**
     * Get a book by ID
     */
    public Book getBook(String bookId) throws BookNotFoundException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID '" + bookId + "' not found");
        }
        return book;
    }

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Search books by title (case-insensitive)
     */
    public List<Book> searchByTitle(String title) {
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search books by author (case-insensitive)
     */
    public List<Book> searchByAuthor(String author) {
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search books by genre (case-insensitive)
     */
    public List<Book> searchByGenre(String genre) {
        return books.values().stream()
                .filter(b -> b.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get most popular books (sorted by total borrowed count)
     */
    public List<Book> getMostPopularBooks() {
        return books.values().stream()
                .sorted((b1, b2) -> Integer.compare(b2.getTotalBorrowed(), b1.getTotalBorrowed()))
                .collect(Collectors.toList());
    }

    /**
     * Check if book is available
     */
    public boolean isAvailable(String bookId) throws BookNotFoundException {
        return getBook(bookId).isAvailable();
    }

    /**
     * Get all books as Map
     */
    public Map<String, Book> getBooksMap() {
        return new HashMap<>(books);
    }

    /**
     * Set books map (for loading from file)
     */
    public void setBooksMap(Map<String, Book> booksMap) {
        this.books = new HashMap<>(booksMap);
    }
}
