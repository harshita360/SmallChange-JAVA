package com.fidelity.state;

import java.util.HashSet;
import java.util.Set;

import com.fidelity.models.Trade;

public class TradeSubject implements Subject<Trade> {
	
	public static TradeSubject getInstance() {
		if(instance==null) {
			synchronized (TradeSubject.class) {
				if(instance==null) {
					instance=new TradeSubject();
				}
			}
		}
		return instance;
	}
	
	private static TradeSubject instance;
	
	Set<MyObserver<Trade>> subscribers;
	
	public  TradeSubject() {
		subscribers=new HashSet<>();
	}

	@Override
	public void register(MyObserver<Trade> observer) {
		this.subscribers.add(observer);
		//System.out.println(observer.getClass()+" registered");
		
	}

	@Override
	public void deRegister(MyObserver<Trade> observer) {
		this.subscribers.remove(observer);
		
	}

	@Override
	public void navigateUpdate(Trade t) {
		//System.out.println("List"+ this.subscribers);
		for(MyObserver<Trade> o:subscribers) {
			try {
				o.update(t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
