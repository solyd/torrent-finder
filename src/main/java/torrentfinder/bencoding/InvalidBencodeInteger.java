package torrentfinder.bencoding;

public class InvalidBencodeInteger extends InvalidBencodeObject {
	public InvalidBencodeInteger(String msg) {
		super(String.format("Invalid bencode integer: %s", msg), true);
	}
}
