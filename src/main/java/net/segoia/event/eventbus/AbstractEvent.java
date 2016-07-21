package net.segoia.event.eventbus;

public abstract class AbstractEvent {

    protected String id;
    protected String et;

    private transient boolean initialized = false;

    /**
     * Override this to lazy initialize this event </br>
     * The method will be called during {@link #getId()} {@link #getEt()} {@link #hashCode()} {@link #equals(Object)}
     * {@link #toString()}
     */
    protected abstract void lazyInit();

    private synchronized void doInit() {
	if (initialized) {
	    return;
	}
	lazyInit();
	initialized = true;
    }

    /**
     * @return the id
     */
    public String getId() {
	doInit();
	return id;
    }

    /**
     * @return the et
     */
    public String getEt() {
	doInit();
	return et;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	doInit();
	final int prime = 31;
	int result = 1;
	result = prime * result + ((et == null) ? 0 : et.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	doInit();
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AbstractEvent other = (AbstractEvent) obj;
	if (et == null) {
	    if (other.et != null)
		return false;
	} else if (!et.equals(other.et))
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /**
     * This simply makes sure that the {@link #doInit()} is called before generating toString
     */
    @Override
    public String toString() {
	doInit();
	return null;
    }

}
