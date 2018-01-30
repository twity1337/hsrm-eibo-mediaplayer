package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Synthesizer.SynthesizerManager;

public class StopNoteHandler implements NetworkEventHandlerInterface {
    @Override
    public void handleRequest(NetworkEventPacket packet) {
        SynthesizerManager.getInstance().stopNote(packet.getSenderName(), packet.getEventArgs()[0]);
    }
}
