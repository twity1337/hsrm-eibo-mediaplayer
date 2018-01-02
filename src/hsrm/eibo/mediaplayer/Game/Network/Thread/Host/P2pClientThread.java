package hsrm.eibo.mediaplayer.Game.Network.Thread.Host;

import hsrm.eibo.mediaplayer.Game.Network.SocketManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class P2pClientThread extends Thread {

    private int serverPort;
    private InetAddress serverAddress;

    public P2pClientThread(InetAddress address, int port) {
        super("Thread-Game-Host-P2pClient");
        serverPort = port;
        serverAddress = address;
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
            try {
                byte[] content = new byte[SocketManager.PACKET_LENGTH];
                DatagramPacket packet = new DatagramPacket(content, SocketManager.PACKET_LENGTH);
                socket.send(packet);
                System.out.println("Test");
            } catch (IOException e) {
                e.printStackTrace();
                this.interrupt();
                break;
            }
        }
        socket.close();
    }
}
