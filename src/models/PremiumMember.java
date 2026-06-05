package models;

/**
 * PremiumMember class - enhanced borrowing privileges
 * Borrow limit: 5 books
 * Fine rate: 2,500 VND per day (50% discount)
 */
public class PremiumMember extends Member {

    public PremiumMember(String memberId, String name, String phone, String email) {
        super(memberId, name, phone, email);
        this.borrowLimit = 5;
        this.finePerDay = 2500;  // VND per day (50% discount)
    }

    /**
     * Calculate fine for PremiumMember: 2,500 VND per day (polymorphic override)
     */
    @Override
    public long calculateFine(int daysOverdue) {
        return daysOverdue * finePerDay;
    }

    @Override
    public String toString() {
        return String.format("PremiumMember{ID='%s', name='%s', borrowed=%d/%d, fine=2500/day}",
                memberId, name, borrowedBookIds.size(), borrowLimit);
    }
}
