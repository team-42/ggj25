package github.team42.ggj25.buzzer;

import java.time.Instant;

public class BuzzerState {
    protected long lastBuzzerCheck;
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
        if (this.isTriggered) {
            this.setIsTriggered(false);
            return true;
        }
        return false;
    }
}
