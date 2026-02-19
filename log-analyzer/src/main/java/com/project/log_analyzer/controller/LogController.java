package com.project.log_analyzer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.log_analyzer.model.LogEntry;
import com.project.log_analyzer.repository.InMemoryLogRepository;
import com.project.log_analyzer.service.LogParserService;

/* LogController */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    LogParserService logParserService;

    @Autowired
    InMemoryLogRepository logRepository;

    /**
     * Get the index page.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Upload a log file and parse its contents.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadLogFile(@RequestParam("file") MultipartFile file) {
        try {
            List<LogEntry> logEntries = logParserService.parseLogs(file);
            logRepository.saveLogs(logEntries);
            return ResponseEntity.ok(Map.of("status", "success", "count", logEntries.size()));
        } catch (Exception e) {
            return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    /**
     * Get all log entries.
     */
    @GetMapping()
    public ResponseEntity<List<LogEntry>> getAllLogs() {
        List<LogEntry> logEntries = logRepository.findAll();
        return ResponseEntity.ok(logEntries);
    }

    /**
     * Search log entries by host and level.
     */
    @GetMapping(value = "/search", params = {"host", "level"})
    public ResponseEntity<List<LogEntry>> searchLogs(@RequestParam String host, @RequestParam String level) {
        List<LogEntry> logEntries = logRepository.findByHostAndLevel(host, level);
        return ResponseEntity.ok(logEntries);
    }

    /**
     * Search log entries by host.
     */
    @GetMapping(value = "/server", params = {"host"})
    public ResponseEntity<List<LogEntry>> searchByHost(@RequestParam String host) {
        List<LogEntry> logEntries = logRepository.findByHost(host);
        return ResponseEntity.ok(logEntries);
    }
}