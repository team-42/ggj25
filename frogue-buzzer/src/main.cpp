#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiManager.h>
#include <WebSocketClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>

// WiFiManager instance

WiFiManager wifiManager;
// WebSocketClient instance
WebSocketClient ws(false);
// HTTP server instance
ESP8266WebServer server(80);

// Function to save string to EEPROM
void saveStringToEEPROM(const String &str, int startAddr) {
    for (int i = 0; i < str.length(); ++i) {
        EEPROM.write(startAddr + i, str[i]);
    }
    EEPROM.write(startAddr + str.length(), '\0'); // Null-terminate the string
    EEPROM.commit();
}

// Function to read string from EEPROM
String readStringFromEEPROM(int startAddr) {
    String str = "";
    char ch;
    for (int i = startAddr; (ch = EEPROM.read(i)) != '\0'; ++i) {
        str += ch;
    }
    return str;
}

void handleRoot() {
    String savedString = readStringFromEEPROM(0);
    String html = "<html><head><style>";
    html += "body { background-image: url('/water_lily_ambient.jpeg'); background-size: cover; text-align: center; }";
    html += "form { display: inline-block; margin-top: 20px; }";
    html += "input[type='text'] { width: 80%; padding: 10px; margin: 10px 0; }";
    html += "input[type='submit'] { padding: 10px 20px; }";
    html += "</style></head><body>";
    html += "<h1>Frogue - Odysseys Button Config</h1>";
    html += "<form action=\"/save\" method=\"POST\">";
    html += "PC IP <input type=\"text\" name=\"savedString\" value=\"" + savedString + "\"><br>";
    html += "<input type=\"submit\" value=\"Speichern und Verbinden\">";
    html += "</form>";
    html += "</body></html>";
    server.send(200, "text/html", html);
}

void handleSave() {
    if (server.hasArg("savedString")) {
        String newString = server.arg("savedString");
        saveStringToEEPROM(newString, 0);
        server.send(200, "text/plain", "String saved and attempting to connect to WebSocket...");

        // Attempt to connect to WebSocket with new string
        if (ws.connect(newString, "/", 13337)) {
            ws.send("Connected");
        } else {

        }
    } else {
        server.send(400, "text/plain", "Bad Request");
    }
}

void setup() {
    pinMode(3, INPUT_PULLUP);

    // Connect to WiFi
    if (!wifiManager.autoConnect("AP-BUZZER")) {
        ESP.restart();
    }

    // Start HTTP server
    server.on("/", handleRoot);
    server.on("/save", HTTP_POST, handleSave);
    server.begin();

    // Initialize EEPROM
    EEPROM.begin(512);

    // Example usage
    String savedString = readStringFromEEPROM(0);
}

void loop() {
    String savedString = readStringFromEEPROM(0);
    // Handle HTTP server
    server.handleClient();

    // Handle WebSocket connection
    if (!ws.isConnected()) {
        if (ws.connect(savedString, "/", 13337)) {
            ws.send("Connected");
        } else {
            delay(3000); // Delay to regulate retry attempts
            return;
        }
    }

    // Check button state and send message if pressed
    if (digitalRead(3) == LOW) {
        ws.send("buzzerHit");
        delay(500);
    }
}
