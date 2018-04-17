package com.java.customer;

import com.java.transaction.TransactionFile;
import com.java.transaction.Transaction;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class CustomerAccountTransaction {

	  @Getter
	  private Map<Integer, CustomerAccount> accounts = new HashMap<>();

	  /**
	   * Apply all transactions from a TransactionFile to their
	   * corresponding customer accounts.*/
	
	  public void applyTransactions(TransactionFile file) {
	    for (Transaction transaction : file.getTransactions()) {
	      applyTransaction(transaction);
	    }
	  }

	  /**
	   * Apply a single transaction to the corresponding customer account.
	  
	   */ 
	  public void applyTransaction(Transaction transaction) {
	    CustomerAccount account = accounts.get(transaction.getCustomerAccountNumber());

	    if (account == null) {
	      account = new CustomerAccount(transaction.getCustomerAccountNumber());
	    }

	    account.apply(transaction);
	    accounts.put(account.getAccountNumber(), account);
	  }

	  /**
	   * Retrieve the balance of a single customer account.
	  */
	  public double getAccountBalance(int customerAccountNumber) {
	    CustomerAccount account = accounts.get(customerAccountNumber);

	    if (account == null) {
	      throw new RuntimeException("Customer account " + customerAccountNumber + " does not exist!");
	    }

	    return account.getBalance();
	  }
}
