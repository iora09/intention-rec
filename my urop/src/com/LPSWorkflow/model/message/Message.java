package com.LPSWorkflow.model.message;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Message container
 */
public class Message {
    private final StringProperty content;
    private final StringProperty typeName;
    private MessageType type;

    public Message(String content, MessageType type) {
        this.content = new SimpleStringProperty(content);
        String typeString;
        switch(type){
            case ERROR:
                typeString = "Error";
                break;
            case WARNING:
                typeString = "Warning";
                break;
            case INFO:
                typeString = "Info";
                break;
            default:
                typeString = "Unknown";
        }
        this.typeName = new SimpleStringProperty(typeString);
        this.type = type;
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public String getTypeName() {
        return typeName.get();
    }

    public void setTypeName(String typeName) {
        this.typeName.set(typeName);
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
