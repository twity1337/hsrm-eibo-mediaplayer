package hsrm.eibo.mediaplayer.Game.Model;


import javafx.scene.paint.Color;

import java.net.InetAddress;

/**
 * class to represent active players and store information
 */
public class BandMember {
    private short index;
    private String name;
    private int instrument;
    private InetAddress clientAddress;
    private static short playerCount = 1;

    public BandMember(String name, int instrument, InetAddress clientAddress) {
        this.name = name;
        this.instrument = instrument;
        this.clientAddress = clientAddress;
        this.index = playerCount;
        playerCount++;
    }
    public BandMember(String name, InetAddress clientAddress) {
        this(name, 0, clientAddress);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInstrument() {
        return instrument;
    }

    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Color getPlayerColor()
    {
        float PHI = (1 + (float) Math.sqrt(5))/2.0f;
        float n = index * PHI - (float) Math.floor(index * PHI);
        return Color.hsb(360/n, (n/2.0), 0.5);
    }


    @Override
    public int hashCode() {
        return (this.name + this.clientAddress.toString()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BandMember))
            return false;
        return ((BandMember) obj).name.equals(this.name) && ((BandMember) obj).clientAddress.equals(this.clientAddress);
    }

    @Override
    public String toString() {
        return this.name + " - " + this.instrument + " @ " + this.clientAddress.toString();
    }
}
