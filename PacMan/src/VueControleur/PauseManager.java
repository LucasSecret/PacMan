package VueControleur;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import modele.Jeu;
import modele.Pacman;

public class PauseManager extends Observable implements Runnable {
	
	private Jeu jeu;
	
	public PauseManager(Jeu j)
	{
		jeu = j;
	}
	
	
	public void start()
	{
		new Thread(this).start();
	}
	
	
	@Override
	public void run() {
		
		
		 try {
	         Thread.sleep(3000); 
	     } catch (InterruptedException ex) {
	         Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
	     }
		 
		 synchronized (jeu) 
		 {
			 jeu.notifyAll();
		 }
		 
	}

}
