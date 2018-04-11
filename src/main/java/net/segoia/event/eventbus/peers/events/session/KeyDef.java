package net.segoia.event.eventbus.peers.events.session;

public class KeyDef {
    private String algorithm;
    private int keySize;
    
    
    public KeyDef(String algorithm, int keySize) {
	super();
	this.algorithm = algorithm;
	this.keySize = keySize;
    }

    public KeyDef() {
	super();
	// TODO Auto-generated constructor stub
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
