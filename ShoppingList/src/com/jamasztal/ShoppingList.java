package com.jamasztal;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;


/*
 * This class defines a ShoppingList application
 * 
 * @author Jan Masztal
 */
public class ShoppingList extends Application{
	DecimalFormat df = new DecimalFormat("#########0.00");
	TableView<Item> itemTable = new TableView<Item>();


	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage theStage) throws Exception {
		theStage.setTitle("Shopping List");

		Group root = new Group();
		Scene scene = new Scene(root);

		VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(40,10,10,10));
		mainBox.setSpacing(10);
		mainBox.setAlignment(Pos.CENTER);
		mainBox.getStyleClass().add("vbox");

		root.getChildren().add(mainBox);

		
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
		
		final ObservableList<Item> itemList = FXCollections.observableArrayList();

		//test data
		itemList.add( new Item("Soda",5.00,9));
		itemList.add( new Item("Turkey",1.99,9) );

		//Menu

		final FileChooser fileChooser = new FileChooser();

		MenuBar menuBar = new MenuBar();

		Menu menuFile = new Menu("File");

		MenuItem save = new MenuItem();
		save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		save.setText("Save");
		save.setOnAction( new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				try{
					File f = fileChooser.showSaveDialog(theStage);

					if(f == null) {return;}

					PrintWriter pr = new PrintWriter(f);

					for(Item i : itemList){
						pr.println(i.getName());
						pr.println(i.getPrice());
						pr.println(i.getQty());
					}
					pr.close();
				}catch (Exception e){e.printStackTrace();}
			}});

		MenuItem open = new MenuItem();
		open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		open.setText("Open");
		open.setOnAction( new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				try{
					File f = fileChooser.showOpenDialog(theStage);
					if(f == null) {return;}
					
					Scanner sc = new Scanner(f);
					itemList.removeAll(itemList);
					while(sc.hasNextLine()){
						Item temp = new Item();
						temp.setName(sc.nextLine());
						sc.hasNextLine();
						temp.setPrice(Double.parseDouble(sc.nextLine()));
						sc.hasNextLine();
						temp.setQty(Integer.parseInt(sc.nextLine()));
						itemList.add(temp);
					}
					sc.close();
					itemTable.setItems(itemList);
				}catch (Exception e){e.printStackTrace();}
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
				dialogVbox.getStyleClass().add("vbox");
				
				root.getChildren().add(dialogVbox); 

				dialog.setScene(scene);
				dialog.show();
			}
				});

		menuHelp.getItems().addAll(about);	


		menuBar.prefWidthProperty().bind(theStage.widthProperty());
		menuBar.getMenus().addAll(menuFile,menuHelp);

		root.getChildren().addAll(menuBar);

		//Item table
		itemTable.setItems(itemList);
		itemTable.setEditable(true);
		mainBox.getChildren().add( itemTable );

		//Price column

		TableColumn<Item, String> itemColumn = new TableColumn<Item,String>("Item");

		itemColumn.setPrefWidth(128);

		itemColumn.setCellValueFactory( 
				new PropertyValueFactory<Item, String>("name")
				);

		itemColumn.setCellFactory( TextFieldTableCell.forTableColumn() );

		itemColumn.setOnEditCommit(
				new EventHandler< CellEditEvent<Item, String> >() 
				{
					public void handle( CellEditEvent<Item, String> t ) 
					{
						int rowNum = t.getTablePosition().getRow();
						Item p = (Item) t.getTableView().getItems().get(rowNum);
						String s = t.getNewValue();
						p.setName( s );
					}
				}
				);


		itemTable.getColumns().add(itemColumn);

		// Price column

		TableColumn<Item,Double> priceColumn = new TableColumn<Item, Double>("Price");

		priceColumn.setPrefWidth(128);

		priceColumn.setCellValueFactory( 
				new PropertyValueFactory<Item, Double>("price")
				);

		priceColumn.setCellFactory(TextFieldTableCell.<Item,Double>forTableColumn(new DoubleStringConverter()) );

		priceColumn.setOnEditCommit(
				new EventHandler< CellEditEvent<Item, Double> >() 
				{
					public void handle( CellEditEvent<Item, Double> t ) 
					{
						int rowNum = t.getTablePosition().getRow();
						Item i = (Item) t.getTableView().getItems().get(rowNum);
						double p = t.getNewValue();
						i.setPrice(p);
					}


				}
				);

		itemTable.getColumns().add(priceColumn);

		//Quantity column

		TableColumn<Item,Integer> qtyColumn = new TableColumn<Item, Integer>("QTY");

		qtyColumn.setPrefWidth(128);

		qtyColumn.setCellValueFactory( 
				new PropertyValueFactory<Item, Integer>("qty")
				);

		qtyColumn.setCellFactory(TextFieldTableCell.<Item,Integer>forTableColumn(new IntegerStringConverter()) );

		qtyColumn.setOnEditCommit(
				new EventHandler< CellEditEvent<Item, Integer> >() 
				{
					public void handle( CellEditEvent<Item, Integer> t ) 
					{
						int rowNum = t.getTablePosition().getRow();
						Item i = (Item) t.getTableView().getItems().get(rowNum);
						int q =  t.getNewValue();
						i.setQty(q);
					}


				}
				);

		itemTable.getColumns().add(qtyColumn);

		// manual entry

		final TextField itemNameField = new TextField();
		itemNameField.setPromptText("Item Name");

		final TextField priceField = new TextField();
		priceField.setPromptText("Price");

		final TextField qtyField = new TextField();
		qtyField.setPromptText("qty");

		Button itemButton = new Button();
		itemButton.setText("add");
		itemButton.setPrefWidth( 100 );
		itemButton.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				String n = itemNameField.getText();
				double p = Double.parseDouble(priceField.getText());
				int q = Integer.parseInt(qtyField.getText());
				Item i = new Item(n,p,q);
				itemList.add( i );
			}
				}
				);

		Button totalButton = new Button();
		totalButton.setText("Total Price: $0");
		totalButton.setMinWidth( 150 );
		totalButton.setOnAction( new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				double totalPrice = 0;

				for(Item i: itemList){
					totalPrice += i.getPrice() * i.getQty();
				}

				totalButton.setText("Total Price: $"+ df.format(totalPrice));
			}
				}
				);

		mainBox.getChildren().addAll( itemNameField, priceField, qtyField, itemButton,totalButton );

		theStage.setScene(scene);
		theStage.show();

	}

}
