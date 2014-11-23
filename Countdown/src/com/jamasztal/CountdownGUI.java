package com.jamasztal;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.util.*;
import javafx.animation.*;

/*
 * This class defines a CountdownGUI application
 * 
 * @author Jan Masztal
 */
public class CountdownGUI extends Application 
{

	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(final Stage theStage) 
	{
		theStage.setTitle("Final Countdown");

		Group root = new Group();
		Scene theScene = new Scene(root);

		final VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(10));  
		mainBox.setSpacing(10);               
		mainBox.setAlignment(Pos.CENTER);

		root.getChildren().add(mainBox);

		// code -------------------------------------------------

		Countdown cd = new Countdown();
		cd.reset();

		Text timeDisplay = new Text("Time Goes Here");

		Button allButton = new Button();
		allButton.setText("Words go here");
		allButton.setOnAction(
				new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						if ( cd.getState() == Countdown.DEFAULT 
								|| cd.getState() == Countdown.PAUSED   )
						{
							cd.resume();
						}
						else if ( cd.getState() == Countdown.RUNNING )
						{
							cd.pause();
						}
						else
						{
							System.err.println(" You broke something :( ");
						}

					}
				}
				);

		Button resetButton = new Button();
		resetButton.setText("Reset");
		resetButton.setOnAction(
				new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						cd.reset();
					}
				});

		mainBox.getChildren().addAll( timeDisplay, allButton, resetButton );

		final HBox setTimeBox = new HBox();
		setTimeBox.setPadding(new Insets(10));  
		setTimeBox.setSpacing(10);               
		setTimeBox.setAlignment(Pos.CENTER);

		final TextField timeField = new TextField();
		timeField.setPromptText("Countdown Time");

		Button timerSetButton = new Button();
		timerSetButton.setText("Set");
		timerSetButton.setOnAction(
				new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						int t = Integer.parseInt(timeField.getText());
						cd.setUserCountdownTime(t);

					}
				}
				);

		setTimeBox.getChildren().addAll( timerSetButton,timeField);

		mainBox.getChildren().add(setTimeBox);

		Timeline timey = new Timeline();

		timey.setCycleCount( Timeline.INDEFINITE );

		KeyFrame kf = new KeyFrame(
				Duration.seconds(0.01),
				new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						if(cd.getCountdownTime() <= 0){
							String displayText = "Time: 0";
							timeDisplay.setText( displayText );
						}else{
							cd.update( System.currentTimeMillis() );
							String displayText = "Time: " + cd.getCountdownTime();

							timeDisplay.setText( displayText );

							if ( cd.getState() == Countdown.DEFAULT )
							{
								allButton.setText("Start");
							}
							else if ( cd.getState() == Countdown.RUNNING )
							{
								allButton.setText("Pause");
							}
							else if ( cd.getState() == Countdown.PAUSED )
							{
								allButton.setText("Resume");
							}
							else
							{
								System.err.println(" :( :( :`( ");
							}
						}
					}
				});

		timey.getKeyFrames().add( kf );

		timey.play();

		// code --------------------------------------------------------

		theStage.setScene(theScene);
		theStage.show();
	}
}
