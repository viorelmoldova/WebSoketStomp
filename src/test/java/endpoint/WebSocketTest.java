package endpoint;

import com.moldvio.WebSocketStompApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WebSocketStompApplication.class)
@AutoConfigureMockMvc
public class WebSocketTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Test
    public void testWebSocketConnection() throws Exception {
        // Verificăm că endpoint-ul WebSocket răspunde corect
        mockMvc.perform(MockMvcRequestBuilders.get("/ws"))
                .andExpect(status().isOk());
    }

    @Test
    public void testWebSocketMessaging() throws Exception {
        // Testează trimiterea unui mesaj pe un canal WebSocket
        String testMessage = "Test message";

        // Configurează WebSocket client
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new org.springframework.messaging.converter.StringMessageConverter());

        // Conectează clientul la server
        stompClient.connect("ws://localhost:8098/ws", new org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter() {});

        // Trimite un mesaj
        messagingTemplate.convertAndSend("/topic/messages", testMessage);

        // Aici poți adăuga validări suplimentare
    }
}
