package com.fidelity.test.service;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.service.TradeService;

public class TradeServiceTest {

	
	private TradeService tservice;
	private PortfolioRepository portrepo;
    Portfolio portfolio;
	
	@BeforeEach
	public void setUp() {
		tservice=new TradeService();
		portfolio=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
	}
//	
//	@Test
//	void excecuteBuyOrder() throws IneligibleOrderException, Exception
//	{
//		Trade t=tservice.carryBuyTransaction(new  Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876)));
//		assertNotNull(t);
//	}
	
	
	
}
