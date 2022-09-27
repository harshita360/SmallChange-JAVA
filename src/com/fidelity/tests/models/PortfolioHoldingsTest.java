package com.fidelity.tests.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fidelity.models.PortfolioHoldings;

@DisplayName("Portfolio Holdings Test")
public class PortfolioHoldingsTest {
	
	PortfolioHoldings holding;
	LocalDateTime now;
	
	@BeforeEach
	public void setUp() {
		now=LocalDateTime.now();
		holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
	}
	
	@DisplayName("Should assert for two same holdings")
	@Test
	public void testForEqual() {
		PortfolioHoldings holding2=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		assertEquals(holding, holding2);
	}
	
	@DisplayName("Should assert not equal for two different holdings")
	@Test
	public void testForNotEqual() {
		PortfolioHoldings holding2=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1001.544),now,now);
		assertNotEquals(holding, holding2);
	}

}
