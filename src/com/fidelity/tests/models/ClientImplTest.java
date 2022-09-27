package com.fidelity.tests.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fidelity.exceptions.ClientException;
import com.fidelity.models.Client;
import com.fidelity.repository.ClientRepository;
import com.fidelity.repository.impl.ClientRepositoryImpl;

@TestInstance(Lifecycle.PER_CLASS)
public class ClientImplTest {
	ClientRepository client;
	
	@BeforeAll
	public void setUp() {
		Client clnt1 = new Client(new BigInteger("123"),"user-1","user1@gmail.com","pwd2341","653672","India",LocalDate.now(),null,null,null);
		Client clnt2= new Client(new BigInteger("321"),"user-2","user2@gmail.com","pwd3333","653672","India",LocalDate.now(),null,null,null);
		Client clnt3 = new Client(new BigInteger("456"),"user-3","user3@gmail.com","pwd4444","653672","India",LocalDate.now(),null,null,null);
		client=new ClientRepositoryImpl(new ArrayList<Client>(List.of(clnt1,clnt2,clnt3)));
	}
	
	@AfterAll
	public void tearDown() {
		client=null;
	}

	@Test
	public void authenticateUserSuccessTest() {
		Client user = client.authenticateUser("user1@gmail.com", "pwd2341");
		assertNotNull(user);
		
	}
	
	@Test
	public void authenticateUserFailureTest() {
		assertThrows(ClientException.class,()->{
			client.authenticateUser("user2@gmail.com", "pwd2341");
		});
	}
	
	@Test
	public void getLoggedInUserSuccessTest() {
		assertEquals(client.getLoggedInUser().getClientId(),new BigInteger("123"));
		
	}
	
	@Test
	public void logoutSuccessTest1() {
		client.authenticateUser("user1@gmail.com", "pwd2341");
		boolean value = client.logoutUser();
		assertTrue(value);
		
	}
	
	@Test
	public void logoutSuccessTest2() {
		boolean value = client.logoutUser();
		assertFalse(value);
		
	}
	
	@Test
	public void getUserByEmailSuccessTest() {
		Client value = client.getUserByEmail("user1@gmail.com");
		assertEquals(value.getClientId(),new BigInteger("123"));
	}
	
	@Test
	public void getUserByEmailFailureTest1() {
		assertThrows(ClientException.class,()->{
			client.getUserByEmail("user5@gmail.com");
		});
	}
	
	@Test
	public void getUserByEmailFailureTest2() {
		assertThrows(IllegalArgumentException.class,()->{
			client.getUserByEmail("");
		});
	}
	
	@Test
	public void getUserByEmailFailureTest3() {
		assertThrows(NullPointerException.class,()->{
			client.getUserByEmail(null);
		});
	}
}
