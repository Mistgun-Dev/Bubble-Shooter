/*
-------BUBBLE-SHOOTER GAME-------
Projet JAVA 
Professeur : FX Talgorn
Année : 2015/2016
ROZARIO BELFORT ET NEHARI MOHAMED
*/

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class Main extends Application {

    static Scene scene;
    static Group main_root = new Group();
    static Menu menu = new Menu();
    static ImageView picture_background;
    

    @Override
    public void start(Stage Stage) 
    { 
        scene = new Scene(main_root, 1024,639,Color.LIGHTBLUE); 
        Stage.setTitle("Bubble Shooter");
        menu.load(Stage);
        Stage.show();     
    }
    
    //Fonction Main
     public static void main(String[] args) 
     {
        Application.launch(Main.class, args);
     }
}

