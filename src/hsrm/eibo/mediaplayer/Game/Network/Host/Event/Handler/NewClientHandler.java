package hsrm.eibo.mediaplayer.Game.Network.Host.Event.Handler;

public class NewClientHandler implements NetworkEventHandlerInterface {


    @Override
    public byte[] handleRequest(Object args) {

        System.out.println("Hello " + args.toString());
        return ("new_player:"+args.toString()+";").getBytes();
    }
}
