package hsrm.eibo.mediaplayer.Game.Network.General.Model;

import hsrm.eibo.mediaplayer.Game.Network.Host.Event.NetworkEventDispatcher;

import java.io.Serializable;

/**
 * A serializable Packet object for sending packages over network protocols.
 */
public class NetworkEventPacket implements Serializable{

    private static final long serialVersionUID = -2908104199930149826L;

    /**
     * ID of senderId; 0 is serverclient
     */
    private byte senderId = 0;

    /**
     * Second segment of the data packet representing the network event.
     */
    private NetworkEventDispatcher.NetworkEventType eventType = null;

    /**
     * Third segment of the data packet.
     * An array with all string arguments followed by event type.
     */
    private String[] eventArgs;

    /**
     * Generates a NetworkEventPacket out of given parameters.
     *
     * @param eventType
     * @param eventArgs
     */
    public NetworkEventPacket(byte senderId, NetworkEventDispatcher.NetworkEventType eventType, String[] eventArgs) {
        this.senderId = senderId;
        this.eventType = eventType;
        this.eventArgs = eventArgs;
    }

    /**
     * Getter for eventType
     * @return
     */
    public NetworkEventDispatcher.NetworkEventType getEventType() {
        return eventType;
    }

    /**
     * Getter for eventArgs
     * @return
     */
    public String[] getEventArgs() {
        return eventArgs;
    }

    /**
     * Getter for Sender id
     * @return
     */
    public byte getSenderId() {
        return senderId;
    }
}
