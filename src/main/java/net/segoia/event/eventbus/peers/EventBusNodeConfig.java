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
package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityConfig;
import net.segoia.event.eventbus.peers.util.EventNodeHelper;

public class EventBusNodeConfig {
    /**
     * If this is enabled, this node will relay events from other peers as well
     */
    private boolean autoRelayEnabled;

    /**
     * What internal events this node is allowed to share ( forward to peers ) </br>
     * If left null, it will share all events
     */
    private Condition allowedSharedEvents;

    /**
     * The default condition that will use for a peering request
     */
    private Condition defaultRequestedEvents = LooseEventMatchCondition.build(Events.SCOPE.EBUS, Events.CATEGORY.PEER);

    /**
     * If true, this node will handle all incoming events regardless of the destination
     */
    private boolean god;

    private EventNodeSecurityConfig securityConfig = new EventNodeSecurityConfig();

    /**
     * A helper class
     */
    private EventNodeHelper helper = new EventNodeHelper();

    private PeersManagerConfig peersManagerConfig;

    /**
     * @return the autoRelayEanbled
     */
    public boolean isAutoRelayEnabled() {
	return autoRelayEnabled;
    }

    /**
     * @param autoRelayEnabled
     *            the autoRelayEanbled to set
     */
    public void setAutoRelayEnabled(boolean autoRelayEnabled) {
	this.autoRelayEnabled = autoRelayEnabled;
    }

    /**
     * @return the allowedSharedEvents
     */
    public Condition getAllowedSharedEvents() {
	return allowedSharedEvents;
    }

    /**
     * @param allowedSharedEvents
     *            the allowedSharedEvents to set
     */
    public void setAllowedSharedEvents(Condition allowedSharedEvents) {
	this.allowedSharedEvents = allowedSharedEvents;
    }

    /**
     * @return the defaultRequestedEvents
     */
    public Condition getDefaultRequestedEvents() {
	return defaultRequestedEvents;
    }

    /**
     * @param defaultRequestedEvents
     *            the defaultRequestedEvents to set
     */
    public void setDefaultRequestedEvents(Condition defaultRequestedEvents) {
	this.defaultRequestedEvents = defaultRequestedEvents;
    }

    /**
     * @return the god
     */
    public boolean isGod() {
	return god;
    }

    /**
     * @param god
     *            the god to set
     */
    public void setGod(boolean god) {
	this.god = god;
    }

    public EventNodeSecurityConfig getSecurityConfig() {
	return securityConfig;
    }

    public void setSecurityConfig(EventNodeSecurityConfig securityConfig) {
	this.securityConfig = securityConfig;
    }

    public EventNodeHelper getHelper() {
	return helper;
    }

    public void setHelper(EventNodeHelper helper) {
	this.helper = helper;
    }

    public PeersManagerConfig getPeersManagerConfig() {
	return peersManagerConfig;
    }

    public void setPeersManagerConfig(PeersManagerConfig peersManagerConfig) {
	this.peersManagerConfig = peersManagerConfig;
    }

}
