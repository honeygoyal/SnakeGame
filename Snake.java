package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;

public class Snake extends Application {

    Map map;
    Snake mp;

    @Override
    public void start(Stage primaryStage) throws Exception{
        map = new Map();
        mp = map.snake;
        Scene scene = new Scene(map, 640, 640);
        map.setStyle("-fx-background-color:BLACK");

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ev) {
                if (ev.getCode() == KeyCode.UP && mp.direction != "d"){
                    mp.direction = "u";
                }
                if (ev.getCode()==KeyCode.DOWN && mp.direction != "u"){
                    mp.direction = "d";
                }
                if (ev.getCode()== KeyCode.RIGHT && mp.direction != "l"){
                    mp.direction = "r";
                }
                if (ev.getCode()==KeyCode.LEFT && mp.direction != "r"){
                    mp.direction = "l";
                }
                map.reDraw();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mp.move();
                        for (int i = 0; i < mp.size; i++) {
                            for (int j = i+1; j < mp.size; j++) {
                                Position f = mp.positions.get(i);
                                Position s = mp.positions.get(j);
                                if (f.x == s.x && f.y == s.y) {
                                    System.exit(0);
                                }
                            }
                        }
                        map.reDraw();
                    }
                });
            }
        },0, 500);

        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class Snake {
    ArrayList<Position> positions = new ArrayList<>();
    String direction = "r";
    Position last = new Position(0,0);
    int size = 2;
    Snake() {
        positions.add(new Position(0,0));
        positions.add(new Position(0,1));
    }
    public void moveUp() {
        Position last = positions.get(size-1);
        positions.add(new Position((last.x+10-1)%10, last.y));
        this.last = positions.get(0);
        positions.remove(0);
    }
    public void moveDown() {
        Position last = positions.get(size-1);
        positions.add(new Position((last.x+10+1)%10, last.y));
        this.last = positions.get(0);
        positions.remove(0);
    }
    public void moveRight() {
        Position last = positions.get(size-1);
        positions.add(new Position(last.x, (last.y+1+10)%10));
        this.last = positions.get(0);
        positions.remove(0);
    }
    public void moveLeft() {
        Position last = positions.get(size-1);
        positions.add(new Position(last.x, (last.y-1+10)%10));
        this.last = positions.get(0);
        positions.remove(0);
    }
    public void move() {
        switch(direction) {
            case "l": moveLeft();break;
            case "r": moveRight();break;
            case "u": moveUp();break;
            case "d": moveDown();break;
        }
    }
}

class Map extends Pane {
    Snake snake = new Snake();
    Feed feed;
    Map() {
        setFeed();
    }

    public void setFeed() {
        Position pos = new Position(0,0);
        do {
            pos = new Position((int)(Math.random()*10), (int)(Math.random()*10));
            System.out.println(pos);
        } while (snake.positions.contains(pos));
        feed = new Feed(pos);
    }

    public void reDraw() {
        getChildren().clear();
        Position last = snake.positions.get(snake.size-1);
        if (last.x == feed.pos.x && last.y == feed.pos.y) {
            snake.positions.add(0,snake.last);
            setFeed();
            snake.size++;
        }
        for(Position p: snake.positions) {
            Rectangle r = new Rectangle(p.y*64, p.x*64, 64, 64);
            if (p.x != last.x || p.y != last.y)
                r.setFill(Color.GREEN);
            else r.setFill(Color.WHITE);
            getChildren().add(r);
        }
        Rectangle r = new Rectangle(feed.pos.y*64, feed.pos.x*64, 64, 64);
        r.setFill(Color.RED);
        getChildren().add(r);
    }
}

class Feed {
    Position pos;
    Feed(Position pos) {
        this.pos = pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}

class Position implements Comparable<Position>{
    int x;
    int y;
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int compareTo(Position o) {
        if (o.x == x && o.y == y) {
            return 0;
        }
        return 1;
    }
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}