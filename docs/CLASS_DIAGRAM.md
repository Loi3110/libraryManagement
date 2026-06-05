# Library Management System - Class Diagram

## Class Relationships

```
┌─────────────────────────────────────────────────────────────────┐
│                         Book                                     │
├─────────────────────────────────────────────────────────────────┤
│ - bookId: String (final)                                         │
│ - title: String                                                  │
│ - author: String                                                 │
│ - genre: String                                                  │
│ - publicationYear: int                                           │
│ - quantity: int                                                  │
│ - totalBorrowed: int                                             │
├─────────────────────────────────────────────────────────────────┤
│ + Book(bookId, title, author, genre, year, quantity)            │
│ + getters/setters for all attributes                            │
│ + decreaseStock(): void                                          │
│ + increaseStock(): void                                          │
│ + isAvailable(): boolean                                         │
│ + toString(): String                                             │
└─────────────────────────────────────────────────────────────────┘
                              ▲
                              │ uses
                              │
        ┌─────────────────────┴─────────────────────┐
        │                                           │
┌───────────────────────────────┐    ┌──────────────────────────────┐
│           Member (Abstract)   │    │   BorrowingTransaction       │
├───────────────────────────────┤    ├──────────────────────────────┤
│ # memberId: String (final)    │    │ - transactionId: String      │
│ # name: String                │    │ - bookId: String             │
│ # phone: String               │    │ - memberId: String           │
│ # email: String               │    │ - borrowDate: LocalDate      │
│ # borrowLimit: int            │    │ - dueDate: LocalDate         │
│ # finePerDay: long            │    │ - returnDate: LocalDate      │
│ # borrowedBooks: List         │    │ - fine: long                 │
├───────────────────────────────┤    ├──────────────────────────────┤
│ + Member(id, name, ...)       │    │ + BorrowingTransaction(...)  │
│ + calculateFine(...): long    │    │ + getters/setters            │
│ + canBorrow(): boolean        │    │ + calculateDaysOverdue()     │
│ + addBorrowedBook(Book)       │    │ + isReturned(): boolean      │
│ + removeBorrowedBook(Book)    │    │ + toString(): String         │
│ + getters/setters             │    └──────────────────────────────┘
│ + toString(): String          │
└───────────────────────────────┘
        ▲                    ▲
        │ extends          │ extends
        │                  │
┌───────────────┐    ┌──────────────────┐
│ RegularMember │    │  PremiumMember   │
├───────────────┤    ├──────────────────┤
│ borrowLimit:3 │    │ borrowLimit: 5   │
│ finePerDay:   │    │ finePerDay:      │
│  5000 VND     │    │  2500 VND        │
├───────────────┤    ├──────────────────┤
│ - calculateFine()  │ - calculateFine() │
│   (5000 per day)   │   (2500 per day)  │
└───────────────┘    └──────────────────┘
```

## Service Classes

```
┌──────────────────────────────────────────────┐
│          BookService                         │
├──────────────────────────────────────────────┤
│ - books: Map<String, Book>                   │
├──────────────────────────────────────────────┤
│ + addBook(Book): void                        │
│ + updateBook(Book): void                     │
│ + removeBook(String bookId): void            │
│ + getBook(String bookId): Book               │
│ + getAllBooks(): List<Book>                  │
│ + searchByTitle(String): List<Book>          │
│ + searchByAuthor(String): List<Book>         │
│ + searchByGenre(String): List<Book>          │
│ + getMostPopularBooks(): List<Book>          │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│        MemberService                         │
├──────────────────────────────────────────────┤
│ - members: Map<String, Member>               │
├──────────────────────────────────────────────┤
│ + addMember(Member): void                    │
│ + updateMember(Member): void                 │
│ + removeMember(String memberId): void        │
│ + getMember(String memberId): Member         │
│ + getAllMembers(): List<Member>              │
│ + searchByName(String): List<Member>         │
│ + getMembersWithMostBorrowings(): List<>     │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│      BorrowingService                        │
├──────────────────────────────────────────────┤
│ - transactions: Map<String, Borrowing...>    │
├──────────────────────────────────────────────┤
│ + borrowBook(...): void                      │
│ + returnBook(...): void                      │
│ + getTransaction(String): BorrowingTx        │
│ + getMemberBorrowingHistory(...): List<>     │
│ + getCurrentlyBorrowedBooks(): List<>        │
│ + getOverdueBooks(): List<>                  │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│       ReportService                          │
├──────────────────────────────────────────────┤
│ - bookService: BookService                   │
│ - memberService: MemberService               │
│ - borrowingService: BorrowingService         │
├──────────────────────────────────────────────┤
│ + generateCurrentBorrowingReport(): void     │
│ + generateOverdueBooksReport(): void         │
│ + generateMostPopularBooksReport(): void     │
│ + generateMembersWithMostBorrowingsReport(): │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│         DataManager                          │
├──────────────────────────────────────────────┤
│ - bookService: BookService                   │
│ - memberService: MemberService               │
│ - borrowingService: BorrowingService         │
├──────────────────────────────────────────────┤
│ + saveAllData(): void                        │
│ + loadAllData(): void                        │
│ + saveBooks(): void                          │
│ + loadBooks(): void                          │
│ + saveMembers(): void                        │
│ + loadMembers(): void                        │
│ + saveTransactions(): void                   │
│ + loadTransactions(): void                   │
└──────────────────────────────────────────────┘
```

## Main UI Class

```
┌──────────────────────────────────────────────┐
│     LibraryManagementUI                      │
├──────────────────────────────────────────────┤
│ - bookService: BookService                   │
│ - memberService: MemberService               │
│ - borrowingService: BorrowingService         │
│ - reportService: ReportService               │
│ - scanner: Scanner                           │
├──────────────────────────────────────────────┤
│ + start(): void                              │
│ + displayMainMenu(): void                    │
│ + manageBooks(): void                        │
│ + manageMembers(): void                      │
│ + manageBorrowing(): void                    │
│ + viewReports(): void                        │
│ + handleBookOperations(): void               │
│ + handleMemberOperations(): void             │
│ + handleBorrowingOperations(): void          │
│ + handleReportOperations(): void             │
└──────────────────────────────────────────────┘
```

## Exception Hierarchy

```
Exception
├── BookNotFoundException (extends Exception)
├── MemberNotFoundException (extends Exception)
├── ValidationException (extends Exception)
└── BorrowingException (extends Exception)
```

## Data Flow

```
User Input
    ↓
LibraryManagementUI (Console Interface)
    ↓
├── BookService
│   └── Book Objects
│   └── File I/O
│
├── MemberService
│   └── Member Objects (RegularMember, PremiumMember)
│   └── File I/O
│
├── BorrowingService
│   └── BorrowingTransaction Objects
│   └── File I/O
│
└── ReportService
    └── Generate Reports
    └── Display to User
```

## Design Patterns Used

1. **Service Pattern**: Separate business logic into services
2. **DAO Pattern**: DataManager handles persistence
3. **Template Method**: Member base class with calculateFine() template
4. **Singleton Pattern**: Can be applied to DataManager for single instance
5. **Builder Pattern**: Can be used for complex object creation
