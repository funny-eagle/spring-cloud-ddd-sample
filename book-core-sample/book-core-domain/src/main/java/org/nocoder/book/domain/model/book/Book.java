package org.nocoder.book.domain.model.book;

/**
 * Domain 对象的实例只有两种获取方式：
 * 1、通过 Factory 进行创建并返回；
 * 2、通过 Repository 进行查询并返回
 * @author yangjinlong
 */
public class Book {

    /**
     * 构造方法包内仅可见，仅供 BookFactory 使用
     * @param name
     */
    Book(String name) {
        this.name = name;
    }

    Book(Long id, String name) {
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
