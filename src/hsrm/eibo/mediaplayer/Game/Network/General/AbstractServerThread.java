package hsrm.eibo.mediaplayer.Game.Network.General;

import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public abstract class AbstractServerThread extends Thread {
    public static final int SO_TIMEOUT = 1000;
    protected int serverPort;

    public AbstractServerThread(String threadName, int port) {
        super(threadName);
        this.setDaemon(true);
        this.serverPort = port;
    }

    protected NetworkEventPacket receiveAndHandleNetworkPacket(DatagramSocket socket) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[AbstractSocketManager.PACKET_LENGTH], AbstractSocketManager.PACKET_LENGTH);
        socket.receive(datagramPacket);
        System.out.println("~~ [" + this.getClass().getName() + "] package received from " + datagramPacket.getAddress().toString());
        Object deserializedPacket = SerializationUtils.deserialize(datagramPacket.getData());
        if (deserializedPacket instanceof NetworkEventPacket){
            ((NetworkEventPacket) deserializedPacket).setRemoteIpAdress(datagramPacket.getAddress());
            return NetworkEventDispatcher.dispatch((NetworkEventPacket) deserializedPacket);
        }
        return null;
    }
}
