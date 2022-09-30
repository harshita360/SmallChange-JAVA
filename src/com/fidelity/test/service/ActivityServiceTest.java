package com.fidelity.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fidelity.exceptions.ActivityException;
import com.fidelity.models.Client;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.repository.ActivityRepository;
import com.fidelity.repository.ClientRepository;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.service.ActivityService;
import com.fidelity.state.TradeSubject;

public class ActivityServiceTest {
	ActivityService activityService;
	ClientRepository clientRepo;
	PortfolioRepository portRepo;
	ActivityRepository activityRepo;
	Client client;
	BigInteger clientId;
	TradeSubject subject;
	ArrayList<Trade> trades;
	
	@BeforeEach
	public void setUp() throws Exception {
		client = new Client(clientId, null, null, null, null, null, null, null, null, null);
		clientId=BigInteger.valueOf(123);
		clientRepo=mock(ClientRepository.class);
		portRepo=mock(PortfolioRepository.class);
		activityRepo=mock(ActivityRepository.class);
		subject=new TradeSubject();
		activityService=new ActivityService(activityRepo,clientRepo,portRepo,subject);
	}
	
	@AfterEach
	public void tearDown() {
		activityService = null;
	}
	
	
	@Test
	public void addActivityNullTradeFailureTest() {
		assertThrows(NullPointerException.class,()->{
			activityService.addActivity(null);
		});
	}
	
	@Test
	public void getUserActivityExistsSuccessTest() {
		trades=new ArrayList<Trade>();
		trades.add(Mockito.any(Trade.class));
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		when(activityRepo.getUserActivity(clientId)).thenReturn(trades);
		assertNotEquals(activityService.getUserActivity(clientId).size(),0);
	}
	
	@Test
	public void getUserActivityEmptySuccessTest() {
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		assertEquals(activityService.getUserActivity(new BigInteger("345")).size(),0);
	}
	
	@Test
	public void getUserActivityInvalidClientIdFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			when(clientRepo.getUserById(clientId)).thenReturn(client);
			activityService.getUserActivity(new BigInteger("-2"));
		});
	}
	
	@Test
	public void getUserActivityNullClientIdFailureTest() {
		assertThrows(NullPointerException.class,()->{
			when(clientRepo.getUserById(clientId)).thenReturn(client);
			activityService.getUserActivity(null);
		});
	}
	
	@Test
	public void getPortfolioActivityExistsSuccessTest() {
		when(portRepo.getPortfolioForAuserFromPortfolioId(Mockito.any())).thenReturn(Mockito.any(Portfolio.class));
		assertNotNull(activityService.getPortfolioActivity("port-2"));
	}
	
	@Test
	public void getPortfolioActivityEmptySuccessTest() {
		assertEquals(activityService.getPortfolioActivity("port-4").size(),0);
	}
	
	@Test
	public void getPortfolioActivityInvalidPortfolioIdFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			activityService.getPortfolioActivity("");
		});
	}
	
	@Test
	public void getPortfolioActivityNullPortfolioIdFailureTest() {
		assertThrows(NullPointerException.class,()->{
			activityService.getPortfolioActivity(null);
		});
	}
	
	
	@Test
	public void deleteActivityClientIdNullClientIdFailureTest() {
		assertThrows(NullPointerException.class,()->{
			activityService.deleteActivityClientId(null);
		});
	}
	
	@Test
	public void deleteActivityClientIdInvalidClientIdFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			activityService.deleteActivityClientId(new BigInteger("-23"));
		});
	}
	

	
	@Test
	public void deleteActivityClientIdNoActivityFailureTest() {
		when(clientRepo.getUserById(new BigInteger("457"))).thenThrow(ActivityException.class);
		assertThrows(ActivityException.class,()->{
			activityService.deleteActivityClientId(new BigInteger("457"));
		});
	}
	
	@Test
	public void deleteActivityPortfolioIdNullPortfolioIdFailureTest() {
		assertThrows(NullPointerException.class,()->{
			activityService.deleteActivityPortfolioId(null);
		});
	}
	
	@Test
	public void deleteActivityPortfolioIdInvalidPortfolioIdFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			activityService.deleteActivityPortfolioId("");
		});
	}
	
	
	@Test
	public void deleteActivityPortfolioIdNoActivityFailureTest() {
		when(portRepo.getPortfolioForAuserFromPortfolioId("port-03")).thenThrow(ActivityException.class);
		assertThrows(ActivityException.class,()->{
			activityService.deleteActivityPortfolioId("port-03");
		});
	}
	

}
