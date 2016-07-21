package net.segoia.event.conditions;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public class LooseEventMatchCondition extends Condition{
    
    public LooseEventMatchCondition(String id) {
	super(id);
    }


    private String scope;
    private String category;
    private String name;
    
    
    @Override
    public boolean test(EventContext input) {
	Event e = input.getEvent();
	if(scope != null && !scope.equals(e.getScope())) {
	    return false;
	}
	if(category !=null && !category.equals(e.getCategory())) {
	    return false;
	}
	if(name != null && !name.equals(e.getName())) {
	    return false;
	}
	
	return true;
    }


    /**
     * @return the scope
     */
    public String getScope() {
        return scope;
    }


    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param scope the scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }


    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    

}
