package hsrm.eibo.mediaplayer.Game.Network.Client.Thread;

import hsrm.eibo.mediaplayer.Game.Controller.GameManager;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;
import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import org.apache.commons.lang.SerializationUtils;

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
        byte[] data = SerializationUtils.serialize(new NetworkEventPacket(
                GameManager.getGameSettings().getPlayerName(),
                NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_GOODBYE
        ));
        DatagramPacket packet = new DatagramPacket(data, data.length, this.serverAddress, AbstractSocketManager.APPLICATION_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}
