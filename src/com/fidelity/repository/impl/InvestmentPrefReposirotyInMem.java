package com.fidelity.repository.impl;

import java.math.BigInteger;

import com.fidelity.enums.ResourceType;
import com.fidelity.models.InvestmentPreference;
import com.fidelity.repository.InvestmentPrefRepository;

public class InvestmentPrefReposirotyInMem extends InvestmentPrefRepository{

	private InvestmentPreference ip = null;
	private static InvestmentPrefReposirotyInMem instance;
	
	public static InvestmentPrefReposirotyInMem getInstance(ResourceType resource) {
		if(resource.equals(ResourceType.PROTY_TYPE)) {
			return new InvestmentPrefReposirotyInMem();
		}
		if(instance==null) {
			synchronized (ClientReposirotyInMem.class) {
				if(instance==null) {
					instance=new InvestmentPrefReposirotyInMem();;
					System.out.println("created new repo");
				}
				
			}
		}
		return instance;
	}

	@Override
	public void updatePref(InvestmentPreference pref) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InvestmentPreference getExistingPref(BigInteger clientId) {
		// TODO Auto-generated method stub
		return null;
	}

}
