package ru.mail.fortune.formulaparse.lexic.analyse;

import java.util.HashMap;
import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.ElementsCreater;
import ru.mail.fortune.formulaparse.elementsexecuter.OperandType;

public class OperandLexicAnalyser extends LexicalAnalyser {

	private Map<Character, Object> operandTypes = new HashMap<Character, Object>();
	public static final String OPERAND_REG_EXP = "[\\+\\-\\*\\/]";

	public OperandLexicAnalyser() {
		super(false, false);
		initOperands();
	}

	private void initOperands() {
		operandTypes.put('+', OperandType.ADD);
		operandTypes.put('-', OperandType.SUB);
		operandTypes.put('*', OperandType.MULT);
		operandTypes.put('/', OperandType.DIV);
	}

	@Override
	public Object getObjectFromToken() {
		return ElementsCreater.creteOperand(operandTypes.get(token.charAt(0)));
	}

	@Override
	public void nextLetter(char letter) {
		isValid = parseComplete = (token += letter).matches(OPERAND_REG_EXP);

	}
}
