package com.fidelity.service;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;

public abstract class TradeService {
	
	static private TradeService instance;
	public static TradeService getInstance() throws Exception {
		if(instance==null) {
			synchronized (PortfolioService.class) {
				if(instance==null) {
					instance=new TradeServiceImpl(null,null);
				}
			}
		}
		return instance;
	}
     
	public abstract Trade executeOrder(Order order) throws Exception;
	public abstract Trade carryBuyTransaction(Order order) throws Exception;
	public abstract Trade carrySellTransaction(Order order) throws Exception;
}
