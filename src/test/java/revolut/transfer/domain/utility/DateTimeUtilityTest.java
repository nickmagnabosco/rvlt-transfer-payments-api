package revolut.transfer.domain.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeUtilityTest {
    @Test
    public void timestamp_toLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp t = DateTimeUtility.toTimestamp(now);
        assertThat(t).isNotNull();
        assertThat(t.toLocalDateTime()).isEqualTo(now);
    }

    @Test
    public void LocalDateTime_toTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp t = Timestamp.valueOf(now);
        LocalDateTime localDateTime = DateTimeUtility.fromTimestamp(t);
        assertThat(localDateTime).isEqualTo(now);
    }
}