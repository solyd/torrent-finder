package torrentfinder.krpc;

public class InvalidKRPCQuery extends InvalidKRPCMessage {
	public InvalidKRPCQuery(String err) {
		super(String.format("Invalid KRPCQuery: %s", err), true);
	}
}
