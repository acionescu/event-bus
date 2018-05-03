package net.segoia.event.eventbus.peers.security;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.peers.vo.comm.CommOperationDef;

public class CommManagerConfig {
    private Map<String, CommOperation> txOperations=new HashMap<>();
    private Map<String, CommOperationContextBuilder> txOpContextBuilders= new HashMap<>();
    
    private Map<String, CommOperation> rxOperations=new HashMap<>();
    private Map<String, CommOperationContextBuilder> rxOpContextBuilders = new HashMap<>();
    
    public <O extends CommOperation<?,?,?>> O getTxOperation(CommOperationDef def) {
	return (O)txOperations.get(def.getType());
    }
    
    public CommOperationContextBuilder getTxOpContextBuilder(CommOperationDef def) {
	return txOpContextBuilders.get(def.getType());
    }
    
    public <O extends CommOperation> O getRxOperation(CommOperationDef def) {
	return (O)rxOperations.get(def.getType());
    }
    
    public CommOperationContextBuilder getRxOpContextBuilder(CommOperationDef def) {
	return rxOpContextBuilders.get(def.getType());
    }
    
    public  void addTxOperation(String type, CommOperation op) {
	txOperations.put(type, op);
    }
    
    public void addTxOpContextBuilder(String type, CommOperationContextBuilder builder) {
	txOpContextBuilders.put(type, builder);
    }
    
    public void addRxOperation(String type, CommOperation op) {
	rxOperations.put(type, op);
    }
    
    public void addRxOpContextBuilder(String type, CommOperationContextBuilder builder) {
	rxOpContextBuilders.put(type, builder);
    }
}
