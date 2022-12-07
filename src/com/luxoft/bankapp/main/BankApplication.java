package com.luxoft.bankapp.main;

import java.util.HashSet;
import java.util.Set;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.domain.SavingAccount;
import com.luxoft.bankapp.email.EmailService;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.NotEnoughFundsException;
import com.luxoft.bankapp.exceptions.OverdraftLimitExceededException;
import com.luxoft.bankapp.service.BankDataLoaderService;
import com.luxoft.bankapp.service.BankReport;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.utils.Arguments;

public class BankApplication {
	
	private static Bank bank;
	
	public static void main(final String[] args) throws InterruptedException {
		Arguments arguments = new Arguments(args);

		final String fileName = args[1];
		
		if (arguments.hasKey("-loadbank")) {
			BankService.setSerializationFileName(args[2]);
			bank = new Bank();
			EmailService emailService = new EmailService();
			bank.setEmailService(emailService);
			BankDataLoaderService bankDataLoaderService = new BankDataLoaderService(bank);
			bankDataLoaderService.readClients(fileName);
			printBalance();
			emailService.close();
		} else  if (arguments.hasKey("-loadfeed")) {
			BankService.setSerializationFileName(fileName);
			bank = BankService.readBank();	
			printBalance();
		} else if (arguments.hasKey("-statistics")) {
			BankService.setSerializationFileName(fileName);
			bank = BankService.readBank();
			BankReport bankReport = new BankReport(bank);
			
			System.out.println("Bank number of clients: " + bankReport.getNumberOfClients());
			System.out.println("Bank number of accounts: " + bankReport.getNumberOfAccounts());
			System.out.println("Total sum in accounts: " + bankReport.getTotalSumInAccounts());
			System.out.println("Bank credit sum: " + bankReport.getBankCreditSum());
			
			
			System.out.println("Bank clients sorted by name: " + bankReport.getClientsSorted());
			System.out.println("Bank accounts sorted by sum: " + bankReport.getAccountsSortedBySum());
			System.out.println("Bank accounts: " + bankReport.getCustomerAccounts());
			System.out.println("Bank clients by city: " + bankReport.getClientsByCity());
			
		}
		else {
			bank = new Bank();
			modifyBank();
			BankService.printMaximumAmountToWithdraw(bank);
		}
		
	}
	
	private static void modifyBank() {
		Client client1 = new Client("John", Gender.MALE);
		Account account1 = new SavingAccount(1, 100);
		Account account2 = new CheckingAccount(2, 100, 20);
		Set<Account> accounts = new HashSet<Account>();
		accounts.add(account1);
		accounts.add(account2);
		account1.setClient(client1);
		account2.setClient(client1);
		client1.setAccounts(accounts);
		
		try {
		   BankService.addClient(bank, client1);
		} catch(ClientExistsException e) {
			System.out.format("Cannot add an already existing client: %s%n", client1);
	    } 

		account1.deposit(100);
		try {
		  account1.withdraw(10);
		} catch (OverdraftLimitExceededException e) {
	    	System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	    	System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  account2.withdraw(90);
		} catch (OverdraftLimitExceededException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  account2.withdraw(100);
		} catch (OverdraftLimitExceededException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  BankService.addClient(bank, client1);
		} catch(ClientExistsException e) {
		  System.out.format("Cannot add an already existing client: %s%n", client1);
	    } 
	}
	
	private static void printBalance() {
		System.out.format("%nPrint balance for all clients%n");
		for(Client client : bank.getClients()) {
			System.out.println("Client: " + client);
		}
	}

}
