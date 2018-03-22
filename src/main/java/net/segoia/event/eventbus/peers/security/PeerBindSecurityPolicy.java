package net.segoia.event.eventbus.peers.security;

import java.util.List;

/**
 * Defines a general security policy that governs the communication between a node and a peer
 * @author adi
 *
 */
public class PeerBindSecurityPolicy {
    /**
     * Require a public key from peers
     */
    private boolean spkiRequiredForPeers;

    /**
     * Require a public key from peers, digitally signed by a certificate authority
     */
    private boolean pkiRequiredForPeers;
    
    
    /**
     * The list of algorithms accepted for key generation
     */
    private List<String> acceptedKeyAlgorithms;
    
    /**
     * The list of algorithms accepted for signing
     */
    private List<String> acceptedSignatureAlgorithms;
    

    public boolean isSpkiRequiredForPeers() {
        return spkiRequiredForPeers;
    }

    public void setSpkiRequiredForPeers(boolean spkiRequiredForPeers) {
        this.spkiRequiredForPeers = spkiRequiredForPeers;
    }

    public boolean isPkiRequiredForPeers() {
        return pkiRequiredForPeers;
    }

    public void setPkiRequiredForPeers(boolean pkiRequiredForPeers) {
        this.pkiRequiredForPeers = pkiRequiredForPeers;
    }

    public List<String> getAcceptedKeyAlgorithms() {
        return acceptedKeyAlgorithms;
    }

    public void setAcceptedKeyAlgorithms(List<String> acceptedKeyAlgorithms) {
        this.acceptedKeyAlgorithms = acceptedKeyAlgorithms;
    }

    public List<String> getAcceptedSignatureAlgorithms() {
        return acceptedSignatureAlgorithms;
    }

    public void setAcceptedSignatureAlgorithms(List<String> acceptedSignatureAlgorithms) {
        this.acceptedSignatureAlgorithms = acceptedSignatureAlgorithms;
    }

}
