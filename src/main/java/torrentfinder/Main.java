package torrentfinder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import torrentfinder.bencoding.BencodeDictionary;
import torrentfinder.bencoding.BencodeObject;
import torrentfinder.bencoding.BencodeObjectBuilder;
import torrentfinder.bencoding.InvalidBencodeObject;
import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.dht.DHTNodeId;
import torrentfinder.dht.DHTNodeInfo;
import torrentfinder.dht.DHTNodeInfo.InvalidDHTNodeInfo;
import torrentfinder.dht.AnnouncePeerQuery;
import torrentfinder.dht.FindNodeQuery;
import torrentfinder.dht.GetPeersQuery;
import torrentfinder.dht.Infohash;
import torrentfinder.dht.PingQuery;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.KRPCMessage;
import torrentfinder.krpc.KRPCResponse;
import torrentfinder.utils.Hexdump;


public class Main {
	static Logger LOG = LogManager.getLogger(Main.class.getName());
	/*
	 * node ids and infohashes are 160 bits
	 * 	
	 * bencoded dicts sent over UDP
	 * single request packet, single response packet
	 * 
	 * 3 msg type: query, response, error
	 * 4 queries: ping, find_node, get_peers, announce_peer
	 * 
	 * every msg has :
	 * 	key "t" with value of transaction id (2 bytes usually) (generatd in request, echoed in response)
	 * 	key "y" with single char value describing the type of message (q, r, e)
	 * 
	 * contact peer info: 6 byte string (4 byte ip, 2 byte port)
	 * contact node info: 26 bytes string (20 byte node id, 6 byte ip-port)  
	 * 
	 * queries contain:
	 * 		"q" - string for method name
	 * 		"a" - dict, named arguments to query, contains:
	 * 			"id" - node id 20 bytes (NOT node contact info)
	 * 
	 * 		ping
	 * 			arguments:  {"id" : "<querying nodes id>"}
	 * 			response: {"id" : "<queried nodes id>"}
	 * 
	 * 		find_node
	 * 			arguments:  {"id" : "<querying nodes id>", "target" : "<id of target node>"}
	 * 			response: {"id" : "<queried nodes id>", "nodes" : "<compact node info>"}  
	 * 
	 * 		get_peers
	 * 			arguments:  {"id" : "<querying nodes id>", "info_hash" : "<20-byte infohash of target torrent>"}
	 * 			response: {"id" : "<queried nodes id>", "token" :"<opaque write token>", "values" : ["<peer 1 info string>", "<peer 2 info string>"]}
	 * 			or: {"id" : "<queried nodes id>", "token" :"<opaque write token>", "nodes" : "<compact node info>"}
	 * 
	 * 		announce_peer
	 * 			arguments:  {"id" : "<querying nodes id>",
	 * 						 "implied_port": <0 or 1>,
	 *	    				 "info_hash" : "<20-byte infohash of target torrent>",
	 *						 "port" : <port number>,
	 *						 "token" : "<opaque token>"}
	 *
	 *			response: {"id" : "<queried nodes id>"}
	 * 
	 * responses:
	 * 		"r" - dict with return values
	 * 
	 * errors:
	 * 		"e" - list, 1st element is error, 2nd error mesg (string)
	 * 		error code: 201, 202, 203, 204
	 * 
	 */
	
	public static void main(String[] args) {
		new InteractiveDHT().run();
	}
	
	public static void mainss(String[] args) throws InvalidBencodeObject, InvalidKRPCMessage, IOException, InvalidDHTId, InvalidDHTNodeInfo {
		DatagramSocket socket;
		KRPCMessage msg;
		DHTNodeId myid = new DHTNodeId("abcdefghij0123456789");
		
		
		PingQuery pingq = new PingQuery("aifewa", myid);
		
		socket = pingq.sendTo(BootstrapDHTNodes.Bittorrent);		
		msg = KRPCMessage.recieveFrom(socket);
		
		if (! msg.isResponse()) {
			LOG.debug("expected response, got: {}", msg.toString());
			return;
		}
		
		PingQuery.Response pingResponse = new PingQuery.Response((KRPCResponse) msg);
		LOG.debug("Got ping response (query id {}), responding node id: {}" ,
		          pingResponse.transactionId(), pingResponse.respondingNodeId());
			
		
		
		
		
		
//		FindNodeQuery findq = new FindNodeQuery("aa", myid, pingResponse.respondingNodeId());
//		socket = findq.sendTo(BootstrapDHTNodes.Bittorrent);
//		msg = KRPCMessage.recieveFrom(socket);
//		
//		if (! msg.isResponse()) {
//			LOG.debug("expected response, got: {}", msg.toString());
//			return;
//		}
//		
//		FindNodeQuery.Response findResponse = new FindNodeQuery.Response((KRPCResponse) msg);
//		for (DHTNodeInfo node : findResponse.nodes()) 
//			LOG.debug(node.toString());
		
		
		
		
		
		
		Infohash infoh = new Infohash(new byte[] {(byte) 0x02, (byte) 0x06, (byte) 0x87, (byte) 0x4E,
		                                          (byte) 0xF6, (byte) 0x5C, (byte) 0x89, (byte) 0x09,
		                                          (byte) 0xA7, (byte) 0x97, (byte) 0xF3, (byte) 0x37,
		                                          (byte) 0x66, (byte) 0xE8, (byte) 0x66, (byte) 0x26,
		                                          (byte) 0x7E, (byte) 0x57, (byte) 0x4A, (byte) 0x10});
		
//		GetPeersQuery getpeersq = new GetPeersQuery("aa", myid, infoh);
//		socket = getpeersq.sendTo(BootstrapDHTNodes.Utorrent);
//		msg = KRPCMessage.recieveFrom(socket);
//			
//		if (! msg.isResponse()) {
//			LOG.debug("expected response, got: {}", msg.toString());
//			return;
//		}
//		
//		GetPeersQuery.Response getpeersResponse = new GetPeersQuery.Response((KRPCResponse) msg);
//		if (! getpeersResponse.hasPeers()) {
//			for (DHTNodeInfo peer : getpeersResponse.closestPeers()) {
//				
//			}
//		}
		
//		AnnouncePeerQuery announce = new AnnouncePeerQuery("aa", myid, infoh, 8281, new byte[] { 0x00 }, true);
//		socket = announce.sendTo(BootstrapDHTNodes.Bittorrent);
//		msg = KRPCMessage.recieveFrom(socket);
//			
//		if (! msg.isResponse()) {
//			LOG.debug("expected response, got: {}", msg.toString());
//			return;
//		}
//		
//		AnnouncePeerQuery.Response getpeersResponse = new AnnouncePeerQuery.Response((KRPCResponse) msg);
//		LOG.debug(getpeersResponse.respondingNodeId());
		
		
//		if (responseMsg instanceof KRPCError) {
//			LOG.debug("Error in ping query: {}", responseMsg.toString());
//			return;
//		}
		
					

//		FindNodeQuery findnode = new FindNodeQuery("aa", 
//		                                           new DHTNodeId("abcdefghij0123456789kkk"),
//		                                           new DHTNodeId("mnopqrstuvwxyz123456"));
//		byte[] tmp2 = findnode.encode();
//		LOG.debug(findnode.toString());
//		LOG.debug(new String(tmp2, TFUtils.StringEncodings.ASCII));

		//d1:ad2:id20:abcdefghij0123456789e1:q4:ping1:t1:��1:y1:qe
		//d1:ad2:id20:abcdefghij0123456789e1:q4:ping1:t2:aa1:y1:qe
		
		/*
		 * recieve a message, 
		 * read it as bencoded dictionary
		 *  
		 */
	}
	
	public static void readtorrentfile(String[] args) {
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream("/home/sol/.tmp/bla.torrent"));

			byte[] inbuff = new byte[100000];
			in.read(inbuff);
			
			LOG.debug(BencodeObjectBuilder.buildFrom(inbuff).toString());
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidBencodeObject e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (in != null)
				try {
					in.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	public static void dhtdemo(String[] args) {				
		InetSocketAddress bootstrapNode = BootstrapDHTNodes.Bittorrent;
		InetAddress bootstrapNodeAddress = bootstrapNode.getAddress();
		
		if (bootstrapNodeAddress == null) {
			LOG.error(String.format("Failed to resolve %s", bootstrapNode));
			return;
		}

		LOG.debug(String.format("address for %s is %s",
		                        bootstrapNode, 
		                        bootstrapNodeAddress.getHostAddress()));

		BencodeDictionary pingRequest = new BencodeDictionary() 
			.put("t", "aa")
			.put("y", "q")
			.put("q", "ping")		
			.put("a", new BencodeDictionary("id", "abcdefghij0123456789"));
		
		byte[] pingRequestBytes = pingRequest.encode();

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();

			DatagramPacket requestPacket = 
					new DatagramPacket(pingRequestBytes, 
					                   pingRequestBytes.length, 
					                   bootstrapNodeAddress,
					                   bootstrapNode.getPort());
			try {
				socket.send(requestPacket);
			}
			catch (IOException e) {
				LOG.error(String.format("Failed to send ping request: %s", e.getStackTrace().toString()));
				return;
			}

			byte[] pingResponseBytes = new byte[80];
			DatagramPacket responsePacket = new DatagramPacket(pingResponseBytes, pingResponseBytes.length);

			try {
				socket.receive(responsePacket);
			}
			catch (IOException e) {
				LOG.error(String.format("Failed to recieve ping response: %s", e.getStackTrace().toString()));
				return;
			}

			LOG.debug(Hexdump.dumpHexString(responsePacket.getData()));
			BencodeObject pingResponse = BencodeObjectBuilder.buildFrom(responsePacket.getData());
			LOG.info(pingResponse.toString());
		}
		catch (SocketException e1) {
			LOG.error(String.format("Failed to create UDP socket: %s", e1.getStackTrace().toString()));
			return;
		}
		catch (InvalidBencodeObject e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (socket != null)
				socket.close();
		}
	}
	
}
