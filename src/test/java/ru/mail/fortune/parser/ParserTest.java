package ru.mail.fortune.parser;

import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.mail.fortune.formulaparse.elementsexecuter.FormulaExecuter;
import ru.mail.fortune.formulaparse.elementsexecuter.Function;
import ru.mail.fortune.formulaparse.elementsexecuter.FunctionContainer;
import ru.mail.fortune.formulaparse.elementsexecuter.OperandType;
import ru.mail.fortune.formulaparse.lexic.LexicParser;

/**
 * Unit test for simple App.
 */
public class ParserTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public ParserTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ParserTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		long startTime = System.currentTimeMillis();
		List<Object> parseElements = LexicParser.parseString(null, "2+2");
		assertEquals(parseElements.size(), 3);
		assertEquals(parseElements.get(0), 2.0);
		assertEquals(parseElements.get(1), OperandType.ADD);
		assertEquals(parseElements.get(2), 2.0);
		HashMap<String, Object> variables = new HashMap<String, Object>();
		variables.put("x1", 5.0);
		variables.put("x2", 6.1);
		parseElements = LexicParser.parseString(variables,
				"2*7/9+x1-x2+func1(3+5-10,x1,x2/75+10)");
		assertEquals(parseElements.size(), 11);
		assertEquals(parseElements.get(0), 2.0);
		assertEquals(parseElements.get(1), OperandType.MULT);
		assertEquals(parseElements.get(2), 7.0);
		assertEquals(parseElements.get(3), OperandType.DIV);
		assertEquals(parseElements.get(4), 9.0);
		assertEquals(parseElements.get(5), OperandType.ADD);
		assertEquals(parseElements.get(6), 5.0);
		assertEquals(parseElements.get(7), OperandType.SUB);
		assertEquals(parseElements.get(8), 6.1);
		assertEquals(parseElements.get(9), OperandType.ADD);
		FunctionContainer functionContainer = (FunctionContainer) parseElements
				.get(10);
		assertEquals(functionContainer.elements.size(), 3);

		assertEquals(
				FormulaExecuter.execute(null,
						LexicParser.parseString(null, "2-2+9")), 9.0);

		variables.clear();
		variables.put("x1", 1.0);
		variables.put("x2", 2.0);
		HashMap<String, Function> functions = new HashMap<String, Function>();
		functions.put("f1", new Function() {

			public Object execute(List<Object> arguments) {

				return (Double) arguments.get(0) * (Double) arguments.get(1)
						- (Double) arguments.get(2);
			}
		});
		functions.put("f2", new Function() {

			public Object execute(List<Object> arguments) {

				return (Double) arguments.get(0) * 5;
			}
		});

		assertEquals(FormulaExecuter.execute(functions,
				LexicParser.parseString(variables, "2*8-x1*f1(1,x2,f2(x1))")),
				19.0);

		functions.clear();
		functions.put(Function.VOID_FUNCTION_NAME, Function.VOID_FUNCTION);
		assertEquals(FormulaExecuter.execute(functions,
				LexicParser.parseString(variables, "8.0*(5-10+88/8)/6")), 8.0);
		System.out.println("time neaded is "
				+ (System.currentTimeMillis() - startTime) + " ms");
	}
}
