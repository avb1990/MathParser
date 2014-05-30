package ru.mail.fortune.formulaparse.lexic;

import java.util.List;
import java.util.Map;

public interface ParserHelper {
	public List<Object> parse(Map<String, Object> variables, String expression);
}
