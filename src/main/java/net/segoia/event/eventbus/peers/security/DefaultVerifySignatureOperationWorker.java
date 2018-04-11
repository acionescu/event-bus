package net.segoia.event.eventbus.peers.security;

import java.security.Signature;

public class DefaultVerifySignatureOperationWorker extends SignatureBasedOperationWorker implements VerifySignatureOperationWorker{

    public DefaultVerifySignatureOperationWorker(Signature signature) {
	super(signature);
    }

    @Override
    public boolean verify(byte[] data, byte[] signature) throws Exception {
	Signature sig = getSignature();
	sig.update(data);
	return sig.verify(signature);
    }

}
