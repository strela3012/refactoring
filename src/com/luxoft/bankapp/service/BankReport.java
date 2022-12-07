package com.luxoft.bankapp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;

public class BankReport {

    private Bank bank;

    public BankReport(Bank bank){
        this.bank = bank;
    }
    
    public Bank getBank() {
		return bank;
	}

    /* Returns the number of bank clients */
    public int getNumberOfClients(){
        return bank.getClients().size();
    }
    
    /* Returns the total number of accounts for all bank clients */    
    public int getNumberOfAccounts(){
    	Set<Account> accounts = new HashSet<Account>();
        for (Client client: bank.getClients())
        	accounts.addAll(client.getAccounts());
        return accounts.size();
    }
    
    /* Returns the set of clients in alphabetical order */
    public SortedSet<Client> getClientsSorted() {
        SortedSet<Client> clients = new TreeSet<>(new Comparator<Client>() {
        	@Override
			public int compare(Client client1, Client client2) {
			    if (client1.getName() == null) {
                    return 1;
			    }
				return client1.getName().compareTo(client2.getName());
			}
        });
        clients.addAll(bank.getClients());
        return clients;
    }

    /* Returns the total sum (balance) from the accounts of all bank clients */
    public double getTotalSumInAccounts(){
        double sum = 0.0;
        Set<Account> accounts = new HashSet<Account>();
        for (Client client: bank.getClients())
        	accounts.addAll(client.getAccounts());
        for (Account account: accounts) {
            sum += account.getBalance();
        }
        return Math.round(sum * 100) / 100d;
    }

    /* Returns the set of all accounts. The list is ordered by current account balance */
    public SortedSet<Account> getAccountsSortedBySum(){
        SortedSet<Account> result = new TreeSet<>(new Comparator<Account>() {
            @Override public int compare(Account o1, Account o2) {
                return (int) Math.round(o1.getBalance() - o2.getBalance());
            }
        });
        for (Client client: bank.getClients())
            result.addAll(client.getAccounts());
        return result;
    }

    /* Returns the total amount of credits granted to the bank clients. That is, the sum of all values above account balance for CheckingAccount */
    public double getBankCreditSum() {
        double result = 0.0;
        Set<Account> accounts = new HashSet<Account>();
        for (Client client: bank.getClients())
        	accounts.addAll(client.getAccounts());
        for (Account account: accounts)
        	if (account instanceof CheckingAccount) {
        		if (account.getBalance() < 0) {
                    result -= account.getBalance();
        		}
        	}
            
        return Math.round(result * 100) / 100d;
    }

    /* Returns a map of client accounts */
    public Map<Client, Collection<Account>> getCustomerAccounts(){
        Map<Client, Collection<Account>> result = new HashMap<>();
        for (Client client: bank.getClients()) {
            result.put(client, client.getAccounts());
        }
        return result;
    }
    
    /* Returns a map of cities and clients */
    public Map<String, List<Client>> getClientsByCity() {
        Set<Client> sortedClients = bank.getClients();
        Map<String, List<Client>> clientCities = new TreeMap<>();
        for (Client client : sortedClients) {
            String city = client.getCity();
            if (!clientCities.containsKey(city)) {
                List<Client> clientsNotYetInMap = new ArrayList<>();
                clientsNotYetInMap.add(client);
                clientCities.put(city, clientsNotYetInMap);
            } else {
                clientCities.get(city).add(client);
            }
        }
        return clientCities;
    }


}