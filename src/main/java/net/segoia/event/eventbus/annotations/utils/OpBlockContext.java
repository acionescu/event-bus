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
package net.segoia.event.eventbus.annotations.utils;

import java.util.ArrayDeque;
import java.util.Deque;

public class OpBlockContext {
    private ClassInfo ownerClass;

    /**
     * A stack that reflects the context of this op block
     */
    private Deque<CodeElement> codeContext = new ArrayDeque<>();

    /**
     * The index of the current block in the list of code blocks in this context
     */
    private int blockIndex;

    public OpBlockContext() {
	super();
    }

    public OpBlockContext(ClassInfo rootClass) {
	pushCodeElement(rootClass);
    }

    public OpBlockContext pushCodeElement(CodeElement element) {
	codeContext.push(element);

	if (element instanceof ClassInfo) {
	    /* set the owner class */
	    ownerClass = (ClassInfo) element;
	}
	return this;
    }

    public ClassInfo getOwnerClass() {
	return ownerClass;
    }

    public void setOwnerClass(ClassInfo ownerClass) {
	this.ownerClass = ownerClass;
    }

    public Deque<CodeElement> getCodeContext() {
	return codeContext;
    }

    public void setCodeContext(Deque<CodeElement> codeContext) {
	this.codeContext = codeContext;
    }

    public int getBlockIndex() {
	return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
	this.blockIndex = blockIndex;
    }

}
