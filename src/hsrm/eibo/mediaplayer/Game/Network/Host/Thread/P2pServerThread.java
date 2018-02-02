package hsrm.eibo.mediaplayer.Game.Network.Host.Thread;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractServerThread;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class P2pServerThread extends AbstractServerThread {

    public P2pServerThread(int port) {
        super("Thread-Game-Thread-P2pServer", port);
    }

    @Override
    public void run() {
        DatagramSocket socket;
        try {
            // open socket on the default server port...
            socket = new DatagramSocket(this.serverPort);
            socket.setSoTimeout(SO_TIMEOUT);
        } catch (SocketException e) {
            ErrorHandler.getInstance().addError(e);
            return;
        }
        System.out.println("Server successfully started on port " + this.serverPort + " ...");

        do
        {
            try {
                    this.receiveAndHandleNetworkPacket(socket);
            } catch (SocketTimeoutException e)
            {
                // do nothing on SocketTimeout - just don't get stuck in loop...
            } catch (IOException e) {
                e.printStackTrace();
                this.interrupt();
                break;
            }
        } while(!super.isInterrupted());
        socket.close();
        System.out.println("Server socket closed");
    }
}
