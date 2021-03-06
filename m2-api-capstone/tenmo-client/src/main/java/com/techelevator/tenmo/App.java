package com.techelevator.tenmo;

import com.techelevator.tenmo.auth.models.AuthenticatedUser;
import com.techelevator.tenmo.auth.models.UserCredentials;
import com.techelevator.tenmo.auth.services.AuthenticationService;
import com.techelevator.tenmo.auth.services.AuthenticationServiceException;
import com.techelevator.tenmo.models.Transfers;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;
import io.cucumber.java.eo.Do;

import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmo;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {

    	this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		console.showWelcomeBanner();
		registerAndLogin();
		this.tenmo = new TenmoService(API_BASE_URL, this.currentUser);
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		console.printOutBalance(tenmo.getAccountBalance());

	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		List<Transfers> transfersList = tenmo.getTransfersFromAccountId(tenmo.getAccountIdFromUsername(currentUser.getUser().getUsername()));
		console.transferDisplayMenuStart();
		for (Transfers eachTransfer : transfersList) {
			console.displayTransfer(eachTransfer);
		}
		console.transferDisplayMenuEnd();
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		while (true){
			String[] usernames = tenmo.getUsernames();

			console.displayUsernames(usernames);
			String getUsernameTransfer = getUsernameToTransferTo(usernames);

			if(getUsernameTransfer.equals("0")){
				break;
			}
		double amountToTransfer = getAmountToTransfer();
			String userConfirmation = getUserConfirmation(amountToTransfer, getUsernameTransfer);
			if(userConfirmation.equalsIgnoreCase("N")) {
				break;
			}
			Transfers newTransfer = new Transfers(2,2,
					tenmo.getAccountIdFromUsername(getUsernameTransfer),
					tenmo.getAccountIdFromUsername(currentUser.getUser().getUsername()), amountToTransfer);
			tenmo.addTransfer(newTransfer);
			tenmo.addToBalance(tenmo.getAccountFromUsername(getUsernameTransfer), amountToTransfer );
			tenmo.subtractFromBalance(tenmo.getAccountFromUsername(currentUser.getUser().getUsername()), amountToTransfer);
			break;
	}
	}

	private String getUserConfirmation(double amountToTransfer, String getUsernameTransfer){
		String string = console.getUserInput("Are you sure you want to transfer $" + amountToTransfer + " to " + getUsernameTransfer + " (Y/N)");

		while(true) {
			if (!string.equalsIgnoreCase("Y") && !string.equalsIgnoreCase("N")) {
				console.enterValidEntry();
				string = console.getUserInput("Are you sure you want to transfer $" + amountToTransfer + " to " + getUsernameTransfer + " (Y/N)");
			} else {
				break;
			}
		}return string;
    }

	private double getAmountToTransfer() {
		String userInput = console.getUserInput("How much would you like to transfer?");
		while (true) {
			if (!console.isNumeric(userInput)) {
				console.invalidTransferAmount();
				userInput = console.getUserInput("How much would you like to transfer?");
			} else {
				double userInputAsDouble = Double.parseDouble(userInput);
				if (userInputAsDouble <= 0) {
					console.invalidTransferAmount();
					userInput = console.getUserInput("How much would you like to transfer?");
				} else if (userInputAsDouble > tenmo.getAccountBalance()) {
					console.insufficientFunds();
					userInput = console.getUserInput("How much would you like to transfer?");
				} else {
					return userInputAsDouble;
				}
			}
		}
	}

	private String getUsernameToTransferTo(String[] usernames) {
		while (true) {
			String userInput = console.getUserInput("Enter username of user you are sending to (0 to cancel)");

				for (int i = 0; i < usernames.length; i++) {
					if (userInput.equals(usernames[i]) || userInput.equals("0")) {
						return userInput;

					} else if (i == (usernames.length - 1) && !userInput.equals(usernames[i])){
						 console.usernameNotFound();

					}
				}
			}
		}


	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		console.displayMessage("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
				console.displayMessage("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
				console.showRegistrationFailed(e.getMessage());
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				console.showLoginFailed(e.getMessage());
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
