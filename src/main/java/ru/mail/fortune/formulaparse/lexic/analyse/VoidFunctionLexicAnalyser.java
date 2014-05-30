package ru.mail.fortune.formulaparse.lexic.analyse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.ElementsCreater;
import ru.mail.fortune.formulaparse.elementsexecuter.Function;
import ru.mail.fortune.formulaparse.lexic.ParserHelper;

public class VoidFunctionLexicAnalyser extends LexicalAnalyser {

	public static final String INNER_FUNC_REG_EXP = "\\(.+\\)";
	private Map<String, Object> variables;
	private ParserHelper parseHelper;
	private boolean isTokenStarted;
	private int bracketCnt;

	public VoidFunctionLexicAnalyser(Map<String, Object> variables,
			ParserHelper parseHelper) {
		super(true, false);
		this.variables = variables;
		this.parseHelper = parseHelper;
		resetAnalyserState();
		isTokenStarted = true;

	}

	@Override
	public void nextLetter(char letter) {
		int tokenLen = token.length();
		if (isTokenStarted && letter != '(' && bracketCnt == 0)
			isValid = isTokenStarted = false;
		else if (isTokenStarted && letter == '(') {
			if (bracketCnt > 0)
				token += letter;
			bracketCnt++;
		} else if (isTokenStarted && letter == ')') {
			if (--bracketCnt == 0)
				isValid = parseComplete = true;
			else
				token += letter;
		} else if (isTokenStarted && bracketCnt > 0)
			token += letter;
		else if (!(VariableLexicAnalyser.isLetter(letter) || NumberLexicalAnalyser
				.isNumber(letter)))
			isTokenStarted = true;

	}

	@Override
	public Object getObjectFromToken() {
		List<Object> objects = new ArrayList<Object>();

		objects.add(parseHelper.parse(variables, token));
		return ElementsCreater.createFunctionElement(Function.VOID_FUNCTION_NAME,
				objects);
	}

	@Override
	public void resetAnalyserState() {
		super.resetAnalyserState();
		isTokenStarted = false;
		bracketCnt = 0;
	}
}
