package VueControleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import modele.Jeu;

public class FenetreFinDePartie extends JFrame {
  
	private int timeUpdate, nbFantome, timer, nbVies;
	Color couleurMur, couleurCouloir;
	
	public FenetreFinDePartie(boolean aGagne)
	{
	    this.setTitle("Partie Terminée !");
	    this.setSize(400, 200);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    
	    PanelFinDePartie panel = new PanelFinDePartie(this, aGagne);

	    this.add(panel, BorderLayout.CENTER);
	    this.setVisible(true);
	}
	
	public FenetreFinDePartie(boolean aGagne, int tUpd, int nbF, int t, int nbV)
	{
		timeUpdate = tUpd;
		timer = t;
		nbFantome = nbF;
		nbVies = nbV;	
		
		this.setTitle("Partie Terminée !");
	    this.setSize(400, 200);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    
	    PanelFinDePartie panel = new PanelFinDePartie(this, aGagne);

	    this.add(panel, BorderLayout.CENTER);
	    this.setVisible(true);
	}
	
	public int getTimeUpdate() { return timeUpdate; }
	public int getNbFantome() { return nbFantome; }
	public int getTimer() { return timer; }
	public int getNbVies() { return nbVies; }
	
}


class PanelFinDePartie extends JPanel
{
	private boolean aGagne;
	private JButton recommencer, quitter, parametres;
	
	private FenetreFinDePartie fenetre;
	
	public PanelFinDePartie(FenetreFinDePartie fen, boolean a)
	{
		fenetre = fen;
		aGagne = a; 
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,0,30,0);
		gbc.weightx = 0.2;
		gbc.weighty = 1;
		
		parametres = new JButton("Paramètres");
		add(parametres, gbc);

		gbc.gridx = 1;
		recommencer = new JButton("Recommencer");
		add(recommencer, gbc);

		gbc.gridx = 2;
		quitter = new JButton("Retour au menu");		
		add(quitter, gbc);

		addActionListener();		
		
	
	}
	
	
	
	
	public void addActionListener()
	{
		recommencer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.dispose();
				
				Jeu jeu = new Jeu(fenetre.getTimeUpdate(), 
								  fenetre.getNbFantome(), 
								  fenetre.getNbVies(), 
								  fenetre.getTimer());
                
                VueControleurPacMan vc = new VueControleurPacMan(Jeu.SIZE_X, Jeu.SIZE_Y);
                
                jeu.addObserver(vc);
                vc.setJeu(jeu);
                vc.setVisible(true);
                
                jeu.start();
                
                
			}
		});
		
		
		parametres.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FenetreSettings(fenetre.getTimeUpdate(), 
									fenetre.getNbFantome(), 
									fenetre.getNbVies(), 
									fenetre.getTimer());
                fenetre.dispose();				
			}
		});
		
		quitter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FenetreMenu();
				fenetre.dispose();
			}
		});
	}
	
	public void paintComponent(Graphics g){                
	         
	    String phrase;
	    if(aGagne)
	    	phrase = "Vous avez gagné!";
	    else
	    	phrase = "Vous avez perdu!";
	    
	    Font font = new Font("Courier", Font.BOLD, 20);
	    g.setFont(font);
	    g.setColor(Color.red);
	    g.drawString(phrase, 100, 40);                
	  } 
	  	
}