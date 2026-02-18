package com.project.log_analyzer.model;

import javax.persistence.Entity;

@lombok.Data
@Entity
public class LogEntry {

    private String timestamp;

    private String message;

    private String level;

    private String host;

        public LogEntry() {
        }

        public LogEntry(String timestamp, String message, String level, String host) {
            this.timestamp = timestamp;
            this.message = message;
            this.level = level;
            this.host = host;
        }
}