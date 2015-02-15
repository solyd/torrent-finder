package torrentfinder.krpc;

public class InvalidKRPCError extends InvalidKRPCMessage {
	public InvalidKRPCError(String err) {
		super(String.format("Invalid KRPCError: %s", err), true);
	}
}
