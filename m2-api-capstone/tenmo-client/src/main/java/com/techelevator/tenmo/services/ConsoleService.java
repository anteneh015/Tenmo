package com.techelevator.tenmo.services;


import com.techelevator.tenmo.models.Transfers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.DoubleAccumulator;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	private TenmoService tenmo;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public void showWelcomeBanner() {
		out.println("*********************");
		out.println("* Welcome to TEnmo! *");
		out.println("*********************");
		out.flush();
	}

	public void displayMessage(String message) {
		out.println(message);
		out.flush();
	}

	public void showRegistrationFailed(String message) {
		out.println("REGISTRATION ERROR: "+message);
		out.println("Please attempt to register again.");
		out.flush();
	}

	public void showLoginFailed(String message) {
		out.println("LOGIN ERROR: "+message);
		out.println("Please attempt to login again.");
		out.flush();
	}

	public void printError(String errorMessage) {
		System.err.println(errorMessage);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void printOutBalance(double viewBalance){
		System.out.println("User Account Balance:");
		System.out.println("------------------------");
		System.out.println("$" + viewBalance);
		System.out.println("------------------------");
	}

	public void displayUsernames(String[] usernameArray) {
		System.out.println("Select a user to send money to");
		System.out.println("------------------------------------");
		for (String eachString : usernameArray) {
			System.out.println(eachString);
		}
		System.out.println("------------------------------------\n");
	}

//	public void displayTransfers(List<Transfers> transfersList) {
//		System.out.println("Transfers:");
//		System.out.printf("%-10s | %-20s | %-20s | %-20s \n", "ID", "Account sent from", "Account sent to","Amount");
//		System.out.println("-------------------------------------------------------------------------------");
//		for (Transfers eachTransfer: transfersList) {
//			System.out.printf("%-10s | %-20s | %-20s | %-20s \n", eachTransfer.getTransferId(), eachTransfer.getAccountFrom(), eachTransfer.getAccountTo(), "$" + eachTransfer.getAmountTransfer());
//		}
//		System.out.println("-------------------------------------------------------------------------------");
//	}

	public void transferDisplayMenuStart() {
		System.out.println("Transfers:");
		System.out.printf("%-10s | %-20s | %-20s | %-20s \n", "ID", "Account sent from", "Account sent to","Amount");
		System.out.println("-------------------------------------------------------------------------------");
	}
	public void transferDisplayMenuEnd() {
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
	}

	public void displayTransfer(Transfers transfers) {
		System.out.printf("%-10s | %-20s | %-20s | %-20s \n", transfers.getTransferId(), transfers.getAccountFrom(), transfers.getAccountTo(), "$" + transfers.getAmountTransfer());
	}

	public void usernameNotFound() {
		System.out.println("");
		System.out.println("Username not found, please enter a valid one.\n");
	}

	public void enterValidEntry(){
		System.out.println("");
		System.out.println("Please enter a valid entry.\n");
	}

	public void insufficientFunds() {
		System.out.println("");
		System.out.println("You do not have enough of a balance to transfer that amount.\n");
	}

	public boolean isNumeric(String string){
		try{
			Double.parseDouble(string);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	public void invalidTransferAmount(){
		System.out.println("");
		System.out.println("Invalid amount, please try again.\n");
	}
}
