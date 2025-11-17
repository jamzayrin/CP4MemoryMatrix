import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Card extends StackPane {

    private final Label front;
    private final Region back;
    private boolean revealed = false;
    private boolean matched = false;
    private final String value;
    private final double cardSize;
    private final MemoryGame game;

    public Card(String val, double size, MemoryGame gameRef) {
        value = val;
        cardSize = size;
        game = gameRef;

        front = new Label(val);
        front.setStyle("-fx-font-size: " + (cardSize / 2) + "px;");
        front.setVisible(false);

        back = new Region();
        back.setStyle("-fx-background-color: #64b5f6; -fx-border-color: #1565c0; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12;");
        setEffect(new DropShadow(6, Color.GRAY));

        getChildren().addAll(back, front);
        setCursor(Cursor.HAND);

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> game.onCardClicked(this));
    }

    public String getValue() { return value; }
    public boolean isRevealed() { return revealed; }
    public boolean isMatched() { return matched; }

    public void setMatched(boolean m) {
        matched = m;
        if (m) back.setStyle("-fx-background-color: #81c784; -fx-border-color: #2e7d32; -fx-border-width: 2;");
    }

    public void reveal() {
        revealed = true;
        back.setVisible(false);
        front.setVisible(true);
        animateFlip();
    }

    public void hide() {
        revealed = false;
        back.setVisible(true);
        front.setVisible(false);
        animateFlip();
    }

    public void pop() {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), this);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private void animateFlip() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        st.setFromX(0);
        st.setToX(1);
        st.play();
    }
}
