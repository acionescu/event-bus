package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public abstract class Condition {
    private String id;
    
    
    public Condition(String id) {
	super();
	if(id == null){
	    throw new RuntimeException("id cannot be null");
	}
	this.id = id;
    }

    public abstract boolean test(EventContext input);

    public String getId() {
        return id;
    }



    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Condition other = (Condition) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }
    
    

}
