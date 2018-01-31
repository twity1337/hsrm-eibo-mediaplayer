package hsrm.eibo.mediaplayer.Game.Network.Host.Thread;

import hsrm.eibo.mediaplayer.Game.Network.General.AbstractClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class P2pClientThread extends AbstractClientThread {

    public P2pClientThread(InetAddress address, int port) {
        super("Thread-Game-Thread-P2pClient", port, address);
    }

    @Override
    public void run() {
        DatagramSocket socket = null;


        while(!super.isInterrupted())
        {
            while(!this.hasDataToProcess()) {
                try {
                    InetAddress[] connectedClients = SocketHostManager
                            .getInstance()
                            .getConnectedClientAddresses();
                    for (InetAddress clientAddress : connectedClients) {
                        DatagramPacket packet = new DatagramPacket(this.pollDataToProcess(), AbstractSocketManager.PACKET_LENGTH, clientAddress, AbstractSocketManager.APPLICATION_PORT);
                        socket.send(packet);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.interrupt();
                    break;
                }
            }

            try {
                Thread.sleep(IDLE_SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                this.interrupt();
                break;
            }

        }
        socket.close();
    }
}
