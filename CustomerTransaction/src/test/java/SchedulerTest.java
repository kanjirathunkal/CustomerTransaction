import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = Main.class,
    properties = {
        "processing.baseDir=/",
        "processing.pendingDir=/tmp"
    }
)

public class SchedulerTest {

	@Autowired
	  private Scheduler scheduler;

	  @Test
	  public void schedulerRuns() {
	    scheduler.executeTransactionProcessor();
	  }
}
