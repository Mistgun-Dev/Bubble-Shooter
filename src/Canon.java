import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

//Classe qui gère le canon et la flèche
public class Canon {
    
    static ImageView picture_canon, picture_arrow, picture_borderline, picture_line;    
    static Image image_canon, image_arrow, image_borderline;  
    static double angle_arrow;
    
    Canon()
    {
        picture_canon = new ImageView();
        image_canon = new Image(getClass().getResourceAsStream("pictures/game/canon/canon.png"));
        picture_canon.setX((Game.game_scene.getWidth() /2) - (image_canon.getWidth() /2));
        picture_canon.setY(Game.game_scene.getHeight() - image_canon.getHeight());
        picture_canon.setImage (image_canon);
        
        picture_arrow = new ImageView();
        image_arrow = new Image(getClass().getResourceAsStream("pictures/game/canon/arrow.png"));
        picture_arrow.setX(picture_canon.getX() + ((image_canon.getWidth()/2) - (image_arrow.getWidth()/2) ) );
        picture_arrow.setY(picture_canon.getY() - 100 );
        picture_arrow.setImage (image_arrow);
        
        picture_borderline = new ImageView();
        image_borderline = new Image(getClass().getResourceAsStream("pictures/game/canon/borderline.png"));
        picture_borderline.setX(0);
        picture_borderline.setY(0);
        picture_borderline.setImage(image_borderline);       
    }
    
    //Fonction qui affiche le canon, la flèche et le borderline
    public void print()
    {
        Game.game_root.getChildren().add(picture_arrow); 
        Game.game_root.getChildren().add(picture_canon); 
        Game.game_root.getChildren().add(picture_borderline);
    }
    
    //Fonction qui fait pivoter la flèche en fonction de la position du curseur
    public void move_arrow(MouseEvent e)
    {
        
        double pivotX, pivotY;
        double mouse_moveX, mouse_moveY;
        double distX, distY ;
        
        pivotX = picture_arrow.getX() + (image_arrow.getWidth()/2) ;
        pivotY = picture_arrow.getY() + (image_arrow.getHeight()/2) ;
          
        mouse_moveX = e.getSceneX();
        mouse_moveY = e.getSceneY();

        distX = mouse_moveX - pivotX;
        distY = mouse_moveY - pivotY;

        angle_arrow = Math.atan2(distY, distX);
        angle_arrow = angle_arrow * (180/Math.PI);

        if(angle_arrow <= -160 || angle_arrow > 90)
            angle_arrow = -160;
        if(angle_arrow >= -20 && angle_arrow < 90)
            angle_arrow = -20;
             
        picture_arrow.setRotate(90 + angle_arrow);
    }
}
