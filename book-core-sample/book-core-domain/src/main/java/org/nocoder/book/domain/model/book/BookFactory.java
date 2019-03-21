package org.nocoder.book.domain.model.book;

public interface BookFactory {
    Book createBook(long id, String name) throws Exception;
}
