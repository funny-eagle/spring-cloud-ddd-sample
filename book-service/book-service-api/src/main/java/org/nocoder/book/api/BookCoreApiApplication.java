package org.nocoder.book.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages="org.nocoder")
@EnableDiscoveryClient
public class BookCoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookCoreApiApplication.class, args);
    }

}
