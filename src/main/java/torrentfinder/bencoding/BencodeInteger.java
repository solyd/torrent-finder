package torrentfinder.bencoding;

import java.nio.ByteBuffer;

import torrentfinder.utils.ByteBufferBuilder;
import torrentfinder.utils.TFUtils;

public class BencodeInteger implements BencodeObject {
	public static final char	 	Prefix = 'i';
	public static final char		Suffix = 'e';
	protected static final byte[] 	PrefixBytes = new byte[] { Prefix };
	protected static final byte[] 	SuffixBytes = new byte[] { Suffix };
	
	protected long m_value;
	
	BencodeInteger() {
		m_value = 0;
	}
	
	BencodeInteger(long value) {
		m_value = value;
	}

	public byte[] encode() {
		return new ByteBufferBuilder()
			.append(PrefixBytes)
			.append(m_value)
			.append(SuffixBytes)
			.buildArray();
	}

	public void decode(ByteBuffer data) throws InvalidBencodeInteger {
		char prefix = (char) data.get(); 
		if (prefix != Prefix)
			throw new InvalidBencodeInteger(String.format("Prefix is %c, expected %c", prefix, Prefix));
		
		m_value = TFUtils.readASCIINumber(data);
		
		char suffix = (char) data.get(); 
		if (suffix != Suffix)
			throw new InvalidBencodeInteger(String.format("Suffix is %c, expected %c", suffix, Suffix));
	}
	
	public long value() {
		return m_value;
	}
	
	@Override
	public String toString() {
		return Long.toString(m_value);
	}
}
