package net.segoia.event.eventbus.peers.security;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import net.segoia.util.crypto.CryptoUtil;

public class SpkiPrivateIdentityDataLoader implements PrivateIdentityDataLoader<SpkiPrivateIdentityData> {
    private SpkiIdentityDef indentityDef;

    private SpkiPrivateIdentityData data;

    @Override
    public void load() {
	try {
	    KeyPair keyPair = CryptoUtil.loadKeys(indentityDef.getAlgorithm(), indentityDef.getPath(),
		    indentityDef.getName());

	    data = new SpkiPrivateIdentityData(keyPair.getPrivate(), keyPair.getPublic(), indentityDef.getAlgorithm(),
		    indentityDef.getKeySize());
	} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {

	}
    }

    public SpkiIdentityDef getIndentityDef() {
	return indentityDef;
    }

    public void setIndentityDef(SpkiIdentityDef indentityDef) {
	this.indentityDef = indentityDef;
    }

    public SpkiPrivateIdentityData getData() {
	return data;
    }

}
