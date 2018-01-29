package hsrm.eibo.mediaplayer.Game.Network.General.Event;

import hsrm.eibo.mediaplayer.Game.Network.Client.Event.Handler.NewClientConnectedHandler;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler.*;

public class NetworkEventDispatcher  {

    public enum NetworkEventType {
        EVENT_SERVER_HELLO(new NewClientConnectedHandler()),


        EVENT_CLIENT_HELLO(new NewClientHandler()),
        EVENT_CLIENT_GOODBYE(new DisconnectClientHandler()),
        EVENT_CLIENT_PING(new PingHandler()),
        EVENT_CLIENT_NOTE(new PlayNoteHandler());
        NetworkEventHandlerInterface eventHandler;

        NetworkEventType(NetworkEventHandlerInterface eventHandler)
        {
            this.eventHandler = eventHandler;
        }
    };

    /**
     * Dispatches an incoming network request and sends returns it network answer as NetworkEventPacket
     * @param packet the packet to dispatch
     * @return the resonse of the incoming request.
     */
    public static NetworkEventPacket dispatch(NetworkEventPacket packet) {
        return packet.getEventType().eventHandler.handleRequest(packet);
    }

}
