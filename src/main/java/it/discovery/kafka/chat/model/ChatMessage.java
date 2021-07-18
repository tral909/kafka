package it.discovery.kafka.chat.model;

public record ChatMessage(String sender, String text, Chat chat) {

}
