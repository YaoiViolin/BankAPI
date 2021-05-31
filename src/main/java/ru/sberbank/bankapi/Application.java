package ru.sberbank.bankapi;

import ru.sberbank.bankapi.Controller.Server;
import ru.sberbank.bankapi.Service.ObjectToJsonConverter;
import ru.sberbank.bankapi.Service.Service;
import ru.sberbank.bankapi.Service.ServiceImpl;

import java.io.IOException;
import java.util.Map;


public class Application {


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();



    }

}




