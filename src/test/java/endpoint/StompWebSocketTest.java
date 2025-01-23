package endpoint;

import com.moldvio.WebSocketStompApplication;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WebSocketStompApplication.class)
@AutoConfigureMockMvc
public class StompWebSocketTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Injectează SimpMessagingTemplate

    @Test
    public void testWebSocketConnection() throws Exception {
        // Verificăm că endpoint-ul WebSocket răspunde corect
        mockMvc.perform(MockMvcRequestBuilders.get("/ws"))
                .andExpect(status().isOk());
    }

    @Test
    public void testWebSocketMessaging() throws Exception {
        String testMessage = "Test message";

        // Configurează WebSocket client
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new org.springframework.messaging.converter.StringMessageConverter());

        // Conectează clientul la server
        stompClient.connect("ws://localhost:8098/ws", new org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter() {});

        // Trimite un mesaj folosind SimpMessagingTemplate
        messagingTemplate.convertAndSend("/topic/messages", testMessage);

        // Aici poți adăuga validări suplimentare pentru a verifica dacă mesajul a fost primit corect
    }

    @Test
    public void testReceiveMessageFromServer() throws Exception {
        // Creăm un latch pentru a sincroniza testul
        CountDownLatch latch = new CountDownLatch(1);

        // Creăm clientul WebSocket
        WebSocketClient webSocketClient = new SockJsClient(
                Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))
        );

        // Creăm clientul WebSocket STOMP
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        // Conectăm la server prin WebSocket
        StompSession stompSession = stompClient.connect("ws://localhost:8098/ws", new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                // Abonăm-ne la canalul "/topic/messages"
                session.subscribe("/topic/messages", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders stompHeaders, Object o) {
                        // Când primim mesajul, eliberăm latch-ul
                        System.out.println("Mesaj primit: " + o);
                        latch.countDown();  // Eliberăm latch-ul când mesajul este primit
                    }
                });
            }
        }).get();

        // Așteptăm până când mesajul este primit
        boolean messageReceived = latch.await(10, TimeUnit.SECONDS);

        // Verificăm că mesajul a fost primit
        assertTrue(messageReceived, "Mesajul nu a fost primit la timp");

        // Oprirea conexiunii
        stompSession.disconnect();
    }
}
