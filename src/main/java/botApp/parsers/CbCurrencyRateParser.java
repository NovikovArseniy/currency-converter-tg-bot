package botApp.parsers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Maps;

import botApp.enums.Currencies;
import botApp.parsers.cbEntity.ValCurs;
import botApp.parsers.cbEntity.Valute;

@Component
public class CbCurrencyRateParser {

	String cbUrl = "https://www.cbr.ru/scripts/XML_daily.asp";
	
	public String parseCurrencyRatesList(){
		StringBuilder result = new StringBuilder();
		Map<String, Valute> valMap = getValMap();
		for (Currencies currency : Currencies.values()) {
			if (currency.getName() != "RUB") {
				Valute val = valMap.get(currency.getName());
				result.append("1 ")
				.append(val.getCharCode())
				.append(" = ")
				.append(Double.valueOf(val.getValue().replaceAll(",", "."))/Double.valueOf(val.getNominal()))
				.append(" RUB \n");
			}
		}
		return result.toString();
	}
	
	public Double parseCurrencyRate(String fromCurrency, String toCurrency) {
		Map<String, Valute> valMap = getValMap();
		if (fromCurrency == toCurrency) {
			return Double.valueOf(1);
		} else if (toCurrency.equals("RUB")) {
			Valute valute = valMap.get(fromCurrency);
			return Double.valueOf(valute.getValue().replaceAll(",", "."))/Double.valueOf(valute.getNominal());
		} else if (fromCurrency.equals("RUB")) {
			Valute valute = valMap.get(toCurrency);
			return Double.valueOf(valute.getNominal())/Double.valueOf(valute.getValue().replaceAll(",", "."));
		} else {
			Valute fromValute = valMap.get(fromCurrency);
			Valute toValute = valMap.get(toCurrency);
			return Double.valueOf(fromValute.getValue().replaceAll(",", ".")) 
					* Double.valueOf(toValute.getNominal())
					/ Double.valueOf(fromValute.getNominal()) 
					/ Double.valueOf(toValute.getValue().replaceAll(",", "."));
		}
	}
	
	public Double convert(Double value, String fromCurrency, String toCurrency) {
		return value*parseCurrencyRate(fromCurrency, toCurrency);
	}
	
	private ResponseEntity<String> getResponseEntity(String url){
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		return response;
	}
	private Map<String, Valute> getValMap() {
		ResponseEntity<String> response = getResponseEntity(cbUrl);
		XmlMapper mapper = new XmlMapper();
		ValCurs valCurs = new ValCurs();
		try {
			valCurs = mapper.readValue(response.getBody(), ValCurs.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		List<Valute> valList = valCurs.getValutes();
		Map<String, Valute> valMap = Maps.uniqueIndex(valList, Valute::getCharCode);
		return valMap;
	}
}
