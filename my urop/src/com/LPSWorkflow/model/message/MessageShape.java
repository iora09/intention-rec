package com.LPSWorkflow.model.message;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Visual component for Messages
 */
public class MessageShape extends Region {
    private MessageData messageData;
    private Message message;

    private StackPane containerPane;
    private Label contentLabel;
    private Button closeButton;

    public MessageShape(Message msg) {
        messageData = MessageData.getInstance();
        message = msg;

        containerPane = new StackPane();
        contentLabel = new Label(msg.getContent());
        closeButton = new Button("x");

        switch(message.getType()){
            case ERROR:
                contentLabel.getStyleClass().add("message-error-label");
                break;
            case INFO:
                contentLabel.getStyleClass().add("message-info-label");
                break;
        }
        closeButton.getStyleClass().add("message-close-button");

        containerPane.getChildren().addAll(contentLabel, closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

        closeButton.setOnAction(actionEvent -> {
            // remove itself from the MessageData when closed
            messageData.getMessageList().remove(message);
        });

        this.getChildren().add(containerPane);
    }
}
