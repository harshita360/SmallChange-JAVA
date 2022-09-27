package com.fidelity.repository;


import java.util.List;

import com.fidelity.models.Client;

public abstract class ClientRepository {
	public abstract Client authenticateUser(String email,String password);
	public abstract Client getLoggedInUser();
	public abstract boolean logoutUser();
	public abstract Client getUserByEmail(String email);

}
