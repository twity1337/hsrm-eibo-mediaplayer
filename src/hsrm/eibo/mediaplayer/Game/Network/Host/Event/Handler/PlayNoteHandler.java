package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.General.ObservableConnectedClientList;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.KeyboardChangeEvent;
import hsrm.eibo.mediaplayer.Game.Synthesizer.SynthesizerManager;

public class PlayNoteHandler implements NetworkEventHandlerInterface {


    @Override
    public void handleRequest(NetworkEventPacket packet) {
        int playedNote = packet.getEventArgs()[0];
        SynthesizerManager.getInstance().playNote(packet.getSenderName(), playedNote);

        ObservableConnectedClientList<BandMember> band = SocketHostManager.getInstance().getConnectedClientList();
        BandMember comparable = new BandMember(packet.getSenderName(), packet.getRemoteIpAdress());

        KeyboardChangeEvent.getInstance().change(KeyboardChangeEvent.EventCode.KEY_PRESSED, playedNote,band.get(comparable).getPlayerColor());

    }
}
