package net.segoia.event.eventbus.annotations.utils;

public class GenericCodeBlock extends OpBlockInfo {
    private String code;

    public GenericCodeBlock() {
	super();
	// TODO Auto-generated constructor stub
    }

    public GenericCodeBlock(String code) {
	super();
	this.code = code;
    }

    @Override
    public String getCode(OpBlockContext context) {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

}
