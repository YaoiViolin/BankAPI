package ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.Service.ServiceImpl.ObjectToJsonConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ObjectToJsonConverterTest {

    @Test
    void convertListToJsonString() {
        List<Object> cards = new ArrayList<>();
        cards.add(new Card(1, "0000"));
        cards.add(new Card(2, "0001"));

        String actual = ObjectToJsonConverter.convertListToJsonString(cards);
        String expected = "{\"id\":1,\"number\":\"0000\"}{\"id\":2,\"number\":\"0001\"}";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void convertCardBalanceToJson() {
        Map<String, BigDecimal> map = Collections.singletonMap("balance", new BigDecimal("11.5"));
        String actual = ObjectToJsonConverter.convertCardBalanceToJson(map);
        String expected = "{\"balance\":11.5}";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void covertJsonToMap() {
        String jsonString = "{ \"id\" : 1 }";
        Map<String, String> actual = ObjectToJsonConverter.covertJsonToMap(jsonString);
        Map<String, String> expected = Collections.singletonMap("id", "1");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void convertSingleCardToJson() {
        Card card = new Card(1, "0000");
        String actual = ObjectToJsonConverter.convertSingleCardToJson(card);
        String expected = "{\"id\":1,\"number\":\"0000\"}";

        Assertions.assertEquals(expected, actual);
    }
}