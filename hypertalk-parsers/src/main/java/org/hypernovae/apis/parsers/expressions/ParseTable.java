package org.hypernovae.apis.parsers.expressions;

public class ParseTable {
	
	public final ParseTableLine[] TABLE;
	private ParseTable(ParseTableLine[] lines) {
		this.TABLE = lines;
	}
	
	public static ParseTable of(ParseTableLine[] lines) {
		return new ParseTable(lines);
	}

}
