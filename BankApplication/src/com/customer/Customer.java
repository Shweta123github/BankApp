package com.customer;

import java.sql.ResultSet;
import java.util.Scanner;

import main.Main;

public class Customer {

	public void customerLogin()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter customer account no :");
		String accno1=s.next();
		
		System.out.println("Enter customer password :");
		String pass1=s.next();
		
		ResultSet rs=CustomerDbOperations.custLogin(accno1, pass1);
		
		try
		{
			if(rs.next())
			{
				String name1=rs.getString("name");
				String phone_no=rs.getString("phone_no");
				String amount=rs.getString("amount");
				String active=rs.getString("active");
				
				CustomerBean cb=new CustomerBean();
				cb.setName(name1);
				cb.setAccount_no(accno1);
				cb.setPhone_no(phone_no);
				cb.setAmount(amount);
				cb.setActive(active);
				
				System.out.println("-----------------Welcome "+name1+"------------------");
				startCustomerPanel(cb);
			}
			else
			{
				System.err.println("-----------------Invalid Credentials-----------------");
				
				Main m=new Main();
				m.startBankApp();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void startCustomerPanel(CustomerBean cb)
	{
		System.out.println("Choose one option below :-\r\n"
							+ "1. Transfer Amount\r\n"
							+ "2. Check Balance\r\n"
							+ "3. Statement\r\n"
							+ "4. Logout");
		
		Scanner s=new Scanner(System.in);
		int i=s.nextInt();
		
		switch(i)
		{
			case 1:
				transferAmount(cb);
				break;
			case 2:
				checkBalance(cb);
				break;
			case 3:
				statement(cb);
				break;
			case 4:
				Main m=new Main();
				m.startBankApp();
				break;
			default:
				System.err.println("--------------Invalid Input--------------");
		}
	}
	
	public void transferAmount(CustomerBean cb)
	{
		String from_accno=cb.getAccount_no();
		String balance=cb.getAmount();
		
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter the account no in which you want to transfer the amount :");
		String to_accno=s.next();
		
		System.out.println("Enter the amount you want to transfer :");
		String transfer_amount=s.next();
		
		if(Integer.parseInt(balance) >= Integer.parseInt(transfer_amount))
		{
			boolean status=CustomerDbOperations.transferTheAmount(from_accno, to_accno, transfer_amount, cb);
			if(status)
			{
				System.out.println("-----------------Amount transfered successfully---------------------");
				int new_balance=Integer.parseInt(balance) - Integer.parseInt(transfer_amount);
				cb.setAmount(String.valueOf(new_balance));
			}
			else
			{
				System.err.println("-----------------Amount not transfered due to some error---------------------");
			}
		}
		else
		{
			System.err.println("--------------Insufficient Funds in Your Account----------------");
			System.err.println("--------------Your available balance is : "+balance+"rs--------------");
		}
		
		startCustomerPanel(cb);
	}
	
	public void statement(CustomerBean cb)
	{
		ResultSet rs=CustomerDbOperations.getMiniStatement(cb.getAccount_no());
		
		try
		{
			System.out.println("Account No.\tDeposit | Withdrawl\tAmount\tDetails \t\t\tDate \t\tTime");
			while(rs.next())
			{
				String accno=rs.getString("account_no");
				String dept_with=rs.getString("dept_with");
				String amount=rs.getString("amount");
				String comment=rs.getString("comment");
				String date1=rs.getString("date1");
				String time1=rs.getString("time1");
				
				System.out.println(accno+" \t"+dept_with+" \t\t"+amount+" \t"+comment+" \t\t\t\t"+date1+" \t"+time1);
			}
			System.out.println("-----------------------------------------------------------------------------------------------------------------");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		startCustomerPanel(cb);
	}
	
	public void checkBalance(CustomerBean cb)
	{
		System.out.println("Your current balance is : "+cb.getAmount()+"rs");
		
		startCustomerPanel(cb);
	}
}


