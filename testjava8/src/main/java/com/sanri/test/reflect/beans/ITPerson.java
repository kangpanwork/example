package com.sanri.test.reflect.beans;


public class ITPerson extends Person{
	private Language language;
	private boolean work;
	
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public boolean isWork() {
		return work;
	}
	public void setWork(boolean work) {
		this.work = work;
	}
}
