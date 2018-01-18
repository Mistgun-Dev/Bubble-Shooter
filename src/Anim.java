import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.application.Platform;

public class Anim {
    
    ImageView picture_toto, picture_game_over, picture_happy, picture_mood, picture_win;
    Image image_toto , image_game_over, image_happy, image_mood, image_win;
    double totoX, game_overX;
    double totoY, game_overY;
    long time_animation;

    Anim()
    {
        this.time_animation = 2000; // durée d'apparition des têtes
    }
    
    //Fonction qui affiche l'image Gam Over si on perd la partie
    public void print_game_over()
    {     
        this.picture_game_over = new ImageView();
        this.image_game_over= new Image(getClass().getResourceAsStream("pictures/game/animation/game_over.png"));
        this.game_overX = ((Main.scene.getWidth() /2) - (this.image_game_over.getWidth() /2));
        this.game_overY = ((Main.scene.getHeight()/2) - (this.image_game_over.getHeight() /2));
        this.picture_game_over.setX(game_overX);
        this.picture_game_over.setY(game_overY);
        this.picture_game_over.setImage (image_game_over); 
        
        this.picture_game_over.setVisible(true);
        Game.game_root.getChildren().add(this.picture_game_over); 
    }
    
    //Fonction qui affiche le GIF Win si on gagne la partie
    public void print_win()
    {     
        this.picture_win = new ImageView();
        this.image_win= new Image(getClass().getResourceAsStream("pictures/game/animation/win.gif"));
        this.picture_win.setX(Game.game_scene.getWidth()/2 - image_win.getWidth()/2);
        this.picture_win.setY(Game.game_scene.getHeight()/2 - image_win.getHeight()/2);
        this.picture_win.setImage (image_win); 
        
        this.picture_win.setVisible(true);
        Game.game_root.getChildren().add(this.picture_win); 
    }
    
    //Fonction qui affiche le GIF du perso animé lorsqu'on tire une bulle
    public void print_toto()
    {
        if(this.picture_mood != null || this.picture_toto != null)
       {
            this.picture_toto.setVisible(false);
            this.picture_toto = null;
            this.picture_mood.setVisible(false);
            this.picture_mood = null;
        }
        this.picture_toto = new ImageView();
        
        this.image_toto = new Image(getClass().getResourceAsStream("pictures/game/animation/toto.gif"));
        this.totoX = ((Main.scene.getWidth() /2) - 200);
        this.totoY = (Main.scene.getHeight() - this.image_toto.getHeight());
        this.picture_toto.setX(totoX);
        this.picture_toto.setY(totoY);
        this.picture_toto.setImage (image_toto);
        this.picture_toto.setVisible(true);
        
        Game.game_root.getChildren().add(this.picture_toto);
        
        this.picture_mood = new ImageView();
        this.image_mood = new Image(getClass().getResourceAsStream("pictures/game/animation/normal.png"));
        this.picture_mood.setX((this.picture_toto.getX() + (this.image_toto.getWidth() /2)) - (this.image_mood.getWidth()/2)+3 );
        this.picture_mood.setY(this.totoY - this.image_mood.getHeight() + 65);
        this.picture_mood.setVisible(true);
        this.picture_mood.setImage (image_mood);     
        
        
        
        this.picture_mood.setRotate(-3);
        
        Game.game_root.getChildren().add(this.picture_mood);
    }
    
    //Fonction qui affiche les humeurs du perso en fonction de l'état du jeu (win, game_over, bulle détruite)
    public void print_toto_mood(String mood)
    {
       if(this.picture_mood != null)
       {
            this.picture_mood.setVisible(false);
            this.picture_mood = null;
        }

        this.picture_mood = new ImageView();
        this.image_mood = new Image(getClass().getResourceAsStream("pictures/game/animation/"+mood+".png"));
        if(mood.compareTo("happy") != 0)
        {
            this.picture_mood.setX((this.picture_toto.getX() + (this.image_toto.getWidth() /2)) - (this.image_mood.getWidth()/2)+3 );
            this.picture_mood.setY(this.totoY - this.image_mood.getHeight() + 65);
        }
        else
        {
            this.picture_mood.setX((this.picture_toto.getX() + (this.image_toto.getWidth() /2)) - (this.image_mood.getWidth()/2) );
            this.picture_mood.setY(this.totoY - this.image_mood.getHeight() + 68);
        }
        this.picture_mood.setImage (image_mood);
        
        this.picture_mood.setRotate(-3);
        Game.game_root.getChildren().add(this.picture_mood);
        
        if(mood.compareTo("sad") != 0) // tant qu'on a pas perdu on continue
        {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() 
                        {
                            @Override
                            public void run() 
                            {  
                                Platform.runLater(new Runnable() 
                                {
                                    @Override
                                    public void run() 
                                    {
                                         picture_mood.setVisible(false);
                                         image_mood = new Image(getClass().getResourceAsStream("pictures/game/animation/normal.png"));
                                         picture_mood.setX((picture_toto.getX() + (image_toto.getWidth() /2)) - (image_mood.getWidth()/2)+3 );
                                         picture_mood.setY(totoY - image_mood.getHeight() + 65);
                                         picture_mood.setImage (image_mood);     
                                         picture_mood.setRotate(-3);
                                         picture_mood.setVisible(true);
                                         Game.game_root.getChildren().add(picture_mood);
                                    }
                                });    
                            }
                        }, 
                         this.time_animation
                        );
        }
    }
    
    
}
