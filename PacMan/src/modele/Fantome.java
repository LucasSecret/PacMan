/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.awt.font.NumericShaper.Range;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author freder
 */


public class Fantome extends Entite {

    private Point maPosition;
    
    private HashMap<Point, LinkedList<Point>> mapCouloirs;

    public Fantome(Jeu _jeu) {
        super(_jeu);
       
    	mapCouloirs = getMapCouloir();
    }
    

    
    
    @Override
    public void choixDirection() {
        
    	System.out.println("Choix direction : ");
    	Point pacManPosition = jeu.getHashMap().get(jeu.getPacman());
    	maPosition = jeu.getHashMap().get(this);
    	
    	Stack<Point> cheminASuivre = BFS(jeu.getHashMap().get(this), pacManPosition);
    	
    	if(cheminASuivre != null)
    		calculerDirectionDepuisPremierPoint(cheminASuivre);
    	
    	else if(d == null)
    		d = Direction.values()[(int) (Math.random() * 3)];
    }
    
    private void calculerDirectionDepuisPremierPoint(Stack<Point> stack)
    {
    	if(stack.size() != 0)
    	{
    		Point premierPoint = stack.pop();
        	switch(maPosition.x - premierPoint.x)
        	{
        	case -1:
        		System.out.println("GAUCHE");
        		d = Direction.droite;
        		break;
        	case 1 : 
        		System.out.println("DROITE");
        		d = Direction.gauche;
        		break;
        	case 0:
        		switch(maPosition.y - premierPoint.y)
        		{
        		case -1:
        			System.out.println("BAS");
        			d = Direction.bas;
        			break;
        		case 1:
        			System.out.println("HAUT");
        			d = Direction.haut;
        			break;
        		}
        		break;
        	}
    	}
    	
    }
    
    
    private HashMap<Point, LinkedList<Point>> getMapCouloir()
    {
    	HashMap<Point, LinkedList<Point>> map = new HashMap<Point, LinkedList<Point>>();
    	 
    	//Rajoute tous les points qui ne sont pas des Murs
    	for(int i=0; i<jeu.SIZE_X; i++)
    	{
    		for(int j=0; j<jeu.SIZE_Y; j++)
    		{
    			if(!(jeu.getGrilleObjets()[i][j] instanceof Murs))
    			{
    				map.put(new Point(i, j), new LinkedList<Point>());
    			}
    		}
    	}
    	
    	System.out.println("\n\n\n\n\n");
    	
    	//Rajoute tous les voisins des points entrés précédemment (qui ne sont pas eux aussi des murs)
    	for(Point p : map.keySet())
    	{
    		LinkedList<Point> voisins = new LinkedList<Point>(); 
			
    		for(int i = 0; i<Direction.values().length; i++)
    		{	
    			if((i == Direction.gauche.ordinal() && p.x == 0) || (i == Direction.droite.ordinal() && p.x == jeu.SIZE_X -1))
    				continue;
    			
    			if((i == Direction.haut.ordinal() && p.y == 0) || (i == Direction.bas.ordinal() && p.y == jeu.SIZE_Y-1))
    				continue;
    			
    			Point voisin = jeu.calculerPointCible(p, Direction.values()[i]);
    			
    			if(!(jeu.getGrilleObjets()[voisin.x][voisin.y] instanceof Murs))
    				voisins.add(voisin);
    			
    		}
    		
       		map.put(p, voisins);
    	}
    	
    
    	return map;
    }
    
    private boolean pointsEgaux(Point a, Point b)
    {
    	return (a.x == b.x && a.y == b.y);
    }
    
    private Stack<Point> BFS(Point pointDepart, Point pointArrivee)
    {
    	boolean visites[][] = new boolean[jeu.SIZE_X][jeu.SIZE_Y]; 
    	Point precedent[][] = new Point[jeu.SIZE_X][jeu.SIZE_Y];
    	for(int i=0; i< jeu.SIZE_X-1; i++)
    		for(int j=0; j<jeu.SIZE_Y-1; j++)
    			precedent[i][j] = null;
    	
    	
    	boolean aTrouve = false;
        LinkedList<Point> queue = new LinkedList<Point>(); 

        visites[pointDepart.x][pointDepart.y]=true; 
        precedent[pointDepart.x][pointDepart.y] = null;
        
        queue.add(pointDepart); 
  
        while (queue.size() != 0 && !aTrouve) 
        { 
            Point currentPoint = queue.poll(); 
            System.out.println("Point Courant : " + currentPoint);
            
            if(pointsEgaux(currentPoint, pointArrivee))
            	aTrouve = true;
            
            LinkedList<Point> voisins = mapCouloirs.get(currentPoint);
            System.out.println("Voisins : " );
            
            if(voisins != null)
            {
            	for(Point voisin : voisins) 
	            { 
	            	System.out.println(voisin);
	                if (!visites[voisin.x][voisin.y]) 
	                { 
	                	visites[voisin.x][voisin.y] = true; 
	                	precedent[voisin.x][voisin.y] = currentPoint;
	                    queue.add(voisin); 
	                }              
	            }
            }
        } 
        
        if(aTrouve) //S'il existe, on fais le chemin en sens inverse pour connaitre la direction que doit prendre le fantome
        {
        	//System.out.println("GAGNEEEEEEEE");
        	int compteur = 0;
        	Stack<Point> directions = new Stack<Point>();
        	Point currentPoint = jeu.getHashMap().get(jeu.getPacman());
        	
        	while(currentPoint != pointDepart)
        	{
        		directions.add(currentPoint);
        		currentPoint = precedent[currentPoint.x][currentPoint.y];
        	}
        	
        	return directions;
        }
        
        return null;
    }
}
