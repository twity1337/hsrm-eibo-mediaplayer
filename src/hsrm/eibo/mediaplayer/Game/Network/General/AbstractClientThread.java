package hsrm.eibo.mediaplayer.Game.Network.General;

import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import org.apache.commons.lang.SerializationUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractClientThread extends Thread {

    public static final int IDLE_SLEEP_TIME_MS = 50;
    protected int serverPort;
    protected InetAddress serverAddress;
    /**
     * The processing queue of all commands to process.
     */
    private static Queue<byte[]> dataProcessingQueue = new ConcurrentLinkedQueue<>();

    public AbstractClientThread(String threadName, int port, InetAddress address) {
        super(threadName);
        serverPort = port;
        serverAddress = address;
    }

    /**
     * Pushes the given packet to the clients processing queue to send over network.
     * @param packet The NetworkEventPacket to send.
     */
    public synchronized void pushToProcessingQueue(NetworkEventPacket packet) {

        // add timecode prefix to data before checking packet length..
        byte[] data = SerializationUtils.serialize(packet);

        if(data.length >= AbstractSocketManager.PACKET_LENGTH)
        {
            throw new RuntimeException("Length of data parameter is too big. Data must be maximal " + (AbstractSocketManager.PACKET_LENGTH-6) + " bytes.");
        }
        dataProcessingQueue.add(data);
    }

    protected DatagramSocket openSocket()
    {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            socket.close();
            return null;
        }
        return socket;
    }

    protected byte[] pollDataToProcess()
    {
        return dataProcessingQueue.poll();
    }
    protected boolean hasDataToProcess()
    {
        return dataProcessingQueue.isEmpty();
    }
}
