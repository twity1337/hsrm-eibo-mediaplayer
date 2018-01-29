package hsrm.eibo.mediaplayer.Game.Network.Client.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler.NetworkEventHandlerInterface;

public class NewClientConnectedHandler implements NetworkEventHandlerInterface {


    @Override
    public NetworkEventPacket handleRequest(NetworkEventPacket packet) {

        System.out.println(packet.getEventArgs()[0] + " joined the game.");
        return null;
    }
}
