package ru.mail.fortune.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mail.fortune.formulaparse.elementsexecuter.Function;
import ru.mail.fortune.formulaparse.elementsexecuter.FunctionContainer;
import ru.mail.fortune.formulaparse.elementsexecuter.OperandType;
import ru.mail.fortune.formulaparse.lexic.LexicParser;
import ru.mail.fortune.formulaparse.lexic.ParserHelper;
import ru.mail.fortune.formulaparse.lexic.analyse.FunctionLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.LexicalAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.NumberLexicalAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.OperandLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.VariableLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.VoidFunctionLexicAnalyser;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AnalysersTest extends TestCase {

	public static Test suite() {
		return new TestSuite(AnalysersTest.class);
	}

	public AnalysersTest(String name) {
		super(name);
	}

	private void simpleParse(LexicalAnalyser analyser, String expression,
			List<Object> objects) {
		for (int i = 0; i < expression.length(); i++) {
			if (analyser.isParseComplete()) {
				objects.add(analyser.getObjectFromToken());
				analyser.resetAnalyserState();
			} else if (!analyser.isTokenValid())
				analyser.resetAnalyserState();
			analyser.nextLetter(expression.charAt(i));

		}
		analyser.nextLetter(LexicParser.END_STRING);
		if (analyser.isParseComplete())
			objects.add(analyser.getObjectFromToken());
		analyser.resetAnalyserState();
	}

	public void testNumberLexicalAnalyser() {

		LexicalAnalyser analyser = new NumberLexicalAnalyser();
		List<Object> objects = new ArrayList<Object>();

		String expression = "5454.5d5+95.6";
		simpleParse(analyser, expression, objects);

		assertEquals(objects.size(), 3);
		assertEquals(objects.get(0), 5454.5);
		assertEquals(5.0, objects.get(1));
		assertEquals(objects.get(2), 95.6);
	}

	public void testOperandAnalyser() {

		OperandLexicAnalyser analyser = new OperandLexicAnalyser();
		List<Object> objects = new ArrayList<Object>();

		String expression = "*4-h/9+5";
		simpleParse(analyser, expression, objects);

		assertEquals(objects.size(), 4);
		assertEquals(objects.get(0), OperandType.MULT);
		assertEquals(objects.get(1), OperandType.SUB);
		assertEquals(objects.get(2), OperandType.DIV);
		assertEquals(objects.get(3), OperandType.ADD);

	}

	public void testVariableAnalyser() {
		List<Object> objects = new ArrayList<Object>();
		Map<String, Object> variables = new HashMap<String, Object>();
		double x1 = 56.89;
		variables.put("x1", x1);
		double x2 = 5.0;
		variables.put("x2", x2);
		double x3 = 0.5656;
		variables.put("x3", x3);
		LexicalAnalyser analyser = new VariableLexicAnalyser(variables);
		String expression = "x1+x2,x3";
		simpleParse(analyser, expression, objects);

		assertEquals(objects.size(), 3);
		assertEquals(objects.get(0), x1);
		assertEquals(objects.get(1), x2);
		assertEquals(objects.get(2), x3);

	}

	public void testFunctionAnalyser() {
		List<Object> objects = new ArrayList<Object>();

		LexicalAnalyser analyser = new FunctionLexicAnalyser(null,
				getHelper(FuncTypes.NAMED));
		String expression = "f1(f2(2),5,6)+f3(2,6)";
		simpleParse(analyser, expression, objects);

		assertEquals(objects.size(), 2);
		FunctionContainer firstContainer = (FunctionContainer) objects
				.get(0);
		assertEquals(firstContainer.name, "f1");
		assertEquals(firstContainer.elements.size(), 3);
		FunctionContainer containerInFirstContainer = (FunctionContainer) ((List<Object>) (firstContainer.elements
				.get(0))).get(0);
		assertEquals(containerInFirstContainer.name, "f2");
		assertEquals(containerInFirstContainer.elements.size(), 1);
		FunctionContainer secondContainer = (FunctionContainer) objects
				.get(1);
		assertEquals(secondContainer.name, "f3");
		assertEquals(secondContainer.elements.size(), 2);

	}

	public void testVoidFunction() {
		List<Object> objects = new ArrayList<Object>();
		VoidFunctionLexicAnalyser analyser = new VoidFunctionLexicAnalyser(
				null, getHelper(FuncTypes.VOID));
		String expression = "(kdfj990+dfg(1,2)(12))+df()+234+(1+2)";
		simpleParse(analyser, expression, objects);
		assertEquals(objects.size(), 2);
		FunctionContainer firstVoid = (FunctionContainer) objects.get(0);
		assertEquals(firstVoid.name, Function.VOID_FUNCTION_NAME);
		assertEquals(firstVoid.elements.size(), 1);
		FunctionContainer voidInFirstVoid = (FunctionContainer) ((List<Object>) firstVoid.elements
				.get(0)).get(0);
		assertEquals(voidInFirstVoid.name, Function.VOID_FUNCTION_NAME);
		assertEquals(voidInFirstVoid.elements.size(), 1);
		FunctionContainer secondContainer = (FunctionContainer) objects
				.get(1);
		assertEquals(secondContainer.name, Function.VOID_FUNCTION_NAME);
		assertEquals(secondContainer.elements.size(), 1);

	}

	private enum FuncTypes {
		NAMED, VOID
	}

	private ParserHelper getHelper(final FuncTypes type) {
		return new ParserHelper() {

			public List<Object> parse(Map<String, Object> variables,
					String expression) {
				List<Object> objects = new ArrayList<Object>();
				simpleParse(
						type == FuncTypes.NAMED ? new FunctionLexicAnalyser(
								variables, getHelper(type))
								: new VoidFunctionLexicAnalyser(variables,
										getHelper(type)), expression, objects);
				return objects;
			}
		};
	}
}
