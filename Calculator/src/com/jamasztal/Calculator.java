package com.jamasztal;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import javafx.event.*;

import javafx.geometry.*;

import java.net.*;
import java.io.*;
import java.util.*;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;

import javafx.scene.control.ScrollPane.*;

import javafx.util.*;
import javafx.animation.*;

import net.objecthunter.exp4j.*;

// JavaFX documentation: http://docs.oracle.com/javafx/2/api/

/**
 * @author Lee Stemkoski
 */
public class Calculator extends Application 
{
	/**
	 *   Run the application.
	 *   Note: Application is a Process, only one allowed per VM.
	 */
	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(final Stage theStage) 
	{
		theStage.setTitle("DisplayGUI Demo");
		theStage.setResizable(false);
		
		Group root = new Group();
		Scene theScene = new Scene(root);
		

		final VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(10));  
		mainBox.setSpacing(1);  
		mainBox.setAlignment(Pos.CENTER); 
		mainBox.getStyleClass().add("vbox");

		root.getChildren().add(mainBox);

		// code -------------------------------------------------

		try
		{
			File cssFile = new File("res/displayStyle.css");
			URL  cssURL  = cssFile.toURI().toURL();   
			theScene.getStylesheets().clear();
			theScene.getStylesheets().add( cssURL.toString() );
		}
		catch (Exception ex)
		{
			System.out.println("Couldn't find/parse stylesheet file.");
			ex.printStackTrace();
		}

		// model
		final Display myDisplay = new Display();

		// view 

		final Label displayLeft = new Label();
		displayLeft.getStyleClass().add("displayText");

		final Label displayRight = new Label();
		displayRight.getStyleClass().add("displayText");

		final Label displayCursor = new Label();
		displayCursor.setText("|");
		displayCursor.getStyleClass().add("displayCursor");

		HBox displayRow = new HBox();
		displayRow.setPadding(new Insets(6));
		displayRow.setAlignment(Pos.CENTER_RIGHT);

		displayRow.getChildren().addAll(displayLeft, displayCursor, displayRight);

		final ScrollPane sp = new ScrollPane();
		sp.prefWidth(400);
		sp.prefHeight(60);
		sp.setVbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setContent(displayRow);

		mainBox.getChildren().add( sp );

		// controller

		Timeline timey = new Timeline();
		timey.setCycleCount(Timeline.INDEFINITE);

		KeyFrame displayViewUpdater = new KeyFrame(
				Duration.seconds(0.1), 
				new EventHandler<ActionEvent>() 
				{
					public void handle(ActionEvent event) 
					{
						displayLeft.setText( myDisplay.getSubstringLeft() );
						displayRight.setText( myDisplay.getSubstringRight() );

						int time = (int)System.currentTimeMillis(); 

						// oscillate value between 0 and 1
						double opacity = Math.abs( Math.sin( time / 1000.0 * 3.14 ) );

						displayCursor.setOpacity( opacity );
					}
				});

		timey.getKeyFrames().add(displayViewUpdater);
		timey.play();



		// navigation buttons 
		Button buttonLeft =new Button();
		buttonLeft.setText("\u2190");
		buttonLeft.setPrefWidth(74);
		buttonLeft.setPrefHeight(24);
		buttonLeft.getStyleClass().add("button3");
		buttonLeft.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				myDisplay.moveLeft();
			}                    
				});

		Button buttonRight = new Button();
		buttonRight.setText("\u2192");
		buttonRight.setPrefWidth(74);
		buttonRight.setPrefHeight(24);
		buttonRight.getStyleClass().add("button3");
		buttonRight.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				myDisplay.moveRight();
			}                    
				});

		Button buttonBacksp = new Button();
		buttonBacksp.setText("\u232B");
		buttonBacksp.setPrefWidth(74);
		buttonBacksp.setPrefHeight(24);
		buttonBacksp.getStyleClass().add("button3");
		buttonBacksp.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				myDisplay.delete();
			}                    
				});

		Button buttonAC = new Button();
		buttonAC.setPrefWidth(80);
		buttonAC.setPrefHeight(24);
		buttonAC.setText("AC");
		buttonAC.getStyleClass().add("button4");
		
		buttonAC.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				myDisplay.clear();
			}                    
				});

		Button buttonEval = new Button();
		buttonEval.setText("=");
		buttonEval.setPrefWidth(70);
		buttonEval.setPrefHeight(50);
		buttonEval.getStyleClass().add("button1");
		buttonEval.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				String mathString = myDisplay.getContents();

				Expression e = new ExpressionBuilder(mathString)
				.build();

				String result = "";

				try
				{
					double y = e.evaluate();
					result = Double.toString(y);
				}
				catch (Exception ex)
				{
					result = "Error";   
				}

				myDisplay.clear();
				myDisplay.insert(result);
			}                    
				});

		// Number pad button


		GridPane numberPadGrid = new GridPane();
		numberPadGrid.setPadding(new Insets(10)); 
		numberPadGrid.setHgap(10);
		numberPadGrid.setVgap(10);
	

		String[] numberPadButtonNames = {
				"7", "8", "9", "4", "5", "6", "1", "2", "3", ".", "0"};

		String[] numberPadButtonInserts = {
				"7", "8", "9", "4", "5", "6", "1", "2", "3", ".", "0"};

		for (int i = 0; i < numberPadButtonNames.length; i++)
		{
			Button b = buttonMaker( numberPadButtonNames[i], numberPadButtonInserts[i], myDisplay );
			b.getStyleClass().add("button1");
			numberPadGrid.add( b, i%3, i/3 );
		}
		numberPadGrid.add( buttonEval, 2, 3 );

		// Operation pad button


		GridPane operationPadGrid = new GridPane();
		operationPadGrid.setPadding(new Insets(10)); 
		operationPadGrid.setHgap(10);
		operationPadGrid.setVgap(10);
		



		String[] operationPadButtonNames = {
				"/", "*", "-", "+"};

		String[] operationPadButtonInserts = {
				"/", "*", "-", "+"};

		for (int i = 0; i < operationPadButtonNames.length; i++)
		{
			Button b = buttonMaker( operationPadButtonNames[i], operationPadButtonInserts[i], myDisplay );
			b.getStyleClass().add("button2");
			operationPadGrid.add( b, i/4, i/1 );
		}

		GridPane displayControlPadGrid = new GridPane();
		displayControlPadGrid.setPadding(new Insets(10)); 
		displayControlPadGrid.setHgap(3);
		displayControlPadGrid.setVgap(10);

		displayControlPadGrid.add( buttonLeft, 0, 0 );
		displayControlPadGrid.add( buttonRight, 1, 0 );
		displayControlPadGrid.add( buttonBacksp, 2, 0 );
		displayControlPadGrid.add( buttonAC, 3, 0 );

		final VBox controVBox = new VBox();
		controVBox.setPadding(new Insets(1));  
		controVBox.setSpacing(-5);  
		controVBox.setAlignment(Pos.CENTER); 


		final HBox controlHBox = new HBox();
		controlHBox.setPadding(new Insets(1));  
		controlHBox.setSpacing(-10);  
		controlHBox.setAlignment(Pos.CENTER); 

		controlHBox.getChildren().addAll(numberPadGrid,operationPadGrid);
		controVBox.getChildren().addAll(displayControlPadGrid,controlHBox);

		mainBox.getChildren().add(controVBox);
		// code -------------------------------------------------

		theStage.setScene(theScene);
		theStage.show();
	}

	public Button buttonMaker(String labelText, final String insertText, final Display disp)
	{
		Button b = new Button();
		b.setText(labelText);
		b.setMinWidth(70);
		b.setMinHeight(50);
		b.setMaxWidth(70);
		b.setMaxHeight(50);
		b.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				disp.insert( insertText );
			}                    
				});
		return b;
	}

}

