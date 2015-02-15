package torrentfinder.utils;

import java.nio.ByteBuffer;
import java.util.Random;

public class TFUtils {
	public static class SizeOf {
		public static final int Long = 8;
	}
	
	public static byte[] randomBytes(int length) {
		byte[] res = new byte[length];
		new Random().nextBytes(res);
		return res;
	}
	
	public static byte[] readBytesFrom(ByteBuffer buffer, int count) {
		if (count <= 0 || buffer.remaining() < count)
			return null;
		
		byte[] res = new byte[count];
		buffer.get(res);
		return res;
	}
	
	public static int readTwoBytesAsInt(ByteBuffer buffer) {
		return buffer.getChar();
	}
	
	public static boolean isASCIINumber(byte ascii) {
		return (int) ascii >= (int) '0' &&
			   (int) ascii <= (int) '9';
	}
	
	public static int fromASCII(byte ascii) {
		return ascii - '0';
	}
	
	public static String readASCIIString(ByteBuffer buff, int length) {
		if (buff.remaining() < length)
			return null;
		
		if (length == 0)
			return "";
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; ++i)
			result.append((char) buff.get());
		
		return result.toString();
	}
	
	public static long readASCIINumber(ByteBuffer buff) {
		if (! buff.hasRemaining()) 
			return 0;
		
		int result = 0;
		byte b = buff.get();
		for (; isASCIINumber(b); b = buff.get())
			result = result * 10 + TFUtils.fromASCII(b);
		
		if (! isASCIINumber(b))
			buff.position(buff.position() - 1);
		
		return result;
	}
	
	public static int advanceUntil(ByteBuffer buff, byte stopper) {
		int startingPosition = buff.position();
		while (buff.get() != stopper);
		return buff.position() - startingPosition;
	}
	
	public static int numBytesUntil(ByteBuffer buff, byte stopper) {
		buff.mark();
		int result = advanceUntil(buff, stopper);
		buff.reset();
		return result;
	}
}
