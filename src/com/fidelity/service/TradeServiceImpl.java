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
import com.fidelity.state.TradeSubject;

public class TradeServiceImpl extends TradeService{

	PortfolioService portservice;
	private TradeSubject subject;
	
	
	
	public TradeServiceImpl(PortfolioService portservice,TradeSubject subject) throws Exception {
		//System.out.println("Subject ="+subject);
		if(portservice==null)
		{
			this.portservice=PortfolioService.getInstance();
			
		}
		else
		{
			this.portservice=portservice;
		}
		if(subject==null) {
			this.subject=TradeSubject.getInstance();
			//System.out.println("git oy subjectr");
		}else {
			this.subject=subject;
		}
		
	}


	@Override
	public Trade executeOrder(Order order) throws Exception {
		// TODO Auto-generated method stub
		
		
		if(order.getDirection().equals("B"))
		{
			
			Trade executed=carryBuyTransaction(order);
			subject.navigateUpdate(executed);
			return executed;
		}
		else {
			Trade executed=carrySellTransaction(order);
			subject.navigateUpdate(executed);
			return executed;
		}
		
		
		
		
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
