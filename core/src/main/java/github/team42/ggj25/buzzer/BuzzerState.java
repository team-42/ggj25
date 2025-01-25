package github.team42.ggj25.buzzer;

import java.time.Instant;

public class BuzzerState {
    private long lastBuzzerCheck;
    private boolean isTriggered;
    private boolean isConnected;

    public BuzzerState() {
        this.lastBuzzerCheck = Instant.now ().getEpochSecond ();
        this.isConnected = false;
        this.isTriggered = false;
    }
public boolean getIsTriggered() {
        return this.isTriggered;
}
public void setIsTriggered(boolean value) {
        this.isTriggered = value;
}
    public boolean getIsConnected() {
        return this.isConnected;
    }

    public void setIsConnected() {
        this.isConnected = true;
    }

    public boolean triggeredSinceLastCheck() {
        if (Instant.now ().getEpochSecond () - this.lastBuzzerCheck > 0.5f
            && this.getIsTriggered()) {
            this.setIsTriggered(false);
            return true;
        }
        return false;
    }
}
