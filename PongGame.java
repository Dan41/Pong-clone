package javaFX;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PongGame extends Application {
	
	Paddle paddle1 = new Paddle();
	Paddle paddle2 = new Paddle();
	
	AnimationTimer up1;
	AnimationTimer down1;
	AnimationTimer up2;
	AnimationTimer down2;
	
	AnimationTimer ballMove;
	Timeline wait;
	
	private int transitionX = 6;
	private int transitionY = 3;
	
	private int scoreCount1 = 0;
	private int scoreCount2 = 0;
	
	private boolean playable = true;
	
	Pane root = new Pane();
	
	public Parent create() {
		root.setPrefSize(600, 500);
		
		Rectangle background = new Rectangle(600, 500);
		
		Line separate = new Line();
		separate.setStroke(Color.WHITE);
		separate.setStrokeWidth(3);
		separate.setStartX(300);
		separate.setStartY(0);
		separate.setEndX(300);
		separate.setEndY(500);
		
		Circle design = new Circle();
		design.setRadius(100);
		design.setStroke(Color.WHITE);
		design.setStrokeWidth(2);
		design.setCenterX(300);
		design.setCenterY(250);
		
		paddle1.setTranslateX(20);
		paddle1.setTranslateY(200);
		
		paddle2.setTranslateX(560);
		paddle2.setTranslateY(200);
		
		up1 = new AnimationTimer() {
			public void handle(long now) {
				if (paddle1.getTranslateY() < 0) {
					up1.stop();
				}
				else {
					paddle1.setTranslateY(paddle1.getTranslateY() - 5);
				}
			}
		};
		down1 = new AnimationTimer() {
			public void handle(long now) {
				if (paddle1.getTranslateY() > 400) {
					down1.stop();
				}
				else {
					paddle1.setTranslateY(paddle1.getTranslateY() + 5);
				}
			}
		};
		
		up2 = new AnimationTimer() {
			public void handle(long now) {
				if (paddle2.getTranslateY() < 0) {
					up2.stop();
				}
				else {
					paddle2.setTranslateY(paddle2.getTranslateY() - 5);
				}
			}
		};
		
		down2 = new AnimationTimer() {
			public void handle(long now) {
				if (paddle2.getTranslateY() > 400) {
					down2.stop();
				}
				else {
					paddle2.setTranslateY(paddle2.getTranslateY() + 5);
				}
			}
		};
		
		Circle ball = new Circle();
		ball.setCenterX(300);
		ball.setCenterY(250);
		ball.setFill(Color.WHITE);
		ball.setRadius(12);
		
		Text score1 = new Text("" + scoreCount1);
		score1.setFill(Color.WHITE);
		score1.setTranslateX(262);
		score1.setTranslateY(40);
		score1.setFont(Font.font(50));
		
		Text score2 = new Text("" + scoreCount2);
		score2.setFill(Color.WHITE);
		score2.setTranslateX(307);
		score2.setTranslateY(40);
		score2.setFont(Font.font(50));
		
		Text player1 = new Text();
		Text player2 = new Text();
		player1.setTranslateY(270);
		player2.setTranslateY(270);
		player1.setTranslateX(100);
		player2.setTranslateX(375);
		player1.setFont(Font.font(60));
		player2.setFont(Font.font(60));
		player1.setFill(Color.WHITE);
		player2.setFill(Color.WHITE);
		
		wait = new Timeline(
			new KeyFrame(Duration.seconds(1), event ->  {
				ballMove.start();
		}));
		
		ballMove = new AnimationTimer() {
			public void handle(long now) {
				if (ball.getCenterY() > 488 || ball.getCenterY() < 12) {
					transitionY *= -1;
				}
				if (ball.getCenterX() + 12 > paddle2.getTranslateX()
					&& ball.getCenterX() + 12 < paddle2.getTranslateX() + 20
					&& ball.getCenterY() > paddle2.getTranslateY() - 12
					&& ball.getCenterY() < paddle2.getTranslateY() + 124) {
					if (transitionX == 6) {
						transitionX *= -1;
					}
				}
				if (ball.getCenterX() - 12 < paddle1.getTranslateX() + 20
					&& ball.getCenterX() - 12 > paddle1.getTranslateX()
					&& ball.getCenterY() > paddle1.getTranslateY() - 12
					&& ball.getCenterY() < paddle1.getTranslateY() + 124) {
					if (transitionX == - 6) {
						transitionX *= -1;
					}
				}
				if (ball.getCenterX() > 612) {
					ball.setCenterX(295);
					ball.setCenterY(250);
					scoreCount1++;
					score1.setText("" + scoreCount1);
					ballMove.stop();
					if (scoreCount1 == 7) {
						playable = false;
						player1.setText("Won");
						player2.setText("Lost");
					}
					else {
						wait.play();
					}
				}
				if (ball.getCenterX() < -12) {
					ball.setCenterX(305);
					ball.setCenterY(250);
					scoreCount2++;
					score2.setText("" + scoreCount2);
					ballMove.stop();
					if (scoreCount2 == 7) {
						playable = false;
						player1.setText("Lost");
						player2.setText("Won");
					}
					else {
						wait.play();
					}
				}
				ball.setCenterX(ball.getCenterX() + transitionX);
				ball.setCenterY(ball.getCenterY() + transitionY);
			}
		};
		
		wait.play();
		
		root.getChildren().addAll(background, design, separate, paddle1, paddle2, ball, score1, score2, player1, player2);
		
		return root;
	}
	
	private class Paddle extends Pane {
		Rectangle paddle = new Rectangle(20, 100);
		public Paddle() {
			paddle.setFill(Color.WHITE);
			
			getChildren().add(paddle);
		}
	}

	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(create());
		
		scene.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.W) && playable) {
				up1.start();
			}
			if (event.getCode().equals(KeyCode.S) && playable) {
				down1.start();
			}
			if (event.getCode().equals(KeyCode.UP) && playable) {
				up2.start();
			}
			if (event.getCode().equals(KeyCode.DOWN) && playable) {
				down2.start();
			}
		});
		
		scene.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.W)) {
				up1.stop();
			}
			if (event.getCode().equals(KeyCode.S)) {
				down1.stop();
			}
			if (event.getCode().equals(KeyCode.UP)) {
				up2.stop();
			}
			if (event.getCode().equals(KeyCode.DOWN)) {
				down2.stop();
			}
		});
		
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
