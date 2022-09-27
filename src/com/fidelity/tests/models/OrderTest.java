package com.fidelity.tests.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;


public class OrderTest {

	private Order newOrder;
	private Portfolio portfolio;
	
	@BeforeEach
	public void setUp() {
		portfolio=new Portfolio("CLP1",BigInteger.valueOf(1004566),"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",null);
	}
	
	@Test
	void checkOrderCreation() throws IneligibleOrderException {
		newOrder=new Order("UUTT789","B",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		assertNotNull(newOrder);
	}
	
	@Test
	void checkForInvalidOrder()
	{
		Exception e=assertThrows(IneligibleOrderException.class, () -> {
			new Order("UUTT789","Y",portfolio.getClientId(),portfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
			
		});
		assertEquals("The order does not have a valid direction", e.getMessage());
	}
}
