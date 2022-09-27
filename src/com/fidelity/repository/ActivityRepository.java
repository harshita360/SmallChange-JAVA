package com.fidelity.repository;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.models.Trade;

public abstract class ActivityRepository {
	
	public abstract void addActivity(Trade trade);
	public abstract List<Trade>  getUserActivity(BigInteger userId);
	public abstract List<Trade>  getPortfolioActivity(String portfolioId);
	public abstract void deleteActivityClientId(BigInteger clientId);
	public abstract void deleteActivityPortfolioId(String portfolioId);
	public abstract List<Trade> getActivities();
	public abstract void setActivities(List<Trade> activities);
}
