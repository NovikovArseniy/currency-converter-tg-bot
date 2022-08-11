package botApp.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import botApp.data.CurrentDataRepository;
import botApp.keyboards.InlineKeyboardMaker;
import botApp.parsers.CbCurrencyRateParser;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {

	@Autowired
	private CurrentDataRepository currentDataRepository;
	
	@Autowired
	private CbCurrencyRateParser currencyRateParser;
	
	@Autowired
	private InlineKeyboardMaker inlineKeyboardMaker;
	
	public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        
		final String chatId = buttonQuery.getMessage().getChatId().toString();

        String data = buttonQuery.getData();
        
        if (currentDataRepository.get(chatId).size() == 1) {
        	currentDataRepository.get(chatId).add(data);
        	SendMessage sendMessage = new SendMessage(chatId, "Выберите валюту, в которую собираетесь конвертировать");
        	sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtonsOfCurrencies());
            return sendMessage;
        } else if (currentDataRepository.get(chatId).size() == 2) {
            String fromCurrency = currentDataRepository.get(chatId).get(1);
            StringBuilder answerMessage = new StringBuilder();
            answerMessage.append(currentDataRepository.getValue(chatId))
            .append(" ")
            .append(currentDataRepository.get(chatId).get(1))
            .append(" = ")
            .append(currencyRateParser.convert(currentDataRepository.getValue(chatId), fromCurrency, data))
            .append(" ")
            .append(data);
            currentDataRepository.deleteKey(chatId);
            return new SendMessage(chatId, answerMessage.toString());
        } else {
        	return new SendMessage(chatId, "Unexpected error");
        }
	}
}
