package com.helloworld.core;

public class EquationRequest {
	private EquationElement equation;
	private Integer bannerid;
	private Integer zoneid;
	
	public EquationRequest(EquationElement equation, Integer bannerid,
			Integer zoneid) {
		
		this.equation = equation;
		this.bannerid = bannerid;
		this.zoneid = zoneid;
	}
	
	public EquationRequest(){};
	public EquationElement getEquation() {
		return equation;
	}
	public void setEquation(EquationElement equation) {
		this.equation = equation;
	}
	public Integer getBannerid() {
		return bannerid;
	}
	public void setBannerid(Integer bannerid) {
		this.bannerid = bannerid;
	}
	public Integer getZoneid() {
		return zoneid;
	}
	public void setZoneid(Integer zoneid) {
		this.zoneid = zoneid;
	}
	
	

}
