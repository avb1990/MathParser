package ru.mail.fortune.formulaparse;

import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.FormulaExecuter;
import ru.mail.fortune.formulaparse.lexic.LexicParser;

public class FormulaParseFacade {
	public static Object parseString(String expression,
			Map<String, Object> variables) {

		return FormulaExecuter.execute(LexicParser.parseString(variables,
				expression));
	}
}
