package net.segoia.event.eventbus.peers.events.auth.id;

import net.segoia.event.eventbus.peers.events.session.KeyDef;

public class SharedIdentityType extends IdentityType {
    public static final String TYPE = "SHARED";

    private KeyDef keyDef;

    public SharedIdentityType() {
	super(TYPE);
    }
    
    

    public SharedIdentityType( KeyDef keyDef) {
	this();
	this.keyDef = keyDef;
    }



    public KeyDef getKeyDef() {
	return keyDef;
    }

    public void setKeyDef(KeyDef keyDef) {
	this.keyDef = keyDef;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((keyDef == null) ? 0 : keyDef.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SharedIdentityType other = (SharedIdentityType) obj;
	if (keyDef == null) {
	    if (other.keyDef != null)
		return false;
	} else if (!keyDef.equals(other.keyDef))
	    return false;
	return true;
    }

}
