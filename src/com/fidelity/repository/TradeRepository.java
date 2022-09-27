package com.fidelity.repository;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;

public abstract class TradeRepository {
     
	public abstract Trade executeOrder(Order order);
	public abstract Trade carryBuyTransaction(Order order);
	public abstract Trade carrySellTransaction(Order order);
}
