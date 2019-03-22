package org.nocoder.book.api.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.nocoder.commons.mybatispage.PageInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by qingniu on 2016/10/26.
 */
@Configuration
@MapperScan("org.nocoder.book.infrastructure.repository")
@EnableTransactionManagement
public class MybatisConfig {

    private static final String MYBATIS_CONFIG_FILE = "mybatis-config.xml";

    /**
     * 注册datasource，通过@ConfigurationProperties(prefix="c3p0")将properties文件中c3p0开头的属性map到datasource相应的属性上
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "c3p0")
    public ComboPooledDataSource dataSource() {
        return new ComboPooledDataSource();
    }


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG_FILE));
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setDatabaseType("mysql");
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
