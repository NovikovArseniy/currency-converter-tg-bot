package botApp.enums;

public enum ButtonNameEnum {

	CURRENCY_RATES("Курсы валют"),
	CONVERT("Конвертер");
	
	private final String buttonName;
	
	private ButtonNameEnum(String buttonName) {
		this.buttonName = buttonName;
	}
	
	public String getButtonName() {
		return buttonName;
	}
}
