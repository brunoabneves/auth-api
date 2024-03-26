package store.ojuara.authapi.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Service
public class MessageSourceService {
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key, String defalt, Locale locale, Object ... args) {

        key = key.replaceAll("[\\{\\}]", "");

        return messageSource.getMessage(key, args, defalt, locale);
    }

    public String getMessage(String key) {
        return getMessage(key, "", Locale.getDefault());
    }

    public String getMessageFieldSource(FieldError field) {
        //TODO -> CONVERTER UTF-8
        return this.messageSource.getMessage(field, Locale.getDefault());
    }
}
