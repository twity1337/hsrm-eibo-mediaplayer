package hsrm.eibo.mediaplayer.Game.Network.Host.Thread;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractServerThread;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;

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
                    NetworkEventPacket response = this.receiveAndHandleNetworkPacket(socket);
                    if(response != null)
                        P2pClientThread.pushToProcessingQueue(response);
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
