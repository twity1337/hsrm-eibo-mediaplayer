package hsrm.eibo.mediaplayer.Game.Network.General.Model;

import hsrm.eibo.mediaplayer.Game.Network.Host.Event.NetworkEventDispatcher;

public class NetworkEventPacket {

    /**
     * The last 4 bytes of current milliseconds
     */
    private int timecode = 0;

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
     * @param timecode
     * @param eventType
     * @param eventArgs
     */
    public NetworkEventPacket(int timecode, NetworkEventDispatcher.NetworkEventType eventType, String[] eventArgs) {
        this.timecode = timecode;
        this.eventType = eventType;
        this.eventArgs = eventArgs;
    }

    /**
     * Generates a NetworkEventPacket out of a byte array.
     * @param packetData
     */
    public NetworkEventPacket(byte[] packetData) {

    }

    public int getTimecode() {
        return timecode;
    }

    public NetworkEventDispatcher.NetworkEventType getEventType() {
        return eventType;
    }

    public String[] getEventArgs() {
        return eventArgs;
    }

    public byte[] toBytes() {
        return null;
    }

    private byte[] generateTimeCode()
    {
        long currentTime = System.currentTimeMillis();
        return new byte[] {
                (byte) (currentTime >> 24),
                (byte) (currentTime >> 16),
                (byte) (currentTime >> 8),
                (byte) currentTime
        };
    }
}
