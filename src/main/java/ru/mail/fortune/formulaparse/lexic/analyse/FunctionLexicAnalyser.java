package ru.mail.fortune.formulaparse.lexic.analyse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.ElementsCreater;
import ru.mail.fortune.formulaparse.lexic.ParserHelper;

public class FunctionLexicAnalyser extends LexicalAnalyser {

	private String innerExpression;
	private String functionName;
	private Map<String, Object> variables;
	private int bracketCnt;
	private ParserHelper parseHelper;

	public FunctionLexicAnalyser(Map<String, Object> variables,
			ParserHelper parseHelper) {
		super(false, false);
		resetState();
		this.variables = variables;
		this.parseHelper = parseHelper;
	}

	@Override
	public Object getObjectFromToken() {

		List<Object> objects = new ArrayList<Object>();
		String[] args = innerExpression.split(",");
		for (String arg : args)
			objects.add(parseHelper.parse(variables, arg));
		return ElementsCreater.createFunctionElement(functionName, objects);
	};

	@Override
	public void nextLetter(char letter) {
		if (innerExpression == null && letter != '(') {
			functionName += letter;
			isValid = functionName.matches(VariableLexicAnalyser.VAR_REG_EXP);
		} else if (innerExpression == null && letter == '(') {
			innerExpression = "";
			isValid = functionName.matches(VariableLexicAnalyser.VAR_REG_EXP);
		} else if (innerExpression != null && letter == '(') {
			innerExpression += letter;
			bracketCnt++;
		} else if (innerExpression != null && letter == ')') {
			if (--bracketCnt == 0)
				parseComplete = true;
			else
				innerExpression += letter;
		} else if (innerExpression != null)
			innerExpression += letter;
	}

	private void resetState() {
		innerExpression = null;

		functionName = "";
		token = "";
		bracketCnt = 1;
	}

	@Override
	public void resetAnalyserState() {
		super.resetAnalyserState();
		resetState();
	}
}
