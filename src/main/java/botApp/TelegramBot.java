package botApp;


import java.io.IOException;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import botApp.handlers.CallbackQueryHandler;
import botApp.handlers.MessageHandler;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelegramBot extends SpringWebhookBot {

	String botPath;
	String botUsername;
	String botToken;
	MessageHandler messageHandler;
	CallbackQueryHandler callbackQueryHandler;
	
	public TelegramBot(SetWebhook setWebhook, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
		super(setWebhook);
		this.messageHandler = messageHandler;
		this.callbackQueryHandler = callbackQueryHandler;
	}
	

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {
    	if (update.hasCallbackQuery()) {
    		CallbackQuery callbackQuery = update.getCallbackQuery();
    		return callbackQueryHandler.processCallbackQuery(callbackQuery);
    	} else {
	    	Message message = update.getMessage();
	            if (message != null) {
	                return messageHandler.answerMessage(message);
	            }
	        return null;
    	}
    }


	public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
        	e.printStackTrace();
        	return new SendMessage();
        }
	}
}
