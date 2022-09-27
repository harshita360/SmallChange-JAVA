package com.fidelity.repository.impl;

import java.util.ArrayList;
import java.util.List;

import com.fidelity.exceptions.ClientException;
import com.fidelity.models.Client;
import com.fidelity.repository.ClientRepository;

public class ClientRepositoryImpl extends ClientRepository {

	private ArrayList<Client> client;
	public ArrayList<Client> getClient() {
		return client;
	}

	public void setClient(ArrayList<Client> client) {
		this.client = client;
	}

	private Client loggedInUser;
	
	@Override
	public Client authenticateUser(String email, String password) {
		Client client=this.getUserByEmail(email);
			if(client.getPassword()==password) {
				this.loggedInUser=client;
				return client;
			}
			throw new ClientException("Invalid email or password!!!");
	}

	@Override
	public Client getLoggedInUser() {
		return this.loggedInUser;
	}


	@Override
	public boolean logoutUser() {	
		if(this.loggedInUser==null) {
			return false;
		}
		
		this.loggedInUser=null;
		return true;
	}


	public ClientRepositoryImpl(ArrayList<Client> client) {
		super();
		setClient(client);
	}

	@Override
	public Client getUserByEmail(String email) {
		if(email=="") {
			throw new IllegalArgumentException("Invalid input for email!!!");
		}
		if(email==null) {
			throw new NullPointerException("Email cannot be null!!!");
		}
		List<Client> value = client.stream().filter(iter -> iter.getEmail().equals(email)).toList();
		if(value.size()==0) {
			throw new ClientException("No clients with entered email exists!!!");
		}
		return(value.get(0));
	}
}
