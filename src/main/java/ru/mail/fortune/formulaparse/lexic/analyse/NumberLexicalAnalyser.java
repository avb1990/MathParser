package ru.mail.fortune.formulaparse.lexic.analyse;

import ru.mail.fortune.formulaparse.elementsexecuter.ElementsCreater;

public class NumberLexicalAnalyser extends LexicalAnalyser {

	public NumberLexicalAnalyser() {
		super(false, true);
	}

	public static boolean isNumber(char symb) {
		return isOnlyNumber(symb) || symb == '.';
	}

	public static boolean isOnlyNumber(char symb) {
		return symb <= '9' && symb >= '0';
	}

	public static final String NUMB_REG_EXP = "[0-9]+(.[0-9]+)?";

	@Override
	public Object getObjectFromToken() {
		return ElementsCreater.createValue(Double.parseDouble(token));
	}

	@Override
	public void nextLetter(char letter) {
		boolean isNumber = isNumber(letter);
		if (isNumber) {
			token += letter;
		} else if (token.matches(NUMB_REG_EXP)) {
			parseComplete = true;
		} else {
			isValid = false;
		}

	}
}
