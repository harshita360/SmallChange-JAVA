package com.fidelity.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.exceptions.InsufficientBalanceException;
import com.fidelity.exceptions.UnauthenticatedUserException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.repository.PortfolioRepository;

public class TradeServiceImpl extends TradeService{

	PortfolioService portservice;
	
	
	
	public TradeServiceImpl(PortfolioService portservice) throws Exception {
		super();
		if(portservice==null)
		{
			this.portservice=PortfolioService.getInstance();
			
		}
		else
		{
			this.portservice=portservice;
		}
		
	}


	@Override
	public Trade executeOrder(Order order) throws Exception {
		// TODO Auto-generated method stub
		
		
		if(order.getDirection().equals("B"))
		{
			return carryBuyTransaction(order);
		}
		else
			return carryBuyTransaction(order);
		
		
		
		
		
	}

	
	@Override
	public Trade carryBuyTransaction(Order order)throws Exception {
		
		Portfolio portfolio=portservice.getThePortfolioOfIdAndItsIntrumentOnly(order.getPortfolioId(), order.getInstrumentId());
		
		if(!portfolio.checkBuyEligibility(order)) {
				throw new IneligibleOrderException("Portfolio not allowed to do sumit order.Might not have enough balance");
			}
		
		
		BigDecimal executionPrice=order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
		BigDecimal cashValue=executionPrice.add(BigDecimal.valueOf(3));
		
		if(portfolio.getBalance().compareTo(cashValue)<0) {
			throw new InsufficientBalanceException("Not Enough balance to execute order");
		}
		Trade trade=new Trade(UUID.randomUUID().toString(),order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(),LocalDateTime.now(),order.getQuantity(),executionPrice,cashValue);
		
		
		return trade;
	}

	@Override
	public Trade carrySellTransaction(Order order) throws Exception {
		// TODO Auto-generated method stub
        Portfolio portfolio=portservice.getThePortfolioOfIdAndItsIntrumentOnly(order.getPortfolioId(),order.getInstrumentId());
		
		if(!portfolio.checkSellEligibility(order)) {
				throw new IneligibleOrderException("Portfolio not allowed to do sumit order");
			}
		
		
		BigDecimal executionPrice=order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
		BigDecimal cashValue=executionPrice.add(BigDecimal.valueOf(3));
		
		Trade trade=new Trade(UUID.randomUUID().toString(),order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(),LocalDateTime.now(),order.getQuantity(),executionPrice,cashValue);	
		return trade; 
		
	}

	
	
   
}
