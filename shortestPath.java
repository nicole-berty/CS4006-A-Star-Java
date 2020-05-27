/**
 * CS4006 Intelligent Systems - Project.
 *
 * @author: Sean Lynch
 * @author: Michele Cavaliere
 * @author: Nicole Berty
 * @author: Matt Lucey
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class shortestPath extends Application {

    static Square[][] board;
    static ArrayList<Integer> coordinates = new ArrayList<>();
    static GridPane grid = new GridPane();
    static ArrayList<Node> path = new ArrayList<>();

    public static void main(String[] args) {
        Board newBoard = new Board();
        board = newBoard.getBoard();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        printGrid(primaryStage);
        shortestPath.dialogBox();
    }

    /**
     * Method to fill squares on the grid.
     *
     * @SubScene
     */

    public static SubScene fillSquare() {
        Rectangle rec = new Rectangle(200, 200);
        rec.setFill(Color.RED);

        Group group1 = new Group();
        group1.getChildren().add(rec);
        SubScene scene = new SubScene(group1, 75, 75);
        scene.setFill(Color.WHITE);
        return scene;
    }

    /**
     * Method to draw the path of the A* Algorithm.
     *
     * @return
     */

    public static SubScene pathDraw() {
        Circle cir = new Circle(25,25f,20);
        cir.setFill(Color.GREEN);

        Group group1 = new Group();
        group1.getChildren().add(cir);
        SubScene scene = new SubScene(group1, 45, 45);
        scene.setFill(Color.WHITE);
        return scene;
    }

    /**
     * Pops up four dialog box's to the screen to enter the co-ordinates
     * of the start and goal position.
     */

    static void dialogBox() {

        List<String> choices2 = new ArrayList<> ();

        for(int i = 1; i <= 8; i++){
            choices2.add(String.valueOf(i));
        }

        List<String> choices = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            char c = (char) (i + 64);
            choices.add(String.valueOf(c));
        }
        boolean coords1Safe = false;
        boolean coords2Safe = false;
        String error = "";
        while (!coords1Safe) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("1", choices2);
            dialog.setTitle("Choose Coordinates for start");
            dialog.setHeaderText("");
            dialog.setContentText(error + "Choose row number for start position: ");

            Optional<String> results = dialog.showAndWait();
            if (results.isPresent()) {
                coordinates.add(Integer.parseInt(results.get()) - 1);
            }

            ChoiceDialog<String> dialog2 = new ChoiceDialog<>("A", choices);
            dialog2.setTitle("Choose Coordinates for start");
            dialog2.setHeaderText("");
            dialog2.setContentText(error + "Choose column letter for start position: ");

            Optional<String> result2 = dialog2.showAndWait();
            if (result2.isPresent()) {
                coordinates.add((result2.get().charAt(0) - 64) - 1);
            }
            if (!(board[coordinates.get(0)][coordinates.get(1)].getOccupied())) {
                coords1Safe = true;
            }
            else {
                error = "You chose an occupied space, try again! ";
                coordinates.remove(0);
                coordinates.remove(0);
            }
        }
        error = "";
        while (!coords2Safe) {
            ChoiceDialog<String> dialog3 = new ChoiceDialog<>("1", choices2);
            dialog3.setTitle("Choose coordinates for end");
            dialog3.setContentText(error + "Choose row number for end position: ");
            dialog3.setHeaderText("");
            Optional<String> result3 = dialog3.showAndWait();
            if (result3.isPresent()) {
                coordinates.add(Integer.parseInt(result3.get()) - 1);
            }

            ChoiceDialog<String> dialog4 = new ChoiceDialog<>("A", choices);
            dialog4.setTitle("Choose coordinates for end");
            dialog4.setContentText(error + "Choose column letter for end position: ");
            dialog4.setHeaderText("");
            Optional<String> result4 = dialog4.showAndWait();
            if (result4.isPresent()) {
                coordinates.add((result4.get().charAt(0) - 64) - 1);
            }
            if (!(board[coordinates.get(2)][coordinates.get(3)].getOccupied())) {
                coords2Safe = true;
            } else {
                error = "You chose an occupied space, try again! ";
                coordinates.remove(2);
                coordinates.remove(2);
            }
        }

    Text startPos = new Text("Start");
    startPos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 32));
    startPos.setFill(Color.BLUE);

    Text goalPos = new Text("Goal");
    goalPos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 32));
    goalPos.setFill(Color.ORANGE);

    grid.add(startPos, coordinates.get(1) ,coordinates.get(0));
    grid.add(goalPos, coordinates.get(3), coordinates.get(2));
    }

    /**
     * Prints out the grid on the sceen.
     * Adds both numbers and letters to the side and top of the grid.
     * @param primaryStage
     */

    void printGrid(Stage primaryStage){

        Button button = new Button();
        button.setText("Find Shortest Path");
        button.setAlignment(Pos.BOTTOM_CENTER);
        button.setPrefSize(100, 40);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Graph graph = new Graph(board,coordinates.get(0),coordinates.get(1),coordinates.get(2),coordinates.get(3));
                path = graph.Astar();
                for(int i = 0; i < path.size(); i++){
                    grid.add(pathDraw(), path.get(i).y, path.get(i).x);
                }
            }
        };

        button.setOnAction(event);

        Text numbers = new Text("1\n2\n3\n4\n5\n6\n7\n8");
        numbers.setFont(Font.font("Tahoma", FontWeight.NORMAL, 63));

        Text letters = new Text(" A  B  C  D  E  F  G  H");
        letters.setFont(Font.font("Tahoma", FontWeight.NORMAL, 63));

        int rowSize = 8;
        int colSize = 8;
        for(int row = 0; row < rowSize; row++){
            RowConstraints rows = new RowConstraints(75);
            grid.getRowConstraints().add(rows);
        }

        for(int col = 0; col < colSize; col++){
            ColumnConstraints column = new ColumnConstraints(75);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j].getOccupied()) {
                    grid.add(fillSquare(), j, i);
                }
            }
        }

        StackPane stack = new StackPane();
        stack.getChildren().addAll(grid,numbers,letters,button);
        StackPane.setAlignment(letters, Pos.TOP_CENTER);
        StackPane.setAlignment(numbers, Pos.CENTER_LEFT);
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);

        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: WHITE; -fx-grid-lines-visible: true");
        Scene scene = new Scene(stack, (colSize * 89),(rowSize * 92), Color.WHITE);
        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setMinHeight(rowSize * 95);
        primaryStage.setMinWidth(colSize * 89);
        primaryStage.setMaxHeight(rowSize * 95);
        primaryStage.setMaxWidth(colSize * 89);
    }
}

class Node {
    int x,y;
    double gValue = 100, hValue = 100, fValue = 100;
    Node parent;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Graph {

    Square[][] board;
    Node start;
    Node end;

    Graph(Square[][] board, int startX, int startY, int endX, int endY) {
        this.board = board;
        start = new Node(startX, startY);
        end = new Node(endX, endY);
    }

    ArrayList<Node> Astar() {
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        open.add(start);
        //g(n) is the total computed cost
        Node current = open.get(0);
        while(!(current == end)) {
            current.hValue = ManhattanDistance(current);
            current.gValue = g(current);
            current.fValue = current.hValue + current.gValue;
            if (open.size() == 0) return null;
            current = open.get(0);
            for (Node node : open) {
                if (node.hValue <= current.hValue && !(closed.contains(node))) {
                    current = node;
                }
            }
            open.remove(current);
            closed.add(current);
            if (current.hValue == 0) {
                break;
            } else {
                ArrayList<Node> neighboursOfCurrent = new ArrayList<>();
                if (!(current.x + 1 > 7) && !board[current.x + 1][current.y].getOccupied() &&
                        NewNode(current.x + 1, current.y, open, closed)) {
                    Node rightNeighbour = new Node(current.x + 1,current.y);
                    rightNeighbour = values(rightNeighbour);
                    neighboursOfCurrent.add(rightNeighbour);
                }
                if (!(current.x - 1 < 0) && !board[current.x - 1][current.y].getOccupied()
                        &&NewNode(current.x - 1, current.y, open, closed)){
                    Node leftNeighbour = new Node(current.x - 1,current.y);
                    leftNeighbour = values(leftNeighbour);
                    neighboursOfCurrent.add(leftNeighbour);
                }
                if (!(current.y + 1 > 7) && !board[current.x][current.y + 1].getOccupied()
                        &&NewNode(current.x,current.y+1, open, closed)) {
                    Node upNeighbour = new Node(current.x,current.y + 1);
                    upNeighbour = values(upNeighbour);
                    neighboursOfCurrent.add(upNeighbour);
                }
                if (!(current.y - 1 < 0) && !board[current.x][current.y-1].getOccupied()
                        &&NewNode(current.x,current.y - 1, open, closed)) {
                    Node downNeighbour = new Node(current.x,current.y - 1);
                    downNeighbour = values(downNeighbour);
                    neighboursOfCurrent.add(downNeighbour);
                }
                for (Node x: neighboursOfCurrent) {
                    if (x.gValue < current.gValue && closed.contains(x)) {
                        closed.remove(current);
                    } else if (x.gValue < current.gValue && open.contains(x)) {
                        open.remove(current);
                    } else if (!open.contains(x) && !closed.contains(x)) {
                        x.hValue = ManhattanDistance(x);
                        x.parent = current;
                        open.add(x);
                    }
                }
            }
        }
        Node now = closed.get(closed.size() - 1);
        ArrayList<Node> path = new ArrayList<>();
        path.add(0,  now);
        while (now.x != start.x || now.y != start.y) {
            now = now.parent;
            path.add(0, now);
        }
        return path;
    }

    boolean NewNode(int x, int y, ArrayList<Node> open, ArrayList<Node> closed) {
        for (Node value : open) {
            if (value.x == x && value.y == y) {
                return false;
            }
        }
        for (Node node : closed) {
            if (node.x == x && node.y == y) {
                return false;
            }
        }
        return true;
    }

    Node values(Node node) {
        node.gValue = g(node);
        node.hValue = ManhattanDistance(node);
        node.fValue = node.hValue + node.gValue;
        return node;
    }

    //h(n)
    double ManhattanDistance(Node current) {
        int x = end.x - current.x;
        int y  = end.y - current.y;
        double distance = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        return distance;
    }

    //g(n)
    double g(Node current) {
        int x = current.x - start.x;
        int y = current.y - start.y;
        double distance = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        return distance;
    }
}
class Square {

    boolean occupied = false;

    public Square() {

    }

    public Square(boolean occupied) {
        this.occupied = occupied;
    }

    public void changeSquareStatus() {
        if (occupied) {
            occupied = false;
        } else {
            occupied = true;
        }
    }

    public boolean getOccupied() {
        return occupied;
    }
}

class Board {

    Square[][] board;

    public Board() {
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square();
            }
        }
        int obstacle = (int) (Math.random() * 3);
        int startXPos = (int) (Math.random() * 4 + 1);
        int startYPos = (int) (Math.random() * 4 + 1);
        board[startXPos][startYPos].changeSquareStatus();
        int orientation = (int) (Math.random() * 2);
        switch (obstacle) {
            case 0:
                //I shape
                if (orientation == 0) {
                    for (int i = 1; Math.abs(i) < 3; Math.abs(i++)) {
                        if (startYPos + i > 7) {
                            i *= -1;
                            if (i == -2) {
                                i += 1;
                                board[startXPos][startYPos + i].changeSquareStatus();
                                break;
                            }
                        }
                        board[startXPos][startYPos + i].changeSquareStatus();
                        if (i < 0) {
                            i *= -1;
                        }
                    }
                } else {
                    for (int i = 1; Math.abs(i) < 3; Math.abs(i++)) {
                        if (startXPos + i > 7) {
                            i *= -1;
                            if (i == -2) {
                                i += 1;
                                board[startXPos + i][startYPos].changeSquareStatus();
                                break;
                            }
                        }
                        board[startXPos + i][startYPos].changeSquareStatus();
                        if (i < 0) {
                            i *= -1;
                        }
                    }
                }
                break;
            case 1:
                // L shape
                if (orientation == 0) {
                    for (int i = 1; Math.abs(i) < 3; Math.abs(i++)) {
                        if (startYPos + i > 7) {
                            i *= -1;
                        }
                        board[startXPos][startYPos + i].changeSquareStatus();
                        if (i < 0) {
                            i *= -1;
                        }
                    }
                    if (startXPos + 1 > 7) {
                        board[startXPos - 1][startYPos].changeSquareStatus();
                    } else {
                        board[startXPos + 1][startYPos].changeSquareStatus();
                    }
                } else {
                    for (int i = 1; Math.abs(i) < 3; Math.abs(i++)) {
                        if (startXPos + i > 7) {
                            i *= -1;
                        }
                        board[startXPos + i][startYPos].changeSquareStatus();
                        if (i < 0) {
                            i *= -1;
                        }
                    }
                    if (startYPos + 1 > 7) {
                        board[startXPos][startYPos - 1].changeSquareStatus();
                    } else {
                        board[startXPos][startYPos + 1].changeSquareStatus();
                    }
                }
                break;
            case 2:
                //T shape
                if (startXPos > 5 && startYPos > 5) {
                    startXPos = (int) (Math.random() * 4 + 1);
                    startYPos = (int) (Math.random() * 4 + 1);
                }
                if (orientation == 0) {
                    for (int i = 1; i < 3; i++) {
                        board[startXPos + i][startYPos].changeSquareStatus();
                    }
                    for (int i = 1; i < 3; i++) {
                        board[startXPos + 1][startYPos + i].changeSquareStatus();
                    }
                } else {
                    for (int i = 1; i < 3; i++) {
                        board[startXPos][startYPos + i].changeSquareStatus();
                    }
                    for (int i = 1; i < 3; i++) {
                        board[startXPos + i][startYPos + 1].changeSquareStatus();
                    }
                }
                break;
        }
    }

    public Square[][] getBoard() {
        return board;
    }
}
