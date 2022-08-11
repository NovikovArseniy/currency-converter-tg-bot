package botApp.enums;

public enum Currencies {
	RUB("RUB"),
	USD("USD"),
	EUR("EUR"),
	GBP("GBP"),
	CNY("CNY");
	
	private final String name;
	
	private Currencies(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
