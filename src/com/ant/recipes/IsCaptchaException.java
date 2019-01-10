package com.ant.recipes;

public class IsCaptchaException extends Exception {
	
  /**
	 * 
	 */
	private static final long	serialVersionUID	= -7668188324990855576L;

	public IsCaptchaException() {
	}
	
	public IsCaptchaException(String message) {
    super(message);
	}
	
	public IsCaptchaException(String message, Throwable throwable) {
	    super(message, throwable);
	}
}
