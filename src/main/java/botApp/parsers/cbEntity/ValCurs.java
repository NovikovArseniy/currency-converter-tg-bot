package botApp.parsers.cbEntity;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValCurs {
	@JacksonXmlProperty(localName = "Date")
	String date;
	@JacksonXmlProperty(localName = "name")
	String name;
	@JacksonXmlProperty(localName = "Valute")
	@JacksonXmlElementWrapper(useWrapping = false)
	List<Valute> valutes;
}
