package ru.mail.fortune.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ru.mail.fortune.formulaparse.elementsexecuter.ElementsExecuterHelper;
import ru.mail.fortune.formulaparse.elementsexecuter.Function;
import ru.mail.fortune.formulaparse.elementsexecuter.FunctionContainer;
import ru.mail.fortune.formulaparse.elementsexecuter.OperandType;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.AddSubExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.ElementsExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.FunctionExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.executers.MultDivExecuter;

public class ElementExecutersTest extends TestCase {

	public void testfunctionExecuter() {
		List<Object> elements = new ArrayList<Object>();
		Map<String, Function> functions = new HashMap<String, Function>();

		FunctionContainer container1 = new FunctionContainer();
		container1.name = "f1";
		List<Object> arg = new ArrayList<Object>();
		arg.add(5.5);
		container1.elements.add(arg);
		arg = new ArrayList<Object>();
		arg.add(8.0);
		container1.elements.add(arg);

		functions.put("f1", new Function() {

			public Object execute(List<Object> arguments) {
				return (Double) arguments.get(0) * (Double) arguments.get(1);
			}
		});

		FunctionContainer container2 = new FunctionContainer();
		container2.name = "f2";
		arg = new ArrayList<Object>();
		arg.add(8.0);
		container2.elements.add(arg);
		arg = new ArrayList<Object>();
		arg.add(9.0);
		container2.elements.add(arg);
		arg = new ArrayList<Object>();
		arg.add(3.0);
		container2.elements.add(arg);

		functions.put("f2", new Function() {

			public Object execute(List<Object> arguments) {
				return (Double) arguments.get(0) * (Double) arguments.get(1)
						/ (Double) arguments.get(2);
			}
		});

		FunctionContainer voidContainer = new FunctionContainer();
		voidContainer.name = Function.VOID_FUNCTION_NAME;
		arg = new ArrayList<Object>();
		arg.add(9.0);
		voidContainer.elements.add(arg);

		elements.add(container1);
		elements.add(OperandType.ADD);
		elements.add(container2);
		elements.add(OperandType.SUB);
		elements.add(voidContainer);

		ElementsExecuter executer = new FunctionExecuter(functions,
				getHelper(functions));

		executer.execute(elements);
		assertEquals(elements.size(), 5);
		assertEquals(elements.get(0), 44.0);
		assertEquals(elements.get(1), OperandType.ADD);
		assertEquals(elements.get(2), 24.0);
		assertEquals(elements.get(3), OperandType.SUB);
		assertEquals(elements.get(4), 9.0);

	}

	private ElementsExecuterHelper getHelper(
			final Map<String, Function> functions) {
		return new ElementsExecuterHelper() {

			public Object execute(List<Object> elements) {
				ElementsExecuter executer = new FunctionExecuter(functions,
						getHelper(functions));
				executer.execute(elements);
				return elements.get(0);
			}
		};
	}

	public void testAddSubExecuter() {
		List<Object> elements = new ArrayList<Object>();
		Object[] objs = new Object[] { 8.5, OperandType.ADD, 4.0,
				OperandType.SUB, 5.0, OperandType.ADD, 9.0, OperandType.ADD,
				5.0, OperandType.SUB, 8.0, OperandType.SUB, 1.0 };
		elements.addAll(Arrays.asList(objs));
		ElementsExecuter executer = new AddSubExecuter();

		executer.execute(elements);

		assertEquals(elements.size(), 1);
		assertEquals(elements.get(0), 12.5);
	}

	public void testMultDivExecuter() {
		List<Object> elements = new ArrayList<Object>();
		Object[] objs = new Object[] { 8.5, OperandType.MULT, 4.0,
				OperandType.DIV, 5.0, OperandType.ADD, 9.0, OperandType.ADD,
				5.0, OperandType.MULT, 8.0, OperandType.MULT, 1.0,
				OperandType.SUB, 10.0 };
		elements.addAll(Arrays.asList(objs));
		ElementsExecuter executer = new MultDivExecuter();
		executer.execute(elements);

		assertEquals(elements.size(), 7);
		assertEquals(elements.get(0), 6.8);
		assertEquals(elements.get(1), OperandType.ADD);
		assertEquals(elements.get(2), 9.0);
		assertEquals(elements.get(3), OperandType.ADD);
		assertEquals(elements.get(4), 40.0);
		assertEquals(elements.get(5), OperandType.SUB);
		assertEquals(elements.get(6), 10.0);

		executer = new AddSubExecuter();
		executer.execute(elements);

		assertEquals(elements.size(), 1);
		assertEquals(elements.get(0), 45.8);

	}
}
