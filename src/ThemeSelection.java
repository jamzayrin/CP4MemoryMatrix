import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ThemeSelection {

    public static Scene create(Stage stage, MemoryGame game) {

        VBox themeBox = new VBox(15);
        themeBox.setAlignment(Pos.CENTER);
        themeBox.setPadding(new Insets(40));

        Image backgroundImage = new Image("/images/themeselectionbg.png");
        BackgroundSize backgroundSize = new BackgroundSize(
                100, 100, true, true, true, true
        );

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, // full coverage
                backgroundSize
        );
        themeBox.setBackground(new Background(background));

        Label label = new Label("Select Theme");
        label.setFont(Font.font("Cambria", 42));

        String[] themes = {
                "Nature",
                "Space Exploration",
                "Art & Paintings"
        };

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);

        for (String theme : themes) {
            Button b = new Button(theme);
            b.setFont(Font.font("Cambria", 20));
            b.setOnAction(e -> {
                game.selectedTheme = theme;
                game.startGame(stage);
            });
            buttons.getChildren().add(b);
        }

        themeBox.getChildren().addAll(label, buttons);

        return new Scene(themeBox, 900, 700);
    }

}
