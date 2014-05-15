package flora.core.logic;

public enum EnumArmorQuality{

	LEADSTONE(250, "Leadstone", 3),
	HARDENED(2500, "Hardened", 4),
	REDSTONE(25000, "Restone", 5),
	RESONANT(250000, "Resonant", 6);

	public int storage;
	public String name;
	public int protection;
	EnumArmorQuality(int storage, String name, int protection){
		this.storage=storage;
		this.name=name;
		this.protection=protection;
	}
}
