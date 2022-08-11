package botApp.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import botApp.data.CurrentDataRepository;
import botApp.enums.ButtonNameEnum;
import botApp.keyboards.InlineKeyboardMaker;
import botApp.keyboards.ReplyKeyboardMaker;
import botApp.parsers.CbCurrencyRateParser;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class MessageHandler {

	//public static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
	@Autowired
	private CbCurrencyRateParser currencyRateParser;
	
	@Autowired
	private ReplyKeyboardMaker replyKeyboardMaker;
	
	@Autowired
	private InlineKeyboardMaker inlineKeyboardMaker;
	
	@Autowired
	private CurrentDataRepository currentDataRepository;
	
    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        
        // TODO switch case
        String inputText = message.getText();
        if (inputText == null) {
        	throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
        	return getStartMessage(chatId);
    	} else if (inputText.equals(ButtonNameEnum.CURRENCY_RATES.getButtonName())) {
    		return getCurrencyRatesList(chatId);
    	} else if (inputText.equals(ButtonNameEnum.CONVERT.getButtonName())) {
    		return new SendMessage(chatId, "Введите количество единиц валюты");
    	} else if (isNumber(inputText)) {
    		return getConvertion(chatId, inputText);
    	} else {
			return getMistakeMessage(chatId);
		}
    }
    
    private SendMessage addMainMenuKeyboard(SendMessage sendMessage) {
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage addInlineKeyboard(SendMessage sendMessage) {
    	sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtonsOfCurrencies());
    	return sendMessage;
    }
    
    private SendMessage getConvertion(String chatId, String text) {
    	currentDataRepository.addValueToKey(chatId, text);
    	SendMessage sendMessage = new SendMessage(chatId, "Выберите валюту, из которой собираетесь конвертировать");
    	return addInlineKeyboard(sendMessage);
    }
    
    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Бот предоставляет курсы валют по данным ЦБ РФ и конвертирует необходимое количество одной валюты в другую.");
        return addMainMenuKeyboard(sendMessage);
    }
    
    private SendMessage getCurrencyRatesList(String chatId) {
    	SendMessage sendMessage = new SendMessage(chatId, currencyRateParser.parseCurrencyRatesList());
    	return addMainMenuKeyboard(sendMessage);
    }
    
    private SendMessage getMistakeMessage(String chatId) {
    	SendMessage sendMessage = new SendMessage(chatId, "Неизвестная команда");
    	return addMainMenuKeyboard(sendMessage);
    }
    
    private boolean isNumber(String text) {
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}