package torrentfinder.dht;

public class Infohash extends DHTId {
	public Infohash(byte[] infohash) throws InvalidDHTId {
		super(infohash);
	}
}
