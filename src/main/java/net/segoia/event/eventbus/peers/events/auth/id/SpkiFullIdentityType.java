package net.segoia.event.eventbus.peers.events.auth.id;

import net.segoia.event.eventbus.peers.events.session.KeyDef;

public class SpkiFullIdentityType extends IdentityType {
    public static final String TYPE = "SPKI_FULL";

    private KeyDef keyDef;

    public SpkiFullIdentityType(KeyDef keyDef) {
	this();
	this.keyDef = keyDef;
    }

    public SpkiFullIdentityType() {
	super(TYPE);
    }

    public KeyDef getKeyDef() {
	return keyDef;
    }

    public void setKeyDef(KeyDef keyDef) {
	this.keyDef = keyDef;
    }

}
