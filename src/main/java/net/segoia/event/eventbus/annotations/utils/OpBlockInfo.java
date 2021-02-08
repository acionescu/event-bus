package net.segoia.event.eventbus.annotations.utils;

public abstract class OpBlockInfo {
    
    /**
     * Return the string representation of this operation block
     * @return
     */
    public abstract String getCode(OpBlockContext context);
}
