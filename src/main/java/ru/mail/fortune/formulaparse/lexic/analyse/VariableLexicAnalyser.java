package ru.mail.fortune.formulaparse.lexic.analyse;

import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.ElementsCreater;

public class VariableLexicAnalyser extends LexicalAnalyser {

	private Map<String, Object> variables;
	public static final String VAR_REG_EXP = "[A-Za-z]+([A-Za-z0-9])*";

	public VariableLexicAnalyser(Map<String, Object> variables) {
		super(false, true);
		this.variables = variables;

	}

	@Override
	public Object getObjectFromToken() {
		return ElementsCreater.createValue(variables.get(token));
	}

	@Override
	public void nextLetter(char letter) {
		if (token.length() == 0 && !isLetter(letter))
			isValid = false;
		else if (isLetter(letter) || NumberLexicalAnalyser.isOnlyNumber(letter))
			token += letter;
		else
			isValid = parseComplete = token.matches(VAR_REG_EXP)
					&& variables.containsKey(token);

	}

	public static boolean isLetter(char symb) {
		return symb <= 'z' && symb >= 'a' || symb <= 'Z' && symb >= 'A';
	}

}
