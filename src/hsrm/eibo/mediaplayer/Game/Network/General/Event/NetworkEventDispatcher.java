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
        EVENT_CLIENT_PLAY_NOTE(new PlayNoteHandler()),
        EVENT_CLIENT_STOP_NOTE(new StopNoteHandler());
        NetworkEventHandlerInterface eventHandler;

        NetworkEventType(NetworkEventHandlerInterface eventHandler)
        {
            this.eventHandler = eventHandler;
        }
    };

    /**
     * Dispatches an incoming network request and sends returns it network answer as NetworkEventPacket
     * @param packet the packet to dispatch
     */
    public static void dispatch(NetworkEventPacket packet) {
        packet.getEventType().eventHandler.handleRequest(packet);
    }

}
