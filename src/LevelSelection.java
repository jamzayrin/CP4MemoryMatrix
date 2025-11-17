import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Set;

public class LevelSelection extends Application {

    public LevelManager levelManager = new LevelManager();

    @Override
    public void start(Stage primaryStage) {
        levelManager.loadProgress(); // load unlocked levels

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        String[] allLevels = levelManager.getAllLevels();
        Set<String> unlocked = levelManager.getUnlockedLevels();

        for (int i = 0; i < allLevels.length; i++) {
            String levelName = allLevels[i];
            Button levelButton = new Button(levelName);
            levelButton.setFont(Font.font("Cambria", 16));
            levelButton.setPrefWidth(200);

            if (unlocked.contains(levelName)) {

                levelButton.setStyle("-fx-background-color: #81c784; -fx-border-color: #2e7d32; -fx-border-width: 2;");
                int index = i;

                levelButton.setOnAction(e -> {

                    // === Confirmation dialog ===
                    javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Play Level");
                    confirm.setHeaderText("Start \"" + levelName + "\"?");
                    confirm.setContentText("Do you want to play this level now?");

                    // Show and wait for user choice
                    var result = confirm.showAndWait();

                    if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                        System.out.println(levelName + " selected!");

                        // TODO: Call MemoryGame to start this level
                        // game.loadLevel(levelName);

                        // Example progression: unlock next level after completion
                        if (index + 1 < allLevels.length) {
                            levelManager.unlockLevel(allLevels[index + 1]);
                        }

                        // Refresh the grid after unlocking
                        start(primaryStage);
                    }
                });

            } else {
                // locked level
                levelButton.setStyle("-fx-background-color: #c0c0c0; -fx-border-color: #7a7a7a; -fx-border-width: 2;");
                levelButton.setDisable(true); // clicking does nothing
            }


            grid.add(levelButton, i % 3, i / 3); // 3 columns per row
        }

        Scene scene = new Scene(grid, 650, 400);
        primaryStage.setTitle("Level Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
