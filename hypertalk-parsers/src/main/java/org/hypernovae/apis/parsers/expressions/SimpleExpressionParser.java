package org.hypernovae.apis.parsers.expressions;

import static org.hypernovae.apis.parsers.expressions.ParsingAction.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;
public class SimpleExpressionParser {
	
	public static final ParseTable PARSE_TABLE
	= ParseTable.of(new ParseTableLine[] {
			ParseTableLine.of(new ParsingAction[] {R,   R,  S,  S,  S,  S,  S,  S,  S,  R, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  S,  S,  S,  S,  S,  S,  S,  R, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  R,  R,  S,  S,  S,  S,  S,  R, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  R,  R,  S,  S,  S,  S,  S,  R, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  R,  R,  S,  S,  S,  S,  S,  R, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  R,  R,  R,  S,  S,  S,  S,  R, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {E4, E4, E4, E4, E4, E4, E4, E4, E4, E4, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {E4, E4, E4, E4, E4, E4, E4, E4, E4, E4, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {E4, E4, E4, E4, E4, E4, E4, E4, E4, E4, S, R, R}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  R,  R,  R,  R,  R,  R,  R, E4, R, R, E4}),
			ParseTableLine.of(new ParsingAction[] {S,   S,  S,  S,  S,  S,  S,  S,  S,  S, S, S, ER1}),
			ParseTableLine.of(new ParsingAction[] {R,   R,  R,  R,  R,  R,ER2,ER2,ER2, E4,EO1,R, R}),
			ParseTableLine.of(new ParsingAction[] {S,   S,  S,  S,  S,  S,  S,  S,  S, E4,  S,ER2, A})
	});
	
//	public static final Scanner SCANNER = new Scanner(System.in);
	public static final BufferedReader READER ;
	
	static {
		READER = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public static final PrintWriter LOGGER;
	
	static {
		try {
			LOGGER = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Could not initialize the logger...");
		}
	}
	public static final int MAX_OPERATIONS = 50;
	public static final int MAX_VALUES = 50;
	
	private String workString;
	private StringTokenizer expressionTokenizer;
	private char opStack[] = new char[MAX_OPERATIONS];
	private double valStack[] = new double[MAX_VALUES];
	private int opTop;
	private int valTop;
	
	private boolean firstToken;
	
	private Token token;
	private Token previousToken; 
	double value;
	
	int error (String msg) {
		System.out.println(String.format("error: %s\n",msg));
		return 1;
	}
	
	public void log(String message ) {
		LOGGER.println(message);
		LOGGER.flush();
	}
	
	String extractToken() {
		String result = null;
		if (expressionTokenizer.hasMoreElements()) {
			result = expressionTokenizer.nextToken();
		}
		return result;
	}
	public int getToken() {
				
		char currentChar='0';
		String currentTokenString = "";
		
		if (firstToken) {
			firstToken = false;
			previousToken = Token.END_OF_STRING;					
		}
		
		currentTokenString = extractToken();
		log(String.format("extracted the following token: %s", currentTokenString));
		if (currentTokenString == null) {
			token = Token.END_OF_STRING;
			return 0;
		}
		currentChar = currentTokenString.charAt(0);
		
		if (currentChar == 'Q' || currentChar == 'q') {
			System.exit(0);
		}		
		
		previousToken = token;
		
		switch(currentChar) {
		case '+': 
			token = Token.ADD;
			break;
		case '-':
			token = Token.SUB;
			break;
		case '*':
			token = Token.MUL;
			break;
		case '/':
			token = Token.DIV;
			break;
		case '^':
			token = Token.POW;
			break;
		case '(':
			token = Token.LEFT_PARENTHESIS;
			break;
		case ')':
			token = Token.RIGHT_PARENTHESIS;
			break;
		case ',':
			token = Token.COMA;
			break;
		case 'f':
			token = Token.FACTORIAL;
			break;
		case 'c':
			token = Token.COMBINATORIAL;
			break;
		case 'p':
			token = Token.PERMUTATION;
			break;
		case '$':
			token = Token.END_OF_STRING;
			break;
			
		default:
			value = Double.parseDouble(currentTokenString);
			token = Token.VAL;
			break;
		
		}
		
		if (token == Token.SUB) {
			if (previousToken != Token.VAL && previousToken != Token.RIGHT_PARENTHESIS) {
				token = Token.UNARY_MINUS;
			}
		}
		
		
		return 0;
	}
	
	public int shift() {
		if (token == Token.VAL) {
			if (++valTop >= MAX_VALUES) {
				return error("Value Stack is exhausted....");
			}
			valStack[valTop] = value;
		} else {
			if (++opTop >= MAX_OPERATIONS) {
				return error ("Operations stack exhausted....");
				
			}
			opStack[opTop] = token.getSymbol().charAt(0);
		}
		
		/*if (getToken() == 0) {
			return 0;
		}
		
		return 1;*/
		return 0;
	}
		
	public double factorial(double n) {
		double i, t;
		for (t = 1, i=1;  i <= n; i++) {
			t *= i;
		}
		
		return t;
	}
	
	public int reduce() {
		switch(opStack[opTop]) {
			case '+':
				if (valTop < 1) {
					return error ("Syntax error....");
				}
				
				valStack[valTop-1] = valStack[valTop-1] + valStack[valTop];
				valTop--;
				break;
			case '-':
				if (valTop < 1) {
					return error("Syntax error...");
					
				}
				
				valStack[valTop-1] = valStack[valTop-1] - valStack[valTop];
				valTop--;
				break;
				
			case '*':
				if (valTop <1) {
					return error("Syntax error....");
					
				}
				valStack[valTop-1] = valStack[valTop-1] * valStack[valTop];
				valTop--;
				break;
			
			
			case '/':
				if (valTop <1) {
					return error("Syntax error....");
				}
				valStack[valTop-1] = valStack[valTop-1] / valStack[valTop];
				valTop--;
				break;
			case 'u':
				if (valTop <0) {
					return error("Syntax error....");
				}
				valStack[valTop] = -valStack[valTop];
				break;
			case '^':
				if (valTop < 1) {
					return error("Syntax error....");
					
				}
				valStack[valTop-1] = Math.pow(valStack[valTop-1], valStack[valTop]);
				valTop--;
				break;
			case 'f':
				if (valTop <0) {
					return error("Syntax error....");
				}
				valStack[valTop] = factorial(valStack[valTop]);
				break;
			case 'p':
				if (valTop <1) {
					return error("Syntax error....");
				}
				valStack[valTop-1] = factorial(valStack[valTop-1])/factorial(valStack[valTop-1]-valStack[valTop]);
				valTop--;
				break;
			case 'c':
				if (valTop < 1) {
					return error ("Syntax error ....");
				}
				valStack[valTop-1] = factorial(valStack[valTop-1])/(factorial(valStack[valTop]) * factorial(valStack[valTop-1]-valStack[valTop]));
				valTop--;
				break;
			case ')':
				opTop--;
				break;
		
		}
		
		opTop--;
		return 0;
	
	}
	
	public int parse() throws IOException {
		System.out.println("Enter an expression (q to quit):");
		workString = READER.readLine();
		expressionTokenizer = new StringTokenizer(workString);
				
		opTop =0;
		valTop = -1;
		opStack[opTop] = Token.END_OF_STRING.getSymbol().charAt(0);
		firstToken = true;
				
		while(true) {
			if (getToken() != 0) {
				return 1;
			}
			
			if (token == Token.VAL) {
				if (shift() != 0) {
					return 1;
				}
				continue;
			}
			
			/* input is an operator */
			log(String.format("stack=%s, token=%s", opStack[opTop]+"", token.getDescription()+":"+token.getSymbol()));
			int lineNumber = Token.fromSymbol(new String(new char[]{ opStack[opTop]})).ordinal();
			int columnNumber = token.ordinal();
			
			switch (PARSE_TABLE.TABLE[lineNumber].ACTION_LINE[columnNumber]) {
				case R:
					if (reduce() != 0) {
						return 1;
					}
					break;
				case S:
					if (shift() != 0) {
						return 1;
					}
					break;
				case A:
					if (valTop != 0) {
						return error("Syntax error....");
					}
					log(String.format("value= %f\n", valStack[valTop]));
					return 0;
					
				case ER1:
					return error("Missing right parenthesis....");
				case EO1:
					return error("An operator is missing ....");
				case ER2:
					return error ("There is an unbalanced right parenthesis....");
				case E4:
					return error("Invalid function argument...");	
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		SimpleExpressionParser parser = new SimpleExpressionParser();
		while(true) {
			parser.parse();
		}
	}
	
	
}
