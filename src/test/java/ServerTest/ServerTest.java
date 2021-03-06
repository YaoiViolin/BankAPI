package ServerTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.Controller.Server;
import ru.sberbank.bankapi.DataAccessObject.DBConnector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sberbank.bankapi.Controller.Server.startServer;
import static ru.sberbank.bankapi.Controller.Server.stopServer;

class ServerTest {

    @BeforeAll
    static void beforeAll() {
        DBConnector.createConnection();
        DBConnector.dbCreate();
        DBConnector.dbInit();
    }

    @AfterAll
    static void afterAll() {
        DBConnector.closeConnection();
        stopServer();
    }

    @Test
    void getCardsListTest() throws IOException, InterruptedException {
        startServer();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/clients/rita/cards/"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean match = Pattern.matches(".\\{\"id\":\\d+,\"number\":\"\\d{16}\"}.+", response.body());
        assertTrue(match);

        Server.stopServer();
    }
    @Test
    void getBalanceTest() throws IOException, InterruptedException {
        startServer();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/clients/rita/cards/1"))
                .GET()
                .build();

        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean match = Pattern.matches("\\{\"balance\":.+}", response.body());
        assertTrue(match);

        Server.stopServer();
    }

    @Test
    void newCardTest() throws URISyntaxException, IOException, InterruptedException {
        startServer();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/clients/rita/cards/"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"id\" : 1 }"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean match = Pattern.matches("\\{\"id\":\\d+,\"number\":\"\\d{16}\"}", response.body());
        assertTrue(match);
       Server.stopServer();
    }

    @Test
    void setBalanceTest() throws URISyntaxException, IOException, InterruptedException {
        startServer();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/clients/rita/cards/1"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"sum\" : 14.7 }"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean match = Pattern.matches("\\{\"balance\":\\d.+}", response.body());
        assertTrue(match);

        Server.stopServer();
    }

    @Test
    void makeTransactionAndGetList() throws URISyntaxException, IOException, InterruptedException {
        startServer();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/counterparty/"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{\"cardNumberFrom\" : \"0000000000000000\", \"cardNumberTo\" : \"0000000000000002\", \"sum\" : 5.35}"))
                .build();

        HttpResponse<String> responseFirst = client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean match = Pattern.matches(".\\{\"balance\":.+},\\{\"balance\":.+}.", responseFirst.body());
        assertTrue(match);

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/counterparty/"))
                .GET()
                .build();

        HttpResponse<String> responseSecond = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        boolean matchList = Pattern.matches(".\\{\"cardNumberFrom\":\"\\d{16}\",\"cardNumberTo\":\"\\d{16}\",\"sum\":.+}.", responseSecond.body());
        assertTrue(matchList);

        Server.stopServer();
    }


}