package com.example.diabedible.service;

import com.example.diabedible.model.Message;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.stream.Collectors;

public class MessageService {

    private static final String FILE_PATH = "data/messages.json";
    private final List<Message> messages;

    public MessageService() {
        this.messages = DataStore.loadListFromFile(
                FILE_PATH,
                new TypeReference<List<Message>>() {}
        );
    }

    public void send(Message m) {
        messages.add(m);
        DataStore.saveListToFile(messages, FILE_PATH);
    }

    public List<Message> inbox(String username) {
        return messages.stream()
                .filter(m -> m.getTo().equals(username))
                .toList();
    }
}
