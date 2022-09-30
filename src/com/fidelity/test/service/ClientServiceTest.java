package com.fidelity.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fidelity.models.Client;
import com.fidelity.repository.ClientRepository;
import com.fidelity.service.ClientService;

public class ClientServiceTest {
	ClientService clientService;
	ClientRepository clientRepo;
	Client client;
	BigInteger clientId;
	
	@BeforeEach
	public void setUp() throws Exception {
		clientId=BigInteger.valueOf(123);
		clientRepo=mock(ClientRepository.class);
		clientService=new ClientService(clientRepo);
		client=new Client(new BigInteger("123"),"user-1","user1@gmail.com","pwd2341","653672","India",LocalDate.now(),null,null,null);
	}
	
	@AfterEach
	public void tearDown() {
		clientService = null;
	}
	
	@Test
	public void registerUserSuccessTest() {
		when(clientRepo.registerNewUser(client)).thenReturn(client);
		assertEquals(clientService.registerNewUser(client),client);
		
	}
	
	@Test
	public void registerUserNullClientFailureTest() {
		assertThrows(NullPointerException.class,()->{
			clientService.registerNewUser(null);
		});
		
	}
	
	@Test
	public void authenticateUserSuccessTest() {
		when(clientRepo.authenticateUser(Mockito.any(),Mockito.any())).thenReturn(client);
		assertEquals(clientService.authenticateUser("user1@gmail.com","pwd2341"),client);
		
	}
	
	@Test
	public void getUserLoggedInSuccessTest() {
		when(clientRepo.getLoggedInUser()).thenReturn(client);
		assertEquals(clientService.getLoggedInUser(),client);
	}
	
	@Test
	public void isUserLoggedInTrueSuccessTest() {
		when(clientRepo.isUserLoggedIn()).thenReturn(true);
		assertTrue(clientService.isUserLoggedIn());
	}
	
	@Test
	public void isUserLoggedInFalseSuccessTest() {
		when(clientRepo.isUserLoggedIn()).thenReturn(false);
		assertFalse(clientService.isUserLoggedIn());
	}
	
	@Test
	public void removeUserByIdNullClientIdFailureTest() {
		assertThrows(NullPointerException.class,()->{
			clientService.removeUserById(null);
		});
	}
	
	@Test
	public void removeUserByIdInvalidClientIdFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			clientService.removeUserById(new BigInteger("-2"));
		});
	}
	
	@Test
	public void getUserByIdSuccessTest() {
		when(clientRepo.getUserById(clientId)).thenReturn(client);
		assertEquals(clientService.getUserById(clientId),client);
	}
	
	@Test
	public void getUserByIdNullClientFailureTest() {
		assertThrows(NullPointerException.class,()->{
			clientService.getUserById(null);
		});
	}
	
	@Test
	public void getUserByIdInvalidClientIdFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			clientService.getUserById(new BigInteger("-2"));
		});
	}
	
	@Test
	public void getUserByEmailSuccessTest() {
		when(clientRepo.getUserByEmail(Mockito.any())).thenReturn(client);
		assertEquals(clientService.getUserByEmail("user1@gmail.com"),client);
	}
	
	@Test
	public void getUserByEmailNullEmailFailureTest() {
		assertThrows(NullPointerException.class,()->{
			clientService.getUserByEmail(null);
		});
	}
	
	@Test
	public void getUserByEmailInvalidEmailFailureTest() {
		assertThrows(IllegalArgumentException.class,()->{
			clientService.getUserByEmail("");
		});
	}

}
