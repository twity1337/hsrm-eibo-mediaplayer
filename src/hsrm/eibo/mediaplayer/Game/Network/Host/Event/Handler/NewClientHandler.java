package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.SynthesizerManager;

public class NewClientHandler implements NetworkEventHandlerInterface {


    @Override
    public void handleRequest(NetworkEventPacket packet) {

        int[] eventArgs = packet.getEventArgs();
        SocketHostManager.getInstance().addConnectedClient(new BandMember(packet.getSenderName(), eventArgs[0], packet.getRemoteIpAdress()));
        System.out.println(packet.getSenderName() + " connected");
        SynthesizerManager.getInstance().occupyChannel(packet.getSenderName(), eventArgs[0]);

        System.out.println("Connected clients are:");
        System.out.println(SocketHostManager.getInstance().getConnectedClients().toString());
    }
}
