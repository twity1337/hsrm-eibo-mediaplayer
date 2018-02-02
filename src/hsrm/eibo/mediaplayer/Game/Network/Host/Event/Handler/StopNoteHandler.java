package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.General.ObservableConnectedClientList;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.KeyboardChangeEvent;
import hsrm.eibo.mediaplayer.Game.Synthesizer.SynthesizerManager;

public class StopNoteHandler implements NetworkEventHandlerInterface {
    @Override
    public void handleRequest(NetworkEventPacket packet) {
        int stopNote = packet.getEventArgs()[0];
        SynthesizerManager.getInstance().stopNote(packet.getSenderName(), packet.getEventArgs()[0]);

        KeyboardChangeEvent.getInstance().change(KeyboardChangeEvent.EventCode.KEY_RELEASED, stopNote, null);
    }
}
