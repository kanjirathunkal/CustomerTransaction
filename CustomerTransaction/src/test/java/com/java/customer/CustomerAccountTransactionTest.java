package com.java.customer;

 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.java.transaction.Transaction;   

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class CustomerAccountTransactionTest {

	private CustomerAccountTransaction accountService = new CustomerAccountTransaction();

	  @Test
	  public void debitIncreasesBalance() {
	    accountService.applyTransaction(new Transaction(1, -100.0));
	    assertEquals(100.0, accountService.getAccountBalance(1), 0);
	  }

	  @Test
	  public void creditDecreasesBalance() {
	    accountService.applyTransaction(new Transaction(1, 100.0));
	    assertEquals(-100.0, accountService.getAccountBalance(1), 0);
	  }

	  @Test(expected = RuntimeException.class)
	  public void unknownAccountThrowsException() {
	    accountService.getAccountBalance(-1);
	  }
}
