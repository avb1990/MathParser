package ru.mail.fortune.formulaparse.lexic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mail.fortune.formulaparse.lexic.analyse.FunctionLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.LexicalAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.NumberLexicalAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.OperandLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.VariableLexicAnalyser;
import ru.mail.fortune.formulaparse.lexic.analyse.VoidFunctionLexicAnalyser;

public class LexicParser {
	private List<LexicalAnalyser> analysers;
	private List<Object> objects = new ArrayList<Object>();
	private boolean stringParsed = false;

	public static final char END_STRING = '\n';
	public static final char START_STRING = '\0';

	public LexicParser(List<LexicalAnalyser> analysers) {
		this.analysers = analysers;
	}

	public LexicParser(List<LexicalAnalyser> analysers, String expression) {
		this(analysers);
		parseString(expression);
	}

	private boolean repeatLetter = false;
	private Character prevCharacter = null;

	private void nextLetter(char letter) {

		if (letter == START_STRING) {
			addLetterToAnalysers(letter,
					getAnalysersWithDependencyOfPrevElement());
			return;
		}
		addLetterToAnalysers(letter);
		if (getAllAnalysersWithValidToken().size() == 0 && letter != END_STRING)
			throw new InternalError("Error occured while parsing string");
		LexicalAnalyser analyser = getAnalyserWithCompleteParsedToken();
		if (analyser != null) {
			objects.add(analyser.getObjectFromToken());
			resetAllAnalysersState();
			addLetterToAnalysers(letter,
					getAnalysersWithDependencyOfPrevElement());
			if (analyser.isCompleteDependOfNextElement()
					&& letter != END_STRING) {
				repeatLetter = true;
				nextLetter(letter);
			}
		}
		repeatLetter = false;
		prevCharacter = letter;
		stringParsed = letter == END_STRING;
	}

	public void parseString(String expression) {
		if (expression == null || expression.length() == 0)
			throw new IllegalArgumentException("Illegal expression");

		String parseString = START_STRING + expression + END_STRING;
		for (int i = 0; i < parseString.length(); i++)
			this.nextLetter(parseString.charAt(i));

	}

	public List<Object> getFormulaObjects() {
		if (!stringParsed)
			throw new IllegalStateException(
					"String isn't parsed! cant return formulaObjects");
		return objects;
	}

	private List<LexicalAnalyser> getAllAnalysersWithValidToken() {
		List<LexicalAnalyser> validAnalysers = new ArrayList<LexicalAnalyser>(
				analysers.size());
		for (LexicalAnalyser analyser : analysers)
			if (analyser.isTokenValid())
				validAnalysers.add(analyser);
		return validAnalysers;
	}

	private void resetAllAnalysersState() {
		for (LexicalAnalyser analyser : analysers)
			analyser.resetAnalyserState();
	}

	private void addLetterToAnalysers(char letter) {
		for (LexicalAnalyser analyser : getAllAnalysersWithValidToken())
			analyser.nextLetter(letter);
	}

	private void addLetterToAnalysers(char letter,
			List<LexicalAnalyser> analysers) {
		for (LexicalAnalyser analyser : analysers)
			analyser.nextLetter(letter);
	}

	private List<LexicalAnalyser> getAnalysersWithDependencyOfPrevElement() {
		List<LexicalAnalyser> dependAnalysers = new ArrayList<LexicalAnalyser>(
				analysers.size());
		for (LexicalAnalyser analyser : analysers)
			if (analyser.isValidDependOfPrevElement())
				dependAnalysers.add(analyser);
		return dependAnalysers;

	}

	private List<LexicalAnalyser> getAnalysersWithDependencyOfNextElement() {
		List<LexicalAnalyser> dependAnalysers = new ArrayList<LexicalAnalyser>(
				analysers.size());
		for (LexicalAnalyser analyser : analysers)
			if (analyser.isCompleteDependOfNextElement())
				dependAnalysers.add(analyser);
		return dependAnalysers;

	}

	private LexicalAnalyser getAnalyserWithCompleteParsedToken() {
		for (LexicalAnalyser analyser : analysers)
			if (analyser.isParseComplete())
				return analyser;
		return null;
	}

	public static LexicParser createParser(Map<String, Object> variables) {
		List<LexicalAnalyser> analysers = getDefaultAnalysersList(variables);

		return new LexicParser(analysers);
	}

	public static LexicParser createParser(Map<String, Object> variables,
			String expression) {
		return new LexicParser(getDefaultAnalysersList(variables), expression);
	}

	public static List<Object> parseString(Map<String, Object> variables,
			String expression) {
		return new LexicParser(getDefaultAnalysersList(variables), expression)
				.getFormulaObjects();
	}

	private static List<LexicalAnalyser> getDefaultAnalysersList(
			Map<String, Object> variables) {
		List<LexicalAnalyser> analysers = new ArrayList<LexicalAnalyser>();
		analysers.add(new NumberLexicalAnalyser());
		analysers.add(new OperandLexicAnalyser());
		analysers.add(new VariableLexicAnalyser(variables));
		ParserHelper helper = new ParserHelper() {

			public List<Object> parse(Map<String, Object> variables,
					String expression) {
				// TODO Auto-generated method stub
				return parseString(variables, expression);
			}
		};
		analysers.add(new VoidFunctionLexicAnalyser(variables, helper));
		analysers.add(new FunctionLexicAnalyser(variables, helper));
		return analysers;
	}
}
