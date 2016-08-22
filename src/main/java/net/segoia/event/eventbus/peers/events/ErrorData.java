package net.segoia.event.eventbus.peers.events;

public class ErrorData {
    private String reason;

    public ErrorData(String reason) {
	super();
	this.reason = reason;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("ErrorData [");
	if (reason != null)
	    builder.append("reason=").append(reason);
	builder.append("]");
	return builder.toString();
    }
    
    
}
