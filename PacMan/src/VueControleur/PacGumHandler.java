package VueControleur;

import java.awt.Point;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import modele.Jeu;
import modele.Murs;
import modele.Objets;
import modele.PacGum;
import modele.PacGumType;
import modele.Pacman;

public class PacGumHandler extends Observable implements Runnable {

	private Jeu jeu_;
	Point coordoneesGum;
	boolean gumClassiqueRemplacee;
	
	public PacGumHandler(Jeu jeu)
	{
		jeu_ = jeu;
		gumClassiqueRemplacee = false;
	}
	
	private int nombreAleatoire(int min, int max)
	{
		return  min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public boolean getGumClassiqueRemplacee() { return gumClassiqueRemplacee; }
	
	
	private void creerGumSpeciale()
	{
		int minType = 1;
		int maxType = PacGumType.values().length - 1;
		
		int typeAleatoire = nombreAleatoire(minType, maxType);
		
		PacGum gumSpeciale = new PacGum(PacGumType.values()[typeAleatoire]);
		
		Objets[][] grille = jeu_.getGrilleObjets();
		
		boolean estUneGomme = false;
		while(!estUneGomme)
		{		
			int aleatoireX = nombreAleatoire(1, jeu_.getSizeX()) -1;
			int aleatoireY = nombreAleatoire(1, jeu_.getSizeY()) -1;
			
			if(!(grille[aleatoireX][aleatoireY] instanceof Murs))
			{
				estUneGomme = true;
				coordoneesGum = new Point(aleatoireX, aleatoireY);
			}
			
		}
		
		if(grille[coordoneesGum.x][coordoneesGum.y] instanceof PacGum)
			gumClassiqueRemplacee = true;
		else
			gumClassiqueRemplacee = false;
		
		
		grille[coordoneesGum.x][coordoneesGum.y] = gumSpeciale;
		jeu_.setGrilleObjets(grille);		
	}

	
	
	private void resetGum()
	{
		Objets[][] grille = jeu_.getGrilleObjets();
		if(grille[coordoneesGum.x][coordoneesGum.y] instanceof PacGum && gumClassiqueRemplacee) //Si la gum spéciale n'a pas été mangé et qu'elle a été mis par dessus une gum classique 
		{                                                                                       //alors on remet la gum classique, sinon on remet rien
			grille[coordoneesGum.x][coordoneesGum.y] = new PacGum(PacGumType.classique);																								
		}
		else
			grille[coordoneesGum.x][coordoneesGum.y] = null;
		jeu_.setGrilleObjets(grille);
	}
	
	
	public void start() {
        new Thread(this).start();
    }
	
	
	@Override
	public void run() {
		
		while(true)
		{
			creerGumSpeciale();
			
			jeu_.refresh();
			
			try {
	            Thread.sleep(5000); 
	        } catch (InterruptedException ex) {
	            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
	        }
			
			resetGum();
			jeu_.refresh();
			
			try {
	            Thread.sleep(5000); 
	        } catch (InterruptedException ex) {
	            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
	        }
		
		}
	}

	
}
