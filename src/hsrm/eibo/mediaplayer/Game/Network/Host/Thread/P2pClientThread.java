package hsrm.eibo.mediaplayer.Game.Network.Host.Thread;

import hsrm.eibo.mediaplayer.Game.Network.SocketManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class P2pClientThread extends Thread {

    private int serverPort;
    private InetAddress serverAddress;

    /**
     * The processing queue of all commands to process.
     */
    private Queue<byte[]> dataProcessingQueue = new ConcurrentLinkedQueue<>();


    public P2pClientThread(InetAddress address, int port) {
        super("Thread-Game-Thread-P2pClient");
        serverPort = port;
        serverAddress = address;
    }

    public synchronized void pushToProcessingQueue(byte[] data) {
        if(data.length >= SocketManager.PACKET_LENGTH)
        {
            throw new RuntimeException("Length of data parameter is too big. Data must be maximal " + SocketManager.PACKET_LENGTH + " bytes.");
        }
        this.dataProcessingQueue.add(data);
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            socket.close();
            return;
        }

        while(!super.isInterrupted())
        {
            while(!this.dataProcessingQueue.isEmpty()) {
                try {
                    InetAddress adressOfClient = InetAddress.getByName("127.0.0.1");
                    DatagramPacket packet = new DatagramPacket(this.dataProcessingQueue.poll(), SocketManager.PACKET_LENGTH, adressOfClient, SocketManager.APPLICATION_PORT);
                    socket.send(packet);
                    System.out.println("~~ package sent ~~");
                } catch (IOException e) {
                    e.printStackTrace();
                    this.interrupt();
                    break;
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                this.interrupt();
                break;
            }

        }
        socket.close();
    }
}
