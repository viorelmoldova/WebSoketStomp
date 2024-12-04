package com.moldvio;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@Log4j2
@SpringBootApplication
public class WebSocketStompApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketStompApplication.class, args);
        log.info("== Conectare prin brauzer: http://{}:{} ==\n", getIp(), port());
    }

    private static String getIp(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Nu s-a putut obține adresa IP: " + e.getMessage());
        }
        return "localhost";
    }
    private static String port(){
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(fis);
            return properties.getProperty("server.port");
        } catch (IOException e) {
            log.error("Eroare la citirea fișierului de proprietăți: " + e.getMessage());
        }
        return "8080";
    }
}