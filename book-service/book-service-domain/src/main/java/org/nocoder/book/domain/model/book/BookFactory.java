package org.nocoder.book.domain.model.book;

/**
 * BookFactory 接口
 * Factory 只负责创建domain对象并返回，不做数据持久化操作
 * @author yangjinlong
 */
public interface BookFactory {
    /**
     * 创建Book对象
     * @param name
     * @return
     * @throws Exception
     */
    Book createBook(String name) throws Exception;
}
