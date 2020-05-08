package VueControleur;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import modele.Direction;
import modele.Entite;
import modele.Fantome;
import modele.Jeu;
import modele.Pacman;
import modele.Murs;
import modele.PacGum;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class VueControleurPacMan extends JFrame implements Observer {

    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

        
    private ImageIcon[] iconePacMan_classique;
    private ImageIcon[] iconePacMan_aTraversMur;
    private ImageIcon[] iconePacMan_invisible;
    private ImageIcon[] iconePacMan_mangeur;
    
    private ImageIcon icoFantome;
    private ImageIcon icoCouloir;
    private ImageIcon icoMurs;
    private ImageIcon icoGumClassique;
    private ImageIcon icoGumInvisible;
    private ImageIcon icoGumMangeur;
    private ImageIcon icoGumATraversMur;

    private int nbVies, nbFantome, timer, timeUpdate;    
 
	Color couleurCouloir;
    
    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associé à une icône, suivant ce qui est présent dans la partie modèle)

    private JLabel scoreLabel, viesLabel, timerLabel;
    private JLabel[] iconesVies;
    
    public VueControleurPacMan(int sizeX_, int sizeY_) {
    	
        sizeX = sizeX_;
        sizeY = sizeY_;

        chargerLesIcones();

        ajouterEcouteurClavier();
    }
    
    
    public VueControleurPacMan(Color cC, int nbF)
    {
		couleurCouloir = cC;
		nbFantome = nbF;
    }
    
    public void setJeu(Jeu jeu_) { 
    	this.jeu = jeu_; 
    	placerLesComposantsGraphiques();
    }
    
   
    private void ajouterEcouteurClavier() {

        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                
                switch(e.getKeyCode()) {  // on écoute les flèches de direction du clavier
                    case KeyEvent.VK_LEFT : jeu.getPacman().setDirection(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.getPacman().setDirection(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.getPacman().setDirection(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.getPacman().setDirection(Direction.haut); break;
                }
            }
        });

    }

   
    private void chargerLesIcones() {
        iconePacMan_classique = chargerIconePacman(iconePacMan_classique, "Images/Pacman_classique");
        iconePacMan_aTraversMur = chargerIconePacman(iconePacMan_aTraversMur, "Images/Pacman_passeMur");
        iconePacMan_invisible = chargerIconePacman(iconePacMan_invisible, "Images/Pacman_invisible");
        iconePacMan_mangeur = chargerIconePacman(iconePacMan_mangeur, "Images/Pacman_mangeur");
        
        icoCouloir = chargerIcone("Images/Couloir.png");
        icoFantome = chargerIcone("Images/fantome_bleu.png");
        icoMurs = chargerIcone("Images/Wall.png");
        icoGumClassique = chargerIcone("Images/gum_classique.png");
        icoGumInvisible = chargerIcone("Images/gum_invisible.png");
        icoGumMangeur = chargerIcone("Images/gum_mangeur.png");
        icoGumATraversMur = chargerIcone("Images/gum_a_travers_mur.png");

    }
    
    private ImageIcon[] chargerIconePacman(ImageIcon[] tabIcone, String cheminFichier) 
    {
    	
    	tabIcone = new ImageIcon[4];
    	tabIcone[0] = chargerIcone(cheminFichier + "/Pacman_droite.png");
    	tabIcone[1] = chargerIcone(cheminFichier + "/Pacman_haut.png");
    	tabIcone[2] = chargerIcone(cheminFichier + "/Pacman_gauche.png");
    	tabIcone[3] = chargerIcone(cheminFichier + "/Pacman_bas.png");
    	
    	return tabIcone;
    }
     
    private ImageIcon chargerIcone(String urlIcone) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPacMan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(image);
    }

    
    private void placerLesComposantsGraphiques() 
    {
        setTitle("PacMan");
        setSize(300, 300);
	    setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();

        //Score 
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 5, 0, 20);
        scoreLabel = new JLabel("Score : ");
        
        panel.add(scoreLabel, c);
        
        // Timer 

        c.gridx = 1;
        c.insets = new Insets(0, 20, 0, 20);
        timerLabel = new JLabel(Integer.toString(timer));
        panel.add(timerLabel, c);
        
        //Vies
        
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 0, 0, 0);

        viesLabel = new JLabel("Vies : ");
        panel.add(viesLabel, c);
        
        //Icones Vies
        
        c.insets = new Insets(0, 0, 0, 0);
        iconesVies = new JLabel[jeu.getNbVies()];
        for(int i=0; i<jeu.getNbVies(); i++)
        {
        	c.gridx = i+3; //Doit commencer a la 4�me colonne
        	c.gridy = 0;
        	c.weightx = 0;
        	c.fill = GridBagConstraints.NONE;
        	c.anchor = GridBagConstraints.EAST;
        	JLabel label = new JLabel();
        	label.setIcon(iconePacMan_classique[0]);
        	iconesVies[i] = label;
        	panel.add(label, c);
        }
        
        // Grille
        
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 10;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        
        JComponent grilleJLabels = new JPanel(new GridLayout(sizeX, sizeY)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        grilleJLabels.setBackground(Color.darkGray);
        tabJLabel = new JLabel[sizeX][sizeY];
        
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                JLabel jlab = new JLabel();
                tabJLabel[y][x] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
                
            }
        }
        
        
        panel.add(grilleJLabels, c);
        
        add(panel);
  
    }
    
    
    private ImageIcon[] choisirCouleurPacman()
    {
    	ImageIcon[] iconePacMan = new ImageIcon[4];
    	
    	if(jeu.getPacman().estInvisible())
    		iconePacMan = iconePacMan_invisible;
    	
    	else if(jeu.getPacman().passeATraversMur())
    		iconePacMan = iconePacMan_aTraversMur;
    	
    	else if(jeu.getPacman().estMangeur())
    		iconePacMan = iconePacMan_mangeur;
    	
    	else
    		iconePacMan = iconePacMan_classique;
    	
    	return iconePacMan;
    }
    
    
    private void afficherInfos()
    {
    	afficherTimer();
    	
	    scoreLabel.setText("Score : " + jeu.getCompteurGumMangee() * 10);
		
        viesLabel.setText("Vies : "); 
        for(int i = 0; i<(iconesVies.length - jeu.getNbVies()); i++) 
        	iconesVies[iconesVies.length - i - 1].setVisible(false);
	
    }
    
    private void afficherTimer()
    {
    	int timer = jeu.getTimer();
    	Color couleur;
    	
    	if(timer < 15)
    		couleur = Color.red;
    	else
    		couleur = Color.black;
    	
    	timerLabel.setText(Integer.toString(jeu.getTimer() / 60) + ":" + Integer.toString(jeu.getTimer() % 60));
    	timerLabel.setForeground(couleur);
    }
    
    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    
    
    private void mettreAJourAffichage() {
    	
    	ImageIcon[] iconePacMan = choisirCouleurPacman();
    	
    	if(!(jeu.getPause()))
    	{
	        for (int x = 0; x < sizeX; x++) 
	        {
	            for (int y = 0; y < sizeY; y++) {
	                if (jeu.getGrille()[x][y] instanceof Pacman) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
	                    tabJLabel[x][y].setIcon(iconePacMan[Direction.valueOf(jeu.getGrille()[x][y].getDirection().toString()).ordinal()]); 
	                    														//il y a une image diff�rente pour chaque direction du Pacman, on utilise donc la direction pour la choisir
	                    
	                } else if (jeu.getGrille()[x][y] instanceof Fantome && ((Entite) jeu.getGrille()[x][y]).estEnVie()) {
	                    tabJLabel[x][y].setIcon(icoFantome);
	                    
	                } else if (jeu.getGrilleObjets()[x][y] instanceof Murs){
	                	tabJLabel[x][y].setIcon(icoMurs);
	                	
	                } else if (jeu.getGrilleObjets()[x][y] instanceof PacGum) {
	                	switch(((PacGum) jeu.getGrilleObjets()[x][y]).getPacGumType())
	                	{
	                	case classique : tabJLabel[x][y].setIcon(icoGumClassique); break;
	                	case invisible : tabJLabel[x][y].setIcon(icoGumInvisible); break;
	                	case mangeur : tabJLabel[x][y].setIcon(icoGumMangeur); break;
	                	case aTraversMur :tabJLabel[x][y].setIcon(icoGumATraversMur); break;
	                	}
	                	
	                	
	                } else {
	                        tabJLabel[x][y].setIcon(icoCouloir);
	                }
	
	            }
	        }
	        
	        afficherInfos();
	      
    	}
        
    	else
    	{
    		for (int x = 0; x < sizeX; x++) 
	        {
	            for (int y = 0; y < sizeY; y++) {
	                if (jeu.getGrille()[x][y] instanceof Pacman) 
	                { 
	                	tabJLabel[x][y].setIcon(icoCouloir);
	                	try {
							Thread.sleep(200); 
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	                	tabJLabel[x][y].setIcon(iconePacMan[Direction.valueOf(jeu.getGrille()[x][y].getDirection().toString()).ordinal()]);														
	                } 
	
	            }
	        }
    	}
    }

    @Override
    public void update(Observable o, Object arg) {
        
        mettreAJourAffichage();
        
        if(jeu.aTermine())
        	dispose();
        
        
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
       */
        
    }

}
