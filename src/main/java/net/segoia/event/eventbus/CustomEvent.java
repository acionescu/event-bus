package net.segoia.event.eventbus;

public abstract class CustomEvent<D> extends Event {

    public CustomEvent(Class<?> clazz) {
	super(clazz);
    }

    protected D data;

    /**
     * @return the data
     */
    public D getData() {
	return data;
    }
    
    
    

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.Event#clone()
     */
    @Override
    public CustomEvent<D> clone() {
	
	 CustomEvent<D> ne = (CustomEvent<D>)super.clone();
	 
	 ne.data = data;
	 
	 return ne;
    }




    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(getClass().getSimpleName()+" [");
	if (super.toString() != null)
	    builder.append("toString()=").append(super.toString()).append(", ");
	if (data != null)
	    builder.append("data=").append(data);
	builder.append("]");
	return builder.toString();
    }




    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((data == null) ? 0 : data.hashCode());
	return result;
    }




    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CustomEvent other = (CustomEvent) obj;
	if (data == null) {
	    if (other.data != null)
		return false;
	} else if (!data.equals(other.data))
	    return false;
	return true;
    }

    
    
    
}
