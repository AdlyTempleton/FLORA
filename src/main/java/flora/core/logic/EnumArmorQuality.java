package flora.core.logic;

public enum EnumArmorQuality{

	LEADSTONE(250, "Leadstone", 4),
	HARDENED(2500, "Hardened", 5),
	REDSTONE(25000, "Redstone", 6),
	RESONANT(250000, "Resonant", 7);

	public int storage;
	public String name;
	public int protection;
	EnumArmorQuality(int storage, String name, int protection){
		this.storage=storage;
		this.name=name;
		this.protection=protection;
	}
}
