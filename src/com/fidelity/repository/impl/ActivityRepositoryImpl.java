package com.fidelity.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fidelity.exceptions.ActivityException;
import com.fidelity.models.Trade;
import com.fidelity.repository.ActivityRepository;

public class ActivityRepositoryImpl extends ActivityRepository{
	private List<Trade> activities;

	@Override
	public List<Trade> getActivities() {
		return activities;
	}

	@Override
	public void setActivities(List<Trade> activities) {
		this.activities = activities;
	}

	public ActivityRepositoryImpl() {
		activities=new ArrayList<Trade>();
	}
	
	@Override
	public void addActivity(Trade trade) {
		if(trade==null) {
			throw new NullPointerException("An empty trade cannot be executed!!!");
		}
		activities.add(trade);
		System.out.println("Trade has been successfully added to activity!!!");
		
	}

	@Override
	public List<Trade> getUserActivity(BigInteger clientId) {
		if(clientId==null) {
			throw new NullPointerException("Client id cannot be null!!!");
		}
		if(clientId.compareTo(BigInteger.ZERO)<=0) {
			throw new IllegalArgumentException("Invalid value for client id!!!");
		}
		return (activities.stream().filter( value -> value.getClientId().equals(clientId)).toList());
	}

	@Override
	public List<Trade> getPortfolioActivity(String portfolioId) {
		if(portfolioId=="") {
			throw new IllegalArgumentException("Invalid value for portfolio id!!!");
		}
		if(portfolioId==null) {
			throw new NullPointerException("Portfolio id cannot be null!!!");
		}
		return (activities.stream().filter( value -> value.getPortfolioId().equals(portfolioId)).toList());
	}

	@Override
	public void deleteActivityClientId(BigInteger clientId) {
		if(clientId==null) {
			throw new NullPointerException("Client id cannot be null!!!");
		}
		if(clientId.compareTo(BigInteger.ZERO)<=0) {
			throw new IllegalArgumentException("Invalid value for client id!!!");
		}
		if(activities.size()==0) {
			throw new ActivityException("No activity to remove!!!");
		}
		List<Trade> remList = getUserActivity(clientId);
		if(remList==null) {
			throw new ActivityException("No activity for client: "+clientId+" to remove!!!");
		}
		activities.removeAll(remList);
		System.out.println("Activity has been successfully removed!!!");
		
	}

	@Override
	public void deleteActivityPortfolioId(String portfolioId) {
		if(portfolioId=="") {
			throw new IllegalArgumentException("Invalid value for portfolio id!!!");
		}
		if(portfolioId==null) {
			throw new NullPointerException("Portfolio id cannot be null!!!");
		}
		if(activities.size()==0) {
			throw new ActivityException("No activity to remove!!!");
		}
		List<Trade> remList = getPortfolioActivity(portfolioId);
		if(remList==null) {
			throw new ActivityException("No activity for portfolio: "+portfolioId+" to remove!!!");
		}
		activities.removeAll(remList);
		System.out.println("Activity has been successfully removed!!!");
		
	}
	

}
