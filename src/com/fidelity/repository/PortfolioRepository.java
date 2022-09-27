package com.fidelity.repository;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.Portfolio;
import com.fidelity.repository.impl.PortfolioRepositoryInMemImpl;

public abstract class PortfolioRepository {
	
	public static PortfolioRepository getInstance(Implementations implementation,ResourceType type) throws Exception {
		switch(implementation) {
		case IN_MEM: return PortfolioRepositoryInMemImpl.getInstance(type);
		default: throw new Exception("Invalid Choice");
		}
	}
	
	public abstract List<Portfolio> getPortfoliosForAUser(BigInteger clientId);
	public abstract Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId);
	public abstract Portfolio addNewPortfolio(Portfolio portfolio);
	public abstract void deletePortfolioById(String portfolioID) throws Exception;
	public abstract void deletePortfolioByClientId(BigInteger clientId) throws Exception;
	public abstract Portfolio getPortfolioFromIdAndLoadOfInstrument(String portfolioId,String instrumentId) throws Exception;
	public abstract Portfolio updatePortfolioFromIdAndLoadOfInstrument(Portfolio portfolio,String instrumentId) throws Exception;

}
