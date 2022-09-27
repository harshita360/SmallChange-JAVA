package com.fidelity.repository;

import java.math.BigInteger;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Client;
import com.fidelity.repository.impl.ClientReposirotyInMem;

public abstract class ClientRepository {
	
	public static ClientRepository getInstance(Implementations implementation,ResourceType type) throws Exception {
		switch(implementation) {
		case IN_MEM: return ClientReposirotyInMem.getInstance(type);
		default: throw new Exception("Invalid Choice");
		}
	}
	public abstract Client registerNewUser(Client client) throws Exception;
	public abstract Client authenticateUser(String email,String password) throws Exception;
	public abstract Client getLoggedInUser();
	public abstract boolean isUserLoggedIn();
	public abstract void logoutUser();
	public abstract void removeUserById(BigInteger clientId) throws Exception;
	public abstract Client getUserById(BigInteger clientId) throws Exception;
	public abstract Client getUserByEmail(String email) throws Exception;
}
