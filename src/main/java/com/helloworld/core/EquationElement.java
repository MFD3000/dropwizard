package com.helloworld.core;

import java.util.List;

public class EquationElement {
	private String operation;
	private String termcode;
	private List<EquationElement> members;

	
	public EquationElement(){
		
		
	}
	
	public EquationElement( String operation, String termcode, List<EquationElement> members){
		this.operation = operation;
		this.termcode = termcode;
		this.members = members;
		
	}
	
	 public String getOperation()
	    {
	      return this.operation;
	    }

	    public void setOperation(String operation)
	    {
	      this.operation = operation;
	    }
	
	    public String getTermcode()
	    {
	      return this.termcode;
	    }

	    public void setTermcode(String termcode)
	    {
	    	this.termcode = termcode;
	    }
	
	    public List<EquationElement> getMembers()
	    {
	      return this.members;
	    }

	    public void setMembers(List<EquationElement> members)
	    {
	      this.members = members;
	    }
	   

}
