package com.helloworld.core;

public class Kli {
    private int termcode;
    private int parentTermCode;
    private String termDescription;
    private long adicTotal;
    private long adicOnline;
    

    public Kli(int termcode, String termDescription, int parentTermCode) {
    	this.termcode = termcode;
    	this.termDescription = termDescription;
    	this.parentTermCode = parentTermCode;
       
    }

    public int getTermcode()
    {
      return termcode;
    }

    public void setTermcode(int termcode)
    {
      this.termcode = termcode;
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
    
    public long getAdicTotal()
    {
      return this.adicTotal;
    }
    
    public void setAdicTotal(long adicTotal)
    {
      this.adicTotal = adicTotal;
    }
    
    public long getAdicOnline()
    {
      return this.adicOnline;
    }
    
    public void setAdicOnline(long adicOnline)
    {
      this.adicOnline = adicOnline;
    }
    
}

