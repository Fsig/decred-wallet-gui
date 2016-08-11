package com.hosvir.decredwallet;

import java.util.Comparator;

/**
 * 
 * @author Troy
 *
 */
public class TicketComparator implements Comparator<Ticket>{

	@Override
	public int compare(Ticket t1, Ticket t2) {
		if(t1.getBlockHeight() < t2.getBlockHeight())
			return 1;
		else if(t1.getBlockHeight() > t2.getBlockHeight())
			return -1;
		else
			return 0;
	}

}
