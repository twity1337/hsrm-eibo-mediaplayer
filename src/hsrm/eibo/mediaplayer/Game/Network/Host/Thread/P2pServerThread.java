package hsrm.eibo.mediaplayer.Game.Network.Host.Thread;

import hsrm.eibo.mediaplayer.Game.Network.SocketManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class P2pServerThread extends Thread {

    private int serverPort;

    public P2pServerThread(int port) {
        super("Thread-Game-Thread-P2pServer");
        this.setDaemon(true);
        this.serverPort = port;
    }

    @Override
    public void run() {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(this.serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        do
        {
            try {
                byte[] content = new byte[SocketManager.PACKET_LENGTH];
                DatagramPacket packet = new DatagramPacket(content, SocketManager.PACKET_LENGTH);
                socket.receive(packet);
                System.out.println("~~ package received ~~");
                System.out.println(new String(packet.getData()));
            } catch (IOException e) {
                e.printStackTrace();
                this.interrupt();
                break;
            }
        } while(!super.isInterrupted());
        socket.close();
    }
}
