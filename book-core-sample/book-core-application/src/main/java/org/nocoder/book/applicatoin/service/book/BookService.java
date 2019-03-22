package org.nocoder.book.applicatoin.service.book;

import org.nocoder.book.applicatoin.dto.book.BookDto;
import org.nocoder.book.applicatoin.exception.BookServiceException;

/**
 * BookService
 *
 * @author yangjinlong
 */
public interface BookService {
    int addBook(BookDto book) throws BookServiceException;

    BookDto getBookById(long id) throws BookServiceException;
}
