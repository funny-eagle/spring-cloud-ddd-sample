package org.nocoder.book.domain.model.book;

public class Book {

    Book(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
