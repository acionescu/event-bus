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
package net.segoia.event.eventbus.app;

public class EventNodeAppControllerConfig {
    /**
     * it only supports clients with version equal or greater than this value
     */
    private int minClientVersion;
    
    private String appInitDataFile;

    public EventNodeAppControllerConfig(int minClientVersion) {
	super();
	this.minClientVersion = minClientVersion;
    }

    public EventNodeAppControllerConfig() {
	super();
	// TODO Auto-generated constructor stub
    }

    public int getMinClientVersion() {
	return minClientVersion;
    }

    public void setMinClientVersion(int minClientVersion) {
	this.minClientVersion = minClientVersion;
    }

    public String getAppInitDataFile() {
        return appInitDataFile;
    }

    public void setAppInitDataFile(String appInitDataFile) {
        this.appInitDataFile = appInitDataFile;
    }
    
    

}
