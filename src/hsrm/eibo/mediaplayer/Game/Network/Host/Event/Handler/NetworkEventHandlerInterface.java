package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;

public interface NetworkEventHandlerInterface {
    void handleRequest(NetworkEventPacket packet);
}
