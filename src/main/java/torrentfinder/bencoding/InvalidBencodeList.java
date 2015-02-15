package torrentfinder.bencoding;

public class InvalidBencodeList extends InvalidBencodeObject {
	public InvalidBencodeList(String msg) {
		super(String.format("Invalid bencode list: %s", msg), true);
	}
}
