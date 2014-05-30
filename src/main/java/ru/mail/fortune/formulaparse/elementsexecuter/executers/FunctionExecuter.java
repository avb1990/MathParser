package ru.mail.fortune.formulaparse.elementsexecuter.executers;

import java.util.List;
import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.ElementsExecuterHelper;
import ru.mail.fortune.formulaparse.elementsexecuter.Function;
import ru.mail.fortune.formulaparse.elementsexecuter.FunctionContainer;

public class FunctionExecuter implements ElementsExecuter {

	private Map<String, Function> functions;
	private ElementsExecuterHelper helper;

	public FunctionExecuter(Map<String, Function> functions,
			ElementsExecuterHelper helper) {
		this.functions = functions;
		this.helper = helper;
	}

	public void execute(List<Object> objects) {
		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) instanceof FunctionContainer) {
				FunctionContainer funcContainer = (FunctionContainer) objects
						.get(i);
				for (int j = 0; j < funcContainer.elements.size(); j++)
					funcContainer.elements.set(j, helper
							.execute((List<Object>) funcContainer.elements
									.get(j)));
				Function function = null;
				if (funcContainer.name == Function.VOID_FUNCTION_NAME)
					function = Function.VOID_FUNCTION;
				else
					function = functions.get(funcContainer.name);
				objects.set(i, function.execute(funcContainer.elements));
			}
	}
}
