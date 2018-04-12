package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public class SpkiSpkiCommManagerBuilder implements CommManagerBuilder {
    private CommManagerConfig config;

    public SpkiSpkiCommManagerBuilder() {
	super();

	config = new CommManagerConfig();

	/* add tx operations */
	config.addTxOperation(SignCommOperationDef.TYPE, new SignCommOperation());
	config.addTxOperation(EncryptWithPublicCommOperationDef.TYPE, new EncryptWithPublicCommOperation());

	/* add rx operations */
	config.addRxOperation(SignCommOperationDef.TYPE, new VerifySignatureCommOperation());
	config.addRxOperation(EncryptWithPublicCommOperationDef.TYPE, new DecryptWithPrivateCommOperation());

	/* add tx operation context builders */
	config.addTxOpContextBuilder(SignCommOperationDef.TYPE, new SpkiOperationContextBuilder<SignCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(SignCommOperationDef def, SpkiPrivateIdentityManager privateIdentity,
		    SpkiPublicIdentityManager peerIdentity) {
		return new SignCommOperationContext(def, privateIdentity, peerIdentity);
	    }

	});

	config.addTxOpContextBuilder(EncryptWithPublicCommOperationDef.TYPE, new SpkiOperationContextBuilder<EncryptWithPublicCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(EncryptWithPublicCommOperationDef def,
		    SpkiPrivateIdentityManager privateIdentity, SpkiPublicIdentityManager peerIdentity) {
		return new EncryptWithPrivateOperationContext(def, privateIdentity, peerIdentity);
	    }
	});

	/* add rx operation context builder */

	config.addRxOpContextBuilder(SignCommOperationDef.TYPE, new SpkiOperationContextBuilder<SignCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(SignCommOperationDef def, SpkiPrivateIdentityManager privateIdentity,
		    SpkiPublicIdentityManager peerIdentity) {
		// byte[] data = opContext.getFullData();
		// String json;
		// try {
		// json = new String(data, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// throw new RuntimeException("Failed to convert bytes to UTF-8 string");
		// }
		// SignCommOperationOutput signOperationOutput = JsonUtils.fromJson(json,
		// SignCommOperationOutput.class);

		return new VerifySignatureOperationContext(def, privateIdentity, peerIdentity);
	    }
	});

	config.addRxOpContextBuilder(EncryptWithPublicCommOperationDef.TYPE, new SpkiOperationContextBuilder<EncryptWithPublicCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(EncryptWithPublicCommOperationDef def,
		    SpkiPrivateIdentityManager privateIdentity, SpkiPublicIdentityManager peerIdentity) {
		return new DecryptWithPrivateOperationContext(def, privateIdentity, peerIdentity);
	    }
	});

    }

    @Override
    public SpkiSpkiCommManager build(CommProtocolContext context) {
	SpkiSpkiCommManager commManager = new SpkiSpkiCommManager();
	commManager.setOurIdentity((SpkiPrivateIdentityData) context.getOurIdentity());
	commManager.setPeerIdentity((SpkiPublicIdentityManager) context.getPeerIdentity());
	commManager.setTxStrategy(context.getTxStrategy());
	commManager.setRxStrategy(context.getRxStrategy());

	commManager.setConfig(config);

	return commManager;
    }
}
