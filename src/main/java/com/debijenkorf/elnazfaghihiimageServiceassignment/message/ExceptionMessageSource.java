package com.debijenkorf.elnazfaghihiimageServiceassignment.message;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionMessageSource extends ReloadableResourceBundleMessageSource {
    public ExceptionMessageSource(){
        this.setBasename("classpath:exception");
        this.setDefaultEncoding("UTF-8");
        this.setDefaultLocale(new Locale("en"));
    }
}
