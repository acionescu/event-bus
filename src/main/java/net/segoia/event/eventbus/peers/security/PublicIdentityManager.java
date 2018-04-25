package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;

public interface PublicIdentityManager<I extends NodeIdentity<?>> {
    public String getType();
    /**
     * If this supports encryption return the max supported size of data that can be encrypted
     * @return
     */
    public int getMaxSupportedEncryptedDataBlockSize();
    
    public I getIdentity();
    
    /**
     * A unique string to identify this identity by
     * @return
     */
    public String getIdentityKey();
}
