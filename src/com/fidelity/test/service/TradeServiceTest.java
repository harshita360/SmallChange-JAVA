package com.fidelity.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.exceptions.InsufficientBalanceException;
import com.fidelity.models.Client;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;
import com.fidelity.repository.ClientRepository;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.service.PortfolioService;
import com.fidelity.service.TradeServiceImpl;
import com.fidelity.warehouse.Warehouse;

public class TradeServiceTest {

	
	private TradeServiceImpl tservice;
	private PortfolioService pservice;
	private ClientRepository userRepo;
    Portfolio portfolio;
    Client clnt1;
	
	@BeforeEach
	public void setUp() throws Exception {
		 clnt1 = new Client(new BigInteger("1004566"),"user-1","user1@gmail.com","pwd2341","653672","India",LocalDate.now(),null,null,null);
		
		userRepo=ClientRepository.getInstance(Implementations.IN_MEM , ResourceType.SINGLETON);
		
		userRepo.registerNewUser(clnt1);
		pservice=PortfolioService.getInstance();
		tservice=new TradeServiceImpl(pservice);
		portfolio=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
		
		pservice.addNewUserPortfolio(portfolio);
	}
	
	@Test
	public void correct() {
		assertTrue(true);
	}
	
	@Test
	void excecuteBuyOrder() throws IneligibleOrderException, Exception
	{
		Trade t=tservice.carryBuyTransaction(new  Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876)));
		assertNotNull(t);
	}
	

//	
//   @Test
//   void buyingStockwithLowBalance() {
//	   Portfolio portfolionew=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
//				new BigDecimal(100),"Brokerage Portfolio",null);
//	   Exception e=assertThrows(IneligibleOrderException.class, () -> {
//			tservice.carryBuyTransaction(new Order("UUTT987","B",portfolionew.getClientId(),portfolionew.getPortfolioId(),"Q34R",10,new BigDecimal(128.90)));
//		});
//		assertEquals("Portfolio not allowed to do sumit order.Might not have enough balance", e.getMessage());
//   }
//	
	
//	@Test
//	void sellingNotExistingOrder() {
//		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(5),new BigDecimal(1000.544),LocalDateTime.now(),LocalDateTime.now());
//		List<PortfolioHoldings> holdings=new ArrayList<>();
//		holdings.add(holding);
//		Portfolio portfolionew=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
//				new BigDecimal(100),"Brokerage Portfolio",holdings); 
//		
//		Exception e=assertThrows(IneligibleOrderException.class, () -> {
//			tservice.carrySellTransaction(new Order("CLP1","S",portfolionew.getClientId(),portfolionew.getPortfolioId(),"Q34R",10,new BigDecimal(128.90)));
//		});
//		assertEquals("Portfolio not allowed to do sumit order", e.getMessage());
//   
//		
//	}
	
	
	
}
