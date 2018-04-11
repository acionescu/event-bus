package net.segoia.event.eventbus.peers.security;

import java.security.Signature;

public class DefaultSignOperationWorker extends SignatureBasedOperationWorker implements SignOperationWorker{

    public DefaultSignOperationWorker(Signature signature) {
	super(signature);
	// TODO Auto-generated constructor stub
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
	Signature signature = getSignature();
	signature.update(data);
	return signature.sign();
    }

}
