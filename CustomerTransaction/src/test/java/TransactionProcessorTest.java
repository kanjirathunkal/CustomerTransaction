
import com.java.customer.CustomerAccountTransaction;
import com.java.util.FileUtils;
import org.junit.After;
import org.junit.Before; 
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TransactionProcessorTest {

	 private TransactionProcessor processor = new TransactionProcessor(new CustomerAccountTransaction());

	  private Path pendingDir;

	  @Rule
	  public TemporaryFolder reportsDir = new TemporaryFolder(Paths.get("/tmp").toFile());
	  @Rule
	  public TemporaryFolder archiveDir = new TemporaryFolder(Paths.get("/tmp").toFile());

	  @Before
	  public void before() throws IOException {
	    pendingDir = new ClassPathResource("pending").getFile().toPath();

	    processor.setPendingDir(pendingDir.toString());
	    processor.setReportsDir(reportsDir.getRoot().toString());
	    processor.setArchiveDir(archiveDir.getRoot().toString());
	  }

	  @Test
	  public void pendingFilesAreProcessedCorrectly() {
	    List<Path> pending = FileUtils.listFiles(pendingDir);

	    processor.execute();

	    // There should be the same number of reports as pending files
	    List<Path> reports = FileUtils.listFiles(reportsDir.getRoot().toPath());
	    assertEquals(pending.size(), reports.size());

	    // Ensure filenames are correct
	    for (Path path : reports) {
	      assertTrue(path.getFileName().toString().matches("finance_customer_transactions_report-.*\\.txt"));
	    }

	    // There should be the same number of archived filed as the original
	    // amount of pending files
	    List<Path> archive = FileUtils.listFiles(archiveDir.getRoot().toPath());
	    assertEquals(pending.size(), archive.size());

	    // There should be no more pending files
	    assertEquals(0, FileUtils.listFiles(pendingDir).size());
	  }

	  @After
	  public void after() {
	    // Move the archived files back to the pending directory
	    for (Path path : FileUtils.listFiles(archiveDir.getRoot().toPath())) {
	      FileUtils.moveFile(path, Paths.get(pendingDir.toString(), path.getFileName().toString()));
	    }
	  }
	
	
}
