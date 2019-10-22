package com.tik.android.component.account.verify.bean;

public class Ticket {
	private String ticket;

	public void setTicket(String ticket){
		this.ticket = ticket;
	}

	public String getTicket(){
		return ticket;
	}

	@Override
 	public String toString(){
		return 
			"Ticket{" +
			"ticket = '" + ticket + '\'' + 
			"}";
		}
}
