/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Random;



/**
 *
 * @author fred
 */
public class Pacman extends Entite {
	
	private boolean invisible, mangeur, aTraversMur;

    public Pacman(Jeu _jeu) {
        super(_jeu);
        d = Direction.droite;
        
        invisible = mangeur = aTraversMur = false;
    }
    
    public boolean estMangeur() { return mangeur;}
    public void setMangeur(boolean b) { mangeur = b;}
    
    public boolean estInvisible() { return invisible;}
    public void setInvisible(boolean b) {invisible = b;}
    
    public boolean passeATraversMur() { return aTraversMur;}
    public void setATraversMur(boolean b) { aTraversMur = b;}
    
    public void setDirection(Direction _d) {
        d = _d;
    }

    @Override
    public void choixDirection() {
        
    }

}
