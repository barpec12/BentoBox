package world.bentobox.bentobox.api.events;

import java.util.UUID;

/**
 * Fired when a message is going to an offline player
 *
 * @author tastybento
 * @since 1.5.0
 */
public class OfflineMessageEvent extends PremadeEvent {
    private final UUID offlinePlayer;
    private final String message;

    /**
     * @param offlinePlayer
     * @param message
     */
    public OfflineMessageEvent(UUID offlinePlayer, String message) {
        this.offlinePlayer = offlinePlayer;
        this.message = message;
    }

    /**
     * @return the offlinePlayer
     */
    public UUID getOfflinePlayer() {
        return offlinePlayer;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

}
