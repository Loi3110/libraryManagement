package models;

/**
 * RegularMember class - standard borrowing privileges
 * Borrow limit: 3 books
 * Fine rate: 5,000 VND per day
 */
public class RegularMember extends Member {

    public RegularMember(String memberId, String name, String phone, String email) {
        super(memberId, name, phone, email);
        this.borrowLimit = 3;
        this.finePerDay = 5000;  // VND per day
    }

    /**
     * Calculate fine for RegularMember: 5,000 VND per day
     */
    @Override
    public long calculateFine(int daysOverdue) {
        return daysOverdue * finePerDay;
    }

    @Override
    public String toString() {
        return String.format("RegularMember{ID='%s', name='%s', borrowed=%d/%d, fine=5000/day}",
                memberId, name, borrowedBookIds.size(), borrowLimit);
    }
}
