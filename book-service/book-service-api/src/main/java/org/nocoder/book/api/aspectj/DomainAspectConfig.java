package org.nocoder.book.api.aspectj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Admin on 2017/7/13.
 */
@Configuration
public class DomainAspectConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DomainAspectConfig.class);

    @Bean
    public AnnotationBeanConfigurerAspect annotationBeanConfigurerAspect(){
        return AnnotationBeanConfigurerAspect.aspectOf();
    }
}
