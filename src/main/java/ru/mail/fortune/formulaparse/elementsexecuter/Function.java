package ru.mail.fortune.formulaparse.elementsexecuter;

import java.util.List;

public interface Function {

	public static final Function VOID_FUNCTION = new Function() {
	
		public Object execute(List<Object> arguments) {
	
			return arguments.get(0);
	
		}
	};
	public static final String VOID_FUNCTION_NAME = "VOID_FUNCTION";

	public abstract Object execute(List<Object> arguments);
}
