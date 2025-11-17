import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class MemoryGame extends Application {

    private int COLS = 4;
    private int ROWS = 4;
    private int CARD_COUNT = COLS * ROWS;
    public int totalScore = 0;  // accumulates scores from all levels


    private Card firstSelected = null;
    private Card secondSelected = null;
    private boolean busy = false;
    private int attempts = 0;
    private int matchesFound = 0;

    private Label attemptsLabel;
    private Label matchesLabel;
    private Label timeLabel;
    private Timeline timer;
    private int elapsedSeconds = 0;

    private List<String> cardValues;

    public String selectedLevel = "Classic – 4x4";
    public String selectedTheme = "Black and White Icons";

    private double cardSize = 120;

    private boolean timerStarted = false;
    private int mismatches = 0;
    public int score = 0;
    public int basePoints = 0;
    public int timeBonus = 0;
    public int accuracyBonus = 0;

    public LevelManager levelManager;

    @Override
    public void start(Stage primaryStage) {
        levelManager = new LevelManager();
        levelManager.loadProgress();
        showHomeMenu(primaryStage);
    }

    private void showHomeMenu(Stage stage) {
        stage.setScene(HomeMenu.create(stage, this));
        stage.show();
    }


    public void showThemeSelection(Stage stage) {
        stage.setScene(ThemeSelection.create(stage, this));
    }


    public void setLevelDimensions(String level) {
        switch (level) {
            case "Easy – 4x3": COLS = 4; ROWS = 3; break;
            case "Classic – 4x4": COLS = 4; ROWS = 4; break;
            case "Medium – 5x4": COLS = 5; ROWS = 4; break;
            case "Hard – 6x5": COLS = 6; ROWS = 5; break;
            case "Expert – 8x5": COLS = 8; ROWS = 5; break;
            case "Master – 8x6": COLS = 8; ROWS = 6; break;
            case "Grandmaster – 9x6": COLS = 9; ROWS = 6; break;
            case "Legendary – 10x6": COLS = 10; ROWS = 6; break;
            default: COLS = 4; ROWS = 4; break;
        }
        CARD_COUNT = COLS * ROWS;
        if (CARD_COUNT % 2 != 0) CARD_COUNT++;
        basePoints = 100 * (Arrays.asList(levelManager.getAllLevels()).indexOf(level) + 1);
    }

    public void startGame(Stage stage) {
        firstSelected = null;
        secondSelected = null;
        busy = false;
        attempts = 0;
        matchesFound = 0;
        elapsedSeconds = 0;
        timerStarted = false;
        mismatches = 0;
        score = 0;
        timeBonus = 0;
        accuracyBonus = 0;

        computeCardSize(stage);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #fafafa, #e0e0e0);");

        HBox topBar = makeTopBar(stage);
        root.setTop(topBar);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        generateCardValues();
        List<Card> cards = createCards();
        int i = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (i >= cards.size()) break;
                Card card = cards.get(i++);
                card.setPrefSize(cardSize, cardSize);
                grid.add(card, c, r);
            }
        }
        root.setCenter(grid);
        root.setBottom(makeBottomBar());

        Scene scene = new Scene(root, Math.max(900, COLS * (cardSize + 16) + 200),
                Math.max(700, ROWS * (cardSize + 16) + 200));
        stage.setScene(scene);
    }

    private void computeCardSize(Stage stage) {
        double allowedW = 700.0 / COLS * 3;
        double allowedH = 500.0 / ROWS * 3;
        cardSize = Math.max(60, Math.min(140, Math.min(allowedW, allowedH)));
    }

    private HBox makeTopBar(Stage stage) {
        HBox top = new HBox(16);
        top.setPadding(new Insets(12));
        top.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(selectedLevel + " | " + selectedTheme);
        title.setFont(Font.font("Cambria", 22));

        attemptsLabel = new Label("Attempts: 0");
        matchesLabel = new Label("Matches: 0/" + (CARD_COUNT / 2));
        timeLabel = new Label("Time: 0:00");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button home = new Button("Home");
        home.setOnAction(e -> showHomeMenu(stage));

        Button restart = new Button("Restart");
        restart.setOnAction(e -> startGame(stage));

        top.getChildren().addAll(title, attemptsLabel, matchesLabel, timeLabel, spacer, home, restart);
        return top;
    }

    private HBox makeBottomBar() {
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(12));
        bottom.setAlignment(Pos.CENTER);
        Label hint = new Label("Tip: Click a card to flip. Match all pairs!");
        bottom.getChildren().add(hint);
        return bottom;
    }

    private void generateCardValues() {
        int pairsNeeded = CARD_COUNT / 2;
        cardValues = CardValueGenerator.generate(selectedTheme, pairsNeeded);
    }

    private List<Card> createCards() {
        List<Card> cards = new ArrayList<>();
        for (String val : cardValues) cards.add(new Card(val, cardSize, this));
        return cards;
    }

    public void onCardClicked(Card card) {
        if (busy || card.isMatched() || card.isRevealed()) return;

        if (!timerStarted) startTimer();
        card.reveal();

        if (firstSelected == null) {
            firstSelected = card;
            return;
        }

        if (secondSelected == null && card != firstSelected) {
            secondSelected = card;
            busy = true;
            attempts++;
            updateStats();

            Timeline t = new Timeline(new KeyFrame(Duration.millis(600), ev -> checkMatch()));
            t.play();
        }
    }

    private void checkMatch() {
        if (firstSelected != null && secondSelected != null) {
            if (firstSelected.getValue().equals(secondSelected.getValue())) {
                firstSelected.setMatched(true);
                secondSelected.setMatched(true);
                matchesFound++;
                firstSelected.pop();
                secondSelected.pop();

                // Update the match count immediately
                updateStats();

                if (matchesFound == CARD_COUNT / 2) Platform.runLater(this::showLevelComplete);
            } else {
                mismatches++;
                firstSelected.hide();
                secondSelected.hide();
            }
        }
        firstSelected = null;
        secondSelected = null;
        busy = false;
    }


    private void showLevelComplete() {
        if (timer != null) timer.stop();

        computeScore();

        String nextLevel = getNextLevel();
        boolean hasNext = nextLevel != null;

        Stage popup = new Stage();
        popup.setTitle("Level Complete");
        popup.initOwner(timeLabel.getScene().getWindow());
        popup.setResizable(false);

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);

        Label header = new Label("Level Complete!");
        header.setFont(Font.font("Cambria", 26));

        Label result = new Label(
                "Base Points: " + basePoints + "\n" +
                        "Time Bonus: " + timeBonus + "\n" +
                        "Accuracy Bonus: " + accuracyBonus + "\n" +
                        "Total Score: " + score + "\n\n" +
                        (hasNext ? "Proceed to next level?" : "You finished the last level!")
        );
        result.setFont(Font.font("Cambria", 16));
        result.setWrapText(true);

        Button nextBtn = new Button("Next Level");
        Button restartBtn = new Button("Restart");
        Button exitBtn = new Button("Exit to Menu");

        nextBtn.setDisable(!hasNext);

        HBox buttons = new HBox(15, nextBtn, restartBtn, exitBtn);
        buttons.setAlignment(Pos.CENTER);

        // Button behavior
        nextBtn.setOnAction(e -> {
            popup.close();
            selectedLevel = nextLevel;
            setLevelDimensions(selectedLevel);
            startGame((Stage) timeLabel.getScene().getWindow());
        });

        restartBtn.setOnAction(e -> {
            popup.close();
            startGame((Stage) timeLabel.getScene().getWindow());
        });

        exitBtn.setOnAction(e -> {
            popup.close();
            showHomeMenu((Stage) timeLabel.getScene().getWindow());
        });

        layout.getChildren().addAll(header, result, buttons);

        Scene scene = new Scene(layout, 420, 330);
        popup.setScene(scene);

        popup.show();   // <- non-blocking, works perfectly
    }


    private String getNextLevel() {
        String[] all = levelManager.getAllLevels();
        for (int i = 0; i < all.length - 1; i++) if (all[i].equals(selectedLevel)) return all[i + 1];
        return null;
    }

    private void startTimer() {
        timerStarted = true;
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds++;
            int min = elapsedSeconds / 60;
            int sec = elapsedSeconds % 60;
            timeLabel.setText(String.format("Time: %d:%02d", min, sec));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateStats() {
        attemptsLabel.setText("Attempts: " + attempts);
        matchesLabel.setText("Matches: " + matchesFound + "/" + (CARD_COUNT / 2));
    }

    public void computeScore() {
        timeBonus = Math.max(0, (int)((300.0 / (elapsedSeconds + 1)) * 10));
        accuracyBonus = Math.max(0, 200 - (mismatches * 12));
        score = basePoints + timeBonus + accuracyBonus;

        totalScore += score; // add current level's score to total
    }


    public static void main(String[] args) {
        launch(args);
    }
}

