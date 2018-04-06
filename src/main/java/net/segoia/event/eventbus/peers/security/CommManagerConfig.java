package net.segoia.event.eventbus.peers.security;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public class CommManagerConfig {
    private Map<String, CommOperation> txOperations=new HashMap<>();
    private Map<String, OperationContextBuilder> txOpContextBuilders= new HashMap<>();
    
    private Map<String, CommOperation> rxOperations=new HashMap<>();
    private Map<String, OperationContextBuilder> rxOpContextBuilders = new HashMap<>();
    
    public <O extends CommOperation<?,?>> O getTxOperation(CommOperationDef def) {
	return (O)txOperations.get(def.getType());
    }
    
    public OperationContextBuilder getTxOpContextBuilder(CommOperationDef def) {
	return txOpContextBuilders.get(def.getType());
    }
    
    public  void addTxOperation(String type, CommOperation op) {
	txOperations.put(type, op);
    }
    
    public void addTxOpContextBuilder(String type, OperationContextBuilder builder) {
	txOpContextBuilders.put(type, builder);
    }
    
    public void addRxOperation(String type, CommOperation op) {
	rxOperations.put(type, op);
    }
    
    public void addRxOpContextBuilder(String type, OperationContextBuilder builder) {
	rxOpContextBuilders.put(type, builder);
    }
}
