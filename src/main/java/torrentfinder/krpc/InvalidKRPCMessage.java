package torrentfinder.krpc;

public class InvalidKRPCMessage extends Exception {
	public InvalidKRPCMessage(String err) {
		super(String.format("Invalid KRPCMessage: %s", err));
	}
	
	protected InvalidKRPCMessage(String err, boolean ignored) {
		super(err);
	}
}
