package com.fidelity.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.repository.ClientRepository;
import com.fidelity.repository.PortfolioRepository;
import com.fidelity.state.MyObserver;
import com.fidelity.state.TradeSubject;

public class PortfolioService implements MyObserver<Trade> {
	
	static private PortfolioService instance;
	public static PortfolioService getInstance() throws Exception {
		if(instance==null) {
			synchronized (PortfolioService.class) {
				if(instance==null) {
					instance=new PortfolioService(null, null,null);
				}
			}
		}
		return instance;
	}
	
	PortfolioRepository portRepo;
	ClientRepository clientRepo;
	TradeSubject subject;
	
	public PortfolioService(PortfolioRepository portRepo, ClientRepository clientRepo,TradeSubject subject) throws Exception {
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
		this.subject.register(this);
		System.out.print("Registered with subject");
		
	}

	private PortfolioService extracted() {
		return this;
	}
	
	public Portfolio addDefaultPortfolioToClient(BigInteger clientId) throws Exception {
		extracted().clientRepo.getUserById(clientId);
		if(extracted().portRepo.getPortfoliosForAUser(clientId).size()!=0) {
			throw new Exception("Client already has a portfolio");
		}
		Portfolio portfolio=new Portfolio(UUID.randomUUID().toString(),
				clientId,"BROKERAGE",BigDecimal.valueOf(100000),"Default Portfio",null);
		return extracted().portRepo.addNewPortfolio(portfolio);
	}
	
	
	public List<Portfolio> getUserPortfolios(BigInteger clientId) throws Exception{
		extracted().clientRepo.getUserById(clientId);
		return extracted().portRepo.getPortfoliosForAUser(clientId);
		
	}
	
	public Portfolio addNewUserPortfolio(Portfolio portfolio) throws Exception {
		if( portfolio.getPortfolioId()!=null || portfolio.getHoldings().size()!=0
				|| portfolio.getPortfolioName()==null || portfolio.getBalance()==null || 
				portfolio.getPortfolioTypeName()==null || portfolio.getClientId()==null) {
			throw new Exception("Correct details not provided");
		}
		extracted().clientRepo.getUserById(portfolio.getClientId());
		portfolio.setPortfolioId(UUID.randomUUID().toString());
		return extracted().portRepo.addNewPortfolio(portfolio);
	}
	
	public Portfolio getThePortfolioGromPortfolioId(String portfolioId) throws Exception {
		Portfolio portfolio=extracted().portRepo.getPortfolioForAuserFromPortfolioId(portfolioId);
		if(portfolio==null) {
			throw new Exception("Portfolio Not Found");
		}
		return portfolio;
	}
	
	public void deleteAllUserPortfolios(BigInteger clientId) throws Exception {
		extracted().clientRepo.getUserById(clientId);
		extracted().portRepo.deletePortfolioByClientId(clientId);
	}
	
	public Portfolio getThePortfolioOfIdAndItsIntrumentOnly(String portfolioId,String instrumentId) throws Exception {
		Portfolio portfolio=extracted().portRepo.getPortfolioFromIdAndLoadOfInstrument(portfolioId, instrumentId);
		if(portfolio==null) {
			throw new Exception("Portfolio not found");
		}
		return portfolio;
	}

	@Override
	public void update(Trade t) throws Exception {
		Portfolio port=this.portRepo.getPortfolioFromIdAndLoadOfInstrument(t.getPortfolioId(), t.getInstrumentId());
		port.updateHoldings(t);
		this.portRepo.updatePortfolioFromIdAndLoadOfInstrument(port,t.getInstrumentId());
		
	}

	

}
