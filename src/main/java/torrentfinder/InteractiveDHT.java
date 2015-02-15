package torrentfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import torrentfinder.dht.AnnouncePeerQuery;
import torrentfinder.dht.DHTId;
import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.dht.DHTNodeId;
import torrentfinder.dht.DHTQuery;
import torrentfinder.dht.DHTQueryType;
import torrentfinder.dht.FindNodeQuery;
import torrentfinder.dht.GetPeersQuery;
import torrentfinder.dht.PingQuery;
import torrentfinder.utils.TFUtils;

public class InteractiveDHT {
	static Logger LOG = LogManager.getLogger(InteractiveDHT.class.getName());
	
	private static final String QuitsCommand = "quit";
	
	private DHTNodeId m_thisNodeId;
	private BufferedReader m_stdin = new BufferedReader(new InputStreamReader(System.in));		
	
	private Map<DHTQueryType, Class<? extends DHTQuery>> m_queryClasses =
			new HashMap<DHTQueryType, Class<? extends DHTQuery>>() {{
				put(DHTQueryType.AnnouncePeer , AnnouncePeerQuery.class);
				put(DHTQueryType.FindNode     , FindNodeQuery.class);
				put(DHTQueryType.GetPeers     , GetPeersQuery.class);
				put(DHTQueryType.Ping   	  , PingQuery.class);
			}};
	
	public InteractiveDHT() {
		try {
			m_thisNodeId = new DHTNodeId(TFUtils.randomBytes(DHTId.IdByteLength));
		}
		catch (InvalidDHTId e) {
			// can't happen
		}
	}
	
	public void run() {
		System.out.format("This node id: %s\n", m_thisNodeId.toString());
		printCommands();
		
		String cmd = "";
		
		while (! cmd.equals(QuitsCommand)) {
			try {
				cmd = m_stdin.readLine();
			}
			catch (IOException e) {
				LOG.error("Failed to read input from stdin: {}", e.getMessage());
				continue;
			}

			DHTQueryType query = DHTQueryType.fromString(cmd);
			if (query == null) {
				onInvalidCommand(cmd);
				continue;
			}
			
			switch (query) {
				case Ping:
					//doPing();
					break;
					
				default:
					onInvalidCommand(cmd);
			}	
		}
	}
	
	private void doPing() throws IOException {
		System.out.format("Choose ping target:\n");
		for (InetSocketAddress node : BootstrapDHTNodes.Nodes)
			System.out.format("%d) %s\n", BootstrapDHTNodes.Nodes.indexOf(node), node.toString());
		
		int pingTarget;
		try {
			pingTarget = Integer.valueOf(m_stdin.readLine());	
		}
		catch (NumberFormatException e) {
			System.out.format("invalid number formatting\n");
			return;
		}
		
		if (pingTarget < 0 || pingTarget >= BootstrapDHTNodes.Nodes.size()) {
			System.out.format("invalid choice\n");
			return;
		}
		
		InetSocketAddress node = BootstrapDHTNodes.Nodes.get(pingTarget);
		
		PingQuery pingq = new PingQuery("a", m_thisNodeId);
//
//		DatagramSocket socket = pingq.sendTo(BootstrapDHTNodes.Bittorrent);		
//		msg = KRPCMessage.recieveFrom(socket);
//
//		if (! msg.isResponse()) {
//			LOG.debug("expected response, got: {}", msg.toString());
//			return;
//		}
//
//		PingQuery.Response pingResponse = new PingQuery.Response((KRPCResponse) msg);
//		LOG.debug("Got ping response (query id {}), responding node id: {}" ,
//		          pingResponse.transactionId(), pingResponse.respondingNodeId());

	}
	
	private void printCommands() {
		for (DHTQueryType query : DHTQueryType.values())			
			System.out.format("%s\n", query.toString());
	}
	
	private void onInvalidCommand(String cmd) {
		System.out.format("invalid command %s, use one of:\n", cmd);
		printCommands();
	}
}
