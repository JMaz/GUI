package com.jamasztal;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.web.*;

/*
 * @author Jan Masztal
 */
public class Browser extends Application{

	WebPage tabby;
	String homePage;
	/**
	 *   Run the application.
	 *   Note: Application is a Process, only one allowed per VM.
	 */
	public static void main(String[] args) 
	{
		launch(args);
	}	

	@Override
	public void start(Stage theStage) 
	{
		theStage.setTitle("FoBrowser");
		theStage.setMinHeight(700);
		theStage.setMinWidth(1100);
		theStage.setResizable(false);

		Group root = new Group();
		Scene theScene = new Scene(root);

		VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(30,0,0,0));    
		mainBox.setSpacing(0);
		mainBox.getStyleClass().add("root");
		mainBox.setAlignment(Pos.CENTER);

		root.getChildren().add(mainBox);

		final TabPane tabPane = new TabPane();
		tabPane.setPrefWidth(800);
		tabPane.setPrefHeight(600);
		tabPane.getStyleClass().add("root");

		try
		{
			File cssFile = new File("res/style.css");
			URL  cssURL  = cssFile.toURI().toURL();   
			theScene.getStylesheets().clear();
			theScene.getStylesheets().add( cssURL.toString() );
		}
		catch (Exception ex)
		{
			System.out.println("Couldn't find/parse stylesheet file.");
			ex.printStackTrace();
		}

		MenuBar menuBar = new MenuBar();

		Menu menuFile = new Menu("File");

		MenuItem save = new MenuItem();
		save.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		save.setText("New Tab");
		save.setOnAction( new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				Tab tabby = new WebPage(homePage);
				tabPane.getTabs().add( tabby );
			}});

		MenuItem open = new MenuItem();
		open.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		open.setText("Close");
		open.setOnAction( new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				System.exit(0);
			}});

		menuFile.getItems().addAll(save,open);

		Menu menuHelp = new Menu("Help");

		MenuItem about = new MenuItem();
		about.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
		about.setText("About");
		about.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent event)
			{
				final Stage dialog = new Stage();
				//	dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(theStage);

				Group root = new Group();
				Scene scene = new Scene(root);

				try
				{
					File cssFile = new File("res/style.css");
					URL  cssURL  = cssFile.toURI().toURL();   
					scene.getStylesheets().clear();
					scene.getStylesheets().add( cssURL.toString() );
				}
				catch (Exception ex)
				{
					System.out.println("Couldn't find/parse stylesheet file.");
					ex.printStackTrace();
				}
				VBox dialogVbox = new VBox();
				dialogVbox.setPadding(new Insets(10));
				dialogVbox.getChildren().add(new Text("code created by Jan Masztal"));
				dialogVbox.getStyleClass().add("hbox");

				root.getChildren().add(dialogVbox); 

				dialog.setScene(scene);
				dialog.show();
			}
				});

		menuHelp.getItems().addAll(about);	

		menuBar.prefWidthProperty().bind(theStage.widthProperty());
		menuBar.getMenus().addAll(menuFile,menuHelp);

		root.getChildren().addAll(menuBar);

		homePage = "http://www.adelphi.edu";
		tabby = new WebPage(homePage);
		tabPane.getTabs().add( tabby );

		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
				tabby = (WebPage) arg2;
			}
		});

		HBox urlBar = new HBox();
		urlBar.setPadding(new Insets(0,0,10,10));    
		urlBar.setSpacing(10);
		urlBar.setAlignment(Pos.TOP_LEFT);
		urlBar.prefWidthProperty().bind(mainBox.widthProperty());
		urlBar.getStyleClass().add("hbox");

		TextField urlTextField = new TextField();
		urlTextField.setText("http://");
		urlTextField.setPrefWidth(930);

		Button backButton = new Button();
		backButton.setText("\u2190");
		backButton.setOnAction(
				new EventHandler() 
				{
					public void handle(Event t) 
					{
						tabby.goBack();
					}           
				});

		Button forwardButton = new Button();
		forwardButton.setText("\u2192");
		forwardButton.setOnAction(
				new EventHandler() 
				{
					public void handle(Event t) 
					{
						tabby.goForward();			
					}           
				});

		Button homeButton = new Button();
		homeButton.setText("\u2302");
		homeButton.setOnAction(
				new EventHandler() 
				{
					public void handle(Event t) 
					{
						tabby.goToPage(homePage);		
					}           
				});

		Button goButton = new Button();
		goButton.setText("\u23ce");
		goButton.setOnAction(
				new EventHandler() 
				{
					public void handle(Event t) 
					{
						String url = urlTextField.getText();
						tabby.goToPage(url);

					}           
				});

		urlBar.getChildren().addAll(backButton, forwardButton,homeButton,urlTextField,goButton);

		mainBox.getChildren().add( urlBar);
		mainBox.prefWidthProperty().bind( theStage.widthProperty());
		mainBox.getChildren().add( tabPane );

		theStage.setScene(theScene);
		theStage.show();
	}
}
