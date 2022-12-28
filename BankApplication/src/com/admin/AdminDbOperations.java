package com.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dbcon.DbConnection;

public class AdminDbOperations {

	static boolean openCustAccount(String name, String accno, String phno, int amount, String pass)
	{
		boolean status=false;
		try(
				Connection con=DbConnection.getConnection();
				PreparedStatement ps=con.prepareStatement("insert into customers values(?,?,?,?,?,?)");
			)
		{
			ps.setString(1, name);
			ps.setString(2, accno);
			ps.setString(3, phno);
			ps.setInt(4, amount);
			ps.setString(5, pass);
			ps.setString(6, "true");
			
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
	
	public static boolean closeCustAccount(String accno)
	{
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("update customers set active='false' where account_no=?");
			ps.setString(1, accno);
			
			int i=ps.executeUpdate();
			if(i>0)
			{
				status = true;
			}
			else
			{
				status = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return status;
	}
	
	public static void depositWithdrawl(String accno, String dept_with, String amount, String comment, String date1, String time1)
	{
		if(dept_with.equals("deposit"))
		{
			boolean active=checkAccountIsActive(accno);
			if(active)
			{
				updateDepositAmt(accno, amount);
				insertStatement(accno, dept_with, amount, comment, date1, time1);
			}
			else
			{
				System.err.println("-------------This account is not active----------------");
			}
		}
		else if(dept_with.equals("withdrawl"))
		{
			boolean active=checkAccountIsActive(accno);
			if(active)
			{
				if(canWithdrawlAmt(accno, amount))
				{
					updateWithdrawlAmt(accno, amount);
					insertStatement(accno, dept_with, amount, comment, date1, time1);
				}
				else
				{
					System.err.println("-----------Insufficient Funds-------------");
				}
			}
			else
			{
				System.err.println("-------------This account is not active----------------");
			}
		}
	}
	
	public static void insertStatement(String accno, String dept_with, String amount, String comment, String date1, String time1)
	{
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("insert into statement values(?,?,?,?,?,?)");
			ps.setString(1, accno);
			ps.setString(2, dept_with);
			ps.setString(3, amount);
			ps.setString(4, comment);
			ps.setString(5, date1);
			ps.setString(6, time1);
			
			int i=ps.executeUpdate();
			if(i>0)
			{
				System.out.println("-------------Customer Statement Updated Successfully--------------");
			}
			else
			{
				System.err.println("-------------Customer Statement Not Updated Due To Some Error--------------");
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean checkAccountIsActive(String accno)
	{
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("select active from customers where account_no=?");
			ps.setString(1, accno);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				String active=rs.getString("active");
				if(active.equals("true"))
				{
					status=true;
				}
				else if(active.equals("false"))
				{
					status=false;
				}
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
	
	public static void updateDepositAmt(String accno, String dept_amt)
	{
		String old_amt=getCustomerBalance(accno);
		int new_amt=Integer.parseInt(old_amt) + Integer.parseInt(dept_amt);
		
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("update customers set amount=? where account_no=?");
			ps.setString(1, String.valueOf(new_amt));
			ps.setString(2, accno);
			
			int i=ps.executeUpdate();
			if(i>0)
			{
				System.out.println("---------------Customer Amount Updated Successfully-----------------");
			}
			else
			{
				System.err.println("---------------Customer Amount Not Updated Due To Some Error-----------------");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getCustomerBalance(String accno)
	{
		String amount="0";
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("select amount from customers where account_no=?");
			ps.setString(1, accno);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				amount=rs.getString("amount");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return amount;
	}
	
	public static boolean canWithdrawlAmt(String accno, String with_amt)
	{
		boolean status=false;
		String amount="0";
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("select amount from customers where account_no=?");
			ps.setString(1, accno);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				amount=rs.getString("amount");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(Integer.parseInt(amount) >= Integer.parseInt(with_amt))
		{
			status=true;
		}
		else
		{
			status=false;
		}
		
		return status;
	}
	
	public static boolean updateWithdrawlAmt(String accno, String with_amt)
	{
		String old_amt=getCustomerBalance(accno);
		int new_amt=Integer.parseInt(old_amt) - Integer.parseInt(with_amt);
		
		boolean status=false;
		try
		{
			Connection con=DbConnection.getConnection();
			
			PreparedStatement ps=con.prepareStatement("update customers set amount=? where account_no=?");
			ps.setString(1, String.valueOf(new_amt));
			ps.setString(2, accno);
			
			int i=ps.executeUpdate();
			if(i>0)
			{
				System.out.println("---------------Customer Amount Updated Successfully-----------------");
			}
			else
			{
				System.err.println("---------------Customer Amount Not Updated Due To Some Error-----------------");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}


