package torrentfinder;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class BootstrapDHTNodes {
	public static final InetSocketAddress Bittorrent   = new InetSocketAddress("router.bittorrent.com"  , 6881);
	public static final InetSocketAddress Utorrent     = new InetSocketAddress("router.utorrent.com"	, 6881);
	public static final InetSocketAddress Bitcommet    = new InetSocketAddress("router.bitcommet.com"	, 6881);
	public static final InetSocketAddress Transmission = new InetSocketAddress("dht.transmissionbt.com" , 6881);
	
	public static final List<InetSocketAddress> Nodes = 
			Arrays.asList(Bittorrent, Utorrent, Bitcommet, Transmission);
			
}

