package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;

public class DisconnectClientHandler implements NetworkEventHandlerInterface {


    @Override
    public void handleRequest(NetworkEventPacket packet) {
        SocketHostManager.getInstance().getConnectedClientList().remove(
                new BandMember(packet.getSenderName(), packet.getRemoteIpAdress())
        );
        System.out.println("Player " + packet.getSenderName() + " disconnected.");
        System.out.println("Connected clients are:");
        System.out.println(SocketHostManager.getInstance().getConnectedClientList().toString());
    }
}
