package hsrm.eibo.mediaplayer.Game.Network.General;

import org.apache.commons.lang.ArrayUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractClientThread extends Thread {

    protected int serverPort;
    protected InetAddress serverAddress;
    /**
     * The processing queue of all commands to process.
     */
    private Queue<byte[]> dataProcessingQueue = new ConcurrentLinkedQueue<>();

    public AbstractClientThread(String threadName, int port, InetAddress address) {
        super(threadName);
        serverPort = port;
        serverAddress = address;
    }


    public synchronized void pushToProcessingQueue(String data) {
        pushToProcessingQueue(data.getBytes());
    }

    public synchronized void pushToProcessingQueue(byte[] data) {

        // add timecode prefix to data before checking packet length..
        byte[] processedData = ArrayUtils.addAll(ArrayUtils.addAll(this.generateTimeCode(), new byte[]{0,0}), data);

        if(processedData.length >= AbstractSocketManager.PACKET_LENGTH)
        {
            throw new RuntimeException("Length of data parameter is too big. Data must be maximal " + (AbstractSocketManager.PACKET_LENGTH-6) + " bytes.");
        }
        this.dataProcessingQueue.add(processedData);
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
