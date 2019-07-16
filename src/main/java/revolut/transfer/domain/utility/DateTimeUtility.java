package revolut.transfer.domain.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateTimeUtility {
    public static LocalDateTime fromTimestamp(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
