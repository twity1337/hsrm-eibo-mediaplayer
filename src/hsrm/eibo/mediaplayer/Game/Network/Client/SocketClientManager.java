package hsrm.eibo.mediaplayer.Game.Network.Client;

import hsrm.eibo.mediaplayer.Game.Controller.GameManager;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import hsrm.eibo.mediaplayer.Game.Network.Client.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;
import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import org.apache.cxf.jaxrs.ext.Nullable;

import java.net.InetAddress;

/**
 * Manager class for Socket Api on Game clients.
 */
public class SocketClientManager extends AbstractSocketManager {

    private P2pClientThread clientThread;
    private InetAddress serverAddress;

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
        this.serverAddress = serverAddress;
    }

    /**
     * Starts the client thread and automatically sends a "hello"-event packet to the server.
     */
    public void startClient() {
        clientThread = new P2pClientThread(this.serverAddress, APPLICATION_PORT);
        clientThread.start();

        // initiate connection start..
        GameSettings gSettings = GameManager.getGameSettings();
        NetworkEventPacket helloPacket = new NetworkEventPacket(
                GameManager.getGameSettings().getPlayerName(),
                NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_HELLO,
                new int[]{gSettings.getInstrumentId()}
            );
        clientThread.pushToProcessingQueue(helloPacket);
    }

    public P2pClientThread getClientThread() {
        return this.clientThread;
    }

    public void close() {
        clientThread.interrupt();
    }
}