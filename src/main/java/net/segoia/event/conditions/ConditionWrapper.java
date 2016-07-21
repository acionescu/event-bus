package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public class ConditionWrapper extends Condition{
    private String nestedConditionId;
    private Condition nestedCondition;
    

    public ConditionWrapper(String id, String nestedConditionId) {
	super(id);
	this.nestedConditionId = nestedConditionId;
    }

    @Override
    public boolean test(EventContext input) {
	if(nestedCondition == null){
	    loadNestedCondition(input);
	}
	return nestedCondition.test(input);
    }
    
    private void loadNestedCondition(EventContext input){
//	nestedCondition = input.getReportContext().getEngine().getConfig().getCondition(nestedConditionId);
	
	
	if(nestedCondition == null){
	    throw new RuntimeException("Condition with id "+nestedConditionId +" not found.");
	}
	
	throw new UnsupportedOperationException();
    }

}
