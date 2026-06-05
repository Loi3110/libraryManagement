package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Member class representing a library member
 */
public abstract class Member {
    protected final String memberId;  // Unique, cannot be modified
    protected String name;
    protected String phone;
    protected String email;
    protected int borrowLimit;
    protected long finePerDay;
    protected List<String> borrowedBookIds;  // List of borrowed book IDs

    /**
     * Constructor for Member
     */
    public Member(String memberId, String name, String phone, String email) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.borrowedBookIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public long getFinePerDay() {
        return finePerDay;
    }

    public List<String> getBorrowedBookIds() {
        return borrowedBookIds;
    }

    /**
     * Add a borrowed book
     */
    public void addBorrowedBook(String bookId) {
        if (!borrowedBookIds.contains(bookId)) {
            borrowedBookIds.add(bookId);
        }
    }

    /**
     * Remove a borrowed book
     */
    public void removeBorrowedBook(String bookId) {
        borrowedBookIds.remove(bookId);
    }

    /**
     * Check if member can borrow more books
     */
    public boolean canBorrow() {
        return borrowedBookIds.size() < borrowLimit;
    }

    /**
     * Check if member has outstanding borrowed books
     */
    public boolean hasOutstandingBorrowings() {
        return !borrowedBookIds.isEmpty();
    }

    /**
     * Abstract method to calculate fine - implemented by subclasses
     */
    public abstract long calculateFine(int daysOverdue);

    @Override
    public String toString() {
        return String.format("Member{ID='%s', name='%s', phone='%s', email='%s', borrowed=%d/%d}",
                memberId, name, phone, email, borrowedBookIds.size(), borrowLimit);
    }
}
