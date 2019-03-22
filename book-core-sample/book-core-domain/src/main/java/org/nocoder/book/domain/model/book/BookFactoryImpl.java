package org.nocoder.book.domain.model.book;

import org.nocoder.book.infrastructure.repository.BookRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  Factory 只负责创建domain对象并返回，不做数据持久化操作
 *  @author yangjinlong
 */
@Component
public class BookFactoryImpl implements BookFactory {

    @Override
    public Book createBook(String name) throws Exception {
        Book book = new Book(name);
        return book;
    }
}
