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
