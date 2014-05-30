package ru.mail.fortune.formulaparse.elementsexecuter.executers;

import java.util.List;

import ru.mail.fortune.formulaparse.elementsexecuter.OperandType;

public class MultDivExecuter implements ElementsExecuter {

	private static ElementsExecuter EXECUTER = null;

	public void execute(List<Object> objects) {

		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.get(i);
			if (isObjMultOrDiv(object)) {
				Double result = 0.0;
				if (((OperandType) object) == OperandType.MULT)
					result = (Double) objects.get(i - 1)
							* (Double) objects.get(i + 1);
				else if (((OperandType) object) == OperandType.DIV)
					result = (Double) objects.get(i - 1)
							/ (Double) objects.get(i + 1);
				objects.set(i - 1, result);
				objects.remove(i);
				objects.remove(i);
				i -= 2;
			}
		}

	}

	private boolean isObjMultOrDiv(Object object) {
		return object instanceof OperandType
				&& (OperandType.MULT.equals(object) || OperandType.DIV
						.equals(object));
	}

	public static ElementsExecuter createMultDivExecuter() {
		return EXECUTER == null ? EXECUTER = new MultDivExecuter() : EXECUTER;
	}
}
