package com.fidelity.exceptions;

public class InsufficientBalanceException extends Exception{
  public InsufficientBalanceException(String str)
  {
	  super(str);
  }
}
