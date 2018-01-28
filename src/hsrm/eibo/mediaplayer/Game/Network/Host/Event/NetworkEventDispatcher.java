package hsrm.eibo.mediaplayer.Game.Network.Host.Event;

import hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler.*;

public class NetworkEventDispatcher {

    public enum NetworkEventType {
        EVENT_HELLO(0x0, new NewClientHandler()),
        EVENT_GOODBYE(0x1, new DisconnectClientHandler()),
        EVENT_PING(0x2, new PingHandler()),
        EVENT_NOTE(0x3, new PlayNoteHandler());
        byte id;
        NetworkEventHandlerInterface eventHandler;

        NetworkEventType(int id, NetworkEventHandlerInterface eventHandler)
        {
            this.id = (byte) id;
            this.eventHandler = eventHandler;
        }
    };


    public static byte[] dispatch(NetworkEventType type, byte[] data) {
        return type.eventHandler.handleRequest(data);
    }

}
