package torrentfinder.utils;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class ByteBufferBuilder {
	protected int m_totalSize   	 = 0;
	protected List<byte[]> m_buffers = new LinkedList<byte[]>();
	
	public ByteBufferBuilder append(byte[] buffer) {
		if (buffer.length == 0)
			return this;
		
		m_totalSize += buffer.length;
		m_buffers.add(buffer);
		return this;
	}
	
	public ByteBufferBuilder append(long num) {
		return append(ByteBuffer.allocate(TFUtils.SizeOf.Long).putLong(num).array());
	}
	
	public int totalSize() {
		return m_totalSize;
	}
	
	public ByteBuffer buildBuffer() {
		ByteBuffer result = ByteBuffer.allocate(m_totalSize);
		for (byte[] buffer : m_buffers) 
			result.put(buffer);
		return result;
	}
	
	public byte[] buildArray() {
		return buildBuffer().array();
	}
	
	public void reset() {
		m_buffers.clear();
		m_totalSize = 0;
	}
}
