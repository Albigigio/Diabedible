package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private String id;
    private LocalDateTime timestamp;
    private String from;
    private String to;
    private String subject;
    private String body;
    private boolean read;

    public Message() {}

    @JsonCreator
    public Message(
            @JsonProperty("id") String id,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("subject") String subject,
            @JsonProperty("body") String body,
            @JsonProperty("read") boolean read
    ) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.read = read;
    }

    public String getTo() { return to; }
    public boolean isRead() { return read; }
}
