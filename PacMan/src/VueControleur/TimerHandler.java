package VueControleur;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import modele.Jeu;
import modele.Pacman;

public class TimerHandler extends Observable implements Runnable {

	private int timer;
	private Jeu jeu;
	
	public TimerHandler(Jeu j, int t)
	{
		timer = t;
		jeu = j;
	}
	
	public void start() {

        new Thread(this).start();
    }
	
	@Override
	public void run() {
		while(true)
		{
			try {
	            Thread.sleep(1000); 
	        } catch (InterruptedException ex) {
	            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
	        }
			
			timer--;
			jeu.setTimer(timer);
			jeu.refresh();
		}
		
	}
	
}
