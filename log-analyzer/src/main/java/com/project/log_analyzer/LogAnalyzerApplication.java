package com.project.log_analyzer;

import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    OpenAiAutoConfiguration.class
})
public class LogAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogAnalyzerApplication.class, args);
	}

}
