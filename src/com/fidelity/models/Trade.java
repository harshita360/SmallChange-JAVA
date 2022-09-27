package com.fidelity.models;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Trade {
	private String tradeId;
	private String direction;
	private Order order;
	private BigInteger clientId;
	private String portfolioId;
	private String instrumentId;
	private BigDecimal executionPrice;
	private BigDecimal cashvalue;
	private Integer quatity;
	
	
	
	
	public Trade(String tradeId, String direction, Order order, BigInteger clientId, String portfolioId,
			String instrumentId, BigDecimal executionPrice, BigDecimal cashvalue, Integer quatity) {
		super();
		this.tradeId = tradeId;
		this.direction = direction;
		this.order = order;
		this.clientId = clientId;
		this.portfolioId = portfolioId;
		this.instrumentId = instrumentId;
		this.executionPrice = executionPrice;
		this.cashvalue = cashvalue;
		this.quatity = quatity;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public BigInteger getClientId() {
		return clientId;
	}
	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}
	
	
	
}
