package org.nocoder.book.applicatoin.assembler.book;

import org.nocoder.book.applicatoin.dto.book.BookDto;
import org.nocoder.book.domain.model.book.Book;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain 对象与 DTO 对象转换
 *
 * @author yangjinlong
 */
@Component
public class BookAssembler {

    public BookDto convertToBookDto(Book book) {
        if (book == null) {
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setName(book.getName());
        return dto;
    }

    /**
     * 批量装换 Book 为 BookDTO
     *
     * @param bookList
     * @return
     */
    public List<BookDto> convertToBookDtoList(List<Book> bookList) {
        if (CollectionUtils.isEmpty(bookList)) {
            return null;
        }
        List<BookDto> bookDtoList = new ArrayList<>();
        bookList.forEach(book -> {
            BookDto dto = convertToBookDto(book);
            bookDtoList.add(dto);
        });
        return bookDtoList;
    }
}
