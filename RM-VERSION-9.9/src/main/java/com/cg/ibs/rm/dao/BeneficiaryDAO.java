package com.cg.ibs.rm.dao;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.exception.IBSExceptions;

public interface BeneficiaryDAO {

	public Set<Beneficiary> getDetails(String uci) throws IBSExceptions;

	public void copyDetails(String uci, Beneficiary beneficiary)throws IBSExceptions, SQLException, IOException;

	public boolean updateDetails(String uci, Beneficiary beneficiary) throws IBSExceptions, SQLException, IOException;
	
	public boolean deleteDetails(String uci, BigInteger accountNumber) throws IBSExceptions;
	
	public Beneficiary getBeneficiary(String uci, BigInteger accountNumber) throws IBSExceptions, SQLException, IOException;

}
