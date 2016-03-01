package org.hypernovae.apis.parsers.expressions;

public class ParseTableLine {
	
	public final ParsingAction[] ACTION_LINE;
	private ParseTableLine(ParsingAction[] actionLine) {
		this.ACTION_LINE = actionLine;
	}
	
	public static ParseTableLine of(ParsingAction[] actionLine) {
		return new ParseTableLine(actionLine);
	}
	
	
	

}
