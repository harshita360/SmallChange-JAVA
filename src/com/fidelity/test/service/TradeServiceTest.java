package com.fidelity.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.models.Client;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;
import com.fidelity.repository.ClientRepository;
import com.fidelity.service.PortfolioService;
import com.fidelity.service.TradeServiceImpl;

public class TradeServiceTest {

	
	private TradeServiceImpl tservice;
	private PortfolioService pservice;
	private ClientRepository userRepo;
    Portfolio portfolio;
    Client clnt1;
	
	@BeforeEach
	public void setUp() throws Exception {
		 clnt1 = new Client(new BigInteger("1004566"),"user-1","user981@gmail.com","pwd2341","653672","India",LocalDate.now(),null,null,null);
		
		userRepo=ClientRepository.getInstance(Implementations.IN_MEM , ResourceType.SINGLETON);
		
		userRepo.registerNewUser(clnt1);
		pservice=PortfolioService.getInstance();
		tservice=new TradeServiceImpl(pservice,null);
		portfolio=new Portfolio(null,BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
		
		pservice.addNewUserPortfolio(portfolio);
	}
	
	@AfterEach
	public void tearDown() {
		userRepo.removeUserById(BigInteger.valueOf(1004566));
	}
	@Test
	void excecuteBuyOrder() throws IneligibleOrderException, Exception
	{
		Trade t=tservice.carryBuyTransaction(new  Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876)));
		assertNotNull(t);
	}
	

	
   @Test
   void buyingStockwithLowBalance() throws Exception {
	   Portfolio portfolio1=new Portfolio(null,BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(100),"Brokerage Portfolio",null);
	   Portfolio portfolionew=pservice.addNewUserPortfolio(portfolio1);
	   Exception e=assertThrows(IneligibleOrderException.class, () -> {
			tservice.carryBuyTransaction(new Order("UUTT987","B",portfolionew.getClientId(),portfolionew.getPortfolioId(),"Q34R",10,new BigDecimal(128.90)));
		});
		assertEquals("Portfolio not allowed to do sumit order.Might not have enough balance", e.getMessage());
   }
	
	
	@Test
	void sellingNotExistingOrder() throws Exception {
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(5),new BigDecimal(1000.544),LocalDateTime.now(),LocalDateTime.now());
		List<PortfolioHoldings> holdings=new ArrayList<>();
		holdings.add(holding);
		Portfolio portfolionew=new Portfolio(null,BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(100),"Brokerage Portfolio",null);
		pservice.addNewUserPortfolio(portfolionew);
		
		Exception e=assertThrows(IneligibleOrderException.class, () -> {
			tservice.carrySellTransaction(new Order("CLP1","S",portfolionew.getClientId(),portfolionew.getPortfolioId(),"Q34R",10,new BigDecimal(128.90)));
		});
		assertEquals("Portfolio not allowed to do sumit order", e.getMessage());
		
	}
	
	@Test
	void excecuteSellOrder() throws Exception {
		portfolio=new Portfolio(null,BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
		pservice.addNewUserPortfolio(portfolio);
		
		Trade t=tservice.carryBuyTransaction(new  Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876)));
		pservice.update(t);
		Trade tsell=tservice.carrySellTransaction(new  Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",2,new BigDecimal(129.876)));
		assertNotNull(tsell);	

		
	}
	
	
	
}
