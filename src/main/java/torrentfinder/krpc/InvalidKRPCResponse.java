package torrentfinder.krpc;

public class InvalidKRPCResponse extends InvalidKRPCMessage {
	public InvalidKRPCResponse(String err) {
		super(String.format("Invalid KRPCResponse: %s", err), true);
	}
}
