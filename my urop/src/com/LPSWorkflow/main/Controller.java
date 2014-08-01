package com.LPSWorkflow.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private AnchorPane titleBar;
    @FXML
    private StackPane shortcutsPane;
    @FXML
    private ToggleButton helpButton;
    @FXML
    private Tab workflowTab;
    @FXML
    private Tab editTab;
    @FXML
    private TabPane canvasTabPane;
    @FXML
    private Pane mainPane;
    @FXML
    private SplitPane mainSplitPane;
    private double initialX;
    private double initialY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //manually bind width and height so that it fills the parent pane
        mainSplitPane.prefWidthProperty().bind(mainPane.widthProperty());
        mainSplitPane.prefHeightProperty().bind(mainPane.heightProperty());

        // NOTE: shortcuts must be set inside listeners, since their scenes are not set initially.
        canvasTabPane.needsLayoutProperty().addListener(v -> {
            Scene scene = canvasTabPane.getScene();
            if (scene != null) {
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN),
                        () -> canvasTabPane.getSelectionModel().select(editTab));
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN),
                        () -> canvasTabPane.getSelectionModel().select(workflowTab));
            }
        });

        editTab.selectedProperty().addListener((v, b, selected) -> editTab.getContent().setVisible(selected));

        mainPane.setOnMousePressed(mouseEvent -> {
            helpButton.setSelected(false);
        });


        // add handlers so that the window can be moved
        titleBar.setOnMousePressed((MouseEvent me) -> {
            if (me.getButton() != MouseButton.MIDDLE) {
                initialX = me.getSceneX();
                initialY = me.getSceneY();
            }
        });

        titleBar.setOnMouseDragged((MouseEvent me) -> {
            if (me.getButton() != MouseButton.MIDDLE) {
                double newX = me.getScreenX() - initialX;
                double newY = me.getScreenY() - initialY;
                Window window = titleBar.getScene().getWindow();

                if (newY < 0) { //if it goes over the screen
                    newY = 0;
                }

                window.setX(newX);
                window.setY(newY);
            }
        });
    }
}
