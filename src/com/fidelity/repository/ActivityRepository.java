package com.fidelity.repository;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Trade;
import com.fidelity.repository.impl.ActivityRepositoryImpl;
import com.fidelity.repository.impl.ClientReposirotyInMem;

public abstract class ActivityRepository {
	
	public static ActivityRepository getInstance(Implementations implementation,ResourceType type) throws Exception {
		switch(implementation) {
		case IN_MEM: return ActivityRepositoryImpl.getInstance(type);
		default: throw new Exception("Invalid Choice");
		}
	}
	public abstract void addActivity(Trade trade);
	public abstract List<Trade>  getUserActivity(BigInteger userId);
	public abstract List<Trade>  getPortfolioActivity(String portfolioId);
	public abstract void deleteActivityClientId(BigInteger clientId);
	public abstract void deleteActivityPortfolioId(String portfolioId);
	public abstract List<Trade> getActivities();
	public abstract void setActivities(List<Trade> activities);
}
