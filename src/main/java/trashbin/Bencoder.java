package trashbin;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import torrentfinder.utils.TFUtils;

public class Bencoder {
	public static enum ValueType {
		String,
		Integer,
		List,
		Dictionary
	}
	
	public static String decodeAsString(ByteBuffer data) {
		return TFUtils.readASCIIString(data, (int) TFUtils.readASCIINumber(data));
	}
	
	public static long decodeAsInteger(ByteBuffer data) {
		data.get(); // skip the prefix
		long result = TFUtils.readASCIINumber(data);
		data.get(); // skip the suffix
		return result;
	}
	
	public static List<ByteBuffer> decodeAsList(ByteBuffer data) {
		data.get(); // skip the prefix
		return null;
	}
	
	public static Map<String, ByteBuffer> decodeAsDictionary(byte[] data) {
		return null;
	}
	
	
	
	/***
	 * @param data bencoded data
	 * @return true if data is correctly formed, false otherwise
	 */
	public static boolean validate(byte[] data) {
		// TODO
		return true;
	}
	
	public static ValueType typeOfBencodedItem(ByteBuffer data) {
		switch((char) data.get(data.position())) {
			case ValuePrefix.Integer:    return ValueType.Integer;
			case ValuePrefix.List:  	 return ValueType.List;
			case ValuePrefix.Dictionary: return ValueType.Dictionary;
			default:					 return ValueType.String;
		}		
	}
	
	private static long lengthOfBencodedItem(ByteBuffer data) {
//		switch (typeOfBencodedItem(data)) {
//			case String: 
//				return lengthOfStringItem(data);
//			case Integer:
//				return TFUtils.numBytesUntil(data, (byte) ValueSuffix.Integer); 
//				
//			case List:
//			case Dictionary:
//				data.get(); // skip prefix
//				if (isSuffixByte(data.get(data.position())))
//					return 2;
//				
//				return 1 + 
//				
//				int origDataPosition = data.position();
//				long prefixLength = 1;
//				data.get(); // skip prefix
//				
//				int	prefixCount = 1;
//				byte b;
//				while (prefixCount != 0) {
//					b = data.get();
//					if ((char) )
//				}
//				// advance, if prefix count++, if suffix, prefix count--, if count == 0 we reached end
//		}
		// if string then count the length of number, :, + (length of number)
		// else its prefix (1) + ... until we encouter the matching suffix
		return 0;
	}
	
	private static long lengthOfStringItem(ByteBuffer data) {
		int origDataPosition = data.position();
		long stringLength = TFUtils.readASCIINumber(data);
		long suffixLength = 1;
		long asciiEncodedStringLength = data.position() - origDataPosition;
		data.position(origDataPosition);
		
		return stringLength + suffixLength + asciiEncodedStringLength; 
	}
	
	private static boolean isPrefixByte(byte b) {
		return (char) b == ValuePrefix.Integer ||
			   (char) b == ValuePrefix.List ||  
			   (char) b == ValuePrefix.Dictionary;
	}
	
	private static boolean isSuffixByte(byte b) {
		return (char) b == ValueSuffix.Integer ||
		       (char) b == ValueSuffix.List ||  
		       (char) b == ValueSuffix.Dictionary;
	}
	
	private static final char ValueTypeSeperator = ':';
	
	private static class ValuePrefix {
		public static final char Integer	= 'i';
		public static final char List   	= 'l';
		public static final char Dictionary = 'd';
	}

	private static class ValueSuffix {
		public static final char Integer	= 'e';
		public static final char List   	= 'e';
		public static final char Dictionary = 'e';
	}
}
