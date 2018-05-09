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
package net.segoia.event.eventbus.peers.security.rules;

import java.util.regex.Pattern;

public class RegexRuleMatcher extends RuleMatcher {
    public static final String TYPE = "REGEX";
    private String regex;
    private Pattern pattern;

    public RegexRuleMatcher() {
	super(TYPE);
    }

    public RegexRuleMatcher( String regex) {
	this();
	this.regex = regex;
    }

    protected Pattern getPattern() {
	if (pattern == null) {
	    pattern = Pattern.compile(regex);
	}
	return pattern;
    }

    public String getRegex() {
	return regex;
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

    @Override
    public boolean matches(RuleMatchContext context) {
	return getPattern().matcher(context.getEventSignature()).matches();
    }

}
