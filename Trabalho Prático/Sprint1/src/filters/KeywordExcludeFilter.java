package filters;

import logs.LogEntry;

import java.util.HashSet;
import java.util.Set;

public class KeywordExcludeFilter implements LogFilter {

    private final Set<String> blockedKeywords;

    public KeywordExcludeFilter(Set<String> blockedKeywords) {
        this.blockedKeywords = new HashSet<>();
        for (String keyword : blockedKeywords) {
            this.blockedKeywords.add(keyword.toLowerCase());
        }
    }

    @Override
    public String getName() {
        return "KeywordExcludeFilter";
    }

    @Override
    public boolean accept(LogEntry logEntry) {
        String message = logEntry.getMessage().toLowerCase();
        for (String keyword : blockedKeywords) {
            if (message.contains(keyword)) {
                return false;
            }
        }
        return true;
    }
}