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
package net.segoia.event.eventbus.test;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.test.CustomTestEvent.Data;

@EventType(value = "TEST:TEST:EVENT")
public class CustomTestEvent extends CustomEvent<Data> {
    public static final String ET="TEST:TEST:EVENT";
    
    public CustomTestEvent(String prop) {
	super(ET);
	this.data = new Data();
	this.data.prop = prop;
    }

    class Data {
	private String prop;

	/**
	 * @return the prop
	 */
	public String getProp() {
	    return prop;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("Data [");
	    if (prop != null)
		builder.append("prop=").append(prop);
	    builder.append("]");
	    return builder.toString();
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((prop == null) ? 0 : prop.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    Data other = (Data) obj;
	    if (prop == null) {
		if (other.prop != null)
		    return false;
	    } else if (!prop.equals(other.prop))
		return false;
	    return true;
	}

    }
    
}
