/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VueControleur.FenetreFinDePartie;
import VueControleur.FenetreMenu;
import VueControleur.PacGumHandler;
import VueControleur.PacManPowerHandler;
import VueControleur.PauseManager;
import VueControleur.TimerHandler;
import VueControleur.VueControleurPacMan;

/** La classe Jeu a deux fonctions 
 *  (1) Gérer les aspects du jeu : condition de défaite, victoire, nombre de vies
 *  (2) Gérer les coordonnées des entités du monde : déplacements, collisions, perception des entités, ... 
 *
 * @author freder
 */
public class Jeu extends Observable implements Runnable {

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 10;
    
    private Pacman pacMan;
    
    private PacGumHandler pacGumHandler;
    
    private PacManPowerHandler pacmanPowerHandler;
    
    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    
    private Objets[][] grilleObjets = new Objets[SIZE_X][SIZE_Y];
    
    private boolean perdu, terminee, peutFermerLaFenetre;    
    
    private int compteurGum, compteurGumMangee;
    
	private int timeUpdate, nbFantome, nbViesInit, timerInit;
	private int timer, nbVies;

    private boolean pause;
  
    
    public Jeu() {
    	perdu = terminee = peutFermerLaFenetre = false;
    	compteurGum = 0;
    	compteurGumMangee = 0;
    	nbVies = 3;
    	
    	initialisationDesObjets();
        initialisationDesEntites();
    }
    
    public Jeu(int timeUp, int nbF, int nbV, int tim)
    {
    	timeUpdate = timeUp;
    	nbFantome = nbF;
    	nbVies = nbViesInit =  nbV;
    	timerInit = timer = tim;
    	
    	perdu = terminee = peutFermerLaFenetre = false;
    	compteurGum = 0;
    	compteurGumMangee = 0;

    	initialisationDesObjets();
        initialisationDesEntites();
    }
    
    
    //Getters & Setters
    
    public int getSizeX() { return SIZE_X; }
    
    public int getSizeY() {	return SIZE_Y; }
    
    public boolean aTermine() { return peutFermerLaFenetre; }

    public Entite[][] getGrille() { return grilleEntites; }
    
    public Objets[][] getGrilleObjets(){ return grilleObjets; }
    public void setGrilleObjets(Objets[][] grille){ grilleObjets = grille; }
    
    public HashMap<Entite, Point> getHashMap(){ return map; }
    
    public Pacman getPacman() { return pacMan; }
    public void setPacman(Pacman pac) { pacMan = pac; }
    
    public int getCompteurGumMangee() { return compteurGumMangee; }
    
    public int getNbVies() { return nbVies; }
    
    public int getNbViesInit() { return nbViesInit; }
    
    public int getTimer() { return timer; }
    public void setTimer(int t) { timer = t; }
    
    public int getTimerInit() { return timerInit; }
    public void setTimerInit(int t) { timerInit = t; }

    public boolean getPause() { return pause; }
    public void setPause(boolean p) { pause = p; }
    
    
    private int aleatoireEntre(int low, int high)
    {
    	return new Random().nextInt(high-low)+low;
    }
    
    //Methodes d'initilisation
    
    private void initialisationDesEntites() {
        
    	
        pacMan = new Pacman(this);
        resetPositionPacman();
        
        for(int i =0; i< nbFantome; i++)
        {
	        Fantome f = new Fantome(this);
	       
	        int randx = aleatoireEntre(SIZE_X/2, SIZE_X);
	        int randy = aleatoireEntre(SIZE_Y/2, SIZE_Y);
	        
	        while(grilleEntites[randx][randy] instanceof Pacman
	        		|| grilleObjets[randx][randy] instanceof Murs)
	        {
	        	randx = (int) (Math.random() * SIZE_X);
	            randy = (int) (Math.random() * SIZE_Y);
	        }
	        
	        grilleEntites[randx][randy] = f;
	        map.put(f, new Point(randx, randy));   
        }
    }
    
    private void initialisationDesObjets()
    {
    	initialisationDesMurs();
    	initialisationDesGums();
    	initialisationTimer();
    }
    
    
    private void initialisationDesMurs()
    {
    	Murs m = new Murs();
    	grilleObjets[4][0] = m;
    	grilleObjets[4][1] = m;
    	grilleObjets[4][3] = m;

    	grilleObjets[1][1] = m;
    	grilleObjets[2][1] = m;
    	grilleObjets[1][2] = m;
    	grilleObjets[1][3] = m;
    	
    	grilleObjets[1][5] = m;
    	grilleObjets[1][6] = m;
    	grilleObjets[1][7] = m;
    	grilleObjets[1][8] = m;
    	grilleObjets[2][8] = m;

    	grilleObjets[3][3] = m;
    	grilleObjets[3][5] = m;
    	grilleObjets[3][6] = m;

    	
    	grilleObjets[4][8] = m;
    	grilleObjets[4][9] = m;
    	
    	grilleObjets[6][1] = m;
    	grilleObjets[7][1] = m;
    	grilleObjets[8][1] = m;
    	grilleObjets[8][2] = m;
    	grilleObjets[8][3] = m;
    	grilleObjets[8][4] = m;

    	
    	grilleObjets[6][3] = m;
    	grilleObjets[6][5] = m;
    	
    	grilleObjets[8][6] = m;
    	grilleObjets[8][7] = m;
    	grilleObjets[8][8] = m;
    	grilleObjets[7][8] = m;
    	
    	grilleObjets[5][5] = m;
    	grilleObjets[5][6] = m;
    	grilleObjets[6][6] = m;

    	grilleObjets[5][8] = m;
    }
    
    private void initialisationDesGums()
    {
    	PacGum gum = new PacGum(PacGumType.classique);
    	
    	for(int i=0; i<SIZE_X; i++)
    	{
    		for(int j=0; j<SIZE_Y; j++)
    		{
    			if(!(grilleObjets[i][j] instanceof Murs))
    			{	
    				grilleObjets[i][j] = gum;
    				compteurGum++;
    			}
    		}
    		
    	}
    	
    	pacmanPowerHandler = new PacManPowerHandler(this);
    	pacGumHandler = new PacGumHandler(this);
    	pacGumHandler.start();
    }
    
    private void initialisationTimer()
    {
    	TimerHandler timerHandler = new TimerHandler(this, timerInit);
    	timerHandler.start();
    }

    private void resetPositionPacman()
    {
    	grilleEntites[0][0] = pacMan;
        map.put(pacMan, new Point(0, 0));
        
        refresh();
        
        if(grilleObjets[0][0] instanceof PacGum)
		{
			grilleObjets[0][0]= null; //Mange la gum sur la premi�re case quand le Pacman respawn
			compteurGumMangee++;
		}
        
    }
    //Methodes de jeu 
    
    
    /** Permet a une entité  de percevoir sont environnement proche et de définir sa strétégie de déplacement 
     * (fonctionalité utilisée dans choixDirection() de Fantôme)
     */
    public Object regarderEntiteDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return entiteALaPosition(calculerPointCible(positionEntite, d));
    }
    
    public Objets regarderObjetsDansLaDirection(Entite e, Direction d)
    {
    	Point positionEntite = map.get(e);
    	return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    
    /** Si le déclacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) 
    {        
        boolean retour;
        
        Point pCourant = map.get(e);
        Point pCible = new Point();
        pCible = calculerPointCible(pCourant, d);
        
        if(e instanceof Pacman)
        {	
        	if (contenuDansGrille(pCible) && (entiteALaPosition(pCible) == null || pacMan.estInvisible() || pacMan.estMangeur())
                    && (!presenceMur(pCible) || (pacMan.passeATraversMur()))) 
			{
        		deplacerEntite(pCourant, pCible, e);
        		retour = true;
			} 
			
		
			else if(entiteALaPosition(pCible) instanceof Fantome && !(pacMan.estMangeur())) //Si le Pacman atterit sur le Fantome et inversement
			{    
				retour = true;
				nbVies--;
			
				grilleEntites[pCourant.x][pCourant.y] = null;
				
				resetPositionPacman();
				
				pause();
			}
			
			else if(entiteALaPosition(pCible) instanceof Fantome && pacMan.estMangeur())
			{
				((Entite) entiteALaPosition(pCible)).setEstEnVie(false);
				retour = true;
			}
				
			else 
				retour = false;
			
			return retour;
        }
        
        else
        {
        	if (contenuDansGrille(pCible) && (entiteALaPosition(pCible) == null || (entiteALaPosition(pCible) instanceof Pacman) && pacMan.estInvisible())
                    && (!presenceMur(pCible))) 
			{
        		deplacerEntite(pCourant, pCible, e);
        		retour = true;
			} 
						
			else if(entiteALaPosition(pCible) instanceof Pacman && !pacMan.estMangeur()) //Si le Pacman atterit sur le Fantome et inversement
			{    
				retour = true;
				nbVies--;
				
				grilleEntites[pCourant.x][pCourant.y] = null;
				grilleEntites[pCible.x][pCible.y] = e;
				map.put(e, pCible);
				
				resetPositionPacman();
				pause();
			}
			
			else if(entiteALaPosition(pCible) instanceof Pacman && pacMan.estMangeur())
			{
				retour = true;
				e.setEstEnVie(false);
			}
			else retour = false;
			
			return retour;
        }       
    }
    
    
    public Point calculerPointCible(Point pCourant, Direction d) 
    {
        Point pCible = null;
        
        switch(d) 
        {
            case haut: 
            	if(pCourant.y == 0)
            	 	pCible = new Point(pCourant.x, SIZE_Y-1);
            	else
            		pCible = new Point(pCourant.x, pCourant.y - 1); 
            	break;
            	
            case bas : 
            	if(pCourant.y == SIZE_Y-1)
            		pCible = new Point(pCourant.x, 0);
            	else
            		pCible = new Point(pCourant.x, pCourant.y + 1); 
            	break;
            	
            case gauche : 
            	if(pCourant.x == 0)
            		pCible = new Point(SIZE_X - 1, pCourant.y);
            	else
            		pCible = new Point(pCourant.x - 1, pCourant.y);
            	break;
            	
            case droite :
            	if(pCourant.x == SIZE_X -1)
            		pCible = new Point(0, pCourant.y); 
            	else 
            		pCible = new Point(pCourant.x + 1, pCourant.y); 
            	break;     
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) 
    {
       	Entite backUpEntite = grilleEntites[pCible.x][pCible.y];
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
        
        if(e instanceof Pacman)                          
        {
        	if(grilleObjets[pCible.x][pCible.y] instanceof PacGum)
        	{
        		switch(((PacGum)grilleObjets[pCible.x][pCible.y]).getPacGumType())
        		{
        		case classique :
        			compteurGumMangee++; 
					if(compteurGumMangee == compteurGum)
						terminee = true; 
        			break;
        						
        		case invisible : 
        			pacMan.setInvisible(true); 
        			pacmanPowerHandler.start(); 
        			if(pacGumHandler.getGumClassiqueRemplacee())
            		{
            			compteurGumMangee++; 
    					if(compteurGumMangee == compteurGum)
    						terminee = true; 
            		}
        			
        			break;
        			
        		case aTraversMur : 
        			pacMan.setATraversMur(true); 
        			pacmanPowerHandler.start(); 
        			if(pacGumHandler.getGumClassiqueRemplacee())
            		{
            			compteurGumMangee++; 
    					if(compteurGumMangee == compteurGum)
    						terminee = true; 
            		
            		}
        			break;
        			
        		case mangeur : 
        			pacMan.setMangeur(true); 
        			pacmanPowerHandler.start(); 
        			if(pacGumHandler.getGumClassiqueRemplacee())
            		{
            			compteurGumMangee++; 
    					if(compteurGumMangee == compteurGum)
    						terminee = true; 
            		
            		}
        			break;
        		}
        		
        		grilleObjets[pCible.x][pCible.y] = null;        		
        	}
        		
        	
        	if(backUpEntite instanceof Fantome && pacMan.estInvisible()) //Sers a reafficher le Fantome une fois que PacMan lui est pass� dessus et qu'il est invisible														
        		map.put(backUpEntite, pCible);
        	
        	else if(backUpEntite instanceof Fantome && pacMan.estMangeur())
        		backUpEntite.setEstEnVie(false);
        	
        }
    }


    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) 
    {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Object entiteALaPosition(Point p) 
    {
        Object retour = null;
        
        if (contenuDansGrille(p)) 
            retour = grilleEntites[p.x][p.y];
        
        
        return retour;
    }
    
    private Objets objetALaPosition(Point p)
    {
    	Objets retour = null;
    	
    	if(contenuDansGrille(p))
    		retour = grilleObjets[p.x][p.y];
    	
    	return retour;
    }
    
    private boolean presenceMur(Point p)
    {
    	return grilleObjets[p.x][p.y] instanceof Murs;
    }
    
    public void refresh()
    {
    	setChanged();
    	notifyObservers();
    }
    
    public void pause()
    {
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	PauseManager pm = new PauseManager(this);
    	pause = true;
    	synchronized (this) {
    		try {
    			pm.start();
    			wait();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
    	pause = false;
    }
    
    /**
     * Un processus est créé et lancé, celui-ci execute la fonction run()
     */
    public void start() {
        new Thread(this).start();
       
    }

    @Override
    public void run() {

        while (!perdu && !terminee) {
        	if(!pause) 
        	{
	            for (Entite e : map.keySet()) { // déclenchement de l'activité des entités, map.keySet() correspond à la liste des entités
	                if(e.estEnVie())
	                	e.run(); 
	            }
	            
	            refresh(); // notification de l'observer pour le raffraichisssement graphique
	            
	            if(compteurGumMangee == compteurGum)
	            	terminee = true;
	            
	            else if(nbVies <= 0 || timer <= 0)
	            	perdu = true;
	           
	            
	            try {
	                Thread.sleep(600 - timeUpdate); 
	            } catch (InterruptedException ex) {
	                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
	            }
        	}
        }
        
        if(perdu)
    	{
        	FenetreFinDePartie fenetre = new FenetreFinDePartie(false, 
        														timeUpdate, 
        														nbFantome, 
        														timerInit, 
        														nbViesInit);        
        	peutFermerLaFenetre = true;
    	}
        
        else if (terminee)
        {
        	FenetreFinDePartie fenetre = new FenetreFinDePartie(true, 
        														timeUpdate, 
        														nbFantome, 
        														timerInit, 
        														nbViesInit);
        	peutFermerLaFenetre = true;
        }
        
        refresh();
    }
    
    

}
