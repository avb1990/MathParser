package ru.mail.fortune.formulaparse.lexic.analyse;

public abstract class LexicalAnalyser {

	protected boolean isValid;
	protected boolean parseComplete;
	protected String token;
	private boolean isValidDependOfPrevElement;
	private boolean isCompleteDependOfNextElement;

	public LexicalAnalyser(boolean isValidDependOfPrevElement,
			boolean isCompleteDependOfNextElement) {
		resetAnalyserState();
		this.isValidDependOfPrevElement = isValidDependOfPrevElement;
		this.isCompleteDependOfNextElement = isCompleteDependOfNextElement;
	}

	public boolean isTokenValid() {
		return isValid;
	}

	public boolean isParseComplete() {
		return parseComplete;
	}

	public abstract void nextLetter(char letter);

	public abstract Object getObjectFromToken();

	public void resetAnalyserState() {
		isValid = true;
		parseComplete = false;
		token = "";
	}

	public boolean isValidDependOfPrevElement() {
		return isValidDependOfPrevElement;
	}

	protected void setValidDependOfPrevElement(
			boolean isValidDependOfPrevElement) {
		this.isValidDependOfPrevElement = isValidDependOfPrevElement;
	}

	public boolean isCompleteDependOfNextElement() {
		return isCompleteDependOfNextElement;
	}

	protected void setCompleteDependOfNextElement(
			boolean isCompleteDependOfNextElement) {
		this.isCompleteDependOfNextElement = isCompleteDependOfNextElement;
	}

}
