package com.fidelity.service;

import java.math.BigInteger;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Client;
import com.fidelity.repository.ClientRepository;


public class ClientService  {
	private static ClientService instance;
	public static ClientService getInstance() throws Exception {
		if(instance==null) {
			synchronized(ClientService.class) {
				if(instance==null) {
					instance=new ClientService(null);
				}
			}
		}
		return instance;
	}
	
	ClientRepository clientRepo;
	
	public ClientService(ClientRepository clientRepo) throws Exception {
		if(clientRepo==null) {
			this.clientRepo=ClientRepository.getInstance(Implementations.IN_MEM, ResourceType.SINGLETON);
		}else {
			this.clientRepo=clientRepo;
		}
	}
	
	public Client registerNewUser(Client client)  {
		if(client == null) {
			throw new NullPointerException("Null client cannot be registered!!!");
		}
		return(clientRepo.registerNewUser(client));
	}
	

	public Client authenticateUser(String email, String password)  {
		return(clientRepo.authenticateUser(email, password));
	}

	public Client getLoggedInUser() {
		return(clientRepo.getLoggedInUser());
	}

	public boolean isUserLoggedIn() {
		return(clientRepo.isUserLoggedIn());
	}

	public void removeUserById(BigInteger clientId) {
		if(clientId==null) {
			throw new NullPointerException("Client id cannot be null!!!");
		}
		if(clientId.compareTo(BigInteger.ZERO)<=0) {
			throw new IllegalArgumentException("Invalid value for client id!!!");
		}
		clientRepo.removeUserById(clientId);
	}

	public Client getUserById(BigInteger clientId)  {
		if(clientId==null) {
			throw new NullPointerException("Client id cannot be null!!!");
		}
		if(clientId.compareTo(BigInteger.ZERO)<=0) {
			throw new IllegalArgumentException("Invalid value for client id!!!");
		}
		return(clientRepo.getUserById(clientId));
	}

	public Client getUserByEmail(String email)  {
		if(email==null) {
			throw new NullPointerException("Email cannot be null!!!");
		}
		if(email=="") {
			throw new IllegalArgumentException("Invalid value for email!!!");
		}
		return(clientRepo.getUserByEmail(email));
	}

	public void logoutUser() {
		clientRepo.logoutUser();
	}

}
