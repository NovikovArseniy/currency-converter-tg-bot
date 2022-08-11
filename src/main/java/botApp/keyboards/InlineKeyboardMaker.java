package botApp.keyboards;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import botApp.enums.Currencies;


@Component
public class InlineKeyboardMaker {
	
	public InlineKeyboardMarkup getInlineMessageButtonsOfCurrencies() {
		List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
		for (Currencies currency : Currencies.values()) {
			rowList.add(getButton(currency.name(), currency.name()));
		}
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(rowList);
		return inlineKeyboardMarkup;
	}
	
    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}
