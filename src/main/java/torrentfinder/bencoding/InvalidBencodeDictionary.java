package torrentfinder.bencoding;

public class InvalidBencodeDictionary extends InvalidBencodeObject {
	public InvalidBencodeDictionary(String msg) {
		super(String.format("Invalid bencode dictionary: %s", msg), true);
	}
}
