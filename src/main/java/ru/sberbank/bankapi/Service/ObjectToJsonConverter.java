package ru.sberbank.bankapi.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectToJsonConverter {
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

    public static String convertCardBalanceToJson(Map<String, BigDecimal> map) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();

        try {
            mapper.writeValue(writer, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static Map<String, String> covertJsonToMap (String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        try {
            map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String convertSingleCardToJson(Card card) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();

        try {
            mapper.writeValue(writer, card);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
