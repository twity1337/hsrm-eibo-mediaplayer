package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;

public interface NetworkEventHandlerInterface {
    NetworkEventPacket handleRequest(NetworkEventPacket packet);
}
