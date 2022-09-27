package com.fidelity.tests.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;

@DisplayName("Portfolio model Test")
public class PortfolioTest {

	
	Portfolio portfolio;
	
	@BeforeEach
	public void setUp() {
		portfolio=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
	}
	
	@Test
	@DisplayName("Should assert equal for same user and no change in holdings")
	public void testEquality() {
		Portfolio portfolio2=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
		assertEquals(portfolio, portfolio2);
	}
	
	@Test
	@DisplayName("Should assert not equal for same user and change in holdings")
	public void testNotEquality() {
		LocalDateTime now=LocalDateTime.now();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		List<PortfolioHoldings> holdings=new ArrayList<>();
		holdings.add(holding);
		Portfolio portfolio2=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		assertNotEquals(portfolio, portfolio2);
	}
	
	@Test
	@DisplayName("Should update the current balance and holdings on buy instrument order")
	public void testUpdateBuyOrder() throws Exception {
	
		// creating the pre requists to carry this test
		// the order and trade object
		LocalDateTime now=LocalDateTime.now();
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade trade=new Trade("URC-UYUTF-IJN-O",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), now,10,
				new BigDecimal(129.876).multiply(new BigDecimal(10)),new BigDecimal(129.876).multiply(new BigDecimal(10)).add(new BigDecimal(3)) );
		
		portfolio.updateHoldings(trade);
		assertEquals(portfolio.getHoldings().size(), 1);
		assertEquals(portfolio.getBalance().setScale(2,RoundingMode.HALF_UP),new BigDecimal(8698.24).setScale(2,RoundingMode.HALF_UP));
	
	}
	
	@Test
	@DisplayName("Should update the current balance and holdings on sell instrument order")
	public void testUpdateSellOrder() throws Exception {
	
		// creating the pre requists to carry this test
		// the order and trade object
		// buying to seel the data
		LocalDateTime now=LocalDateTime.now();
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade trade=new Trade("URC-UYUTF-IJN-O",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), now,10,
				new BigDecimal(129.876).multiply(new BigDecimal(10)),new BigDecimal(129.876).multiply(new BigDecimal(10)).add(new BigDecimal(3)) );
		
		portfolio.updateHoldings(trade);
		//assertEquals(portfolio.getHoldings().size(), 1);
		//assertEquals(portfolio.getBalance().setScale(2,RoundingMode.HALF_UP),new BigDecimal(8698.24).setScale(2,RoundingMode.HALF_UP));
		
		Order orderS=new Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade tradeS=new Trade("URC-UYUTF-IJN-O",orderS.getDirection(),orderS,orderS.getClientId(),
				orderS.getPortfolioId(),orderS.getInstrumentId(), now,5,
				new BigDecimal(129.876).multiply(new BigDecimal(5)),new BigDecimal(129.876).multiply(new BigDecimal(5)).subtract(new BigDecimal(3)) );
		
		portfolio.updateHoldings(tradeS);
		assertEquals(portfolio.getHoldings().size(), 1);
		assertEquals(portfolio.getBalance().setScale(2,RoundingMode.HALF_UP),new BigDecimal(9344.62).setScale(2,RoundingMode.HALF_UP));
	
	}
	
	@Test
	@DisplayName("Should handle request where the user does not hold quantity to sell")
	public void testUpdateSellOrderBadRequest() throws Exception {
	
		// creating the pre requists to carry this test
		// the order and trade object
		LocalDateTime now=LocalDateTime.now();
		Order order=new Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade trade=new Trade("URC-UYUTF-IJN-O",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), now,10,
				new BigDecimal(12900.876).multiply(new BigDecimal(10)),new BigDecimal(12900.876).multiply(new BigDecimal(10)).add(new BigDecimal(3)) );
		
		Exception e=assertThrows(Exception.class, ()->{
			portfolio.updateHoldings(trade);
		});
		assertEquals(e.getMessage(), "Bad Request");
	
	}
	
	@Test
	@DisplayName("Should handle request where the user does not hold enough balance")
	public void testUpdateBuyOrderBadRequestForLessBalance() throws Exception {
	
		// creating the pre requists to carry this test
		// the order and trade object
		LocalDateTime now=LocalDateTime.now();
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade trade=new Trade("URC-UYUTF-IJN-O",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), now,10,
				new BigDecimal(1290.876).multiply(new BigDecimal(10)),new BigDecimal(1290.876).multiply(new BigDecimal(10)).add(new BigDecimal(3)) );
		
		Exception e=assertThrows(Exception.class, ()->{
			portfolio.updateHoldings(trade);
		});
		assertEquals(e.getMessage(), "Not enough balance in account");
	
	}
	
	@Test
	@DisplayName("Should update the current balance and holdings on sell instrument order on selling full capacity")
	public void testUpdateSellOrderWholeQuantity() throws Exception {
	
		// creating the pre requists to carry this test
		// the order and trade object
		// buying to seel the data
		LocalDateTime now=LocalDateTime.now();
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade trade=new Trade("URC-UYUTF-IJN-O",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), now,10,
				new BigDecimal(129.876).multiply(new BigDecimal(10)),new BigDecimal(129.876).multiply(new BigDecimal(10)).add(new BigDecimal(3)) );
		
		portfolio.updateHoldings(trade);
		assertEquals(portfolio.getHoldings().size(), 1);
		assertEquals(portfolio.getBalance().setScale(2,RoundingMode.HALF_UP),new BigDecimal(8698.24).setScale(2,RoundingMode.HALF_UP));
		
		Order orderS=new Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade tradeS=new Trade("URC-UYUTF-IJN-O",orderS.getDirection(),orderS,orderS.getClientId(),
				orderS.getPortfolioId(),orderS.getInstrumentId(), now,10,
				new BigDecimal(132).multiply(new BigDecimal(10)),new BigDecimal(132).multiply(new BigDecimal(10)).subtract(new BigDecimal(3)) );
		System.out.println(portfolio.getHoldings());
		portfolio.updateHoldings(tradeS);
		assertEquals(portfolio.getHoldings().size(), 0);
		assertEquals(portfolio.getBalance().setScale(2,RoundingMode.HALF_UP),new BigDecimal(10015.24).setScale(2,RoundingMode.HALF_UP));
	
	}
	
	@Test
	@DisplayName("Should handle when selling with more quantity")
	public void testUpdateSellOrderMoreQuantity() throws Exception {
	
		// creating the pre requists to carry this test
		// the order and trade object
		// buying to seel the data
		LocalDateTime now=LocalDateTime.now();
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade trade=new Trade("URC-UYUTF-IJN-O",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), now,10,
				new BigDecimal(129.876).multiply(new BigDecimal(10)),new BigDecimal(129.876).multiply(new BigDecimal(10)).add(new BigDecimal(3)) );
		
		portfolio.updateHoldings(trade);
		assertEquals(portfolio.getHoldings().size(), 1);
		assertEquals(portfolio.getBalance().setScale(2,RoundingMode.HALF_UP),new BigDecimal(8698.24).setScale(2,RoundingMode.HALF_UP));
		
		Order orderS=new Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		Trade tradeS=new Trade("URC-UYUTF-IJN-O",orderS.getDirection(),orderS,orderS.getClientId(),
				orderS.getPortfolioId(),orderS.getInstrumentId(), now,12,
				new BigDecimal(132).multiply(new BigDecimal(12)),new BigDecimal(132).multiply(new BigDecimal(12)).subtract(new BigDecimal(3)) );
		//System.out.println(portfolio.getHoldings());
		Exception e=assertThrows(Exception.class, ()->{
			portfolio.updateHoldings(tradeS);
		});
		assertEquals(e.getMessage(), "Not enough holdings");
	}
	
	@DisplayName("Should return true for buy eligibility")
	@Test
	public void testForBuyEligibility1() {
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(10.876));
		assertTrue(portfolio.checkBuyEligibility(order));
	}
	
	@DisplayName("Should return false for buy eligibility")
	@Test
	public void testForBuyEligibility2() {
		Order order=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",100,new BigDecimal(128.876));
		assertFalse(portfolio.checkBuyEligibility(order));
	}
	
	@DisplayName("Should return true for sell eligibility")
	@Test
	public void testForSelEligibility1() {
		LocalDateTime now=LocalDateTime.now();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		List<PortfolioHoldings> holdings=new ArrayList<>();
		holdings.add(holding);
		Portfolio portfolio2=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		Order order=new Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"TSL",10,new BigDecimal(10.876));
		assertTrue(portfolio2.checkBuyEligibility(order));
	}
	
	@DisplayName("Should return false for sell eligibility")
	@Test
	public void testForSelEligibility2() {
		LocalDateTime now=LocalDateTime.now();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		List<PortfolioHoldings> holdings=new ArrayList<>();
		holdings.add(holding);
		Portfolio portfolio2=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		Order order=new Order("UUTT789","S",portfolio.getClientId(),portfolio.getPortfolioId(),"TSL",100,new BigDecimal(10.876));
		assertFalse(portfolio2.checkSellEligibility(order));
	}
	
}
