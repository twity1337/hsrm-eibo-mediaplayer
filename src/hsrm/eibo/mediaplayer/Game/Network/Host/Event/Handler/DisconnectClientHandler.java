package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;

public class DisconnectClientHandler implements NetworkEventHandlerInterface {


    @Override
    public void handleRequest(NetworkEventPacket packet) {
        SocketHostManager.getInstance().removeConnectedClient(packet.getRemoteIpAdress());
        System.out.println("Player disconnected.");
    }
}
