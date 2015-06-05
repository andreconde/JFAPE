package br.com.jfap.bci.pojo;

import javassist.CtMethod;

/**
 * Represents a method to be modified.
 * @author Andre Conde
 *
 */
public class Method {

	private CtMethod method;
	
	public Method( CtMethod method ) {
		this.method = method;
	}
	
	public void insertAfter(String src) throws Exception {
		method.insertAfter(src);
	}
	
	public void insertBefore(String src) throws Exception {
		method.insertAfter(src);
	}
	
	public void setBody(String src) throws Exception {
		method.setBody(src);
	}
	
	public String getName() {
		return method.getName();
	}
	
}
