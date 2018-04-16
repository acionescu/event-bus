package net.segoia.event.eventbus.peers.security;

import java.util.HashMap;
import java.util.Map;

public class OperationContext<D extends OperationDef> {
    private D opDef;
    
    private Map<Class, OperationDataDeserializer<?>> inputDeserializers;

    public OperationContext(D opDef) {
	super();
	this.opDef = opDef;
    }

    public D getOpDef() {
	return opDef;
    }

    public void setOpDef(D opDef) {
	this.opDef = opDef;
    }
    
    public <T> void addDeserializer(Class<T> clazz, OperationDataDeserializer<T> deserializer) {
	if(inputDeserializers == null) {
	    inputDeserializers = new HashMap<>();
	}
	inputDeserializers.put(clazz, deserializer);
    }
    
    public <T> T deserializeTo(Class<T> clazz, OperationData opData){
	if(clazz.isInstance(opData)) {
	    return (T)opData;
	}
	OperationDataDeserializer<?> d = inputDeserializers.get(clazz);
	return (T)d.toObject(opData.getFullData());
    }

}
