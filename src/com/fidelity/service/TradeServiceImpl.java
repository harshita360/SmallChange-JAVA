package com.fidelity.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.exceptions.InsufficientBalanceException;
import com.fidelity.exceptions.UnauthenticatedUserException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.repository.PortfolioRepository;

public class TradeServiceImpl extends TradeService{

	
	//UserService userservice;
	//PortfolioService portservice;
	PortfolioRepository portRepo;
	
	@Override
	public Trade executeOrder(Order order) throws Exception {
		// TODO Auto-generated method stub
		//checking if the order is assosiated with a valid clientId
//		if(user.getLoggedInUser!=order.getClientId())
//		{
//		  throw new UnauthenticatedUserException("This User ID is not authenticated");	
//		}
		
		if(order.getDirection().equals("B"))
		{
			return carryBuyTransaction(order);
		}
		else
			return carryBuyTransaction(order);
		
		
		
		
		
	}

	
	@Override
	public Trade carryBuyTransaction(Order order)throws Exception {
		
		Portfolio portfolio=portRepo.getPortfolioFromIdAndLoadOfInstrument(order.getPortfolioId(), order.getInstrumentId());
		
		if(!portfolio.checkBuyEligibility(order)) {
				throw new IneligibleOrderException("Portfolio not allowed to do sumit order");
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
        Portfolio portfolio=portRepo.getPortfolioFromIdAndLoadOfInstrument(order.getPortfolioId(),order.getInstrumentId());
		
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
