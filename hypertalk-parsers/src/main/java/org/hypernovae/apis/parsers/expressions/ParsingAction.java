package org.hypernovae.apis.parsers.expressions;

public enum ParsingAction {
	
	
	S("Shift"),
	R("Reduce"),
	A("Accept"),
	ER1("Error", "The right parenthesis is missing"),
	EO1("Error", "An operator is missing"),
	ER2("Error", "Unbalenced right parenthesis"),
	E4("Error", "Invalid function argument")
	;
	
	private final String shortDescription;
	private final String longDescription;
	private final boolean hasOnlyShortDescription;
	
	private ParsingAction(String shortDescription, String longDescription) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		hasOnlyShortDescription = false;
	}
	
	private ParsingAction(String shortDescription, String longDescription, boolean hasOnlyShortDescription) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.hasOnlyShortDescription = hasOnlyShortDescription;
	}
	
	private ParsingAction(String description) {
		this(description, description,true);
	
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public boolean hasOnlyShortDescription() {
		return hasOnlyShortDescription;
	}
	
	

}
