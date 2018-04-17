package com.java.customer;

import com.java.transaction.Transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor

public class CustomerAccount { 

	private final int accountNumber;
    private double balance = 0.0;

	public void apply(Transaction transaction) {
	    double transactionAmount = transaction.getTransactionAmount();
	    	if (transactionAmount < 0) {
	     
		    	/* Negative transaction amounts represent a debit against a customer
		       account, and the customer's account balance is increased.*/
		    	
	    		debit(transactionAmount);
	    	} else {
	      
		    	/* Positive transaction amounts represent a credit against a customer
		       account, and the customer's balance is decreased.*/
		    	
	    		credit(transactionAmount);
	    	}
	  }

	  private void debit(double amount) {
	    balance += Math.abs(amount);
	  }

	  private void credit(double amount) {
	    balance -= amount;
	  }
}
