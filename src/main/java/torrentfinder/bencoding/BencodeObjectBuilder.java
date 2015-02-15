package torrentfinder.bencoding;

import java.nio.ByteBuffer;

public class BencodeObjectBuilder {
	public static BencodeObject buildFrom(byte[] data) throws InvalidBencodeObject {
		return buildFrom(ByteBuffer.wrap(data));
	}
	
	public static BencodeObject buildFrom(ByteBuffer data) throws InvalidBencodeObject {
		BencodeObject result;
		switch((char) data.get(data.position())) {
			case BencodeInteger.Prefix:    	
				result = new BencodeInteger();
				break;
			case BencodeList.Prefix:
				result = new BencodeList();
				break;
			case BencodeDictionary.Prefix: 	
				result = new BencodeDictionary();
				break;
			default:			
				result = new BencodeString();
				break;
		}
		
		result.decode(data);
		return result;
	}
}
