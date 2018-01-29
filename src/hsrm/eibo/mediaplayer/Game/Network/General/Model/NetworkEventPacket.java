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
     * Username of sender
     */
    private String sender = "";

    /**
     * Second segment of the data packet representing the network event.
     */
    private NetworkEventDispatcher.NetworkEventType eventType;

    /**
     * Third segment of the data packet.
     * An array with all arguments followed by event type.
     */
    private int[] eventArgs;

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
    public NetworkEventPacket(String sender, NetworkEventDispatcher.NetworkEventType eventType, int[] eventArgs) {
        this.sender = sender;
        this.eventType = eventType;
        this.eventArgs = eventArgs;
    }

    /**
     * Minimalistic constructor
     * @param sender
     * @param eventType
     */
    public NetworkEventPacket(String sender, NetworkEventDispatcher.NetworkEventType eventType) {
        this(sender, eventType, new int[]{});
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
    public int[] getEventArgs() {
        return eventArgs;
    }

    /**
     * Getter for Sendername
     * @return
     */
    public String getSenderName() {
        return sender;
    }

    public InetAddress getRemoteIpAdress() {
        return remoteIpAdress;
    }

    public void setRemoteIpAdress(InetAddress remoteIpAdress) {
        this.remoteIpAdress = remoteIpAdress;
    }
}
