package com.helloworld.core;

public class Kli {
    private int termCode;
    private int parentTermCode;
    private String termDescription;
    private int adicTotal;
    private int adicOnline;
    

    public Kli(int termCode, String termDescription, int parentTermCode) {
    	this.termCode = termCode;
    	this.termDescription = termDescription;
    	this.parentTermCode = parentTermCode;
       
    }

    public int getTermCode()
    {
      return termCode;
    }

    public void setTermCode(int termCode)
    {
      this.termCode = termCode;
    }

    public String getTermDescription()
    {
      return this.termDescription;
    }

    public void setTermDescription(String termDescription)
    {
      this.termDescription = termDescription;
    }
  
    public int getParentTermCode()
    {
      return this.parentTermCode;
    }

    public void setParentTermCode(int parentTermCode)
    {
      this.parentTermCode = parentTermCode;
    }
    
    public int getAdicTotal()
    {
      return this.adicTotal;
    }
    
    public void setAdicTotal(int adicTotal)
    {
      this.adicTotal = adicTotal;
    }
    
    public int getAdicOnline()
    {
      return this.adicTotal;
    }
    
    public void setAdicOnlinr(int adicOnline)
    {
      this.adicOnline = adicOnline;
    }
    
}

