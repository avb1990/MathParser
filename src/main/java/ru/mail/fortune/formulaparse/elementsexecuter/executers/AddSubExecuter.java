package ru.mail.fortune.formulaparse.elementsexecuter.executers;

import java.util.List;

import ru.mail.fortune.formulaparse.elementsexecuter.OperandType;

public class AddSubExecuter implements ElementsExecuter {
	private static ElementsExecuter EXECUTER;

	public void execute(List<Object> objects) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof OperandType) {
				Double result = 0.0;
				if (((OperandType) objects.get(i)) == OperandType.ADD)
					result = (Double) objects.get(i - 1)
							+ (Double) objects.get(i + 1);
				else
					result = (Double) objects.get(i - 1)
							- (Double) objects.get(i + 1);
				objects.set(i - 1, result);
				objects.remove(i);
				objects.remove(i);
				i -= 2;
			}
		}
	}

	public static ElementsExecuter createAddSubExecuter() {
		return EXECUTER == null ? EXECUTER = new AddSubExecuter() : EXECUTER;
	}
}
