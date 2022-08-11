package botApp.parsers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class CurrencyRateParser {
	
	String key = "9ef013a123ae373bb7b466c17ecb02af";
	
	public Double parseCurrencyRate(String pairs) {
		RestTemplate restTemplate = new RestTemplate();
		String resourseURL = "https://currate.ru/api/?get=rates&pairs=" + pairs + "&key=" + key;
		ResponseEntity<String> response = restTemplate.getForEntity(resourseURL, String.class);
		//System.out.println(response);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(response.getBody());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Double value = root.path("data").findValue(pairs).asDouble();
		return value;
	}
	
	//TODO Передавать лист и обобщить
	public String parseCurrencyRatesList(){
		RestTemplate restTemplate = new RestTemplate();
		String resourseURL = "https://currate.ru/api/?get=rates&pairs=USDRUB,EURRUB,GBPRUB,CNYRUB&key=" + key;
		String[] currencyNames = {"RUB", "USD", "EUR", "GBP", "CNY"};
		ResponseEntity<String> response = restTemplate.getForEntity(resourseURL, String.class);
		//System.out.println(response);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(response.getBody());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		StringBuilder result = new StringBuilder();
		for (int i = 1; i < currencyNames.length; i++) {
			result.append("1 ")
			.append(currencyNames[i])
			.append(" = ")
			.append(root.path("data").findValue(currencyNames[i] + currencyNames[0]).asDouble())
			.append(" " + currencyNames[0])
			.append("\n");
		}
		return result.toString();
	}
	
	public Double convert(Double value, String pair) {
		return value*parseCurrencyRate(pair);
	}
}
