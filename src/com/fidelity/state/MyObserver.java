package com.fidelity.state;

public interface MyObserver<T> {
	
	public void update(T t) throws Exception;

}
