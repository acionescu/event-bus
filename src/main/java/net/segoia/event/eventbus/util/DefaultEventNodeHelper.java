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
package net.segoia.event.eventbus.util;

import java.util.UUID;

import net.segoia.event.eventbus.peers.util.EventNodeHelper;

public class DefaultEventNodeHelper implements EventNodeHelper {
    public String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    public String generateSessionId() {
	return UUID.randomUUID().toString();
    }

    public String generateSecurityToken() {
	return UUID.randomUUID().toString();
    }

    @Override
    public String generateEventId() {
	return UUID.randomUUID().toString();
    }

    @Override
    public String[] splitString(String target, String sep) {
	return target.split(sep);
    }
}
