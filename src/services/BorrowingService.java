package services;

import models.Book;
import models.Member;
import models.BorrowingTransaction;
import exceptions.BookNotFoundException;
import exceptions.MemberNotFoundException;
import exceptions.BorrowingException;
import exceptions.ValidationException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BorrowingService manages borrowing and returning operations
 */
public class BorrowingService {
    private Map<String, BorrowingTransaction> transactions;
    private BookService bookService;
    private MemberService memberService;
    private int transactionCounter;

    public BorrowingService(BookService bookService, MemberService memberService) {
        this.transactions = new HashMap<>();
        this.bookService = bookService;
        this.memberService = memberService;
        this.transactionCounter = 0;
    }

    /**
     * Borrow a book
     */
    public void borrowBook(String memberId, String bookId, LocalDate borrowDate) 
            throws BorrowingException, BookNotFoundException, MemberNotFoundException, ValidationException {
        
        // Validate dates
        if (borrowDate == null) {
            throw new ValidationException("Borrow date cannot be null");
        }
        if (borrowDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Borrow date cannot be in the future");
        }

        // Get member and validate
        Member member = memberService.getMember(memberId);
        if (!member.canBorrow()) {
            throw new BorrowingException("Member '" + memberId + "' has reached borrowing limit of " + member.getBorrowLimit());
        }

        // Get book and validate
        Book book = bookService.getBook(bookId);
        if (!book.isAvailable()) {
            throw new BorrowingException("Book '" + bookId + "' is not available");
        }

        // Create transaction
        LocalDate dueDate = borrowDate.plusDays(7);  // 7-day borrowing period
        String transactionId = "TX" + System.currentTimeMillis();
        BorrowingTransaction transaction = new BorrowingTransaction(transactionId, bookId, memberId, borrowDate, dueDate);

        // Update book stock
        book.decreaseStock();

        // Update member's borrowed books
        member.addBorrowedBook(bookId);

        // Store transaction
        transactions.put(transactionId, transaction);
    }

    /**
     * Return a book
     */
    public long returnBook(String memberId, String bookId, LocalDate returnDate) 
            throws BorrowingException, BookNotFoundException, MemberNotFoundException, ValidationException {
        
        // Validate dates
        if (returnDate == null) {
            throw new ValidationException("Return date cannot be null");
        }
        if (returnDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Return date cannot be in the future");
        }

        // Find active transaction for this member and book
        BorrowingTransaction transaction = transactions.values().stream()
                .filter(t -> t.getMemberId().equals(memberId) && t.getBookId().equals(bookId) && !t.isReturned())
                .findFirst()
                .orElseThrow(() -> new BorrowingException("No active borrowing found for member '" + memberId + "' and book '" + bookId + "'"));

        // Validate return date is after borrow date
        if (returnDate.isBefore(transaction.getBorrowDate())) {
            throw new ValidationException("Return date cannot be before borrow date");
        }

        // Set return date
        transaction.setReturnDate(returnDate);

        // Calculate fine if overdue
        long fine = 0;
        if (transaction.isOverdue()) {
            int daysOverdue = transaction.calculateDaysOverdue();
            Member member = memberService.getMember(memberId);
            fine = member.calculateFine(daysOverdue);
            transaction.setFine(fine);
        }

        // Update book stock
        Book book = bookService.getBook(bookId);
        book.increaseStock();

        // Update member's borrowed books
        Member member = memberService.getMember(memberId);
        member.removeBorrowedBook(bookId);

        return fine;
    }

    /**
     * Get all currently borrowed books
     */
    public List<BorrowingTransaction> getCurrentlyBorrowedBooks() {
        return transactions.values().stream()
                .filter(t -> !t.isReturned())
                .collect(Collectors.toList());
    }

    /**
     * Get all overdue books
     */
    public List<BorrowingTransaction> getOverdueBooks() {
        return transactions.values().stream()
                .filter(t -> !t.isReturned() && t.isOverdue())
                .collect(Collectors.toList());
    }

    /**
     * Get borrowing history for a specific member
     */
    public List<BorrowingTransaction> getMemberBorrowingHistory(String memberId) {
        return transactions.values().stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    /**
     * Get a specific transaction
     */
    public BorrowingTransaction getTransaction(String transactionId) throws BorrowingException {
        BorrowingTransaction transaction = transactions.get(transactionId);
        if (transaction == null) {
            throw new BorrowingException("Transaction '" + transactionId + "' not found");
        }
        return transaction;
    }

    /**
     * Get all transactions as Map
     */
    public Map<String, BorrowingTransaction> getTransactionsMap() {
        return new HashMap<>(transactions);
    }

    /**
     * Set transactions map (for loading from file)
     */
    public void setTransactionsMap(Map<String, BorrowingTransaction> transactionsMap) {
        this.transactions = new HashMap<>(transactionsMap);
    }
}
