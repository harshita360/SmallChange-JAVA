package com.fidelity.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class Order {
	
	private String orderId;
	private String direction;
	private BigInteger clientId;
	private String portfolioId;
	private String instrumentId;
	private Integer quantity;
	private BigDecimal targetPrice;
	public Order(String orderId, String direction, BigInteger clientId, String portfolioId, String instrumentId,
			Integer quantity, BigDecimal targetPrice) {
		super();
		this.orderId = orderId;
		this.direction = direction;
		this.clientId = clientId;
		this.portfolioId = portfolioId;
		this.instrumentId = instrumentId;
		this.quantity = quantity;
		this.targetPrice = targetPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getTargetPrice() {
		return targetPrice;
	}
	public void setTargetPrice(BigDecimal targetPrice) {
		this.targetPrice = targetPrice;
	}
	
	
}