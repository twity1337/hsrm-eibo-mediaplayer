package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;

public class NewClientHandler implements NetworkEventHandlerInterface {


    @Override
    public NetworkEventPacket handleRequest(NetworkEventPacket packet) {

        SocketHostManager.getInstance().addConnectedClient(packet.getRemoteIpAdress());
        System.out.println("Hello " + packet.getEventArgs()[0] + " / " +packet.getEventArgs()[1]);
        return new NetworkEventPacket((byte) 0, NetworkEventDispatcher.NetworkEventType.EVENT_SERVER_HELLO, packet.getEventArgs());
    }
}
