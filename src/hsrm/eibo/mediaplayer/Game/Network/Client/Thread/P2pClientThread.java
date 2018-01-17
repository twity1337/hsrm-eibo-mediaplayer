package hsrm.eibo.mediaplayer.Game.Network.Client.Thread;

import hsrm.eibo.mediaplayer.Game.Network.General.AbstractClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class P2pClientThread extends AbstractClientThread {

    public P2pClientThread(InetAddress serverAddress, int port) {
        super("Thread-Game-Thread-P2pClient", port, serverAddress);
    }

    @Override
    public void run() {

        // TODO: machen... ;; Bisher nur zum Testen...

        DatagramSocket socket = this.openSocket();
        if(socket == null)
            return;

        while(!super.isInterrupted())
        {
            while(!this.hasDataToProcess()) {
                try {
                    byte[] data = this.pollDataToProcess();
                    DatagramPacket packet = new DatagramPacket(data, data.length, this.serverAddress, AbstractSocketManager.APPLICATION_PORT);
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
