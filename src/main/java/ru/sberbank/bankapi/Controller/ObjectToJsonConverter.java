package ru.sberbank.bankapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class ObjectToJsonConverter {
    public static String parseJson (String jsonString) {
        return null;
    }

    public static String convertListToJsonString(List<Card> cardsList) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        cardsList.forEach(card -> {
            try {
                mapper.writeValue(writer, card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return writer.toString();
    }
}
