package hsrm.eibo.mediaplayer.Game.Network.General;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import org.apache.commons.lang.ArrayUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;

public class ObservableConnectedClientList<T extends BandMember> extends Observable {

    /**
     * An array list of connected clients for broadcast messages.
     */
    private ArrayList<T> connectedClients = new ArrayList<>();

    /**
     * All connected client InetAddress-objects
     */
    private InetAddress[] connectedClientAddresses = null;


    /**
     * Adds a new connected client.
     *
     * @param client The client to add.
     */
    public void add(T client) {
        this.connectedClientAddresses = null; // invalidate cached connectedClients on change
        this.connectedClients.add(client);
        this.notifyOnChange();
    }

    /**
     * Removes / Disconnects a connected client.
     * The given client will no longer get network messages.
     *
     * @param client The client to remove.
     */
    public void remove(T client) {
        this.connectedClientAddresses = null; // invalidate cached connectedClients on change
        this.connectedClients.remove(client);
        this.notifyOnChange();
    }

    public T get(T e){
        int index = connectedClients.indexOf(e);
        if(index == ArrayUtils.INDEX_NOT_FOUND)
            return null;
        return connectedClients.get(index);
    }

    /**
     * Returns all connected client addresses as an array list.
     *
     * @return
     */
    public synchronized InetAddress[] getAll() {
        // check if cached connected clients are invalidated..
        if (this.connectedClientAddresses == null) {
            InetAddress[] addresses = new InetAddress[this.connectedClients.size()];
            for (int i = 0; i <= this.connectedClients.size(); i++) {
                addresses[i] = this.connectedClients.get(i).getClientAddress();
            }
            this.connectedClientAddresses = addresses;
        }
        return this.connectedClientAddresses;
    }

    private void notifyOnChange() {
        this.setChanged();
        this.notifyObservers(this.connectedClients);
    }
}
