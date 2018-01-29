package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.SynthesizerManager;

public class NewClientHandler implements NetworkEventHandlerInterface {


    @Override
    public NetworkEventPacket handleRequest(NetworkEventPacket packet) {

        SocketHostManager.getInstance().addConnectedClient(packet.getRemoteIpAdress());
        System.out.println("Hello " + packet.getSenderName());
        SynthesizerManager.getInstance().occupyChannel(packet.getSenderName(), packet.getEventArgs()[0]);
        return new NetworkEventPacket("Server", NetworkEventDispatcher.NetworkEventType.EVENT_SERVER_HELLO, packet.getEventArgs());
}
}
