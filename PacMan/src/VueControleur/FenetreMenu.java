package VueControleur;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import modele.Jeu;


public class FenetreMenu extends JFrame
{
	
	private int timeUpdate, nbFantome, nbVies, timer;
	
	Color couleurCouloir;
	
	public FenetreMenu()
	{
	    this.setSize(500, 500);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    
	    initialisationParametresJeu();
	    
	    MenuPanel panel = new MenuPanel(this, timeUpdate, nbFantome, nbVies, timer);
	    this.setContentPane(panel);
	    this.setLayout(null);
	    this.setVisible(true);
	}
	
	

	public void initialisationParametresJeu()
	{
		int[] settings = SettingsFileHandler.readSettings("settings.txt");
		if(settings.length > 0)
		{
			timeUpdate = settings[0];
			nbFantome = settings[1];
			nbVies = settings[2];
			timer = settings[3];
		}
		
	}

}





class MenuPanel extends JPanel {
 
    private static final long serialVersionUID = 1L;
 
	private Image image;
	private int width, height;
	private int timeUpdate, nbFantome, nbVies, timer;
     
	JFrame fenetre;
	
    public MenuPanel(JFrame fen, int timeUp, int nbF, int nbV, int tim)
    {    	
    	fenetre = fen;
    	timeUpdate = timeUp;
    	nbFantome = nbF;
    	nbVies = nbV;
    	timer = tim;
    	
    	BufferedImage bImage;
		try {
			bImage = ImageIO.read(new File("Images/pacman_menu.jpg"));
			width = bImage.getWidth();
			height = bImage.getHeight();
			image = bImage;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		this.setLayout(null);
		
		addButton();
    }
     
    public void addButton()
    {
    	JButton start = new JButton("");
        JButton settings = new JButton("");
        
        start.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	Jeu jeu = new Jeu(timeUpdate, nbFantome, nbVies, timer);
                
                VueControleurPacMan vc = new VueControleurPacMan(Jeu.SIZE_X, Jeu.SIZE_Y);

                jeu.addObserver(vc);
                vc.setJeu(jeu);         
                vc.setVisible(true);
                               
                jeu.start();
               
                fenetre.dispose();
               
            }
        });
        
        settings.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	FenetreSettings fen = new FenetreSettings(timeUpdate, nbFantome, nbVies, timer);
                fenetre.dispose();
            }
        });
        
        start.setBounds(155, 350, 170, 30);
        start.setOpaque(false);
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);
        
        settings.setBounds(155, 385, 170, 30);
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        
        add(start);
        add(settings);
    }
    
    
    public void paintComponent(Graphics g) {
    	g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
