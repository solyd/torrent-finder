package torrentfinder.bencoding;

import java.nio.ByteBuffer;

public interface BencodeObject {
	public byte[] encode();
	public void decode(ByteBuffer data) throws InvalidBencodeObject;
}
	