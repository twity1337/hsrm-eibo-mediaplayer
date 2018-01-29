package hsrm.eibo.mediaplayer.Game.Network.Client;

import hsrm.eibo.mediaplayer.Game.Controller.GameController;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import hsrm.eibo.mediaplayer.Game.Network.Client.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;
import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import org.apache.cxf.jaxrs.ext.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketClientManager extends AbstractSocketManager {

    private P2pClientThread clientThread;
    private Thread serverThread;
    private InetAddress localhost, serverAddress;

    private static SocketClientManager instance = null;

    public static SocketClientManager getInstance() {
        return getInstance(null);
    }
    public static SocketClientManager getInstance(@Nullable InetAddress serverAddress) {
        if(instance == null && serverAddress != null)
            instance = new SocketClientManager(serverAddress);
        return instance;
    }

    private SocketClientManager(InetAddress serverAddress) {
        try {
            this.serverAddress = serverAddress;
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void startClient() {
        clientThread = new P2pClientThread(this.serverAddress, APPLICATION_PORT);
        clientThread.start();

        // initiate connection start..
        GameSettings gSettings = GameController.getGameSettings();
        NetworkEventPacket helloPacket = new NetworkEventPacket(
                getCurrentSenderId(),
                NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_HELLO,
                new String[]{
                        gSettings.getPlayerName(),
                        Integer.toString(gSettings.getInstrumentId())
                });
        P2pClientThread.pushToProcessingQueue(helloPacket);
    }

    public byte getCurrentSenderId() {
        return 1;
    }

    public void close() {
        clientThread.interrupt();
    }
}