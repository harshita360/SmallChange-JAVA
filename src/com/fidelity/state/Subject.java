package com.fidelity.state;


public interface Subject<T> {
	
	public void register(MyObserver<T> observer);
	public void deRegister(MyObserver<T> observer);
	public void navigateUpdate(T t);

}
