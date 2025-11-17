import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeMenu {
    public static Scene create(Stage stage, MemoryGame game) {

        VBox home = new VBox(25);
        home.setAlignment(Pos.CENTER);
        home.setPadding(new Insets(60));

        Image backgroundImage = new Image("file:/C:/2nd Year - 1st Sem/MemoryMatrix/src/resources/images/background.png");
        BackgroundSize backgroundSize = new BackgroundSize(
                100, 100, true, true, true, true 
        );

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, 
                backgroundSize
        );
        home.setBackground(new Background(background));

        Label subtitle = new Label("Sharpen your mind — match all the pairs!");
        subtitle.setFont(Font.font("Cambria", 18));

        VBox buttons = new VBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Cambria", 20));
        startButton.getStyleClass().add("gradient-btn");
        startButton.setOnAction(e -> {
            game.selectedLevel = game.levelManager.getDefaultLevel();
            game.setLevelDimensions(game.selectedLevel);
            stage.setScene(ThemeSelection.create(stage, game));
        });

        Button levelsButton = new Button("Levels / Difficulty");
        levelsButton.setFont(Font.font("Cambria", 18));
        levelsButton.getStyleClass().add("gradient-btn");
        levelsButton.setOnAction(e -> {
            try {
                Stage levelStage = new Stage(); 
                LevelSelection levelSelection = new LevelSelection();
                levelSelection.start(levelStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button aboutButton = new Button("About the Game / Mechanics");
        aboutButton.setFont(Font.font("Cambria", 18));
        aboutButton.getStyleClass().add("gradient-btn");
        aboutButton.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("About Memory Matrix");
            popup.initOwner(stage);
            popup.setResizable(false);

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(15));
            layout.setAlignment(Pos.TOP_LEFT);

            Label header = new Label("About Memory Matrix");
            header.setFont(Font.font("Cambria", 22));

            Label content = new Label(
                    "Memory Matrix is a single-player puzzle game that tests your memory and pattern recognition skills.\n\n" +
                            "• Flip two cards per turn to find matching pairs.\n" +
                            "• Each level has a randomized grid and increases in size and difficulty.\n" +
                            "• Score is calculated as Base Points + Time Bonus + Accuracy Bonus.\n" +
                            "• You can restart levels or reset the game at any time.\n\n" +
                            "Challenge yourself across progressive levels, from Easy (4x3) to Legendary (10x6) grids!"
            );
            content.setFont(Font.font("Cambria", 16));
            content.setWrapText(true);

            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(ev -> popup.close());
            closeBtn.setFont(Font.font("Cambria", 16));

            layout.getChildren().addAll(header, content, closeBtn);

            Scene scene = new Scene(layout, 450, 350);
            popup.setScene(scene);
            popup.show();
        });

        Button exitButton = new Button("Exit");
        exitButton.setFont(Font.font("Cambria", 16));
        exitButton.getStyleClass().add("gradient-btn");
        exitButton.setOnAction(e -> stage.close());

        buttons.getChildren().addAll(
                startButton,
                levelsButton,
                aboutButton,
                exitButton
        );

        Label scoresLabel = new Label("Scores: " + game.totalScore);
        scoresLabel.setFont(Font.font("Cambria", 16));
        scoresLabel.setPadding(new Insets(20, 0, 0, 0));
        scoresLabel.setAlignment(Pos.CENTER);

        VBox card = new VBox(20, subtitle, buttons, scoresLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(50));
        home.getChildren().add(card);

        Scene scene = new Scene(home, 900, 700);
        scene.getStylesheets().add("file:/C:/2nd Year - 1st Sem/MemoryMatrix/src/resources/style/style.css");
        return scene;

    }
}
