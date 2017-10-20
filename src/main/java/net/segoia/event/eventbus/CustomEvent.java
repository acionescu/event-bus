/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus;

public abstract class CustomEvent<D> extends Event {

    public CustomEvent(Class<?> clazz) {
	super(clazz);
    }
        

    public CustomEvent(Class<?> clazz, D data) {
	super(clazz);
	this.data = data;
    }



    protected D data;

    /**
     * @return the data
     */
    public D getData() {
	return data;
    }
    
    public void setData(D data) {
	this.data = data;
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
