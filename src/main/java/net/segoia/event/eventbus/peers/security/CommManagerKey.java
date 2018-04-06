package net.segoia.event.eventbus.peers.security;

public class CommManagerKey {
    private String ourIdentityType;
    private String peerIdentityType;

    public CommManagerKey(String ourIdentityType, String peerIdentityType) {
	super();
	this.ourIdentityType = ourIdentityType;
	this.peerIdentityType = peerIdentityType;
    }

    public String getOurIdentityType() {
	return ourIdentityType;
    }

    public String getPeerIdentityType() {
	return peerIdentityType;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((ourIdentityType == null) ? 0 : ourIdentityType.hashCode());
	result = prime * result + ((peerIdentityType == null) ? 0 : peerIdentityType.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CommManagerKey other = (CommManagerKey) obj;
	if (ourIdentityType == null) {
	    if (other.ourIdentityType != null)
		return false;
	} else if (!ourIdentityType.equals(other.ourIdentityType))
	    return false;
	if (peerIdentityType == null) {
	    if (other.peerIdentityType != null)
		return false;
	} else if (!peerIdentityType.equals(other.peerIdentityType))
	    return false;
	return true;
    }

}
