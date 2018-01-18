import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.*;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.Animation;

public class Game {
    
    static Scene game_scene;
    static Group game_root = new Group();
    static Bubble bubble_shoot, second_bubble, third_bubble;
    static Canon canon;
    static CloudBubbles nuage;
    static Anim animation;
    static Text scoreText = new Text();
    Text text_button = new Text();
    static FadeTransition blink;
    static boolean bubble_shoot_stop = false;
    static int win_game = 0;
    static int difficulty; // représente le nombre de couleur dans le nuage de bulles
    
    ImageView picture_background, picture_home;
    static ImageView picture_reset;
    
    //Fonction qui charge la partie 
    public void load(Stage Stage, String difficulty)
    {
        game_scene = new Scene(game_root, 1024,639); 
        
        if(difficulty == "easy")
        {
            this.difficulty = 2;
            CloudBubbles.limit_shoot = 20;
            nuage = new CloudBubbles(2);
        }
        if(difficulty == "medium")
        {
            this.difficulty = 3;
            CloudBubbles.limit_shoot = 20;
            nuage = new CloudBubbles(3);
        }
        if(difficulty == "hard")
        {
            this.difficulty = 6;
            CloudBubbles.limit_shoot = 26;
            nuage = new CloudBubbles(3);
        }
  
        canon = new Canon();
        bubble_shoot = new Bubble();
        second_bubble = new Bubble();
        third_bubble = new Bubble();
        bubble_shoot.reload(nuage.choose_bubble_on_selection(nuage.selected_bubble));
        second_bubble.second_reload(nuage.choose_bubble_on_selection(nuage.selected_bubble));
        third_bubble.third_reload(nuage.choose_bubble_on_selection(nuage.selected_bubble));
        animation = new Anim();

        print_background();
        animation.print_toto();
        canon.print();
        bubble_shoot.print();
        second_bubble.print();
        third_bubble.print();
        game_root.getChildren().add(scoreText); 
        game_root.getChildren().add(text_button); 
        print_button_reset();
        print_button_home();
        mouse_event_game(Stage); 
        nuage.print();   
        
        Stage.setScene(game_scene);
    }
    
    //Fonction qui affiche le fond d'écran de la partie
    public void print_background()
    {
        picture_background = new ImageView();
        final Image image1 = new Image(getClass().getResourceAsStream("pictures/game/background/background.png"));
        picture_background.setX(-35);
        picture_background.setImage(image1);
        game_root.getChildren().add(picture_background);
    }
    
    //Fonction qui gère les évènement souris du jeu (mouvement de la souris, clik pour tirer...)
    public void mouse_event_game(Stage Stage)
    {
        DropShadow effect = new DropShadow();
        effect.setOffsetY(0.0f);
        effect.setOffsetX(0.0f);
        text_button.setEffect(effect);
        effect.setColor(Color.ORANGE);
            
        game_scene.setOnMousePressed(new EventHandler<MouseEvent>()
        {    
            @Override
            public void handle(MouseEvent e)
            {        
                double mouse_clickX = e.getSceneX();
                double mouse_clickY = e.getSceneY();
                animation.print_toto();
                if(bubble_shoot_stop != true)
                    bubble_shoot.move(mouse_clickX, mouse_clickY);
            }
        });
        
        game_scene.setOnMouseMoved(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {  
                canon.move_arrow(e);
            }
        });

        picture_reset.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {  
                text_button.setFont(new Font(20));
                text_button.setFill(Color.ORANGE);
                text_button.setX(picture_reset.getX() +12);
                text_button.setY(picture_reset.getY() - 8);
                text_button.setText("RESET");
                text_button.setVisible(true);              
                picture_reset.setEffect(effect);
            }
        });

        picture_reset.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {  
                text_button.setVisible(false);
                picture_reset.setEffect(null);
            }
        });

        picture_reset.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                picture_reset.setEffect(null);
                reset_game();
            }
        });
 
        picture_home.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {  
                text_button.setFont(new Font(20));
                text_button.setFill(Color.ORANGE);
                text_button.setX(picture_home.getX()+9);
                text_button.setY(picture_home.getY() - 8);
                text_button.setText("HOME");
                text_button.setVisible(true);
                
                picture_home.setEffect(effect);
            }
        });

        picture_home.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {  
                text_button.setVisible(false);
                picture_home.setEffect(null);
            }
        });

        picture_home.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {  
                picture_home.setEffect(null);
                Stage.setScene(Menu.menu_scene);     
            }
        });
    }
    
    //Fonction qui fait clignoter le bouton reset ue fois que la partie est terminée
    public static void blink_reset()
    {
        blink = new FadeTransition(Duration.millis(200), picture_reset);
        blink.setFromValue(1.0);
        blink.setToValue(0.3);
        blink.setCycleCount(Animation.INDEFINITE);
        blink.setAutoReverse(true);
        blink.play();
    }
     
    //Fonction qui réinitialise les champs principaux pour refaire une partie
    public void reset_game()
    {
        if(win_game == 1)
            animation.picture_win.setVisible(false);
        if(win_game == -1)
            animation.picture_game_over.setVisible(false);
        
        if(bubble_shoot_stop == true)
            blink.stop();
        nuage.reset();
        nuage.print();
        bubble_shoot.compteur_bubble_shoot = 0;
        bubble_shoot.reload(nuage.choose_bubble_on_selection(nuage.selected_bubble));
        second_bubble.second_reload(nuage.choose_bubble_on_selection(nuage.selected_bubble));
        third_bubble.third_reload(nuage.choose_bubble_on_selection(nuage.selected_bubble));
        bubble_shoot_stop = false;
        game_root.getChildren().add(scoreText);  
        
    }

   //Fonction qui affiche le bouton reset    
   public void print_button_reset()
    {
        this.picture_reset = new ImageView();
        final Image image_reset = new Image(getClass().getResourceAsStream("pictures/game/button/reset.png"));
        this.picture_reset.setX(100);
        this.picture_reset.setY(530);
        this.picture_reset.setImage(image_reset);
        
        this.game_root.getChildren().add(this.picture_reset);
    }
   
   //Fonction qui affiche le bouton home
   public void print_button_home()
    {
        this.picture_home = new ImageView();
        final Image image_home = new Image(getClass().getResourceAsStream("pictures/game/button/home.png"));
        this.picture_home.setX(200);
        this.picture_home.setY(530);
        this.picture_home.setImage(image_home);
        
        this.game_root.getChildren().add(this.picture_home);
    }
}
