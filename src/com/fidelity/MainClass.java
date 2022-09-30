package com.fidelity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Client;
import com.fidelity.models.ClientIdentification;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.repository.ClientRepository;
import com.fidelity.service.PortfolioService;
import com.fidelity.service.TradeService;

public class MainClass {
	
	public static void main(String args[]) throws Exception {
		
		ClientRepository clientRepo=ClientRepository.getInstance(Implementations.IN_MEM, ResourceType.SINGLETON);
		PortfolioService portfolioService=PortfolioService.getInstance();
		TradeService tradeService=TradeService.getInstance();
		
		
		ClientIdentification i=new ClientIdentification("SSN", "765456097");
		Client client1=new Client(new BigInteger("1000"), "Nikhil V", "nikhil@gmail.com",
				"Nikil@123", "560061", "US", LocalDate.of(1999, 9, 9),new BigInteger("1000")  , new ClientIdentification[] {i}  ,"AGGRESSIVE");
		clientRepo.registerNewUser(client1);
		Portfolio returned=portfolioService.addDefaultPortfolioToClient(client1.getClientId());
		
		Portfolio gotPortfolio=portfolioService.getThePortfolioGromPortfolioId(returned.getPortfolioId());
		System.out.println();
		System.out.println(gotPortfolio);
		System.out.println("Holdings: "+gotPortfolio.getHoldings());
		
		
		Order order=new Order("UUTT789","B",gotPortfolio.getClientId(),gotPortfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		
		tradeService.executeOrder(order);
		
		gotPortfolio=portfolioService.getThePortfolioGromPortfolioId(returned.getPortfolioId());
		System.out.println();
		System.out.println(gotPortfolio);
		System.out.println("Holdings: "+gotPortfolio.getHoldings());
		
		
		// buy same second time
		Order order3=new Order("UUTT788","B",gotPortfolio.getClientId(),gotPortfolio.getPortfolioId(),"Q34R",10,new BigDecimal(129.876));
		
		tradeService.executeOrder(order3);
		
		gotPortfolio=portfolioService.getThePortfolioGromPortfolioId(returned.getPortfolioId());
		System.out.println();
		System.out.println(gotPortfolio);
		System.out.println("Holdings: "+gotPortfolio.getHoldings());
		

		Order order2=new Order("UUTT689","S",gotPortfolio.getClientId(),gotPortfolio.getPortfolioId(),"Q34R",4,new BigDecimal(130.876));
		
		tradeService.executeOrder(order2);
		
		gotPortfolio=portfolioService.getThePortfolioGromPortfolioId(returned.getPortfolioId());
		System.out.println();
		System.out.println(gotPortfolio);
		System.out.println("Holdings: "+gotPortfolio.getHoldings());
		
	}

}
