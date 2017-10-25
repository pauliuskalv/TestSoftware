package tables;

import java.util.Vector;

import datamanagement.Service;
import tables.Enums.CustomerStatus;

public class Customer 
{
	// time variables
	int waiting;			// time spent waiting for delivery
	int consuming;			// time spent consuming a product for a sense of when to bring receipt
	int checkout;			// time spent waiting for receipt
	
	int estimate;			// estimated time to deliver a product
	
	CustomerStatus status = CustomerStatus.WaitingForDelivery;
	boolean late = false;

	Vector<Service> orders;
	
	//---------------------------
	
	public Customer(Vector<Service> orders)
	{
		order(orders);
	}
	
	//----------------------------
	
	public void order(Vector<Service> orders)
	{
		this.orders = orders;
		this.estimate = getTimeToWait(orders);
	}
	
	public int getTimeToWait(Vector<Service> orders)					// approximate wait time for order
	{
		int max = 0;
		
		for(Service order : orders)
		{	
			if(order.getTime() > max)
			{
				max = order.getTime();
			}
		}
		
		return max;
	}
	
	public void lateCheck()
	{
		if(this.waiting > this.estimate)
		{
			this.late = true;
		}
	}
	
	public void delivered()				// has product
	{
		this.status = CustomerStatus.Consuming;
		this.late = false;
	}
	
	public void done(Restaurant restaurant)						// waiting to leave
	{
		this.status = CustomerStatus.LookingToCheckout;
		Receipt receipt = new Receipt(this.orders);
		
		// write statistics
		restaurant.currentDay.sold.add(this.orders);
		for(Service service : orders)
		{
			restaurant.currentDay.servicesSold++;
			if(service.getTime() != 0)					// a way to differentiate product
			{
				restaurant.currentDay.productsSold++;
			}
		}
		
	}
	
}
