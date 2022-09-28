package com.fidelity.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fidelity.exceptions.ClientException;
import com.fidelity.models.Client;
import com.fidelity.models.ClientIdentification;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;
import com.fidelity.repository.ClientRepository;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.service.PortfolioService;
import com.fidelity.state.TradeSubject;

@DisplayName("Portfolio Service")
public class PortfolioServiceTest {
	
	PortfolioService portService;
	ClientRepository clientRepo;
	PortfolioRepository portRepo;
	Client client;
	BigInteger clientId;
	TradeSubject subject;
	
	@BeforeEach
	public void setUp() throws Exception {
		clientId=BigInteger.valueOf(35237645);
		//portRepo=mock(PortfolioRepository.class);
		clientRepo=mock(ClientRepository.class);
		
		subject=new TradeSubject();
		portService=new PortfolioService(null, clientRepo,subject);
		
		ClientIdentification identification=new ClientIdentification("SSN", "765456097");
		client=new Client(clientId, "Nikhil V", "nikhil@gmail.com",
				"Nikhil@123", "560061", "US", LocalDate.of(1999, 9, 9),new BigInteger("1000")  , new ClientIdentification[] {identification}  ,"AGGRESSIVE");
	}
	
	@DisplayName("should add default portfolio")
	@Test()
	public void shouldAddDefaultPortfolioOnCalling() throws Exception {
		
		Portfolio defaultOne=new Portfolio(null,
				clientId,"BROKERAGE",BigDecimal.valueOf(100000),"Default Portfio",new ArrayList<>());
		
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		
		Portfolio returned=portService.addDefaultPortfolioToClient(clientId);
		
		assertAll(
				
				() -> assertEquals(returned.getBalance(), defaultOne.getBalance()),
				() -> assertEquals(returned.getPortfolioTypeName(), defaultOne.getPortfolioTypeName()),
				() -> assertEquals(returned.getPortfolioName(), defaultOne.getPortfolioName()),
				() -> assertEquals(returned.getHoldings().size(), defaultOne.getHoldings().size()),
				() -> assertEquals(returned.getClientId(),defaultOne.getClientId()),
				() -> assertNotNull(returned.getPortfolioId())
				
				);
		
	}
	
	@DisplayName("should create default portfolio only once")
	@Test()
	public void shouldhandleErrorOnCallingtoAddDeafultPortfolioMoreThanOnce() throws Exception {
		
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		
		portService.addDefaultPortfolioToClient(clientId);
		
		Exception e=assertThrows(Exception.class, () -> {
			portService.addDefaultPortfolioToClient(clientId);
		});
		assertEquals(e.getMessage(),"Client already has a portfolio");
	}
	
	@DisplayName("should add new portfolio on requested")
	@Test
	public void sohouldAddNewPortfolio() throws Exception {
		
		Portfolio portfolioAddRequest=new Portfolio(null,
			clientId,"BROKERAGE V2",BigDecimal.valueOf(100000),"Custom Portfolio",null);
		//System.out.println(portfolioAddRequest);
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		Portfolio returned=this.portService.addNewUserPortfolio(portfolioAddRequest);
	
		List<Portfolio> retList=portService.getUserPortfolios(clientId);
		assertAll(
				
				() -> assertEquals(returned.getBalance(), portfolioAddRequest.getBalance()),
				() -> assertEquals(returned.getPortfolioTypeName(), portfolioAddRequest.getPortfolioTypeName()),
				() -> assertEquals(returned.getPortfolioName(), portfolioAddRequest.getPortfolioName()),
				() -> assertEquals(returned.getHoldings().size(), portfolioAddRequest.getHoldings().size()),
				() -> assertEquals(returned.getClientId(),portfolioAddRequest.getClientId()),
				() -> assertNotNull(returned.getPortfolioId()),
				() -> assertTrue(retList.contains(returned)),
				() -> assertEquals(this.portService.getThePortfolioGromPortfolioId(returned.getPortfolioId()), returned)
				);
	}
	
	@DisplayName("should throw error if the client not found on default portfilio add")
	@Test()
	public void shouldhandleClientNotFoundError() throws Exception {
		
		BigInteger clientId=BigInteger.valueOf(9545678);
		
		when(clientRepo.getUserById(clientId)).thenThrow( new ClientException("Client not found"));
		
		//portService.addDefaultPortfolioToClient(clientId);
		
		ClientException e=assertThrows(ClientException.class, () -> {
			portService.addDefaultPortfolioToClient(clientId);
		});
		assertEquals(e.getMessage(),"Client not found");
	}
	
	@DisplayName("should throw error if the client not found on portfilio add")
	@Test()
	public void shouldhandleClientNotFoundErrorOnAddPortfolio() throws Exception {
		
		BigInteger clientId=BigInteger.valueOf(9545678);
		
		when(clientRepo.getUserById(clientId)).thenThrow( new ClientException("Client not found"));
		Portfolio portfolioAddRequest=new Portfolio(null,
				clientId,"BROKERAGE V2",BigDecimal.valueOf(100000),"Custom Portfolio",null);
		
		//portService.addDefaultPortfolioToClient(clientId);
		
		ClientException e=assertThrows(ClientException.class, () -> {
			this.portService.addNewUserPortfolio(portfolioAddRequest);
		});
		assertEquals(e.getMessage(),"Client not found");
	}
	
	@DisplayName("should throw error if the client not sent enough details")
	@Test()
	public void shouldhandleErrorOnAddPortfolioNotEnoughDetailsProvided() throws Exception {
		
		
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		Portfolio portfolioAddRequest=new Portfolio(null,
				null,"TYPE",BigDecimal.valueOf(100000),"Custom Portfolio",null);
		
		//portService.addDefaultPortfolioToClient(clientId);
		
		Exception e=assertThrows(Exception.class, () -> {
			this.portService.addNewUserPortfolio(portfolioAddRequest);
		});
		assertEquals(e.getMessage(),"Correct details not provided");
	}
	
	@DisplayName("should throw error on portfolioNotFound")
	@Test
	public void shouldHandlePortfolioNotFound(){
		Exception e=assertThrows(Exception.class, () -> {
			this.portService.getThePortfolioGromPortfolioId(UUID.randomUUID().toString());
		});
		assertEquals(e.getMessage(),"Portfolio Not Found");
	}
	
	@Test
	@DisplayName("should update the portfolio of an instrument operation")
	public void updatePortfolioFromItsSingleHolding() throws Exception {
		// adding new portfolio
		LocalDateTime now=LocalDateTime.now();
		
		//Portfolio defaultOne=new Portfolio(null,
		//		clientId,"BROKERAGE",BigDecimal.valueOf(100000),"Default Portfio",new ArrayList<>());
		
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		
		Portfolio returned=portService.addDefaultPortfolioToClient(clientId);
		
		// buying an instrument should update portfolio on subject emit
		
		Order order=new Order("UUTT789","B",clientId,returned.getPortfolioId(),"TSL",2,new BigDecimal(100));
		Trade trade=new Trade("TRADE_ID-1",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), null,order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).add(new BigDecimal(3)) );
		
		// navigating update about trade to subscribers
		subject.navigateUpdate(trade);
		
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		PortfolioHoldings holdingUpdated=new PortfolioHoldings("TSL",BigInteger.valueOf(2),new BigDecimal(203),now,now);
		holdingsU.add(holdingUpdated);
		Portfolio portfolioU=new Portfolio(returned.getPortfolioId(),clientId,"BROKERAGE",
				new BigDecimal(99797),"Default Portfio",holdingsU);
		
		assertEquals(this.portService.getThePortfolioGromPortfolioId(returned.getPortfolioId()),portfolioU);
		
		
	}
	
	@AfterEach
	public void tareDown() throws Exception {
		this.portService.deleteAllUserPortfolios(clientId);
	}

}
