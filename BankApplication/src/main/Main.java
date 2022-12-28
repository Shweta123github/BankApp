package main;

import java.util.Scanner;

import com.admin.AdminPanel;
import com.customer.Customer;

public class Main {

	public void startBankApp()
	{
		System.out.println("Choose one option below :-\r\n"
							+ "1. Admin Login\r\n"
							+ "2. Customer Login\r\n"
							+ "3. Exit");
		
		Scanner s=new Scanner(System.in);
		int i=s.nextInt();
		
		if(i==1)
		{
			AdminPanel ap=new AdminPanel();
			ap.adminLogin();
		}
		else if(i==2)
		{
			new Customer().customerLogin();
		}
		else if(i==3)
		{
			System.out.println("------------Thank you, please visit again......!!----------");
			System.exit(0);
		}
		else
		{
			System.err.println("----------Invalid input--------------");
			
			startBankApp();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println("----------------Welcome to SP Bank App--------------");
		
//		Main m=new Main();
//		m.startBankApp();
		
		new Main().startBankApp();
	}
}


