package hsrm.eibo.mediaplayer.Game.Model;


import java.net.InetAddress;

public class BandMember {
    private String name;
    private int instrument;
    private InetAddress clientAddress;

    public BandMember(String name, int instrument, InetAddress clientAddress) {
        this.name = name;
        this.instrument = instrument;
        this.clientAddress = clientAddress;
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
