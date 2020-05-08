package VueControleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modele.Jeu;

public class FenetreSettings extends JFrame{
	

  
	private int timeUpdate, nbFantome, timer, nbVies;
	

	
	public FenetreSettings(int timeUpd, int nbF, int nbV, int t)
	{
		timeUpdate = timeUpd;
		timer = t;
		nbFantome = nbF;
		nbVies = nbV;
		
		this.setTitle("Settings");
	    this.setSize(500, 300);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    
	    PanelSettings panel = new PanelSettings(this, timeUpdate, nbFantome, nbVies, timer);

	    this.add(panel, BorderLayout.CENTER);
	    this.setVisible(true);
	}
}


class PanelSettings extends JPanel
{
	private JButton quitter;
	
	private JFrame fenetre;
	private JSlider sliderVies;
	private JSlider sliderFantome;
	private JSlider sliderRapidite;
	
	private JTextField textTimer;
	
	private int timeUpdate, nbFantome, timer, nbVies;
	
	private GridBagConstraints gbcSettings;
	
	public PanelSettings(JFrame fen, int timeUpd, int nbF, int nbV, int t)
	{
		fenetre = fen;
		timeUpdate = timeUpd;
		timer = t;
		nbFantome = nbF;
		nbVies = nbV;
		gbcSettings = new GridBagConstraints();

		this.setLayout(new GridBagLayout());
		placerComposants();	
		
	
	}
	
	private void placerComposants()
	{
		placerVies();
		placerTimer();
		placerNbFantomes();
		placerRapidite();
		placerBoutonSauvegarde();
	}
	
	
	private void placerVies()
	{
		gbcSettings.anchor = GridBagConstraints.WEST;
		gbcSettings.gridx = 0;
		gbcSettings.gridy = 0;
		JLabel vies = new JLabel("Vies : ");
		add(vies, gbcSettings);
		
		gbcSettings.weighty = 1;
		gbcSettings.gridy = 0;
		gbcSettings.gridx = 1;

		sliderVies = new JSlider(JSlider.HORIZONTAL, 0, 5, nbVies);
        sliderVies.setMajorTickSpacing(1);
        sliderVies.setPaintTicks(true);
        sliderVies.setPaintLabels(true);
		add(sliderVies, gbcSettings);
	}
	
	private void placerTimer()
	{
		gbcSettings.gridx = 0;
		gbcSettings.gridy = 1;
		gbcSettings.insets = new Insets(20, 0, 0, 0);
		gbcSettings.anchor = GridBagConstraints.WEST;

		JLabel time = new JLabel("Timer (s) : ");
		add(time, gbcSettings);
		
		gbcSettings.gridx = 1;
		textTimer = new JTextField(Integer.toString(timer));
		textTimer.setColumns(3);
		add(textTimer, gbcSettings);
	}
	
	private void placerNbFantomes()
	{
		gbcSettings.gridx = 0;
		gbcSettings.gridy = 2;
		gbcSettings.insets = new Insets(20, 0, 0, 20);
		JLabel labelFantome = new JLabel("Nombre d'ennemi : ");
		add(labelFantome, gbcSettings);
		
		gbcSettings.insets = new Insets(20, 0, 0, 0);
		gbcSettings.gridx = 1;
		gbcSettings.gridy = 2;
		sliderFantome = new JSlider(JSlider.HORIZONTAL, 1, 5, nbFantome);
		sliderFantome.setMajorTickSpacing(1);
		sliderFantome.setPaintTicks(true);
		sliderFantome.setPaintLabels(true);
		add(sliderFantome, gbcSettings);
		
	}
	
	private void placerRapidite()
	{
		gbcSettings.gridx = 0;
		gbcSettings.gridy = 3;
		JLabel rapidite = new JLabel("Rapidité du jeu : ");
		add(rapidite, gbcSettings);
		
		gbcSettings.gridx = 1;
		gbcSettings.gridy = 3;
		gbcSettings.gridwidth = 10;
		gbcSettings.fill = GridBagConstraints.HORIZONTAL;
		
		sliderRapidite = new JSlider(JSlider.HORIZONTAL, 0, 400, timeUpdate);
		sliderRapidite.setMajorTickSpacing(200);
		sliderRapidite.setMinorTickSpacing(100);
		sliderRapidite.setPaintTicks(true);
		sliderRapidite.setPaintLabels(true);
		
		
		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
	    table.put (0, new JLabel("Très Lent"));
	    table.put (200, new JLabel("Moyen"));
	    table.put (400, new JLabel("Très rapide")); 
	    sliderRapidite.setLabelTable(table);
	    sliderRapidite.setSnapToTicks(true);
	    
	    add(sliderRapidite, gbcSettings);
	}
	
	private void placerBoutonSauvegarde()
	{
		quitter = new JButton("Sauvegarder");		
		addActionListener();	
		
		gbcSettings.gridy = 4;	
		gbcSettings.gridwidth = 1;
		gbcSettings.insets = new Insets(20, 0, 0, 100);

		add(quitter, gbcSettings);	
	}
	
	
	public void addActionListener()
	{
			
		quitter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveSettings();
				FenetreMenu fenetreMenu = new FenetreMenu();
				fenetre.dispose();
			}
		});
		
		sliderFantome.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				nbFantome = sliderFantome.getValue();
			}
		});
		
		sliderRapidite.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				timeUpdate = sliderRapidite.getValue();
			}
		});
		
		sliderVies.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				nbVies = sliderVies.getValue();
			}
		});
		
	}
	
	private void saveSettings()
	{
		timer = Integer.parseInt(textTimer.getText());
		SettingsFileHandler.saveSettings("settings.txt", timeUpdate, nbFantome, nbVies, timer);
	}
		  	
}