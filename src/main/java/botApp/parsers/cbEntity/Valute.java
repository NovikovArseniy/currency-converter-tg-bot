package botApp.parsers.cbEntity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Valute {

	@JacksonXmlProperty(isAttribute = true, localName = "ID")
	String id;
	
	@JacksonXmlProperty(localName = "NumCode")
	String numCode;
	@JacksonXmlProperty(localName = "CharCode")
	String charCode;
	@JacksonXmlProperty(localName = "Nominal")
	String nominal;
	@JacksonXmlProperty(localName = "Name")
	String name;
	@JacksonXmlProperty(localName = "Value")
	String value;
}
