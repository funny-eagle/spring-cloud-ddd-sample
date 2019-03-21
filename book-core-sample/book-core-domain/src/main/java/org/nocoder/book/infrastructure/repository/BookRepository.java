package org.nocoder.book.infrastructure.repository;

import org.nocoder.book.domain.model.book.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {

    int addBook(Book book);
}
