package hsrm.eibo.mediaplayer.Game.Network.Host.Thread;

import hsrm.eibo.mediaplayer.Game.Network.General.AbstractServerThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class P2pServerThread extends AbstractServerThread {

    public P2pServerThread(int port) {
        super("Thread-Game-Thread-P2pServer", port);
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
        System.out.println("Server successfully started on port " + this.serverPort + " ...");

        do
        {
            try {
                byte[] content = new byte[AbstractSocketManager.PACKET_LENGTH];
                DatagramPacket packet = new DatagramPacket(content, AbstractSocketManager.PACKET_LENGTH);
                socket.receive(packet);
                System.out.println("~~ package received from " + packet.getAddress().toString() + ": " + new String(packet.getData()));
            } catch (IOException e) {
                e.printStackTrace();
                this.interrupt();
                break;
            }
        } while(!super.isInterrupted());
        socket.close();
    }
}
