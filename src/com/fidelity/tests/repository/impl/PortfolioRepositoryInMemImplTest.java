package com.fidelity.tests.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.repository.impl.PortfolioRepositoryInMemImpl;

@DisplayName("Portfolio Repository In memory")
public class PortfolioRepositoryInMemImplTest {

	PortfolioRepository portRepo;
	BigInteger userId;
	String portfolioId;
	
	
	@BeforeEach
	public void setUp() {
		portRepo=new PortfolioRepositoryInMemImpl();
		portfolioId=UUID.randomUUID().toString();
		userId=BigInteger.valueOf(425345345);
	}
	
	@DisplayName("Should add portfolio to repository")
	@Test
	public void testAddPortfolio() {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		portRepo.addNewPortfolio(portfolio);
		
		assertEquals(portRepo.getPortfoliosForAUser(userId).size(), 1);
		assertEquals(portRepo.getPortfolioForAuserFromPortfolioId(portfolioId), portfolio);
		
	}
	
	@DisplayName("Should get portfolio and mentioned details of instrument holdings")
	@Test
	public void testRetrivePortfolio() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		portRepo.addNewPortfolio(portfolio);
		
		assertEquals(this.portRepo.getPortfolioFromIdAndLoadOfInstrument(portfolioId, "TSL").getHoldings().size(),1);
		assertEquals(this.portRepo.getPortfolioFromIdAndLoadOfInstrument(portfolioId, "HPQ").getHoldings().size(),0);
		
		
	}
	
	@Test
	@DisplayName("should return null for non existin prtfolio")
	public void testForNonExistingPortfolio() {
		assertNull(portRepo.getPortfolioForAuserFromPortfolioId("YGIUIHO"));
	}
	
	@Test
	@DisplayName("should delete existing portfolio from its id")
	public void deletePortfolioFromItsId() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		portRepo.addNewPortfolio(portfolio);
		
		this.portRepo.deletePortfolioById(portfolioId);
		assertNull(this.portRepo.getPortfolioForAuserFromPortfolioId(portfolioId));
		
	}
	
	@Test
	@DisplayName("should throw error on deleting non existing portfolio")
	public void deletePortfolioFromItsIdError() throws Exception {
		
		Exception e=assertThrows(Exception.class, ()->{
			this.portRepo.deletePortfolioById(portfolioId);
		});
		
		assertEquals(e.getMessage(),"Portfolio Not Present");
		
	}
	
	@Test
	@DisplayName("should update the portfolio of an instrument operation")
	public void updatePortfolioFromItsSingleHolding() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		portRepo.addNewPortfolio(portfolio);
		
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		PortfolioHoldings holdingUpdated=new PortfolioHoldings("TSL",BigInteger.valueOf(8),new BigDecimal(800.544),now,now);
		holdingsU.add(holdingUpdated);
		Portfolio portfolioU=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(12000),"Brokerage Portfolio",holdingsU);
		
		portRepo.updatePortfolioFromIdAndLoadOfInstrument(portfolioU,"TSL");
		assertEquals(this.portRepo.getPortfolioForAuserFromPortfolioId(portfolioId),portfolioU);
		
	}
	
	@Test
	@DisplayName("should update the portfolio after selling total quantity of an instrument")
	public void updatePortfolioFromItsSingleHoldinngSellAll() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(100),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		portRepo.addNewPortfolio(portfolio);
		
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		Portfolio portfolioU=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(11000),"Brokerage Portfolio",holdingsU);
		
		portRepo.updatePortfolioFromIdAndLoadOfInstrument(portfolioU,"TSL");
		assertEquals(this.portRepo.getPortfolioForAuserFromPortfolioId(portfolioId),portfolioU);
		
	}
	
	@Test
	@DisplayName("should update the portfolio after selling total quantity of an instrument")
	public void updatePortfolioFromItsSingleHoldinngNewBuyInstrument() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		Portfolio portfolio=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		portRepo.addNewPortfolio(portfolio);
		
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		PortfolioHoldings holdingUpdated=new PortfolioHoldings("TSL",BigInteger.valueOf(2),new BigDecimal(200),now,now);
		holdingsU.add(holdingUpdated);
		Portfolio portfolioU=new Portfolio(portfolioId,userId,"Brokerage",
				new BigDecimal(9800),"Brokerage Portfolio",holdingsU);
		
		portRepo.updatePortfolioFromIdAndLoadOfInstrument(portfolioU,"TSL");
		assertEquals(this.portRepo.getPortfolioForAuserFromPortfolioId(portfolioId),portfolioU);
		
	}
	
	@AfterEach
	public void tearDown() {
		try {
			portRepo.deletePortfolioByClientId(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
