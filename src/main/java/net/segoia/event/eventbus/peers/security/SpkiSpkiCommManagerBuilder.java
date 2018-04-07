package net.segoia.event.eventbus.peers.security;

import java.io.UnsupportedEncodingException;

import net.segoia.event.eventbus.peers.comm.EncryptCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;
import net.segoia.event.eventbus.util.JsonUtils;

public class SpkiSpkiCommManagerBuilder implements CommManagerBuilder {
    private CommManagerConfig config;

    public SpkiSpkiCommManagerBuilder() {
	super();

	config = new CommManagerConfig();

	/* add tx operations */
	config.addTxOperation("S", new SignCommOperation());
	config.addTxOperation("E", new EncryptWithPublicCommOperation());

	/* add rx operations */
	config.addRxOperation("S", new VerifySignatureCommOperation());
	config.addRxOperation("E", new DecryptWithPrivateCommOperation());

	/* add tx operation context builders */
	config.addTxOpContextBuilder("S", new OperationContextBuilder<SignCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(SignCommOperationDef def, OperationOutput opContext) {
		return new SignCommOperationContext(opContext.getFullOutputData(), def);
	    }

	});

	config.addTxOpContextBuilder("E", new OperationContextBuilder<EncryptCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(EncryptCommOperationDef def, OperationOutput opContext) {
		return new EncryptOperationContext(opContext.getFullOutputData(), def);
	    }
	});

	/* add rx operation context builder */

	config.addRxOpContextBuilder("S", new OperationContextBuilder<SignCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(SignCommOperationDef def, OperationOutput opContext) {
		byte[] data = opContext.getFullOutputData();
		String json;
		try {
		    json = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException("Failed to convert bytes to UTF-8 string");
		}
		SignCommOperationOutput signOperationOutput = JsonUtils.fromJson(json, SignCommOperationOutput.class);

		return new VerifySignatureOperationContext(def, signOperationOutput);
	    }
	});

	config.addRxOpContextBuilder("E", new OperationContextBuilder<EncryptCommOperationDef>() {

	    @Override
	    public OperationContext buildContext(EncryptCommOperationDef def, OperationOutput opContext) {
		return new DecryptOperationContext(opContext.getFullOutputData(), def);
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
