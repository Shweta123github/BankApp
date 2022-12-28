package com.admin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import main.Main;

public class AdminPanel {

	public void adminLogin()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter Admin Email ID :");
		String email1=s.next();
		
		System.out.println("Enter Admin Password :");
		String pass1=s.next();
		
		if(email1.equals("admin@gmail.com") && pass1.equals("admin123"))
		{
			System.out.println("-------------Welcome Admin--------------");
			
			welcomeAdmin();
		}
		else
		{
			System.err.println("-----------Invalid Admin Credentials----------");
			
			Main obj=new Main();
			obj.startBankApp();
		}
	}
	
	private void welcomeAdmin()
	{
		System.out.println("Choose one option below :-\r\n"
							+ "1. Open Account\r\n"
							+ "2. Close Account\r\n"
							+ "3. Deposit\r\n"
							+ "4. Withdrawl\r\n"
							+ "5. Check Balance\r\n"
							+ "6. Logout");
		
		Scanner s=new Scanner(System.in);
		int i=s.nextInt();
		
		switch(i)
		{
			case 1:
				openAccount();
				break;
			case 2:
				closeAccount();
				break;
			case 3:
				depositAmount();
				break;
			case 4:
				withdrawlAmount();
				break;
			case 5:
				checkCustBalance();
				break;
			case 6:
				adminLogout();
				break;
			default:
				welcomeAdmin();
				break;
		}
	}
	
	private void openAccount()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter Customer Name :");
		String cust_name=s.next();
		cust_name=cust_name.substring(0, 1).toUpperCase() + cust_name.substring(1).toLowerCase();
		
		System.out.println("Enter Customer Account No :");
		String cust_accountno=s.next();
		
		boolean accno_status;
		do
		{
			accno_status=false;
			if(cust_accountno.length() != 10)
			{
				System.err.println("-------------Invalid Account No. (account no should be of 10 digits)---------------");
				
				System.out.println("Enter Customer Account No :");
				cust_accountno=s.next();
				
				accno_status=true;
			}
			else if(!cust_accountno.startsWith("100100"))
			{
				System.err.println("-------------Invalid Account No. (account no should starts from 100100)---------------");
				
				System.out.println("Enter Customer Account No :");
				cust_accountno=s.next();
				
				accno_status=true;
			}
		}
		while(accno_status);
		
		System.out.println("Enter Phone No :");
		String cust_phno=s.next();
		
		System.out.println("Enter Amount :");
		int cust_amount=s.nextInt();
		
		while(cust_amount < 5000)
		{
			System.err.println("------------Amount should be 5000rs or more-----------------------");
			System.out.println("Enter Amount :");
			cust_amount=s.nextInt();
		}
		
		String cust_pass=generatePassword(cust_name, cust_accountno);
		
		boolean status=AdminDbOperations.openCustAccount(cust_name, cust_accountno, cust_phno, cust_amount, cust_pass);
		if(status)
		{
			System.out.println("--------------Account opened successfully-------------------------");
			welcomeAdmin();
		}
		else
		{
			System.err.println("----------------Error occured----------------");
			welcomeAdmin();
		}
		
		welcomeAdmin();
	}
	private String generatePassword(String name, String accountno)
	{
		String sub_name=name.substring(0, 3).toLowerCase();
		String sub_accno=accountno.substring(6, 10);
		
		String pass1=sub_name+sub_accno;
		
		return pass1;
	}
	
	private void closeAccount()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter account no. :");
		String accno=s.next();
		
		boolean status=AdminDbOperations.closeCustAccount(accno);
		if(status)
		{
			System.out.println("--------------Account closed successfully-------------");
		}
		else
		{
			System.err.println("-----------Account not closed to to some error--------------");
		}
		
		welcomeAdmin();
	}
	private void adminLogout()
	{
		Main m=new Main();
		m.startBankApp();
	}
	
	private void depositAmount()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter customer account no :");
		String accno=s.next();
		
		System.out.println("Enter the amount you want to deposit");
		String amount=s.next();
		
		System.out.println("Enter comment :");
		String comment=s.next();
		
		LocalDate ld=LocalDate.now();
		String date1=ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalTime lt=LocalTime.now();
		String time1=lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		AdminDbOperations.depositWithdrawl(accno, "deposit", amount, comment, date1, time1);
		
		welcomeAdmin();
	}
	
	private void withdrawlAmount()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter customer account no :");
		String accno=s.next();
		
		System.out.println("Enter the amount you want to withdrawl");
		String amount=s.next();
		
		System.out.println("Enter comment :");
		String comment=s.next();
		
		LocalDate ld=LocalDate.now();
		String date1=ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalTime lt=LocalTime.now();
		String time1=lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		AdminDbOperations.depositWithdrawl(accno, "withdrawl", amount, comment, date1, time1);
		
		welcomeAdmin();
	}
	
	private void checkCustBalance()
	{
		Scanner s=new Scanner(System.in);
		
		System.out.println("Enter customer account no :");
		String accno=s.next();
		
		String balance=AdminDbOperations.getCustomerBalance(accno);
		
		System.out.println("Your balance is : "+balance);
		
		welcomeAdmin();
	}
}


