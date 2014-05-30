package ru.mail.fortune.formulaparse.elementsexecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.executers.AddSubExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.ElementsExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.FunctionExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.MultDivExecuter;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.InternalError;

public class FormulaExecuter {

	private List<ElementsExecuter> executers;

	public FormulaExecuter(List<ElementsExecuter> executers) {
		this.executers = executers;
	}

	public Object executeElements(List<Object> elements) {
		List<Object> execElements = new ArrayList<Object>(elements);
		for (int i = 0; i < executers.size(); i++)
			executers.get(i).execute(execElements);
		if (execElements.size() != 1)
			throw new InternalError("Incorrect elements or it's sequence");

		return execElements.get(0);
	}

	public static FormulaExecuter createExecuter(Map<String, Function> functions) {
		List<ElementsExecuter> executers = new ArrayList<ElementsExecuter>();
		final FormulaExecuter executer = new FormulaExecuter(executers);
		FunctionExecuter funcExecuter = new FunctionExecuter(functions,
				new ElementsExecuterHelper() {

					public Object execute(List<Object> elements) {
						// TODO Auto-generated method stub
						return executer.executeElements(elements);
					}
				});
		executers.add(funcExecuter);
		executers.add(MultDivExecuter.createMultDivExecuter());
		executers.add(AddSubExecuter.createAddSubExecuter());
		return executer;
	}

	public static Object execute(Map<String, Function> functions,
			List<Object> elements) {
		return createExecuter(functions).executeElements(elements);
	}

	public static Object execute(List<Object> elements) {
		return execute(new HashMap<String, Function>(), elements);
	}
}
