package github.team42.ggj25.buzzer;
import org.java_websocket.WebSocket;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;

public class WebSocketServerBuzzer extends WebSocketServer {

private boolean webSocketStatus;
private final BuzzerState buzzerState;
        // Constructor to initialize server with a port
        public WebSocketServerBuzzer(int port, BuzzerState buzzerState) {
            super(new InetSocketAddress(port));
            this.buzzerState = buzzerState;
        }

        // Called when the WebSocket server starts
        @Override
        public void onStart() {
            System.out.println("WebSocket server started successfully");
        }

        // Called when a new WebSocket connection is established
        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        // Called when a message is received from a client
        @Override
        public void onMessage(WebSocket conn, String message) {
            if (message.equals("buzzerHit")) {
                buzzerState.setIsTriggered(true);
            }
        }

        // Called when the WebSocket connection is closed
        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
        }

        // Called when an error occurs
        @Override
        public void onError(WebSocket conn, Exception ex) {

        }

    }
