/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author freder
 */
public abstract class Entite implements Runnable {

    protected Jeu jeu;
    protected Direction d;
    private boolean estEnVie;
    public abstract void choixDirection(); // stratégie de déclassement définie dans les sous-classes, concernant Pacman, ce sont les évènements clavier qui définissent la direction
    
    public void avancerDirectionChoisie() {
        jeu.deplacerEntite(this, d);
    }
    
    
    public Entite(Jeu _jeu) {
        jeu = _jeu;
        estEnVie = true;
    }
    
    public Direction getDirection() { return d; }
    
    public boolean estEnVie() { return estEnVie; }
    public void setEstEnVie(boolean b) { estEnVie = b; }
    
    
    @Override
    public void run() {
        choixDirection();
        avancerDirectionChoisie();
    }
    
}
