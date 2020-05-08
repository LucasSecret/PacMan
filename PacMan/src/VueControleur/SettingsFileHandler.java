package VueControleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SettingsFileHandler {
	
	public static int[] readSettings(String fileName)
	{
		int[] settingsInt = new int[5];

		try 
		{
			File settingsFile = new File(fileName);
			Scanner reader = new Scanner(settingsFile);
			int cpt = 0;
			
			while(reader.hasNextLine() && cpt<5)
			{
				
				String data = reader.nextLine();
				settingsInt[cpt] = Integer.parseInt(data);
				cpt++;
				
			}
			reader.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Fichier non trouvé");
			e.printStackTrace();
		}
		
		return settingsInt;
	}
	
	
	public static void saveSettings(String fileName, int timeUpdate, int nbFantome, int nbVies, int timer)
	{
		try
		{
			FileWriter writer = new FileWriter(fileName);
			writer.write(timeUpdate + "\n"
					+ nbFantome + "\n"
					+ nbVies + "\n"
					+ timer);
			
			writer.close();
		}
		catch(IOException e )
		{
			e.printStackTrace();
		}
	}
	
	
}
