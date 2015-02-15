package torrentfinder.bencoding;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import torrentfinder.utils.ByteBufferBuilder;

public class BencodeDictionary implements BencodeObject {
	public static final char 		Prefix = 'd';
	public static final char		Suffix = 'e';
	protected static final byte[] 	PrefixBytes = new byte[] { Prefix };
	protected static final byte[] 	SuffixBytes = new byte[] { Suffix };
	
	protected Map<BencodeString, BencodeObject> m_dict = new HashMap<BencodeString, BencodeObject>();
	private List<Entry<BencodeString, BencodeObject>> m_sortedDictEntries;
	
	public BencodeDictionary() {
	}
	
	public BencodeDictionary(String key, BencodeObject value) {
		put(key, value);
	}
	
	public BencodeDictionary(String key, String value) {
		put(key, value);
	}
	
	public byte[] encode() {		
		ByteBufferBuilder result = new ByteBufferBuilder().append(PrefixBytes);
		for (Entry<BencodeString, BencodeObject> item : sortedDictEntries())
			result.append(item.getKey().encode()).append(item.getValue().encode()); 
		
		return result.append(SuffixBytes).buildArray();
	}

	public void decode(ByteBuffer data) throws InvalidBencodeObject {
		char prefix = (char) data.get(); 
		if (prefix != Prefix)
			throw new InvalidBencodeDictionary(String.format("Prefix is %c, expected %c", prefix, Prefix));
		
		while ((char) data.get(data.position()) != Suffix) {
			BencodeObject key = BencodeObjectBuilder.buildFrom(data);
			if (! (key instanceof BencodeString))
				throw new InvalidBencodeDictionary("key value is not a bencoded string");
			
			m_dict.put((BencodeString) key, BencodeObjectBuilder.buildFrom(data));
		}
				
		char suffix = (char) data.get(); 
		if (suffix != Suffix)
			throw new InvalidBencodeDictionary(String.format("Suffix is %c, expected %c", suffix, Suffix));
	}
	
	public BencodeDictionary put(BencodeString key, BencodeObject value) {
		m_dict.put(key, value);
		return this;
	}
	
	public BencodeDictionary put(String key, BencodeObject value) {
		return put(new BencodeString(key), value);
	}
	
	public BencodeDictionary put(String key, byte[] value) {
		return put(new BencodeString(key), new BencodeString(value));
	}
	
	public BencodeDictionary put(String key, String value) {
		return put(new BencodeString(key), new BencodeString(value));
	}

	public BencodeDictionary put(String key, int value) {
		return put(new BencodeString(key), new BencodeInteger(value));
	} 
	
	public BencodeObject get(BencodeString key) {
		return m_dict.get(key);
	}
	
	public BencodeObject get(String key) {
		return get(new BencodeString(key));
	}
	
	public BencodeList getListValue(String key) {
		BencodeObject res = get(key);
		if (res == null || (! (res instanceof BencodeList)))
			return null;
		
		return (BencodeList) res; 
	}
	
	public BencodeString getStringValue(String key) {
		BencodeObject res = get(key);
		if (res == null || (! (res instanceof BencodeString)))
			return null;
		
		return (BencodeString) res;
	}
	
	public byte[] getBinaryReturnValue(String key) {
		BencodeObject res = get(key);
		if (res == null || (! (res instanceof BencodeString)))
			return null;

		return ((BencodeString) res).bytes();
	}
	
	public long getLongValue(String key) {
		BencodeObject res = get(key);
		if (res == null || (! (res instanceof BencodeInteger)))
			return 0;
		
		return ((BencodeInteger) res).value();
	}
	
	@Override
	public String toString() {
		if (m_dict.isEmpty())
			return "{}";
		
		StringBuilder result = new StringBuilder();
		result.append("{");
		for (Entry<BencodeString, BencodeObject> item : sortedDictEntries())
			result.append(String.format("%s: %s, ", item.getKey().toString(), item.getValue().toString()));
		result.delete(result.length() - 2, result.length());
		result.append("}");
		return result.toString();
	}
	
	protected List<Entry<BencodeString, BencodeObject>> sortedDictEntries() {
		if (m_sortedDictEntries == null) {
			m_sortedDictEntries = new ArrayList<Entry<BencodeString, BencodeObject>>(m_dict.entrySet());
			m_sortedDictEntries.sort(new DictEntryComparator());	
		}
		
		return m_sortedDictEntries;
	}
	
	public static class DictEntryComparator implements Comparator<Entry<BencodeString, BencodeObject>> {
		public int compare(Entry<BencodeString, BencodeObject> o1,
		                   Entry<BencodeString, BencodeObject> o2) 
		{
			ByteBuffer o1bytes = ByteBuffer.wrap(o1.getKey().bytes());
			ByteBuffer o2bytes = ByteBuffer.wrap(o2.getKey().bytes());
			return o1bytes.compareTo(o2bytes);
		}
		
	}
}
