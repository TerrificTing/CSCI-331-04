package tiles.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tiles.model.Direction;
import tiles.model.Observer;
import tiles.model.TilesModel;

public class TilesGUI extends Application implements Observer<TilesModel, String> {
    private TilesModel model;
    private Label[][] grid = new Label[4][4];
    private Label moves = new Label();
    private Label status = new Label();
    private Label score = new Label();
    @Override
    public void init() {
        // TODO
        this.model = new TilesModel();
        this.model.addObserver(this);
        makeGrid();
        this.model.ready();
    }

    /**
     * Construct the layout for the game.
     *
     * @param primaryStage container (window) in which to render the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        // TODO
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        moves.setText("Moves: \n"+ this.model.getMovesMade());
        hBox.getChildren().add(moves);
        status.setText("Status: \n" + this.model.getGameStatus());
        hBox.getChildren().add(status);
        score.setText("Score: \n" + this.model.getScore());
        hBox.getChildren().add(score);
        Label bestScore = new Label("Best Score: \n" + this.model.getBestScore());
        hBox.getChildren().add(bestScore);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(75);
        borderPane.setTop(hBox);
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        for (int row = 0; row < 4; row ++){
            for (int col = 0; col < 4; col++){
                Label label = new Label(String.valueOf(this.model.getContent(row, col)));
                if (label.getText().equals("0")) {
                    label.setText(" ");
                }
                label.setAlignment(Pos.CENTER);
                label.setMinSize(100,100);
                gridPane.add(label, col, row);
                grid[row][col] = label;
            }
        }
        borderPane.setLeft(gridPane);
        borderPane.setCenter(new Label("\t"));
        VBox rightPanel = new VBox();
        Button newGame = new Button("New Game");
        newGame.setOnAction((event -> {
            this.model.newGame();
        }));
        rightPanel.getChildren().add(newGame);
        rightPanel.getChildren().add(new Label());
        BorderPane buttons = new BorderPane();
        Button top = new Button("^");
        BorderPane.setAlignment(top, Pos.TOP_CENTER);
        top.setOnAction((event -> {
            this.model.move(Direction.NORTH);
        }));
        buttons.setTop(top);
        Button left = new Button("<");
        left.setOnAction((event -> {
            this.model.move(Direction.WEST);
        }));
        buttons.setLeft(left);
        Button right = new Button(">");
        right.setOnAction((event -> {
            this.model.move(Direction.EAST);
        }));
        buttons.setRight(right);
        Button bottom = new Button("v");
        BorderPane.setAlignment(bottom, Pos.BOTTOM_CENTER);
        bottom.setOnAction((event -> {
            this.model.move(Direction.SOUTH);
        }));
        buttons.setBottom(bottom);
        rightPanel.getChildren().add(buttons);
        borderPane.setRight(rightPanel);

        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            switch (key) {
                case UP -> top.fire();
                case DOWN -> bottom.fire();
                case LEFT -> left.fire();
                case RIGHT -> right.fire();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("2048 Tiles");
        primaryStage.show();

        scene.getRoot().requestFocus();

    }

    /**
     * Called by the model, model.TilesModel, whenever there is a state change
     * that needs to be updated by the GUI.
     *
     * @param model the TilesModel
     * @param message the status message sent by the model
     */
    @Override
    public void update(TilesModel model, String message) {
        // TODO
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 4; col++){
                String label = String.valueOf(this.model.getContent(row, col));
                if (!label.equals("0")) {
                    grid[row][col].setText(label);
                }
                else{
                    grid[row][col].setText(" ");
                }
            }
        }
        moves.setText("Moves: \n"+ this.model.getMovesMade());
        status.setText("Status: \n" + this.model.getGameStatus());
        score.setText("Score: \n" + this.model.getScore());
    }

    /**
     * This method is called before the application shuts down.
     * It provides a convenient place to save any information about the game
     * before closing the application.
     */
    @Override
    public void stop() {
        // TODO
        this.model.shutdown();
    }

    public void makeGrid(){
        for (int row = 0; row < grid.length; row++){
            for (int col = 0; col < grid.length; col++){
                Label label = new Label();
                grid[row][col] = label;
            }
        }
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}