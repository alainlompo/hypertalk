package org.hypernovae.apis.parsers.expressions;

public enum Token {
	
	ADD("+","+"),
	SUB("-","-"),
	MUL("*","*"),
	DIV("/","/"),
	POW("^","^"),
	UNARY_MINUS("-: unary minus","u"),
	FACTORIAL("f(x): factorial","f"),
	PERMUTATION("p(n,r): permutations","p"),
	COMBINATORIAL("c(n,r): combinations","c"),
	COMA(", : coma",","),
	LEFT_PARENTHESIS("(: left parenthesis","("),
	RIGHT_PARENTHESIS("): right parenthesis",")"),
	END_OF_STRING(" End of String","$"),
	MAX_OP("Maximum number of operators",""),
	VAL("Value", false,"")
	;
	
	
	private final boolean isOperator;
	private final String description;
	private final String symbol;
	
	private Token(String description, boolean isOperator, String symbol) {
		this.isOperator = isOperator;
		this.description = description;
		this.symbol = symbol;
		
	}
	
	private Token(String description, String symbol) {
		this(description, true, symbol);
	}

	public boolean isOperator() {
		
		return isOperator;
	}

	public String getDescription() {
		return description;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public static Token fromSymbol(String symbol) {
		Token result = null;
		for (Token token: Token.values()) {
			if (token.getSymbol().trim().equalsIgnoreCase(symbol)) {
				result = token;
				break;
			}
		}
		return result;		
	}

}
