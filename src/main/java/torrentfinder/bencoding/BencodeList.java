package torrentfinder.bencoding;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import torrentfinder.utils.ByteBufferBuilder;

public class BencodeList implements BencodeObject, Iterable<BencodeObject>
{
	public static final char 		Prefix = 'l';
	public static final char		Suffix = 'e';
	protected static final byte[] 	PrefixBytes = new byte[] { Prefix };
	protected static final byte[] 	SuffixBytes = new byte[] { Suffix };
	
	protected List<BencodeObject> m_values = new LinkedList<BencodeObject>();

	public byte[] encode() {
		ByteBufferBuilder result = new ByteBufferBuilder().append(PrefixBytes);
		for (BencodeObject obj : m_values)
			result.append(obj.encode());
		
		return result.append(SuffixBytes).buildArray();
	}

	public void decode(ByteBuffer data) throws InvalidBencodeObject {
		char prefix = (char) data.get(); 
		if (prefix != Prefix)
			throw new InvalidBencodeList(String.format("Prefix is %c, expected %c", prefix, Prefix));
		
		while (data.get(data.position()) != Suffix)
			m_values.add(BencodeObjectBuilder.buildFrom(data));
		
		char suffix = (char) data.get();
		if (suffix != Suffix)
			throw new InvalidBencodeList(String.format("Suffix is %c, expected %c", suffix, Suffix));			
	}
	
	public BencodeList add(BencodeObject item) {
		m_values.add(item);
		return this;
	}
	
	public BencodeList add(long num) {
		return add(new BencodeInteger(num));
	}
	
	public BencodeList add(String str) {
		return add(new BencodeString(str));
	}
	
	public List<BencodeObject> values() {
		return m_values;
	}
	
	@Override
	public String toString() {
		if (m_values.size() == 0)
			return "[]";
		if (m_values.size() == 1)
			return String.format("[%s]", m_values.get(0).toString());
		
		StringBuilder result = new StringBuilder();
		result.append("[");
		for (BencodeObject item : m_values) { 
			result.append(item.toString());
			result.append(", ");
		}
		
		result.delete(result.length() - 2, result.length());
		result.append("]");
		return result.toString();
	}

	public Iterator<BencodeObject> iterator() {
		return m_values.iterator();
	}
	
	public int size() {
		return m_values.size();
	}
	
	public BencodeObject get(int index) {
		if (m_values.size() <= index)
			return null;
		
		return m_values.get(index);
	}
}
