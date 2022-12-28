package com.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dbcon.DbConnection;

public class CustomerDbOperations {

	public static ResultSet custLogin(String accno, String pass)
	{
		ResultSet rs=null;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("select * from customers where account_no=? and password=?");
			ps.setString(1, accno);
			ps.setString(2, pass);
			
			rs=ps.executeQuery();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public static boolean transferTheAmount(String from_accno, String to_accno, String transfer_amount, CustomerBean cb)
	{
		LocalDate ld=LocalDate.now();
		String date1=ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalTime lt=LocalTime.now();
		String time1=lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		int i1=0, i2=0;
		
		//----------from account------------------------------------------------------------
		String comment1=transfer_amount+"rs transfered to "+to_accno;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("insert into statement values(?,?,?,?,?)");
			ps.setString(1, from_accno);
			ps.setString(2, "withdrawl");
			ps.setString(3, transfer_amount);
			ps.setString(4, comment1);
			ps.setString(5, date1);
			ps.setString(6, time1);
			
			i1=ps.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		//----------to account------------------------------------------------------------
		String comment2=transfer_amount+"rs deposited by "+cb.getName()+" ("+from_accno+")";
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("insert into statement values(?,?,?,?,?)");
			ps.setString(1, to_accno);
			ps.setString(2, "deposit");
			ps.setString(3, transfer_amount);
			ps.setString(4, comment2);
			ps.setString(5, date1);
			ps.setString(6, time1);
			
			i2=ps.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		boolean main_status=false;
		if(i1>0 && i2>0)
		{
			boolean status1=addAmount(to_accno, transfer_amount);
			boolean status2=subAmount(from_accno, transfer_amount, cb);
			
			if(status1 && status2)
			{
				main_status=true;
			}
			else
			{
				main_status=false;
			}
		}
		else
		{
			main_status=false;
		}
		return main_status;
	}
	
	public static boolean addAmount(String accno, String amount)
	{
		String previous_balance=getCustomerBalance(accno);
		int new_balance=Integer.parseInt(previous_balance) + Integer.parseInt(amount);
		
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("update customers set amount=? where account_no=?");
			ps.setInt(1, new_balance);
			ps.setString(2, accno);
			
			int i=ps.executeUpdate();
			if(i>0)
			{
				status=true;
			}
			else
			{
				status=false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return status;
	}
	
	public static boolean subAmount(String accno, String amount, CustomerBean cb)
	{
		String previous_balance=cb.getAmount();
		int new_balance=Integer.parseInt(previous_balance) - Integer.parseInt(amount);
		
		
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("update customers set amount=? where account_no=?");
			ps.setInt(1, new_balance);
			ps.setString(2, accno);
			
			int i=ps.executeUpdate();
			if(i>0)
			{
				status=true;
			}
			else
			{
				status=false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return status;
	}
	
	public static String getCustomerBalance(String accno)
	{
		String balance="0";
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("select amount from customers where account_no=?");
			ps.setString(1, accno);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				balance=rs.getString("amount");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return balance;
	}
	
	public static ResultSet getMiniStatement(String accno)
	{
		ResultSet rs=null;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("select * from statement where account_no=?");
			ps.setString(1, accno);
			
			rs=ps.executeQuery();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rs;
	}
}


