package com.fidelity.repository;

import java.math.BigInteger;

import com.fidelity.enums.Implementations;
import com.fidelity.enums.ResourceType;
import com.fidelity.models.InvestmentPreference;
import com.fidelity.repository.impl.ClientReposirotyInMem;
import com.fidelity.repository.impl.InvestmentPrefReposirotyInMem;

public abstract class InvestmentPrefRepository {
	public static InvestmentPrefRepository getInstance(Implementations implementation,ResourceType type) throws Exception {
		switch(implementation) {
		case IN_MEM: return InvestmentPrefReposirotyInMem.getInstance(type);
		default: throw new Exception("Invalid Choice");
		}
	}
	public abstract InvestmentPreference getExistingPref(BigInteger clientId);
	public abstract void updatePref(InvestmentPreference pref);
}
