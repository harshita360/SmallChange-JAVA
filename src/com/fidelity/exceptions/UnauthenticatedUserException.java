package com.fidelity.exceptions;

public class UnauthenticatedUserException extends Exception{
    
	public UnauthenticatedUserException(String str)
	{
		super(str);
	}
}
