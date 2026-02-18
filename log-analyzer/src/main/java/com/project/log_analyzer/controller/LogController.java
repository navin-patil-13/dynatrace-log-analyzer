package com.project.log_analyzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.log_analyzer.model.LogEntry;
import com.project.log_analyzer.service.LogParserService;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    LogParserService logParserService;

    @PostMapping("/upload")
    public ResponseEntity<List<LogEntry>> uploadLogFile(@RequestParam("file") MultipartFile file) {
        try {
            List<LogEntry> logEntries = logParserService.parseLogs(file);
            return ResponseEntity.ok(logEntries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
