

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


@Component
@EnableScheduling 
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

  private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

  private final TransactionProcessor transactionProcessor;

  @Schedules({
      @Scheduled(cron = "0 1 6  * * ?"), // 06:01am
      @Scheduled(cron = "0 1 21 * * ?"), // 21:01pm
  })
  public void executeTransactionProcessor() {
    log.info("Performing scheduled transaction processing, time is {}",
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
          .withZone(ZoneOffset.UTC)
          .format(Instant.now()));

    try {
      transactionProcessor.execute();
    } catch (RuntimeException e) {
      log.error("Error processing transactions: {}: {}", e.getMessage(), e.getCause());
    }
  }
}
