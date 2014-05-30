package ru.mail.fortune.formulaparse.elementsexecuter;

import java.util.List;

public class ElementsCreater {
	public static Object createFunctionElement(String name, List<Object> args) {
		FunctionContainer functionContainer = new FunctionContainer();
		functionContainer.name = name;
		functionContainer.elements.addAll(args);
		return functionContainer;
	}

	public static Object createValue(Object value) {
		return value;
	}

	public static Object creteOperand(Object optyType) {
		return optyType;
	}
}
