package com.fidelity.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fidelity.enums.ResourceType;
import com.fidelity.exceptions.ClientException;
import com.fidelity.models.Client;
import com.fidelity.repository.ClientRepository;

public class ClientReposirotyInMem extends ClientRepository{

	private List<Client> clients = new ArrayList<>();
	private Client loggedInUser = null; //For the Current Active User
	private static ClientReposirotyInMem instance;
	
	public static ClientReposirotyInMem getInstance(ResourceType resource) {
		if(resource.equals(ResourceType.PROTY_TYPE)) {
			return new ClientReposirotyInMem();
		}
		if(instance==null) {
			synchronized (ClientReposirotyInMem.class) {
				if(instance==null) {
					instance=new ClientReposirotyInMem();
					System.out.println("created new repo");
				}
				
			}
		}
		return instance;
	}
	
	@Override
	public Client registerNewUser(Client client)  {
		// TODO Auto-generated method stub
		if(this.getUserByEmail(client.getEmail())!=null) {
			throw new ClientException("Already user exist with this email");
		}
		this.clients.add(client);
		return client;
	}

	//Authenticate User Basically sets the Current Active Session to the LoggedInUser
	@Override
	public Client authenticateUser(String email, String password) {
		// TODO Auto-generated method stub
		Client client=this.getUserByEmail(email);
		//System.out.println(user);
		if(client!=null) {
			if(client.CheckPassword(password)) {
				this.loggedInUser=client;
				return client;
			}
		}
		return null;
	}

	@Override
	public Client getLoggedInUser() {
		// TODO Auto-generated method stub
		return this.loggedInUser;
	}

	@Override
	public boolean isUserLoggedIn() {
		// TODO Auto-generated method stub
		if(this.loggedInUser!=null) {
			return true;
		}
		return false;
	}

	@Override
	public void removeUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		Client client = this.getUserById(clientId);
		this.clients.remove(client);
		
	}

	@Override
	public Client getUserById(BigInteger clientId)  {
		// TODO Auto-generated method stub
		Client client = null;
		List<Client> filtered=this.clients.stream().filter( c-> c.getClientId().equals(clientId)).toList();
		if(filtered.size()==1) {
			client=filtered.get(0);
		}
		if(client==null) {
			throw new ClientException("User with requested client id not found");
		}
		return client;
	}

	@Override
	public Client getUserByEmail(String email)  {
		// TODO Auto-generated method stub
		Client client = null;
		List<Client> filtered = this.clients.stream().filter(c->c.getEmail().equals(email)).toList();
		if(filtered.size()==1) {
			client=filtered.get(0);
		}
		if(client==null) {
			throw new ClientException("User with requested email not found");
		}
		return client;
	}

	@Override
	public void logoutUser() {
		// TODO Auto-generated method stub
		this.loggedInUser=null;
	}

		

}
