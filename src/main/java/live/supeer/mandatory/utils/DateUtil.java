package live.supeer.mandatory.utils;

import live.supeer.mandatory.Mandatory;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtil {

    public static String getMonthName(int month) {
        String[] months = {"january", "february", "march", "april", "may", "june",
                "july", "august", "september", "october", "november", "december"};
        return months[month];
    }

    private static final int maxYears = 100000;

    static int dateDiff(final int type, final Calendar fromDate, final Calendar toDate, final boolean future) {
        final int year = Calendar.YEAR;

        final int fromYear = fromDate.get(year);
        final int toYear = toDate.get(year);
        if (Math.abs(fromYear - toYear) > maxYears) {
            toDate.set(year, fromYear +
                    (future ? maxYears : -maxYears));
        }

        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDateDiff(final long date) {
        final Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        final Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }

    public static String formatDateDiff(final Calendar fromDate, final Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return Mandatory.getMessage("messages.time.now");
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        // Temporary 50ms time buffer added to avoid display truncation due to code execution delays
        toDate.add(Calendar.MILLISECOND, future ? 50 : -50);
        final StringBuilder sb = new StringBuilder();
        final int[] types = new int[] {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
        final String[] names = new String[] {Mandatory.getMessage("messages.time.year"), Mandatory.getMessage("messages.time.years"), Mandatory.getMessage("messages.time.month"), Mandatory.getMessage("messages.time.months"), Mandatory.getMessage("messages.time.day"), Mandatory.getMessage("messages.time.days"), Mandatory.getMessage("messages.time.hour"), Mandatory.getMessage("messages.time.hours"), Mandatory.getMessage("messages.time.minute"), Mandatory.getMessage("messages.time.minutes"), Mandatory.getMessage("messages.time.second"), Mandatory.getMessage("messages.time.seconds")};
        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy > 2) {
                break;
            }
            final int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
            }
        }
        // Preserve correctness in the original date object by removing the extra buffer time
        toDate.add(Calendar.MILLISECOND, future ? -50 : 50);
        if (sb.isEmpty()) {
            return Mandatory.getMessage("messages.time.now");
        }
        return sb.toString().trim();
    }

    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String formatDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L); // Convert seconds to milliseconds

        Calendar now = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%02d:%02d", hour, minute);

        if (isSameDay(calendar, now)) {
            return Mandatory.getMessage("messages.days.today") + " " + time;
        } else if (isSameDay(calendar, yesterday)) {
            return Mandatory.getMessage("messages.days.yesterday") + " " + time;
        } else {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String monthName = Mandatory.getMessage("messages.months." + DateUtil.getMonthName(month).toLowerCase());
            return String.format("%d %s %d, %s", day, monthName, year, time);
        }
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
