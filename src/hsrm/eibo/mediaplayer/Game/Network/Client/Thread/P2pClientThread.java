package hsrm.eibo.mediaplayer.Game.Network.Client.Thread;

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


    public P2pClientThread(InetAddress serverAddress, int port) {
        super("Thread-Game-Thread-P2pClient");
        serverPort = port;
        this.serverAddress = serverAddress;
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

        // TODO: machen... ;; Bisher nur zum Testen...

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
                    byte[] data = this.dataProcessingQueue.poll();
                    DatagramPacket packet = new DatagramPacket(data, data.length, this.serverAddress, SocketManager.APPLICATION_PORT);
                    socket.send(packet);
                    System.out.println("~~ package sent to " + this.serverAddress.toString() + " ~~");
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
