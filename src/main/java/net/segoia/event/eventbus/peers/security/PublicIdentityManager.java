package net.segoia.event.eventbus.peers.security;

public interface PublicIdentityManager {
    public String getType();
    /**
     * If this supports encryption return the max supported size of data that can be encrypted
     * @return
     */
    public int getMaxSupportedEncryptedDataBlockSize();
}
