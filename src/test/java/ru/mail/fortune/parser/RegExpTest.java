package ru.mail.fortune.parser;

import ru.mail.fortune.formulaparse.lexic.analyse.NumberLexicalAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.OperandLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.VariableLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.VoidFunctionLexicAnalyser;
import junit.framework.TestCase;

public class RegExpTest extends TestCase {

	public void testNumber() {
		String pattern = NumberLexicalAnalyser.NUMB_REG_EXP;
		assertTrue("54.65".matches(pattern));
		assertFalse("0.".matches(pattern));
		assertFalse("dfk7.6".matches(pattern));
		assertTrue("0.456".matches(pattern));
		assertFalse("".matches(pattern));
	}

	public void testVariable() {
		String pattern = VariableLexicAnalyser.VAR_REG_EXP;
		assertTrue("a".matches(pattern));
		assertTrue("a1fd".matches(pattern));
		assertTrue("adfg12".matches(pattern));
		assertFalse("".matches(pattern));
		assertFalse("1dfgdfg3".matches(pattern));
		assertFalse("dfgdfg4.4".matches(pattern));
	}

	public void testOperand() {
		String pattern = OperandLexicAnalyser.OPERAND_REG_EXP;
		assertTrue("+".matches(pattern));
		assertTrue("-".matches(pattern));
		assertTrue("*".matches(pattern));
		assertTrue("/".matches(pattern));
		assertFalse("++".matches(pattern));
		assertFalse("".matches(pattern));
		assertFalse("dfgdfg/".matches(pattern));
	}

	public void testFunction() {
		String pattern = VariableLexicAnalyser.VAR_REG_EXP
				+ VoidFunctionLexicAnalyser.INNER_FUNC_REG_EXP;
		assertTrue("f(3)".matches(pattern));
		assertTrue("f(3sdfsd,sdfsdf8)".matches(pattern));
		assertTrue("f1sdf(3sdfsd,sdfsdf8)".matches(pattern));

		assertFalse("(sdfsdf)".matches(pattern));
		assertFalse("dfgdfg".matches(pattern));
		assertFalse("1dfgdf(dfd)".matches(pattern));
		assertFalse("dff.gdf(dfd)".matches(pattern));

		assertTrue("(dfg,dfgdfg)"
				.matches(VoidFunctionLexicAnalyser.INNER_FUNC_REG_EXP));
		assertTrue("(dfgdfg)"
				.matches(VoidFunctionLexicAnalyser.INNER_FUNC_REG_EXP));
		assertFalse("()".matches(VoidFunctionLexicAnalyser.INNER_FUNC_REG_EXP));
		assertFalse("func(dfg)"
				.matches(VoidFunctionLexicAnalyser.INNER_FUNC_REG_EXP));
	}
}
