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

import net.segoia.event.eventbus.util.JsonUtils;

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
    
    public String toJson() {
	doInit();
	return JsonUtils.toJson(this);
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
