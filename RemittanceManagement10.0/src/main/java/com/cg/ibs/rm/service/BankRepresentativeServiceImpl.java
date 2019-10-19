package com.cg.ibs.rm.service;

import java.math.BigInteger;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.dao.BankRepresentativeDAO;
import com.cg.ibs.rm.dao.BankRepresentativeDAOImpl;
import com.cg.ibs.rm.exception.IBSExceptions;

public class BankRepresentativeServiceImpl implements BankRepresentativeService {
	private BankRepresentativeDAO bankRepresentativeDAO = new BankRepresentativeDAOImpl();

	@Override
	public Set<String> showRequests() throws IBSExceptions {
		return bankRepresentativeDAO.getRequests();
		
	}

	@Override
	public Set<CreditCard> showUnapprovedCreditCards(String uci) throws IBSExceptions {
		return bankRepresentativeDAO.getCreditCardDetails(uci);

	}

	@Override
	public Set<Beneficiary> showUnapprovedBeneficiaries(String uci) throws IBSExceptions {
		return bankRepresentativeDAO.getBeneficiaryDetails(uci);
	}

	@Override
	public boolean saveCreditCardDetails(String uci, CreditCard card) throws IBSExceptions {
		return bankRepresentativeDAO.copyCreditCardDetails(uci, card);
	}

	@Override
	public void saveBeneficiaryDetails(String uci, Beneficiary beneficiary) throws IBSExceptions {
		bankRepresentativeDAO.copyBeneficiaryDetails(uci, beneficiary);
	}

	@Override
	public boolean checkUciList(String uci) throws IBSExceptions {
		boolean checkUci=false;
		if(bankRepresentativeDAO.getUCIList().contains(uci)) {
			checkUci=true;
		}
		return checkUci;
	}
	public boolean deleteBenficiary(BigInteger accountNumber) throws IBSExceptions {
		boolean checkDelete=false;
		if(bankRepresentativeDAO.deleteBeneficiaryDetails(accountNumber))
			checkDelete=true;
		return checkDelete;
	}

	@Override
	public boolean deleteCreditCard(BigInteger creditCardNumber) throws IBSExceptions {
		boolean checkDelete=false;
		if(bankRepresentativeDAO.deleteCreditCardDetails(creditCardNumber))
			checkDelete=true;
		return checkDelete;
	}
}
