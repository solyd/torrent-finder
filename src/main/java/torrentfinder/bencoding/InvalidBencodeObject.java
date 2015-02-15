package torrentfinder.bencoding;


public class InvalidBencodeObject extends Exception {
	public InvalidBencodeObject(String msg) {
		super(String.format("Invalid bencode object: %s", msg));
	}
	
	protected InvalidBencodeObject(String msg, boolean ignore) {
		super(msg);
	}
}
