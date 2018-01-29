package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Synthesizer.SynthesizerManager;

public class PlayNoteHandler implements NetworkEventHandlerInterface {


    @Override
    public NetworkEventPacket handleRequest(NetworkEventPacket packet) {

        SynthesizerManager.getInstance().playNote(packet.getSenderName(), packet.getEventArgs()[0]);
        return null;
    }
}
