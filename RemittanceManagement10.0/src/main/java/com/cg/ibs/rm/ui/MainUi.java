package com.cg.ibs.rm.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import com.cg.ibs.rm.bean.AutoPayment;
import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.bean.ServiceProvider;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.service.AutoPaymentService;
import com.cg.ibs.rm.service.AutoPaymentServiceImpl;
import com.cg.ibs.rm.service.BankRepresentativeService;
import com.cg.ibs.rm.service.BankRepresentativeServiceImpl;
import com.cg.ibs.rm.service.BeneficiaryAccountService;
import com.cg.ibs.rm.service.BeneficiaryAccountServiceImpl;
import com.cg.ibs.rm.service.CreditCardService;
import com.cg.ibs.rm.service.CreditCardServiceImpl;

public class MainUi {
	private static Scanner scanner;

	private Set<ServiceProvider> serviceProviders;
	private CreditCardService cardService = new CreditCardServiceImpl();
	private BeneficiaryAccountService beneficiaryAccountService = new BeneficiaryAccountServiceImpl();
	private BankRepresentativeService bankRepresentativeService = new BankRepresentativeServiceImpl();
	private AutoPaymentService autopaymentservobj = new AutoPaymentServiceImpl();
	private BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
	private String uci;

	private String message1 = "------------------------";
	private String message2 = "Please enter a valid option.";
	private String message3 = "Choices:";
	private String message4 = "BACK ON HOME PAGE!!";

	private void start() {
		MainMenu choice = null;
		while (MainMenu.QUIT != choice) {
			System.out.println(message1);
			System.out.println("Choose your identity from MENU:");
			System.out.println(message1);
			for (MainMenu menu : MainMenu.values()) {
				System.out.println((menu.ordinal() + 1) + "\t" + menu);
			}
			System.out.println("Your choice :");// choosing of identity whether user or bank representative

			int ordinal = choice(scanner) - 1;
			if (0 <= (ordinal) && MainMenu.values().length > ordinal) {
				choice = MainMenu.values()[ordinal];
				switch (choice) {
				case CUSTOMER:
					login();
					customerAction();
					break;
				case BANKREPRESENTATIVE:
					bankRepresentativeAction();
					break;
				case QUIT:
					System.out.println("Thankyou... Visit again!");
					break;
				}
			} else {
				System.out.println(message2);
				choice = null;
			}
		}
	}

	private void login() {// to enter login details
		int test;
		do {
			test = 0;
			System.out.println("Customer id:");
			try {
				uci = keyboardInput.readLine();

				try {
					if (bankRepresentativeService.checkUciList(uci)) {
						System.out.println("Password");
						keyboardInput.readLine();
						System.out.println("Logged in successfully!!");
					} else {
						System.out.println("Customer ID doesn't exist");
						test = 1;
					}
				} catch (IBSExceptions exception) {
					System.out.println(exception.getMessage());
				}
			} catch (IOException exception) {
				System.out.println(exception.getMessage());
			}
		} while (1 == test);

	}

	private void customerAction() {// facilities provided to the IBS customer
		CustomerUi choice = null;
		System.out.println(message1);
		System.out.println("Choose the desired action");
		System.out.println(message1);
		for (CustomerUi menu : CustomerUi.values()) {
			System.out.println(menu.ordinal() + 1 + "\t" + menu);// showing
																	// options
																	// to the
																	// customer
		}
		System.out.println();
		int ordinal = choice(scanner) - 1;

		if (0 <= ordinal && CustomerUi.values().length > ordinal) {
			choice = CustomerUi.values()[ordinal];
			switch (choice) {
			case CREDITCARD:
				System.out.println("\nThe credit cards already existing for the customer:");
				int count = 1;
				try {
					for (CreditCard card : cardService.showCardDetails(uci)) {
						System.out.println(count + ") " + "Name on the card : " + card.getnameOnCreditCard()
								+ ("\n   Card number : " + card.getcreditCardNumber()) + "\n   Expiry date : "
								+ card.getcreditDateOfExpiry());
						count++;
					}
					addOrDeleteCreditCard();
					customerAction();
				} catch (IBSExceptions e) {
					e.printStackTrace();
				}
				break;
			case BENEFICIARY:
				addOrModifyBeneficiary();
				customerAction();
				break;
			case AUTOPAYMENT:
				System.out.println("\nThe added autopayment services for the customer.");
				int count2 = 1;
				try {
					for (AutoPayment autoPayment : autopaymentservobj.showAutopaymentDetails(uci)) {
						System.out.println(count2 + ") " + "Customer ID : " + uci + "\n   Service Provider ID : "
								+ autoPayment.getServiceProviderId() + "\n   Amount set to be deducted : "
								+ autoPayment.getAmount() + "\n   Date of start : " + autoPayment.getDateOfStart());
						count2++;
					}
					addOrRemoveAutopayments();
					customerAction();
				} catch (IBSExceptions e) {
					e.printStackTrace();
				}
				break;
			case EXIT:
				System.out.println(message4);
				break;
			}
		} else {
			System.out.println(message2);
			customerAction();
		}
	}

	private void addOrDeleteCreditCard() {
		CreditCard card = new CreditCard();
		int creditCardOption;
		do {
			System.out.println("Enter 1 to add a credit card. \nEnter 2 to delete a credit card \nEnter 3 to exit.");
			creditCardOption = choice(scanner);// enter the credit card

			switch (creditCardOption) {
			case 1:
				boolean valid = false;
				String cardNumber;
				String nameOnCard;
				String expiryDate;
				try {
					do {
						System.out.println("Please Enter a valid CreditCard number (16 digits)");
						cardNumber = keyboardInput.readLine();
						valid = cardService.validateCardNumber(cardNumber);
					} while (!valid);
					BigInteger creditCardNumber = new BigInteger(cardNumber);
					card.setcreditCardNumber(creditCardNumber);
					do {
						System.out.println("Please enter a valid Name on your CreditCard (Please ensure no spaces)");
						nameOnCard = keyboardInput.readLine();
						valid = cardService.validateNameOnCard(nameOnCard);
					} while (!valid);
					card.setnameOnCreditCard(nameOnCard);
					do {
						System.out.println(
								"Please enter a valid expiry date on your card number in (DD/MM/YYYY) format.");
						expiryDate = keyboardInput.readLine();
						valid = cardService.validateDateOfExpiry(expiryDate);
					} while (!valid);
					card.setcreditDateOfExpiry(expiryDate);
					cardService.saveCardDetails(uci, card);

					System.out.println("\nDetails entered by you :" + "\nCard name : " + card.getnameOnCreditCard()
							+ "\nCard Number : " + card.getcreditCardNumber() + "\nExpiry date :"
							+ card.getcreditDateOfExpiry());
					System.out.println("\nCard gone for approval.. Good luck!!");
				} catch (IBSExceptions exception) {
					System.out.println(exception.getMessage());
				} catch (IOException exception) {
					System.out.println(exception.getMessage());
				}
				break;
			case 2:
				valid = false;
				try {
					do {
						System.out.println("Please Enter a valid CreditCard number (16 digits)");
						cardNumber = keyboardInput.readLine();
						valid = cardService.validateCardNumber(cardNumber);
					} while (!valid);
					BigInteger creditCardNumber = new BigInteger(cardNumber);
					cardService.deleteCardDetails(uci, creditCardNumber);
					System.out.println("Card deleted!!");
				} catch (IBSExceptions exception) {
					System.out.println(exception.getMessage());
				} catch (IOException exception) {
					System.out.println(exception.getMessage());
				}
				addOrDeleteCreditCard();
				break;
			case 3:
				break;
			default:
				System.out.println("Please enter a valid choice");
				creditCardOption = 0;
			}
		} while (0 == creditCardOption);

	}

	private void addOrModifyBeneficiary() {
		// beneficiary related facilities provided in this sectio
		int count1 = 1;
		int beneficiaryOption;
		try {
			for (Beneficiary beneficiary : beneficiaryAccountService.showBeneficiaryAccount(uci)) {
				System.out.println("\nThe beneficairies already existing for the customer:");
				System.out.println(count1 + ") " + "Name of the beneficiary : " + beneficiary.getAccountName()
						+ "\n   Beneficiary account number : " + beneficiary.getAccountNumber() + "\n   Bank name : "
						+ beneficiary.getBankName() + "\n   IFSC code : " + beneficiary.getIfscCode());
				count1++;
			}
		} catch (IBSExceptions exception) {
			System.out.println(exception.getMessage());
		}
		if (1 == count1) {
			System.out.println("Nothing to show here.. please go on and add a beneficiary to enjoy our services");
		}
		System.out.println(
				"\nEnter 1 to add a beneficiary. \nEnter 2 to modify a beneficiary. \nEnter 3 to delete a beneficiary. \nEnter 4 to exit.");
		beneficiaryOption = choice(scanner);// To choose the facility.
		switch (beneficiaryOption) {
		case 1:
			Type choice = null;
			System.out.println(message1);
			System.out.println("Choose the desired option");
			System.out.println(message1);
			for (Type menu : Type.values()) {
				System.out.println(menu.ordinal() + 1 + "\t" + menu);
			}
			System.out.println(message3);
			int ordinal = choice(scanner) - 1;

			if (0 <= ordinal && CustomerUi.values().length > ordinal) {
				choice = Type.values()[ordinal];
				switch (choice) {
				case SELFINSAME:
					addBeneficiary(choice);
					break;
				case SELFINOTHERS:
					addBeneficiary(choice);
					break;
				case OTHERSINOTHERS:
					addBeneficiary(choice);
					break;
				case OTHERSINSAME:
					addBeneficiary(choice);
					break;
				}
			} else {
				System.out.println(message2);
				addOrModifyBeneficiary();
			}
			break;
		case 2:
			String accountNumberToModify = null;
			if (1 != count1) {
				int count5;
				int choiceToModify;
				do {
					boolean valid11 = false;
					do {
					System.out.println("\nPlease enter a valid account number");
					try {
					accountNumberToModify = keyboardInput.readLine();
					valid11 = beneficiaryAccountService.validateBeneficiaryAccountNumber(accountNumberToModify);
					} catch (IOException exception) {
						System.out.println(exception.getMessage());
					}
					}while(valid11);
					count5 = 0;
					try {
						for (Beneficiary beneficiary : beneficiaryAccountService.showBeneficiaryAccount(uci)) {
							if (beneficiary.getAccountNumber().equals(new BigInteger(accountNumberToModify))) {
								count5++;
							}
						}
					} catch (IBSExceptions exception) {
						System.out.println(exception.getMessage());
					}
				} while (count5 == 0);

				Beneficiary beneficiary = new Beneficiary();
				int test = 0;
				do {
					System.out.println(
							"\nEnter 1 to change the Account Holder Name. \nEnter 2 to change the IFSC code. \nEnter 3 to change the bank name."
									+ "\nEnter 4 to save changes. (Once saved changes, you can't change again till bank verification is done) \nEnter 5 to exit.");
					choiceToModify = choice(scanner);
					switch (choiceToModify) {
					case 1:
						boolean validName;
						String nameInAccount;
						try {
							do {
								System.out.println("Enter a valid account holder name");
								nameInAccount = keyboardInput.readLine();
								validName = beneficiaryAccountService
										.validateBeneficiaryAccountNameOrBankName(nameInAccount);
							} while (!validName);
							beneficiary.setAccountName(nameInAccount);
							test = 0;
						} catch (IOException exception) {
							System.out.println(exception.getMessage());
						}
						break;
					case 2:
						String ifscNewValue;
						boolean validIfsc = false;
						try {
							do {
								System.out.println("Enter a valid IFSC code");
								ifscNewValue = keyboardInput.readLine();
								validIfsc = beneficiaryAccountService.validateBeneficiaryIfscCode(ifscNewValue);
							} while (!validIfsc);
							beneficiary.setIfscCode(ifscNewValue);
							test = 0;
						} catch (IOException exception) {
							System.out.println(exception.getMessage());
						}
						break;
					case 3:
						boolean validbankName;
						String bankNameNewValue;
						try {
							do {
								System.out.println("Enter a new valid bank name");

								bankNameNewValue = keyboardInput.readLine();

								validbankName = beneficiaryAccountService
										.validateBeneficiaryAccountNameOrBankName(bankNameNewValue);
							} while (!validbankName);
							beneficiary.setBankName(bankNameNewValue);
							test = 0;
						} catch (IOException exception) {
							System.out.println(exception.getMessage());
						}
						break;
					case 4:
						try {
							beneficiaryAccountService.modifyBeneficiaryAccountDetails(uci, new BigInteger(accountNumberToModify),
									beneficiary);
							System.out.println("\nModified beneficiary details are gone for approval.");
						} catch (IBSExceptions | SQLException | IOException exception) {
							System.out.println(exception.getMessage());
						}
						break;
					case 5:
						addOrModifyBeneficiary();
						break;

					default:
						System.out.println("Wrong Input");
						test = 0;
						break;
					}
				} while (0 == test);
			} else {
				System.out.println("\nNo beneficiary accounts to modify.");
			}
			break;
		case 3:
			String deleteAccountNum = null;
			boolean valid12 = false;
			if (1 != count1) {
				do {
				System.out.println("Enter a valid account number to be deleted.");
				try {
					deleteAccountNum = keyboardInput.readLine();
				valid12 = beneficiaryAccountService.validateBeneficiaryAccountNumber(deleteAccountNum);
				} catch (IOException exception) {
					System.out.println(exception.getMessage());
				}
				}while(valid12);
				try {
					beneficiaryAccountService.deleteBeneficiaryAccountDetails(uci, new BigInteger(deleteAccountNum));
					System.out.println("Account deleted Successfully");
				} catch (IBSExceptions exception) {
					System.out.println(exception.getMessage());
				}
			} else {
				System.out.println("\nYou need to add beneficiaries first...!");
			}
			break;
		case 4:
			break;
		default:
			System.out.println("Please enter a valid choice");
			addOrModifyBeneficiary();
		}
	}

	private void addBeneficiary(Type type) {
		Beneficiary beneficiary = new Beneficiary();
		boolean valid = false;
		String accountNumber1;
		String nameInAccount;
		String ifsc;
		String bankName;
		try {
			do {
				System.out.println("Please enter a valid Account number(12 digits)");
				accountNumber1 = keyboardInput.readLine();
				valid = beneficiaryAccountService.validateBeneficiaryAccountNumber(accountNumber1);
			} while (!valid);
			BigInteger accountNumber = new BigInteger(accountNumber1);
			beneficiary.setAccountNumber(accountNumber);

			do {
				System.out.println("Please enter a valid Account Holder Name (Case Sensitive)");
				nameInAccount = keyboardInput.readLine();
				valid = beneficiaryAccountService.validateBeneficiaryAccountNameOrBankName(nameInAccount);
			} while (!valid);
			beneficiary.setAccountName(nameInAccount);

			do {
				System.out.println("Please enter a valid IFSC code(11 characters)");
				ifsc = keyboardInput.readLine();
				valid = beneficiaryAccountService.validateBeneficiaryIfscCode(ifsc);
			} while (!valid);
			beneficiary.setIfscCode(ifsc);

			do {
				System.out.println("Enter the bank name (case sensitive)");
				bankName = keyboardInput.readLine();
				valid = beneficiaryAccountService.validateBeneficiaryAccountNameOrBankName(bankName);
			} while (!valid);
			beneficiary.setBankName(bankName);
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}

		beneficiary.setType(type);
		try {
			beneficiaryAccountService.saveBeneficiaryAccountDetails(uci, beneficiary);
			System.out.println("\nThe details entered by you are : " + "\nName of the beneficiary : "
					+ beneficiary.getAccountName() + "\nBeneficiary account number : " + beneficiary.getAccountNumber()
					+ "\nBank name : " + beneficiary.getBankName() + "\nIFSC code : " + beneficiary.getIfscCode());
			System.out.println("Beneficiary gone for approval... Good luck!!");
		} catch (IBSExceptions exception) {
			System.out.println(exception.getMessage());
		}
	}

	private void addOrRemoveAutopayments() {
		AutoPaymentUi choice = null;
		System.out.println(message1);
		System.out.println("Choose a valid option");
		System.out.println(message1);
		for (AutoPaymentUi menu : AutoPaymentUi.values()) {
			System.out.println(menu.ordinal() + 1 + "\t" + menu);
		}
		System.out.println(message3);
		int ordinal = choice(scanner) - 1;

		if (0 <= ordinal && AutoPaymentUi.values().length > ordinal) {
			choice = AutoPaymentUi.values()[ordinal];
			switch (choice) {
			case ADDAUTOPAYMENTS:

				addAutoPayment(); // this method is for adding auto payment
									// details
				addOrRemoveAutopayments();// this method is for showing auto
											// payment menu again
				break;
			case REMOVEAUTOPAYMENTS:
				removeAutoPayment();
				addOrRemoveAutopayments();
				break;
			case EXIT:
				System.out.println(message4);
				break;
			default:
				System.out.println("Enter a valid choice");
				addOrRemoveAutopayments();
			}
		} else {
			System.out.println(message2);
			addOrRemoveAutopayments();
		}
	}

	private void addAutoPayment() {
		BigInteger input = null;// for service provider id as given by use case 5
		AutoPayment autoPayment = new AutoPayment();
		try {
			serviceProviders = autopaymentservobj.showIBSServiceProviders();
		} catch (IBSExceptions exception) {
			System.out.println(exception.getMessage());
		}
		boolean check;
		System.out.println("\nThe IBS service providers are : ");
		for (ServiceProvider serviceProvider : serviceProviders) {
			System.out.println("\nName : " + serviceProvider.getNameOfCompany() + "\nService Provider ID : "
					+ serviceProvider.getSpi());
		}
		int count5 = 0;
		while (count5 == 0) {
			System.out.println("\nEnter a valid service provider id to be registerd.");
			input = scanner.nextBigInteger();

			count5 = 0;
			for (ServiceProvider serviceProvider : serviceProviders) {
				if (serviceProvider.getSpi().equals(input)) {
					count5++;
				}
			}
		}

		autoPayment.setServiceProviderId(input);
		System.out.println("Enter the amount to be deducted");
		BigDecimal amount = scanner.nextBigDecimal();
		autoPayment.setAmount(amount);

		int choice3;
		do {
			choice3 = 0;
			System.out.println("Enter your start date (in format dd/MM/yyyy)");
			String mydate;
			try {
				mydate = keyboardInput.readLine();
				autoPayment.setDateOfStart(mydate);
				check = autopaymentservobj.autoDeduction(uci, autoPayment);
				if (check) {
					System.out.println("AutoPayment of service provider: " + input + " added and Rs. " + amount
							+ " will be deducted per month from the date of start");
				} else {
					System.out.println("Autopayment service could not be added");
				}
			} catch (IBSExceptions exception) {
				choice3 = 1;
				System.out.println(exception.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (choice3 == 1);
	}

	private void removeAutoPayment() {// removal of autopayment

		System.out.println("\nThe added autopayment services for the customer.");
		int count3 = 1;
		try {
			for (AutoPayment autoPayment : autopaymentservobj.showAutopaymentDetails(uci)) {
				System.out.println(count3 + ") " + "Customer ID : " + uci + "\n   Service Provider ID : "
						+ autoPayment.getServiceProviderId() + "\n   Amount set to be deducted : "
						+ autoPayment.getAmount() + "\n   Date of start : " + autoPayment.getDateOfStart());
				count3++;
			}

			BigInteger input3 = null;
			int count6 = 0;
			while (count6 == 0) {
				System.out.println("Enter a valid sevice provider id to be removed");
				input3 = scanner.nextBigInteger();
				for (ServiceProvider serviceProvider : serviceProviders) {
					if (serviceProvider.getSpi().equals(input3)) {
						count6++;
					}
				}
			}
			autopaymentservobj.updateRequirements(uci, input3);
			System.out.println("Autopayment service removed successfully");
		} catch (IBSExceptions exception) {
			System.out.println(exception.getMessage());
		}
	}

	private void bankRepresentativeAction() {// user interface for bank
												// representative
		BankRepresentativeUi choice = null;
		System.out.println(message1);
		System.out.println("Choose a valid option");
		System.out.println(message1);
		for (BankRepresentativeUi menu : BankRepresentativeUi.values()) {
			System.out.println(menu.ordinal() + 1 + "\t" + menu);
		}
		System.out.println(message3);
		int ordinal = choice(scanner) - 1;

		if (0 <= ordinal && BankRepresentativeUi.values().length > ordinal) {
			choice = BankRepresentativeUi.values()[ordinal];
			switch (choice) {
			case VIEWREQUESTS:
				showRequests();
				bankRepresentativeAction();
				break;
			case EXIT:
				System.out.println(message4);
				break;
			}
		} else {
			System.out.println(message2);
			bankRepresentativeAction();
		}
	}

	private void showRequests() {
		Set<String> customerRequests = null;
		String uci = null;

		try {
			customerRequests = bankRepresentativeService.showRequests();
		} catch (IBSExceptions exception) {
			System.out.println(exception.getMessage());
		}

		System.out.println("The following customers have new approval requests");
		for (String customerRequest : customerRequests) {
			int count = 1;
			System.out.println(count + " : " + customerRequest);
			count++;
		}

		System.out.println("Please enter the customer id to view individual requests \nEnter 1 to exit");
		try {
			uci = keyboardInput.readLine();
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}
		if (!uci.equals("1")) {

			if (customerRequests.contains(uci)) {

				int choice7 = 0;
				do {
					System.out.println("Id entered by you is : " + uci);
					int choice1;
					System.out.println(
							"\nEnter 1 to view Creditcard requests. \nEnter 2 to view Beneficiary Requests. \nEnter 3 to exit.");
					choice1 = choice(scanner);
					switch (choice1) {
					case 1:
						Iterator<CreditCard> itCredit = null;
						try {
							itCredit = bankRepresentativeService.showUnapprovedCreditCards(uci).iterator();
						} catch (IBSExceptions exception) {
							exception.getMessage();
						}

						if (!(itCredit.hasNext())) {
							System.out.println("No more credit card requests");
							break;
						}
						int option = 0;
						while (itCredit.hasNext() && 0== option) {
							CreditCard creditCard = itCredit.next();
							System.out.println("Credit card number : " + creditCard.getcreditCardNumber()
									+ "\nCredit card expiry : " + creditCard.getcreditDateOfExpiry() +

									"\nName on the credit card : " + creditCard.getnameOnCreditCard());
							int choice2;
							do {
								System.out.println(
										"\nPress 1 or 2 according to the decision and proceed to the next card."
												+ "\nEnter 1 to approve. \nEnter 2 to disapprove. \nEnter 3 to exit this section.");
								choice2 = choice(scanner);
								switch (choice2) {
								case 1:
									boolean valid7 = true;
									try {
										bankRepresentativeService.saveCreditCardDetails(uci, creditCard);
										if (valid7) {
											System.out.println("Card approved by the bank representative.");
											bankRepresentativeService
													.deleteCreditCard(creditCard.getcreditCardNumber());
										}
									} catch (IBSExceptions exception) {
										exception.getMessage();
									}
									option = 0;
									break;
								case 2:
									try {
										bankRepresentativeService.deleteCreditCard(creditCard.getcreditCardNumber());
										System.out.println("Card disapproved by the bank representative.");
									} catch (IBSExceptions exception) {
										exception.getMessage();
									}
									option = 0;
									break;
								case 3:
									option = 1;
									break;
								default:
									System.out.println("Enter a valid choice of decision of credit card");
									choice2 = 0;
								}
							} while (0 == choice2);
						}
						break;
					case 2:
						Iterator<Beneficiary> itBeneficiary = null;

						// showing beneficiary requests to bank representative
						try {
							itBeneficiary = bankRepresentativeService.showUnapprovedBeneficiaries(uci).iterator();
						} catch (IBSExceptions exception) {
							System.out.println(exception.getMessage());
						}

						if (!(itBeneficiary.hasNext())) {
							System.out.println("No more beneficiary requests");
							break;
						}
						int option1 = 0;
						while (itBeneficiary.hasNext() && 0 == option1) {
							Beneficiary beneficiary = itBeneficiary.next();
							System.out.println("Beneficiary name : " + beneficiary.getAccountName()
									+ "\nBeneficiary Account number : " + beneficiary.getAccountNumber()
									+ "\nBank name : " + beneficiary.getBankName() + "\nIFSC code : "
									+ beneficiary.getIfscCode());
							int choice2;
							do {
								System.out.println(
										"\nPress 1 or 2 according to the decision and proceed to the next beneficiary."
												+ "\nEnter 1 to approve. \nEnter 2 to disapprove. \nEnter 3 to exit this section.");
								choice2 = choice(scanner);
								switch (choice2) {
								case 1:
									try {
										bankRepresentativeService.saveBeneficiaryDetails(uci, beneficiary);
										bankRepresentativeService.deleteBenficiary(beneficiary.getAccountNumber());
										System.out.println("Beneficiary approved by the bank representative.");
									} catch (IBSExceptions exception) {
										System.out.println(exception.getMessage());
									}
									break;
								case 2:

									try {
										bankRepresentativeService.deleteBenficiary(beneficiary.getAccountNumber());
										System.out.println("Beneficiary disapproved  by the bank representative.");
									} catch (IBSExceptions e) {
										System.out.println(e.getMessage());
									}
									break;
								case 3:
									option1 = 1;
									break;

								default:
									System.out.println("Enter a valid choice of decision of beneficiary");
									choice2 = 0;
								}
							} while (0 == choice2);
						}
						break;
					case 3:
						showRequests();
						break;
					default:
						System.out.println("Enter a valid choice of action");
					}
				} while (choice7 == 1);

			} else {
				System.out.println("\nInvalid customer ID");
				showRequests();
			}
		}

	}

	private int choice(Scanner scanner1) {
		while (!scanner1.hasNextInt()) {
			scanner1.next();
			scanner1.nextLine();
			System.out.println("Please enter a valid input");
		}
		int chosenOption = scanner1.nextInt();
		scanner1.nextLine();
		return chosenOption;
	}

	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		MainUi mainUii = new MainUi();
		mainUii.start();
		scanner.close();
		System.out.println();
	}
}
