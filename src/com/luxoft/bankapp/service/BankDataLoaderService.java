package com.luxoft.bankapp.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import com.luxoft.bankapp.domain.AbstractAccount;
import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.utils.Params;

public class BankDataLoaderService {
	private Bank bank;
	
    public BankDataLoaderService(Bank bank) {
		this.bank = bank;
	}

	public void readClients(String fileName) {
    	if (fileName == null) { 
    		return;
    	}
    	
        try {
        	LineNumberReader lineNumberReader = 
        			new LineNumberReader(
        					new BufferedReader(
        							new FileReader(fileName)));
            String line;
            while ((line = lineNumberReader.readLine()) != null) {
            	Params params = new Params(line.split(";"));
            	
            	String name = params.get("name");
            	Gender gender = Gender.parse(params.get("gender"));
            	Account account = AbstractAccount.parse(params);
            	String city = params.get("city");
            	String phoneAreaCode = params.get("phoneAreaCode");
            	String phoneNumber = params.get("phoneNumber");
        		
                Client client = new Client(name, gender, account);
                client.setCity(city);
                client.setPhoneAreaCode(phoneAreaCode);
                client.setPhoneNumber(phoneNumber);
            	
                addClient(client);
            }
            
            lineNumberReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void addClient(Client client) {
		try {
			BankService.addClient(bank, client);
		} catch (ClientExistsException e) {
			e.printStackTrace();
		}
	}

}
