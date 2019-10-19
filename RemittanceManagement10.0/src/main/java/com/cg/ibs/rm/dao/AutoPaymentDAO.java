package com.cg.ibs.rm.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import com.cg.ibs.rm.bean.AutoPayment;
import com.cg.ibs.rm.bean.ServiceProvider;
import com.cg.ibs.rm.exception.IBSExceptions;

public interface AutoPaymentDAO {
	public Set<ServiceProvider> showServiceProviderList() throws IBSExceptions;
	
	public Set<AutoPayment> getAutopaymentDetails(String uci) throws IBSExceptions;

	public boolean copyDetails(String uci, AutoPayment autoPayment) throws IBSExceptions;

	public boolean deleteDetails(String uci, BigInteger serviceProviderId) throws IBSExceptions;

	public BigDecimal getCurrentBalance(String uci) throws IBSExceptions;

	public void setCurrentBalance(String uci, BigDecimal currentBalnce) throws IBSExceptions;

}
