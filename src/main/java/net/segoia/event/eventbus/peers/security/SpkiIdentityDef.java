package net.segoia.event.eventbus.peers.security;

public class SpkiIdentityDef {
    private String name;
    private String path;
    private String algorithm;
    private int keySize;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    public int getKeySize() {
        return keySize;
    }
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }
}
