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

import java.util.ArrayList;
import java.util.List;

public class CodeElement<T> {
    /**
     * Annotations for this element
     */
    private List<AnnotationInfo> annotations = new ArrayList<>();

    public List<AnnotationInfo> getAnnotations() {
	return annotations;
    }

    public void setAnnotations(List<AnnotationInfo> annotations) {
	this.annotations = annotations;
    }

    public T addAnnotation(AnnotationInfo ai) {
	annotations.add(ai);
	return (T)this;
    }
}
