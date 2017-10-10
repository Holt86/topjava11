package ru.javawebinar.topjava.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessagesUtil {

    @Autowired
    private MessageSource source;

    public String getMessage(String key){
        return source.getMessage(key, null, LocaleContextHolder.getLocale());
    }
 }
