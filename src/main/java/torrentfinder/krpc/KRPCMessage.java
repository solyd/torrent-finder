package torrentfinder.krpc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import torrentfinder.bencoding.BencodeDictionary;
import torrentfinder.bencoding.BencodeObject;
import torrentfinder.bencoding.BencodeObjectBuilder;
import torrentfinder.bencoding.BencodeString;
import torrentfinder.bencoding.InvalidBencodeObject;
import torrentfinder.utils.Hexdump;

public abstract class KRPCMessage {
	static Logger LOG = LogManager.getLogger(KRPCMessage.class.getName());
	
	protected static final String TransactionIdKey = "t";
	protected static final String MessageTypeKey   = "y";
	
	private BencodeDictionary m_message;
	private BencodeString	  m_transacionId;
	private KRPCMessageType   m_messageType;
	
	public static KRPCMessage buildFrom(ByteBuffer data) throws InvalidKRPCMessage {
		BencodeObject obj;
		try {
			obj = BencodeObjectBuilder.buildFrom(data);
		}
		catch (InvalidBencodeObject e) {
			throw new InvalidKRPCMessage(String.format("invalid bencode object: %s", e.getMessage()));
		}		
		
		if (! (obj instanceof BencodeDictionary))
			throw new InvalidKRPCMessage(String.format("bencoded object is not dictionary"));
		
		return buildFrom((BencodeDictionary) obj);		
	}
	
	public static KRPCMessage buildFrom(BencodeDictionary msg) throws InvalidKRPCMessage {
		BencodeString msgTypeStr = msg.getStringValue(KRPCMessage.MessageTypeKey);
		if (msgTypeStr == null)
			throw new InvalidKRPCMessage("message type string is not present");

		KRPCMessageType msgType = KRPCMessageType.fromKey(msgTypeStr.string());
		if (msgType == null)
			throw new InvalidKRPCMessage(String.format("invalid message type string: %s", msgTypeStr));

		switch (msgType) {
			case Error:    return new KRPCError(msg);				
			case Query:    return new KRPCQuery(msg);
			case Response: return new KRPCResponse(msg);
		}

		throw new InvalidKRPCMessage("unhandled message type");
	}
	
	public static KRPCMessage recieveFrom(DatagramSocket socket) throws IOException, InvalidKRPCMessage {
		// TODO shouldn't create this buffer each time we are called
		byte[] recievedBytes = new byte[1024];
		DatagramPacket recievedPacket = new DatagramPacket(recievedBytes, recievedBytes.length);
		socket.receive(recievedPacket);
		LOG.debug("recieved from {}:{}", socket.getInetAddress(), Hexdump.dumpHexString(recievedPacket.getData(), 0, recievedPacket.getLength()));
		return buildFrom(ByteBuffer.wrap(recievedPacket.getData(), 0, recievedPacket.getLength()));
	}
	
	protected KRPCMessage(KRPCMessage other) throws InvalidKRPCMessage {
		if (other == null)
			throw new InvalidKRPCMessage("Cannot initialize from null KRPCMessage");
		
		m_message      = other.m_message;
		m_messageType  = other.m_messageType;
		m_transacionId = other.m_transacionId;
	}
		
	protected KRPCMessage(BencodeDictionary msg) throws InvalidKRPCMessage {
		if (msg == null) 
			throw new InvalidKRPCMessage("bencoded dictionary is null");
		
		m_transacionId = msg.getStringValue(KRPCMessage.TransactionIdKey); 
		if (m_transacionId == null)
			throw new InvalidKRPCMessage("null transaction id");
		
		BencodeString msgTypeStr = msg.getStringValue(MessageTypeKey);
		if (msgTypeStr == null)
			throw new InvalidKRPCMessage("no message type key");
		
		m_messageType = KRPCMessageType.fromKey(msgTypeStr.string());
		if (m_messageType == null)
			throw new InvalidKRPCMessage(String.format("invalid message type: %s", msgTypeStr.toString()));
		
		m_message = msg;
	}
	
	protected KRPCMessage(KRPCMessageType type, String transactionId) {
		m_messageType = type;
		m_transacionId = new BencodeString(transactionId);
		m_message = new BencodeDictionary();
		m_message.put(TransactionIdKey, transactionId);
		m_message.put(MessageTypeKey, type.key());
	}
	
	public final DatagramSocket sendTo(InetSocketAddress addr) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		sendTo(socket, addr);
		return socket;
	}
	
	public final void sendTo(DatagramSocket socket, InetSocketAddress addr) throws IOException {
		if (addr == null)
			return;

		// TODO in what cases does this return null
		int port = addr.getPort();
		InetAddress ip = addr.getAddress();
		if (ip == null)
			return;
		
		byte[] msgBytes = encode();
		LOG.debug("Sending [{}] to {}", new String(msgBytes, StandardCharsets.UTF_8), addr.toString());
		
		DatagramPacket requestPacket = new DatagramPacket(msgBytes, msgBytes.length, ip, port);
		socket.send(requestPacket);
	}
	
	public final byte[] encode() {
		return m_message.encode();
	}
	
	public KRPCMessageType type() {
		return m_messageType;
	} 
	
	public String transactionId() {
		return m_transacionId.string();
	}
	
	public boolean isError() {
		return type() == KRPCMessageType.Error;
	}
	
	public boolean isResponse() {
		return type() == KRPCMessageType.Response;
	}
	
	public boolean isQuery() {
		return type() == KRPCMessageType.Query;
	}
	
	@Override
	public String toString() {
		return String.format("KRPC Message type %s, transaction Id %s",
		                     type().name(), transactionId());
	}
	
	protected final void addToMessage(String key, String value) {
		m_message.put(key, value);
	}
	
	protected final void addToMessage(String key, BencodeObject value) {
		m_message.put(key, value);
	}
}
