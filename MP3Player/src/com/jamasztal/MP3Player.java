package com.jamasztal;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import javafx.event.*;

import javafx.geometry.*;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;

import javafx.scene.media.*;
import javafx.util.*;

import java.net.*;
import java.io.*;
import java.util.*;

// JavaFX documentation: http://docs.oracle.com/javafx/2/api/

/**
 * @author Lee Stemkoski
 */
public class MP3Player extends Application 
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
        theStage.setTitle("Sound Example");

        Group root = new Group();
        Scene theScene = new Scene(root);

        VBox mainBox = new VBox();
        mainBox.setPadding(new Insets(10));
        mainBox.setSpacing(10);            
        mainBox.setAlignment(Pos.CENTER_LEFT);
           

        root.getChildren().add(mainBox);

        // code -------------------------------------------------

        Text info = new Text();
            info.setText("Media Player!");


        final MediaView mediaView = new MediaView();

        ChangeListener mediaListener = new ChangeListener()
            {
                public void changed(ObservableValue o, Object oldValue, Object newValue)
                {
                    if ( newValue.equals(MediaPlayer.Status.READY) )
                    {
                        System.out.println( "READY FOR ACTION!" );
                    }
                }
            };

        // when the size of something changes, resize the window appropriately.
        ChangeListener resizeListener = new ChangeListener()
            {
                public void changed(ObservableValue o, Object oldValue, Object newValue)
                {
                    theStage.sizeToScene();
                }
            };

        mainBox.getChildren().addAll(mediaView, info);
        try
        {	
            File        mediaFile   = new File("media/main-theme.mp3");
            // File        mediaFile   = new File("media/speedrun.mp4");
            URL         mediaURL    = mediaFile.toURI().toURL();
            Media       mediaObject = new Media( mediaURL.toString());
            MediaPlayer mediaPlayer = new MediaPlayer( mediaObject );
            mediaView.setMediaPlayer( mediaPlayer );

            mediaPlayer.statusProperty().addListener( mediaListener );

            mainBox.widthProperty().addListener( resizeListener );
            mainBox.heightProperty().addListener( resizeListener );
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Button playButton = new Button();
            playButton.setText("Play");
            playButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        mediaView.getMediaPlayer().play();
                    }
                });
         

        Button pauseButton = new Button();
            pauseButton.setText("Pause");
            pauseButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        mediaView.getMediaPlayer().pause();
                    }
                });

        Button stopButton =new Button();
            stopButton.setText("Stop");
            stopButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        mediaView.getMediaPlayer().stop();
                    }
                });
         

        Button muteButton = new Button();
            muteButton.setText("Mute");
            muteButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        boolean currentlyMuted = mediaView.getMediaPlayer().isMute();
                        mediaView.getMediaPlayer().setMute( !currentlyMuted );
                    }
                });
         

        Button infoButton = new Button();
            infoButton.setText("Info");
            infoButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        MediaPlayer mp = mediaView.getMediaPlayer();
                        System.out.println( "Start time: " + mp.getStartTime().toMillis() );
                        System.out.println( "Stop time: " + mp.getStopTime().toMillis() );
                        System.out.println( "Current time: " + mp.getCurrentTime().toMillis() );

                        Map<String,Object> m = mp.getMedia().getMetadata();
                        for ( Map.Entry<String, Object> e : m.entrySet() ) 
                        {
                            System.out.println(e.getKey() + "=" + e.getValue() );
                        }
                    }
                });
            

        final TextField volumeField = new TextField();
            volumeField.setText("0.85");
          
        Button volumeButton = new Button();
            volumeButton.setText("Set Volume");
            volumeButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        double v = Double.parseDouble( volumeField.getText() );
                        mediaView.getMediaPlayer().setVolume(v);
                    }
                });
            

        final TextField seekField = new TextField();
            seekField.setText("0.85");
           
        Button seekButton = new Button();
            seekButton.setText("Seek Percentage");
            seekButton.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        double seekPercent = Double.parseDouble( seekField.getText() );
                        double stopTime = mediaView.getMediaPlayer().getStopTime().toMillis();
                        double seekTime = seekPercent * stopTime;
                        Duration seekDuration = new Duration( seekTime );
                        mediaView.getMediaPlayer().seek( seekDuration );
                    }
                });
         

        Button rateDouble = new Button();
        rateDouble.setText("X2");
           rateDouble.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        mediaView.getMediaPlayer().setRate(2);
                    }
                });
          
        Button rateNormal = new Button();
        rateNormal.setText("X1");
          rateNormal.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        mediaView.getMediaPlayer().setRate(1);
                    }
                });
            
        Button rateHalf = new Button();
        rateHalf.setText("X0.5");
            rateHalf.setOnAction( new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        mediaView.getMediaPlayer().setRate(0.5);
                    }
                });
            
            
        // to display the current time...
        final TextField timeField = new TextField();
            timeField.setText(" ");
         
        // set up a binding involving a formula
        StringBinding timeExtractor = new StringBinding()
            {
                // initializer for anonymous inner class, works as a constructor.
                // whatever oberservable this is bound to, when the observable changes, this changes also.
                {
                    super.bind( mediaView.getMediaPlayer().currentTimeProperty() );
                }

                @Override
                protected String computeValue()
                {
                    MediaPlayer mp = mediaView.getMediaPlayer();
                    double milliSec = mp.getCurrentTime().toMillis();
                    return milliSec + " ms ";
                }
            };
        timeField.textProperty().bind( timeExtractor );    

        HBox row1 = new HBox();
        row1.getChildren().addAll( playButton, pauseButton, stopButton, muteButton, infoButton );
        HBox row2 = new HBox();
        row2.getChildren().addAll( rateDouble, rateNormal, rateHalf );
        
        mainBox.getChildren().addAll( row1,
            new Separator(),
            row2,
            new Separator(),
            volumeField, volumeButton,
            new Separator(),
            seekField, seekButton,
            new Separator(),
            timeField );

        // code -------------------------------------------------

        theStage.setScene(theScene);
        theStage.show();
    }
}

