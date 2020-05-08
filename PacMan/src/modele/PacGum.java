package modele;


public class PacGum extends Objets{
	private PacGumType gumType;
	
	public PacGum(PacGumType type)
	{
		gumType = type;
	}
	
	public PacGumType getPacGumType()
	{
		return gumType;
	}
}
