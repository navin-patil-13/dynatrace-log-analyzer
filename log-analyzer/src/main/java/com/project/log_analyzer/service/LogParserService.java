package com.project.log_analyzer.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.log_analyzer.model.LogEntry;

@Service
public class LogParserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("null")
    public List<LogEntry> parseLogs(MultipartFile logContent) throws IOException {
        String fileName = logContent.getOriginalFilename();

        if (fileName.endsWith(".json")) {
            return parseJsonLogs(logContent);
        } else if (fileName.endsWith(".csv")) {
            return parseCsvLogs(logContent);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName + ". Only JSON/CSV supported.");
        }
    }

    private List<LogEntry> parseJsonLogs(MultipartFile logContent) {
        try {
            return List.of(objectMapper.readValue(logContent.getInputStream(), LogEntry.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON log file: " + e.getMessage(), e);
        }
    }

    private List<LogEntry> parseCsvLogs(MultipartFile logContent) {
        try (Reader reader = new InputStreamReader(logContent.getInputStream())) {
            try (CSVParser csvParser = 
                new CSVParser(
                    reader, 
                    CSVFormat.DEFAULT.builder()
                    .setHeader("timestamp", "level", "host", "message")
                    .setSkipHeaderRecord(true).build())) {
                return csvParser.stream().map(record -> new LogEntry(
                    record.get("timestamp"),
                    record.get("message"),
                    record.get("level"),
                    record.get("host")
                ))
                .toList();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV log file: " + e.getMessage(), e);
        }
    }
}
