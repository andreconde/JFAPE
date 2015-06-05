package br.com.jfap.bci.pojo;

import javassist.CtConstructor;

public class Constructor {
	private CtConstructor constructor;
	
	public Constructor(CtConstructor constructor) {
		this.constructor = constructor;
	}
	
	public String getName() {
		return constructor.getName();
	}
	
	public void setBody(String src) throws Exception {
		constructor.setBody(src);
	}
}
