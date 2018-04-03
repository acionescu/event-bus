package net.segoia.event.eventbus.peers.test.vo;

public class ClientTestEventTransceiver extends TestEventTransceiver{
    
    public ClientTestEventTransceiver(ServerTestEventTransceiver pairTransceiver) {
	setPairTransceiver(pairTransceiver);
	pairTransceiver.setPairTransceiver(this);
    }

    @Override
    public void start() {
	/* start the peer transceiver, to simulate connection */
	getPairTransceiver().start();
    }

    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }

}
