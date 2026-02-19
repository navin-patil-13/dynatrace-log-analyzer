package com.project.log_analyzer.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.project.log_analyzer.model.LogEntry;


/**
 * In-memory implementation of the LogRepository interface.
 */
@Repository
public class InMemoryLogRepository {

    private final Map<String, List<LogEntry>> storageMap = new ConcurrentHashMap<>();
    private String currentSession = "default";

    /**
     * Save log entries to the current session.
     */
    public void saveLogs(List<LogEntry> logEntries) {
        storageMap.put(currentSession, new ArrayList<>(logEntries));
    }

    /**
     * Find all log entries for the current session.
     */
    public List<LogEntry> findAll() {
        return storageMap.getOrDefault(currentSession, List.of());
    }

    /**
     * Clear all log entries for the current session.
     */
    public void clearLogs() {
        storageMap.remove(currentSession);
    }

    /**
     * Find log entries by host for the current session.
     */
    public List<LogEntry> findByHost(String host) {
        return storageMap.getOrDefault(currentSession, List.of()).stream()
                .filter(logEntry -> logEntry.getHost().toLowerCase().contains(host.toLowerCase()))
                .toList();
    }

    /**
     * Find log entries by user for the current session.
     */
    public List<LogEntry> findByHostAndLevel(String host, String level) {
        return storageMap.getOrDefault(currentSession, List.of()).stream()
                .filter(logEntry -> logEntry.getHost().toLowerCase().contains(host.toLowerCase()))
                .filter(logEntry -> logEntry.getLevel().equalsIgnoreCase(level))
                .toList();
    }

}
