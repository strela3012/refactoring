package com.luxoft.bankapp.employees;


public abstract class AbstractSpecialist extends BankEmployee {
	
	//next element in chain or responsibility 
	protected AbstractSpecialist nextSpecialist; 
	
	public void setNextSpecialist(AbstractSpecialist nextSpecialist){ 
		this.nextSpecialist = nextSpecialist; 
	} 
	
	public void printFinalDecision(boolean decision) {
		if (decision) {
        	System.out.format("Final approval at the %s level%n", this.getClass().getSimpleName());
		} else {
			System.out.format("Rejected at the %s level%n", this.getClass().getSimpleName());
		}
	}
	
	public static double calculateAnalysisComission(double amount, boolean propertyGuarantee) {
		double comission;
	    if (propertyGuarantee) {
	       comission = amount * 0.002;
	    }
	    else {
	       comission = amount * 0.005;
	    }
	    return comission;
	}
	
	abstract public boolean makeDecision(double amount, double creditRate, double clientSalary, boolean propertyGuarantee);
}
