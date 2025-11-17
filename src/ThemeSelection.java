import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ThemeSelection {

    public static Scene create(Stage stage, MemoryGame game) {

        VBox themeBox = new VBox(15);
        themeBox.setAlignment(Pos.CENTER);
        themeBox.setPadding(new Insets(40));

        Label label = new Label("Select Theme");
        label.setFont(Font.font("Cambria", 28));

        String[] themes = {
                "Black and White Icons",
                "Nature",
                "Space Exploration",
                "Holidays",
                "Art & Paintings"
        };

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);

        for (String theme : themes) {
            Button b = new Button(theme);
            b.setFont(Font.font("Cambria", 16));
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
