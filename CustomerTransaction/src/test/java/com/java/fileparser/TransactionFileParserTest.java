package com.java.fileparser;

import com.java.transaction.TransactionFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class TransactionFileParserTest {


	  @Test
	  public void corruptLinesAreSkipped() throws IOException {
	    Path path = new ClassPathResource("pending/finance_customer_transactions-corrupt_lines.csv").getFile().toPath();
	    TransactionFile file = TransactionFileParser.fromPath(path);

	    // There should be one good line and six corrupt lines
	    assertEquals(1, file.getNumAccounts());
	    assertEquals(6, file.getNumSkippedTransactions());
	  }

	  @Test
	  public void emptyFileHandledCorrectly() throws IOException {
	    Path path = new ClassPathResource("pending/finance_customer_transactions-empty.csv").getFile().toPath();
	    TransactionFile file = TransactionFileParser.fromPath(path);

	    assertEquals(0, file.getNumAccounts());
	    assertEquals(0, file.getNumSkippedTransactions());
	  }

	  @Test
	  public void headerOnlyHandledCorrectly() throws IOException {
	    Path path = new ClassPathResource("pending/finance_customer_transactions-header_only.csv").getFile().toPath();
	    TransactionFile file = TransactionFileParser.fromPath(path);

	    assertEquals(0, file.getNumAccounts());
	    assertEquals(0, file.getNumSkippedTransactions());
	  }

	  @Test
	  public void largeFile() throws IOException {
	    Path path = new ClassPathResource("pending/finance_customer_transactions-500k.csv").getFile().toPath();
	    TransactionFile file = TransactionFileParser.fromPath(path);

	    assertEquals(11000, file.getNumAccounts());
	    assertEquals(0, file.getNumSkippedTransactions());
	  }
}
