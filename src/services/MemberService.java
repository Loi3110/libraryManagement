package services;

import models.Member;
import models.RegularMember;
import models.PremiumMember;
import exceptions.MemberNotFoundException;
import exceptions.ValidationException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MemberService manages all member-related operations
 */
public class MemberService {
    private Map<String, Member> members;

    public MemberService() {
        this.members = new HashMap<>();
    }

    /**
     * Add a new member
     */
    public void addMember(Member member) throws ValidationException {
        if (member == null) {
            throw new ValidationException("Member cannot be null");
        }
        if (member.getMemberId() == null || member.getMemberId().isEmpty()) {
            throw new ValidationException("Member ID cannot be empty");
        }
        if (member.getName() == null || member.getName().isEmpty()) {
            throw new ValidationException("Member name cannot be empty");
        }
        if (members.containsKey(member.getMemberId())) {
            throw new ValidationException("Member with ID '" + member.getMemberId() + "' already exists");
        }
        members.put(member.getMemberId(), member);
    }

    /**
     * Update an existing member
     */
    public void updateMember(Member member) throws MemberNotFoundException, ValidationException {
        if (member == null || !members.containsKey(member.getMemberId())) {
            throw new MemberNotFoundException("Member with ID '" + member.getMemberId() + "' not found");
        }
        members.put(member.getMemberId(), member);
    }

    /**
     * Remove a member (only if no outstanding borrowed books)
     */
    public void removeMember(String memberId) throws MemberNotFoundException, ValidationException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Member with ID '" + memberId + "' not found");
        }
        if (member.hasOutstandingBorrowings()) {
            throw new ValidationException("Cannot remove member '" + memberId + "' - has outstanding borrowed books");
        }
        members.remove(memberId);
    }

    /**
     * Get a member by ID
     */
    public Member getMember(String memberId) throws MemberNotFoundException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Member with ID '" + memberId + "' not found");
        }
        return member;
    }

    /**
     * Get all members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    /**
     * Search members by name (case-insensitive)
     */
    public List<Member> searchByName(String name) {
        return members.values().stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get members with most borrowings
     */
    public List<Member> getMembersWithMostBorrowings() {
        return members.values().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getBorrowedBookIds().size(), m1.getBorrowedBookIds().size()))
                .collect(Collectors.toList());
    }

    /**
     * Create a RegularMember
     */
    public Member createRegularMember(String memberId, String name, String phone, String email) {
        return new RegularMember(memberId, name, phone, email);
    }

    /**
     * Create a PremiumMember
     */
    public Member createPremiumMember(String memberId, String name, String phone, String email) {
        return new PremiumMember(memberId, name, phone, email);
    }

    /**
     * Get all members as Map
     */
    public Map<String, Member> getMembersMap() {
        return new HashMap<>(members);
    }

    /**
     * Set members map (for loading from file)
     */
    public void setMembersMap(Map<String, Member> membersMap) {
        this.members = new HashMap<>(membersMap);
    }
}
