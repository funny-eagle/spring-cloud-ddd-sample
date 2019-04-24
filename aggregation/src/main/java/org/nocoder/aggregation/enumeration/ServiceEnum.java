package org.nocoder.aggregation.enumeration;

/**
 * service name
 * @author YangJinlong
 */
public enum ServiceEnum {
    /**
     * book service name
     */
    BOOK_SERVICE("BOOK-SERVICE"),
    /**
     * library service name
     */
    LIBRARY_SERVICE("LIBRARY-SERVICE");

    ServiceEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
