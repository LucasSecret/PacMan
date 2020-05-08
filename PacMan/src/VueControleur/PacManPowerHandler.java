package VueControleur;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import modele.Jeu;
import modele.Pacman;

public class PacManPowerHandler extends Observable implements Runnable {

	private Jeu jeu;
	
	public PacManPowerHandler(Jeu j)
	{
		jeu = j;
	}
	
	public void start()
	{
		 new Thread(this).start();	
	}
	
	private void resetPacManPower()
	{
		Pacman pacman = jeu.getPacman();
		if(pacman.estInvisible())
			pacman.setInvisible(false);
		
		else if(pacman.estMangeur())
			pacman.setMangeur(false);
		
		else if(pacman.passeATraversMur())
			pacman.setATraversMur(false);
		
		jeu.setPacman(pacman);
	}
	
	@Override
	public void run() {
		try {
            Thread.sleep(8000); 
        } catch (InterruptedException ex) {
            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		resetPacManPower();
	}

}
