import java.net.URL;
import javafx.scene.media.AudioClip;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

//Classe qui gère le nuage de bulles
public class CloudBubbles extends Bubble {
    
    Bubble[][] nuage;
    int width_cloud;
    int height_cloud;
    int bubble_same_color;
    int score;
    int number_of_floor;
    int browse_neighbor; // var qui parcour le tableau "neighbor_color" pour utiliser la récursivité (doit garder sa valeur)
                         // au moment de la récursivité, donc a déclarer comme attribut et non en variable local
    int[] selected_bubble;
    static int limit_shoot;
    
    //Constructeur qui construis le nuage en fonction du nombre d'étage passer en paramètre
    public CloudBubbles(int number_floor)
    {
        selected_bubble = new int[Game.difficulty];
        fill_array_selected_bubble(this.selected_bubble);
        this.width_cloud = size_width();
        this.height_cloud = size_height();        
        nuage = new Bubble[this.height_cloud][this.width_cloud];
        this.score = 0;
        print_score();
        this.bubble_same_color = 1;
        this.browse_neighbor =1;
        this.number_of_floor = number_floor;
        fill();
    }
    
    //Fonction qui réinitialise le nuage de bulle 
    public void reset()
    {
        for(int i=0; i<this.height_cloud; i++)
        {
            for(int j=0; j<this.width_cloud; j++)
            {
                if(this.nuage[i][j] != null)
                {
                this.nuage[i][j].remove(); 
                this.nuage[i][j]= null;
                }
            }
        }
        this.score = 0;
        print_score();
        this.browse_neighbor =1;
        fill_array_selected_bubble(this.selected_bubble);
        fill();
    }
    
    //Fonction qui renvoie le nombre de bulles que la fenêtre peut acceuillir en horizontal
    int size_width()
    {
        return ((int)Game.game_scene.getWidth() / (int)this.image_bubble.getWidth()) -1;
    }
    
    //Fonction qui renvoie le nombre de bulles que la fenêtre peut acceuillir en vertical
    int size_height()
    {
        return (int)Game.game_scene.getHeight() / (int)this.image_bubble.getHeight();
    }
    
    /*Fonction qui renvoe 1 si "x" est déja dans le tableau en paramètre (sert a choisir des couleurs aléatoire 
      en fonction de la difficultée)*/
    int doublon(int[] tab, int x)
    {
        for(int i=0;i< Game.difficulty;i++)
        {
            if(tab[i] == x)
                return 1;
        }
        return 0;         
    }

    //Fonction qui rempli le tableau en paramètre, de couleur aléatoire parmis celle qu'il y a en tout, en fonction de la difficultée    
    void fill_array_selected_bubble(int[] tab)
    {
        for(int i=0; i<Game.difficulty; i++)
            tab[i] = -1;

        int x = random();
        tab[0] = x;
        
        for(int i=1; i<Game.difficulty; i++)
        {
            while(doublon(tab,x) == 1)
            {
                x = random();
            }
            
            tab[i] = x;
        }
    }

    // Fonction qui choisi une couleur parmis les couleurs présente dans le tableau "selected_bubble"
    int choose_bubble_on_selection(int[] tab)
    {
        int x = 0 + (int)(Math.random() * ((Game.difficulty-1 - 0) + 1));
        return tab[x]; 
    }
    
    //Fonction qui rempli le nuage de bulle de bulles aléatoire en fonction de la difficultée
    void fill()
    {
        int i,j,k = 0;
        double posX = 24;
        double posY = 0;    
        
        for(i=0;i<this.height_cloud;i++)
        {
            for(j=0;j<this.width_cloud;j++)
            {
                if(i<this.number_of_floor)
                {
                    int choice = choose_bubble_on_selection(this.selected_bubble);
                    this.nuage[i][j] = new Bubble(choice);
                    this.nuage[i][j].setPosition(posX,posY);
                }
                else
                    this.nuage[i][j] = null;

                posX += (this.bubble_width ) ; 
            }
            j++;
            posY += this.bubble_height;
            if(i%2 == 0)
              posX = this.bubble_width /2 +24 ;
            else
                posX = 24;
        }
    }
    
    //Fonction qui intègre une bulle tirée, au nuage de bulles, et la positionne au bon endroit en fonction de l'endroit de sa collision
    public void add(Bubble obj)
    {
        int k = 0;
        double column, line;
        int i = 0;
        line = 0.0;
        // calculer i
        while(k<this.height_cloud)
        {
            if(obj.bubbleY >= line && obj.bubbleY < line + this.bubble_height)
            {
                i = k+1;
                k = this.height_cloud;
            }
            line += this.bubble_height;
            k+=1;
        }
 
            double grid = this.bubble_width/2;
            if(i%2 == 0)
                 column = 24.0;
            else
                column = grid +24;
            
            //bien positionner la boule en absicce
            k = 0;
            while(k < this.width_cloud)
            {
                
                if(obj.bubbleX + grid >= column && obj.bubbleX + grid < column + this.bubble_width)
                {
                        obj.bubbleX = column;  
                        obj.bubbleY = i * this.bubble_height ; 
                }
                
                column += (this.bubble_width )  ;
                k+=1;
            }
            
            int j = ((int)obj.bubbleX / (int)this.bubble_width) ; // calculer j
            
            /*
            //bien positionner la boule en ordonnée
            k = 0;
            grid = this.bubble_height/2;
            if(i%2 == 0)
                 line = 0.0;
            else
                line = 0.0;
            while(k < this.height_cloud)
            {
                
                if(obj.bubbleY + grid >= line && obj.bubbleY + grid < line + this.bubble_height)
                {  
                    obj.bubbleY = line;    
                }
                
                line += (this.bubble_height ) ;
                k+=1;
            }
            
*/
            this.nuage[i][j] = new Bubble(obj.couleur);
            this.nuage[i][j].setPosition(obj.bubbleX,obj.bubbleY);
            this.nuage[i][j].print();

            destroy_bubble_same_color(i,j); // detruire bulle de meme couleur
            
            if(Game.bubble_shoot.compteur_bubble_shoot >= limit_shoot) // abaisser le nuage au bout de 25 coups
            {
                down_the_cloud();
                Game.bubble_shoot.compteur_bubble_shoot = 0;
            }
            
            if(test_game_over() == true)
            {  
                Game.animation.print_toto_mood("sad");
                Game.animation.print_game_over();
                Game.blink_reset();
                Game.win_game = -1;
                Game.bubble_shoot_stop = true;
            }
            else if(test_win() == true)
            {
                Game.animation.print_toto_mood("happy");
                Game.animation.print_win();
                Game.blink_reset();
                Game.win_game = 1;
                Game.bubble_shoot_stop = true;
            }
            else
                Game.bubble_shoot_stop = false; // on reprend le cour de la partie en débloquant la souris
            
    }
    
    //Fonction qui renvoie 1 si il y a doublon des coordonnées d'une bulle dans le tableau passé en paramètre
    public int doublon_voisin(int[][]tab, int x, int y)
    {
        for(int i=0; i < this.height_cloud*this.width_cloud; i++)
        {
                if(tab[i][0] == x && tab[i][1] == y)
                    return 1;
        }
        return 0;
    }
    
    //Fonction qui recherche récursivement les bulles de mêmes couleur collé entre elles, à l'aide de la fonction doublon_voisin
    public void search_bubble_same_color(int i, int j, int[][] neighbor_color)
    {
        
        int compteur = 0;
        
        //bulle haut
        if(i>0 && this.nuage[i-1][j] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i-1][j].couleur)
            {
                if(doublon_voisin(neighbor_color, i-1 ,j) == 0 ) 
                {
                    neighbor_color[this.bubble_same_color][0] = i-1 ; neighbor_color [this.bubble_same_color][1] = j;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
        //----------------------------
        //bulle droite
        if( j< this.width_cloud-1 && this.nuage[i][j+1] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i][j+1].couleur)
            {
                if(doublon_voisin(neighbor_color, i ,j+1) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i ; neighbor_color [this.bubble_same_color][1] = j+1;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
        //----------------------------
        //bulle gauche
        if( j>0 && this.nuage[i][j-1] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i][j-1].couleur)
            {
                if(doublon_voisin(neighbor_color, i ,j-1) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i ; neighbor_color [this.bubble_same_color][1] = j-1;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
       
        //----------------------------
        //bulle bas
        if(i<this.height_cloud-1 && this.nuage[i+1][j] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i+1][j].couleur)
            {
                if(doublon_voisin(neighbor_color, i+1 ,j) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i+1 ; neighbor_color [this.bubble_same_color][1] = j;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
        //----------------------------
        //bulle haut droite que pour ligne modulo 2 = 1
        if(j < this.width_cloud-1 && i > 0 && i%2 == 1 && this.nuage[i-1][j+1] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i-1][j+1].couleur)
            {
                if(doublon_voisin(neighbor_color, i-1 ,j+1) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i-1 ; neighbor_color [this.bubble_same_color][1] = j+1;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
        //----------------------------
        //bulle bas droite pour ligne modulo 2 = 1
         if(j < this.width_cloud-1 && i < this.height_cloud-1 && i%2 == 1 && this.nuage[i+1][j+1] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i+1][j+1].couleur)
            {
                if(doublon_voisin(neighbor_color, i+1 ,j+1) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i+1 ; neighbor_color [this.bubble_same_color][1] = j+1;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
         //----------------------------
         //bulle haut gauche pour ligne modulo 2 = 0
         if(j > 0 && i > 0 && i%2 == 0 && this.nuage[i-1][j-1] != null )
        {
            if(this.nuage[i][j].couleur  == this.nuage[i-1][j-1].couleur)
            {
                if(doublon_voisin(neighbor_color, i-1 ,j-1) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i-1 ; neighbor_color [this.bubble_same_color][1] = j-1;
                    this.bubble_same_color++; 
                    compteur++;
                } 
            }
        }
         //----------------------------
         //bulle bas gauche pour ligne modulo 2 = 0
          if(j > 0 && i < this.height_cloud-1 && i%2 == 0 && this.nuage[i+1][j-1] != null)
        {
            if(this.nuage[i][j].couleur  == this.nuage[i+1][j-1].couleur)
            {
                if(doublon_voisin(neighbor_color, i+1 ,j-1) == 0) // on verifie si la boulle n'est pas déja dans le tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i+1 ; neighbor_color [this.bubble_same_color][1] = j-1;
                    this.bubble_same_color++; 
                    compteur++;
                }
            }
        }
         //----------------------------
         
         if(compteur == 0)
             return;
         
         else
          {
              while(this.browse_neighbor<this.bubble_same_color)
              {
                search_bubble_same_color(neighbor_color[browse_neighbor][0], neighbor_color[browse_neighbor][1] ,neighbor_color);
                this.browse_neighbor++;
              }
          }
    }
    
    //Fonction qui affiche le score (en fonction de bubble_same_color) qui représentele nombre de bulle de même couleur qui seront éclatées 
    void print_score()
    {
        if( this.bubble_same_color >= 3)
            this.score +=  this.bubble_same_color*10;
        Game.scoreText.setFont(new Font(35));
        Game.scoreText.setFill(Color.LIGHTBLUE);
        Game.scoreText.setX(750);
        Game.scoreText.setY(580);
        Game.scoreText.setText("SCORE : "+ this.score); 
    }
    
    //Fonction qui initialise un tableau 2d passé en paramètre avec -1
    void initializate_array(int[][]tab)
    {
        for(int i=0; i < this.height_cloud*this.width_cloud; i++)
        {
                tab[i][0] =  -1;
                tab[i][1] =  -1;
        }
    }
    
    /*Fonction qui détruit les bulles de même couleur collée entre elles si elle sont > ou = à 3, après execution de la fonction
      search_bubble_same_color*/
    public void destroy_bubble_same_color(int i,int j)
    {
        
        int[][] neighbor_color = new int[this.height_cloud*this.width_cloud][2];
        initializate_array(neighbor_color);
        neighbor_color[0][0] = i ; neighbor_color[0][1] = j; // on enrengistre la boule tiré 

        final URL resource = getClass().getResource("/music/destroy_bubble.wav");
        final AudioClip destroy_bubble = new AudioClip(resource.toString());       
        
        search_bubble_same_color(i , j, neighbor_color);
        
        // si plus de trois bulle trouvée alors on les explose
        if(this.bubble_same_color >= 3)
        {
            for(i=0;i<this.bubble_same_color;i++)
            {
                if(this.bubble_same_color <=3)
                    Game.animation.print_toto_mood("pleased");   
                else
                    Game.animation.print_toto_mood("happy"); 

                destroy_bubble.play();
                this.nuage[neighbor_color[i][0]][neighbor_color[i][1]].remove(); //suppresiion des bulles
                this.nuage[neighbor_color[i][0]][neighbor_color[i][1]] = null;
            }
        }
        
       neighbor_color = null;
       print_score();
       destroy_bubble_alone();   
       this.bubble_same_color = 1;
       this.browse_neighbor = 1;
    }
    
    /*Fonction qui détruit les bulles seules, qui tiennent dans le vide (marche une fois sur deux)
      ne détruit pas les tas de bulles qui tiennent dans le vide*/
    public void destroy_bubble_alone()
    {
        for(int i=1;i<this.height_cloud-1;i++)
        {
            for(int j=1;j<width_cloud-1; j++)
            {
                if(i%2 == 0)
                {
                    if(this.nuage[i][j] != null)
                    {
                        if(this.nuage[i-1][j]   == null &&
                           this.nuage[i-1][j-1] == null &&
                           this.nuage[i][j-1]   == null &&
                           this.nuage[i][j+1]   == null &&
                           this.nuage[i+1][j-1] == null &&
                           this.nuage[i+1][j] == null)
                        {
                            this.nuage[i][j].remove();
                            this.nuage[i][j] = null;
                        }
                    }
                }
            }
        }
    }
    
    //Fonction qui test si la partie est perdu, en testant si le seuil limite est atteint pas une bulle
    public boolean test_game_over()
    {
        for(int i=9;i==9;i++)
        {
            for(int j=0 ; j<this.width_cloud; j++)
            {
                if(this.nuage[i][j] != null)
                    return true;
            }
        }
        return false;
    }
    
    //Fonction qui test si la partie est gagner
    public boolean test_win()
    {
        for(int i=0;i<this.height_cloud;i++)
        {
           for(int j=0;j<this.width_cloud; j++)
           {
               if(this.nuage[i][j] != null)
               {
                   if(this.nuage[i][j].afficher != -1)
                       return false;
               }
           }
        }
        return true;
    }
    
    //Fonction qui descend le nuage de bulle d'un étage en fonction du nombre de coup tiré
    public void down_the_cloud()
    {
        for(int i = this.height_cloud-2 ; i>=0; i--)
        {
            for(int j = this.width_cloud-1 ; j>=0 ; j--)
            {
                if(this.nuage[i][j] != null)
                {
                    this.nuage[i+1][j] = null;
                    this.nuage[i+1][j] = new Bubble(this.nuage[i][j].couleur);
                    this.nuage[i+1][j].picture_bubble.setVisible(false);
                  
                    
                    if(i%2 == 0)
                        //this.nuage[i+1][j].bubbleX = this.nuage[i][j].picture_bubble.getX() + 25;
                        this.nuage[i+1][j].bubbleX = this.nuage[i][j].bubbleX + 25;
                    else
                        //this.nuage[i+1][j].bubbleX = this.nuage[i][j].picture_bubble.getX() - 25;
                        this.nuage[i+1][j].bubbleX = this.nuage[i][j].bubbleX - 25;
                    
                    this.nuage[i+1][j].bubbleY = this.nuage[i][j].picture_bubble.getY() + 50;
                    this.nuage[i][j].picture_bubble.setVisible(false);          
                    this.nuage[i+1][j].picture_bubble.setVisible(true);
                    
                    this.nuage[i+1][j].print();
                }
            }
        }
        
        //ajouter une nouvelle ligne au début du nuage
        double posX = 24;
        double posY = 0;
        
        for(int i=0;i<1;i++)
        {
            for(int j=0 ; j<this.width_cloud ;j++)
            {
                int choice = choose_bubble_on_selection(this.selected_bubble);
                this.nuage[i][j] = new Bubble(choice);
                this.nuage[i][j].setPosition(posX,posY);
                posX += this.bubble_width;
                this.nuage[i][j].print();
            }
        }

    }
    
    //Fonction qui affiche le nuage de bulles
    @Override
    public void print()
    {
        int i,j;
        for(i=0;i<this.height_cloud;i++)
        {
            for(j=0;j<this.width_cloud;j++)
            {
                if(this.nuage[i][j] != null)
                {
                    this.nuage[i][j].print();
                }
            }
        }
    }

}
