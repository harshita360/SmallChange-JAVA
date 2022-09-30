package com.fidelity.service;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Trade;
import com.fidelity.repository.ActivityRepository;
import com.fidelity.repository.ClientRepository;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.state.MyObserver;
import com.fidelity.state.TradeSubject;

public class ActivityService implements MyObserver<Trade> {
	private static ActivityService instance;
	public static ActivityService getInstance() throws Exception {
		if(instance==null) {
			synchronized(ActivityService.class) {
				if(instance==null) {
					instance=new ActivityService(null,null,null,null);
				}
			}
		}
		return instance;
	}
	
	PortfolioRepository portRepo;
	ClientRepository clientRepo;
	ActivityRepository activityRepo;
	TradeSubject subject;
	
	public ActivityService(ActivityRepository activityRepo,ClientRepository clientRepo,PortfolioRepository portRepo,TradeSubject subject) throws Exception {
		if(activityRepo==null) {
			this.activityRepo=ActivityRepository.getInstance(Implementations.IN_MEM, ResourceType.SINGLETON);
		}else {
			this.activityRepo=activityRepo;
		}
		if(portRepo==null) {
			this.portRepo=PortfolioRepository.getInstance(Implementations.IN_MEM, ResourceType.SINGLETON);
		}else {
			this.portRepo=portRepo;
		}
		if(clientRepo==null) {
			this.clientRepo=ClientRepository.getInstance(Implementations.IN_MEM, ResourceType.SINGLETON);
		}else {
			this.clientRepo=clientRepo;
		}
		if(subject==null) {
			this.subject=TradeSubject.getInstance();
		}else {
			this.subject=subject;
		}
		
	}
	
	public List<Trade> getUserActivity(BigInteger clientId) {
		if(clientId==null) {
			throw new NullPointerException("Client id cannot be null!!!");
		}
		if(clientId.compareTo(BigInteger.ZERO)<=0) {
			throw new IllegalArgumentException("Invalid value for client id!!!");
		}
		clientRepo.getUserById(clientId);
		return(activityRepo.getUserActivity(clientId));
	}
	
	public List<Trade> getPortfolioActivity(String portfolioId) {
		if(portfolioId=="") {
			throw new IllegalArgumentException("Invalid value for portfolio id!!!");
		}
		if(portfolioId==null) {
			throw new NullPointerException("Portfolio id cannot be null!!!");
		}
		portRepo.getPortfolioForAuserFromPortfolioId(portfolioId);
		return(activityRepo.getPortfolioActivity(portfolioId));
	}

	public void addActivity(Trade trade) {
		if(trade==null) {
			throw new NullPointerException("An empty trade cannot be executed!!!");
		}
		activityRepo.addActivity(trade);
	}
	
	public void deleteActivityClientId(BigInteger clientId) {
		if(clientId==null) {
			throw new NullPointerException("Client id cannot be null!!!");
		}
		if(clientId.compareTo(BigInteger.ZERO)<=0) {
			throw new IllegalArgumentException("Invalid value for client id!!!");
		}
		clientRepo.getUserById(clientId);
		activityRepo.deleteActivityClientId(clientId);
		
	}
	public void deleteActivityPortfolioId(String portfolioId) {
		if(portfolioId=="") {
			throw new IllegalArgumentException("Invalid value for portfolio id!!!");
		}
		if(portfolioId==null) {
			throw new NullPointerException("Portfolio id cannot be null!!!");
		}
		portRepo.getPortfolioForAuserFromPortfolioId(portfolioId);
		activityRepo.deleteActivityPortfolioId(portfolioId);
		
	}

	@Override
	public void update(Trade t) throws Exception {
		this.addActivity(t);
		
	}
	
	
	
	

}
