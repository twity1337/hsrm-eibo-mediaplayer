package hsrm.eibo.mediaplayer.Game.Network.Host.Event;

import hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler.*;

public class NetworkEventDispatcher {

    public enum NetworkEventType {
        EVENT_HELLO(new NewClientHandler()),
        EVENT_GOODBYE(new DisconnectClientHandler()),
        EVENT_PING(new PingHandler()),
        EVENT_NOTE(new PlayNoteHandler());
        NetworkEventHandlerInterface eventHandler;

        NetworkEventType(NetworkEventHandlerInterface eventHandler)
        {
            this.eventHandler = eventHandler;
        }
    };


    public byte[] dispatch(NetworkEventType type, byte[] args) {
        return type.eventHandler.handleRequest(args);
    }

}
