import java.net.URL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Bubble{
    
    ImageView picture_bubble;
    Image image_bubble;
    String COULEUR[] = {"rouge","vert","bleu","violet","jaune","orange"};
    int afficher;
    int couleur;
    boolean collision_brute ;
    double bubble_width, bubble_height;
    double bubbleX;
    double bubbleY;
    double moveX,moveY;
    double angle_bubble;
    int compteur_bubble_shoot;
    int speed = 7;
    
    //constructeur pour bulles aléatoire
    Bubble()
    {   
        int rnd = random();
        this.picture_bubble = new ImageView();
        this.image_bubble = new Image(getClass().getResourceAsStream("pictures/game/bubble/"+this.COULEUR[rnd]+".png"));
        this.picture_bubble.setImage(image_bubble);
        this.afficher = 1;
        this.couleur = rnd;
        this.bubble_width = this.image_bubble.getWidth();
        this.bubble_height = this.image_bubble.getHeight();
        this.collision_brute = false;
    }
    
    //constructeur pour bulle avec couleur prédéfinie pour l ajout d'une bulle au nuage 
    Bubble(int couleur)
    {
        this.couleur = couleur;
        this.picture_bubble = new ImageView();
        this.image_bubble = new Image(getClass().getResourceAsStream("pictures/game/bubble/"+this.COULEUR[couleur]+".png"));
        this.picture_bubble.setImage(image_bubble);
        this.bubble_width = this.image_bubble.getWidth();
        this.bubble_height = this.image_bubble.getHeight();
        this.collision_brute = false;
        this.afficher = 1;
    }
    
    //Fonction qui attribue les positions de la bulle
    public void setPosition(double X, double Y)
    {
        this.bubbleX = X;
        this.bubbleY = Y;
    }
    
    //Fonction qui choisie un chiffre entre 0 et 5 pour choisir une couleur aléatoire dans le tableau "COULEUR"
    public int random()
    {
        return 0 + (int)(Math.random() * ((5 - 0) + 1));
    }
    
    //Fonction qui charge ou recharge la première bulle du canon
    public void reload(int couleur)
    {
        this.compteur_bubble_shoot++;      
        this.couleur = couleur;
        this.image_bubble = new Image(getClass().getResourceAsStream("pictures/game/bubble/"+this.COULEUR[couleur]+".png"));
        this.bubbleX = Canon.picture_canon.getX() + (Canon.image_canon.getWidth()/2) - (this.image_bubble.getWidth()/2);
        this.bubbleY = Canon.picture_canon.getY()- (this.image_bubble.getHeight()/2) - 3;
        this.picture_bubble.setX(this.bubbleX) ;
        this.picture_bubble.setY(this.bubbleY) ;
        this.picture_bubble.setImage(this.image_bubble);
    }
    
    //Fonctoin qui recharge la deuxièmme bulle du canon
    public void second_reload(int couleur)
    {
        this.couleur = couleur;
        this.image_bubble = new Image(getClass().getResourceAsStream("pictures/game/bubble/"+this.COULEUR[couleur]+".png"));
        this.bubbleX = Canon.picture_canon.getX() + (Canon.image_canon.getWidth()/2) - (this.image_bubble.getWidth()/2)+8 ;
        this.bubbleY = Canon.picture_canon.getY() + (Canon.image_canon.getHeight()/2) - (this.image_bubble.getHeight()/2) ;
        this.picture_bubble.setX(this.bubbleX) ;
        this.picture_bubble.setY(this.bubbleY) ;
        this.picture_bubble.setFitWidth(34);
        this.picture_bubble.setFitHeight(34);
        this.picture_bubble.setImage(this.image_bubble);
    }
    
    /*Fonction qui recharge la troisième bulle du canon, "couleur" passé en paramètre est le résultat d'un tirage aléatoire
    parmis des couleurs elle mêmes choisi en fonction de la difficultée*/
    public void third_reload(int couleur)
    {
        this.couleur = couleur;
        this.image_bubble = new Image(getClass().getResourceAsStream("pictures/game/bubble/"+this.COULEUR[couleur]+".png"));
        this.bubbleX = Canon.picture_canon.getX() + (Canon.image_canon.getWidth()/2) - (this.image_bubble.getWidth()/2) +10;
        this.bubbleY = Canon.picture_canon.getY() + (Canon.image_canon.getHeight()/2) - (this.image_bubble.getHeight()/2) + 48;
        this.picture_bubble.setX(this.bubbleX) ;
        this.picture_bubble.setY(this.bubbleY) ;
        this.picture_bubble.setFitWidth(30);
        this.picture_bubble.setFitHeight(30);
        this.picture_bubble.setImage(this.image_bubble);
    }
    
    
    // Fonction qui renvoie un chiffre selon le type de collision (bord, plafond, ou bulle)
    public int collision()
    {
        this.bubbleX = this.picture_bubble.getX();
        this.bubbleY = this.picture_bubble.getY();
        
        //Collision avec bord droit et bord gauche
        if(bubbleX <= 0 || bubbleX + this.image_bubble.getWidth() >= Game.game_scene.getWidth())
            return 1;
        //Collision avec plafond
        if(bubbleY <= 0)
        {
            Game.bubble_shoot_stop = false;
            return 2;
        }
        if(collision_bubble() == 1)
        { 
            Game.nuage.add(Game.bubble_shoot); //Ajouter la bulle tiré au nuage de bulles
            return 3;
        }          
        return 0;
    }

    /*Fonction qui teste si il y a une collision entre deux bulles (si il y a collision brute, la fonction est censé éfféctuée
      un test de colision plus minutieux par pixel mais cette deuxième paartie n'est pas fonctionnelle)*/
    public int collision_bubble()
    {
        double bubbleX = this.picture_bubble.getX();
        double bubbleY = this.picture_bubble.getY();
        double width = Game.nuage.width_cloud;
        double height = Game.nuage.height_cloud;
        int i,j;
        
        
        //if(this.collision_brute == false)
        //{
            for(i=0;i<height;i++)
            {
                for(j=0;j<width;j++)
                {   
                    if(Game.nuage.nuage[i][j] != null)
                    {
                        if( bubbleY < (Game.nuage.nuage[i][j].bubbleY + this.bubble_height) && 
                            bubbleX < Game.nuage.nuage[i][j].bubbleX &&
                            bubbleX + this.bubble_width > Game.nuage.nuage[i][j].bubbleX  

                            || bubbleY < (Game.nuage.nuage[i][j].bubbleY + this.bubble_height) && 
                            bubbleX < Game.nuage.nuage[i][j].bubbleX + this.bubble_width  &&
                            bubbleX + this.bubble_width >  Game.nuage.nuage[i][j].bubbleX + this.bubble_width)
                            {
                                this.collision_brute = true;
                                return 1;
                            }
                    }

                }
            }
        //}
     /*   
        // Une fois la collision bruté éfféctuée on passe a la collision par pixel
        if(this.collision_brute == true)
        {
            
           int width_pixel = (int)this.image_bubble.getWidth(); 
           int height_pixel = (int)this.image_bubble.getHeight(); 
           PixelReader reader = this.image_bubble.getPixelReader();
           WritableImage wImage = new WritableImage(
                    (int)this.image_bubble.getWidth(),
                    (int)this.image_bubble.getHeight());
           PixelWriter pixelWriter = wImage.getPixelWriter();

           for (int x = 0; x < height_pixel; x++) 
           {
               for (int y = 0; y < width_pixel; y++) 
               {
                   Color color = reader.getColor(x,y);

                   if(color.equals(Color.BLACK) == true  /*|| 
                           bubbleY+x  <= (Game.nuage.nuage[this.resI][this.resY].bubbleY + this.bubble_height ) && 
                           //bubbleX +y   <= Game.nuage.nuage[this.resI][this.resY].bubbleX  
                           bubbleX  + this.bubble_width  > Game.nuage.nuage[this.resI][this.resY].bubbleX  +y
                           // Si on tire vers la gauche
                           ||color.equals(Color.BLACK) != true
                           && bubbleY+x  < (Game.nuage.nuage[this.resI][this.resY].bubbleY + this.bubble_height)  
                           && bubbleX + y < Game.nuage.nuage[this.resI][this.resY].bubbleX + this.bubble_width -y  
                           //bubbleX + this.bubble_width  >  Game.nuage.nuage[this.resI][this.resY].bubbleX + this.bubble_width )
                           )
                   {
                       this.collision_brute = false;
                       System.exit(0);
                   }
               }

           }
        }
   */
        return 0;
    }
    
    //Fonction qui affiche la bulle 
    public void print()
    {
        this.picture_bubble.setX(this.bubbleX);
        this.picture_bubble.setY(this.bubbleY);
        Game.game_root.getChildren().add(this.picture_bubble); 
    }
    
    //Fonction qui supprime une bulle en réinitialisant ses champs
    public void remove()
    {
        this.picture_bubble.setVisible(false);
        this.afficher = -1;
        this.couleur = -1;
        this.picture_bubble.setX(-100);
        this.picture_bubble.setY(-100);
        this.bubbleX = -100;
        this.bubbleY = -100;
        this.collision_brute = false;
    }
    
    //Fonction qui calcule l'angle pour la trajectoire de la bulle une fois tirées
    public double calculate_angle_bubble(double mouse_clickX, double mouse_clickY)
    {
        double angle_temp;
        bubbleX = this.picture_bubble.getX();
        bubbleY = this.picture_bubble.getY();
        
        double pivotX, pivotY;
        double distX, distY ;
        
        pivotX = Game.bubble_shoot.picture_bubble.getX() + Game.bubble_shoot.image_bubble.getWidth()/2;
        pivotY = Game.bubble_shoot.picture_bubble.getY() + (Game.bubble_shoot.image_bubble.getHeight()/2);
        
        distX = mouse_clickX - pivotX;
        distY = mouse_clickY - pivotY;

        angle_temp = Math.atan2(distY, distX);
        angle_temp = angle_temp * (180/Math.PI);

        if(angle_temp <= -160 || angle_temp > 90)
            angle_temp = -160;
        if(angle_temp >= -20 && angle_temp < 90)
            angle_temp = -20;
        
        return angle_temp;
    }
    
    //Fonction qui déplace la bulle en fonction de l'angle, tant qu'il n'y a pas de collisions entre bulles ou au plafond
    public void move(double mouse_clickX, double mouse_clickY)
    {    
        this.bubbleX = this.picture_bubble.getX();
        this.bubbleY = this.picture_bubble.getY();
   
        Game.bubble_shoot_stop = true; // pour stoper les clik souris pendant le mouvement de balle
        this.angle_bubble = calculate_angle_bubble(mouse_clickX,mouse_clickY);
        
        this.moveX = Math.cos(Math.toRadians(angle_bubble));
        this.moveY = Math.sin(Math.toRadians(angle_bubble));
                
        Timeline move_bubble = new Timeline();
        move_bubble.setCycleCount(Timeline.INDEFINITE);
        KeyFrame bubbleMove = new KeyFrame(Duration.millis(8),//40 // rapide = 20
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
    
                            bubbleX += (moveX * speed);
                            bubbleY += (moveY * speed);

                            //ceci afin de'eviter les depassement d'ecran sur le dernier mouvement de la balle                       
                            if(bubbleY <= 0)
                                bubbleY = 0;
                            if(bubbleX <= 0)
                                bubbleX = 0;
                            if(bubbleX + bubble_width >= Game.game_scene.getWidth())
                                bubbleX = Game.game_scene.getWidth() - bubble_width ;

                            picture_bubble.setX(bubbleX);
                            picture_bubble.setY(bubbleY);
                            int test_collision = collision();
                            if(test_collision == 1)
                            {
                                double new_angle_bubble =  180 - angle_bubble;
                                moveX = Math.cos(Math.toRadians(new_angle_bubble));
                                moveY = Math.sin(Math.toRadians(new_angle_bubble)); 
                                angle_bubble = new_angle_bubble ;
                            }
                            
                            int x = Game.nuage.choose_bubble_on_selection(Game.nuage.selected_bubble);
                            if(test_collision == 2)
                            {
                                move_bubble.stop();
                                reload(Game.second_bubble.couleur);
                                Game.second_bubble.second_reload(Game.third_bubble.couleur);
                                Game.third_bubble.third_reload(x);
                                
                            }
                            if(test_collision == 3)
                            {
                                move_bubble.stop();
                                reload(Game.second_bubble.couleur);  
                                Game.second_bubble.second_reload(Game.third_bubble.couleur);
                                Game.third_bubble.third_reload(x);
                            }                        
                        }                  
                });
        move_bubble.getKeyFrames().add(bubbleMove);
        move_bubble.play();
    }          
}
