package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * BorrowingTransaction represents a borrowing/returning record
 */
public class BorrowingTransaction {
    private final String transactionId;
    private String bookId;
    private String memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;  // Usually 7 days after borrow date
    private LocalDate returnDate;  // null if not returned yet
    private long fine;

    /**
     * Constructor for BorrowingTransaction
     */
    public BorrowingTransaction(String transactionId, String bookId, String memberId,
                                LocalDate borrowDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public long getFine() {
        return fine;
    }

    public void setFine(long fine) {
        this.fine = fine;
    }

    /**
     * Calculate number of days overdue
     */
    public int calculateDaysOverdue() {
        if (returnDate == null) {
            // If not returned yet, calculate from today
            return (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        } else {
            return (int) ChronoUnit.DAYS.between(dueDate, returnDate);
        }
    }

    /**
     * Check if book has been returned
     */
    public boolean isReturned() {
        return returnDate != null;
    }

    /**
     * Check if book is overdue
     */
    public boolean isOverdue() {
        if (returnDate == null) {
            return LocalDate.now().isAfter(dueDate);
        } else {
            return returnDate.isAfter(dueDate);
        }
    }

    @Override
    public String toString() {
        String status = isReturned() ? String.format("RETURNED on %s", returnDate) : "BORROWED";
        String fineInfo = fine > 0 ? String.format(", fine=%,d VND", fine) : "";
        return String.format("Transaction{ID='%s', book='%s', member='%s', borrowed=%s, due=%s, %s%s}",
                transactionId, bookId, memberId, borrowDate, dueDate, status, fineInfo);
    }
}
