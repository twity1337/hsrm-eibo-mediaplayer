package hsrm.eibo.mediaplayer.Game.Network.General.Model;

import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;

import java.io.Serializable;
import java.net.InetAddress;

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
    private NetworkEventDispatcher.NetworkEventType eventType;

    /**
     * Third segment of the data packet.
     * An array with all string arguments followed by event type.
     */
    private String[] eventArgs;

    /**
     * Remote InetAdress of packet sender. Is set after the packet has been received.
     */
    private transient InetAddress remoteIpAdress;

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
     * Minimalistic constructor
     * @param senderId
     * @param eventType
     */
    public NetworkEventPacket(byte senderId, NetworkEventDispatcher.NetworkEventType eventType) {
        this(senderId, eventType, new String[]{});
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

    public InetAddress getRemoteIpAdress() {
        return remoteIpAdress;
    }

    public void setRemoteIpAdress(InetAddress remoteIpAdress) {
        this.remoteIpAdress = remoteIpAdress;
    }
}
