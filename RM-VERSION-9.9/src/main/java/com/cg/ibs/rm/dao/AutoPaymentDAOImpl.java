package com.cg.ibs.rm.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.cg.ibs.rm.bean.AutoPayment;
import com.cg.ibs.rm.bean.ServiceProvider;
import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.util.ConnectionProvider;
import com.cg.ibs.rm.util.QueryMapper;

public class AutoPaymentDAOImpl implements AutoPaymentDAO {

	@Override
	public Set<AutoPayment> getAutopaymentDetails(String uci) throws IBSExceptions {
		Set<AutoPayment> autoPayments = new HashSet<>();
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.GET_AUTOPAYMENTS);) {
			statement.setBigDecimal(1, new BigDecimal(uci));
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					AutoPayment autoPayment = new AutoPayment();
					autoPayment.setAmount(new BigDecimal(resultSet.getString("Amount")));
					autoPayment.setDateOfStart(resultSet.getString("DateOfStart"));
					autoPayment.setServiceProviderId(new BigInteger(resultSet.getString("SPI")));
					autoPayments.add(autoPayment);
				}
			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return autoPayments;

	}

	@Override
	public void copyDetails(String uci, AutoPayment autoPayment) throws IBSExceptions {
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.ADD_AUTOPAYMENTS);) {
			statement.setBigDecimal(1, (autoPayment.getAmount()));
			statement.setString(2, autoPayment.getDateOfStart());
			statement.setBigDecimal(3, new BigDecimal(autoPayment.getServiceProviderId()));
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
	}

	@Override
	public boolean deleteDetails(String uci, BigInteger serviceProviderId) throws IBSExceptions {
		boolean result = true;
		Connection con = ConnectionProvider.getConnection();
		try(PreparedStatement preparedStatement = con.prepareStatement(QueryMapper.DELETE_AUTOPAYMENTS);){
			preparedStatement.setBigDecimal(1, new BigDecimal(uci));
			preparedStatement.setBigDecimal(2, new BigDecimal(serviceProviderId));
		} catch (SQLException exception) {
			result = false;
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return result;
	}

	@Override
	public Set<ServiceProvider> showServiceProviderList() throws IBSExceptions {
		Set<ServiceProvider> providers = new HashSet<>();
		Connection con = ConnectionProvider.getConnection();
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement(QueryMapper.SERVICE_LIST);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ServiceProvider provider = new ServiceProvider(new BigInteger(resultSet.getString("SPI")),
						resultSet.getString("Company_Name"));
				providers.add(provider);
			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return providers;
	}

	@Override
	public BigDecimal getCurrentBalance(String uci) throws IBSExceptions {
		Connection con = ConnectionProvider.getConnection();
		BigDecimal bigDecimal = null;
		try {
			PreparedStatement preparedStatement = con.prepareStatement(QueryMapper.GET_BALANCE);
			preparedStatement.setBigDecimal(1, new BigDecimal(uci));
			ResultSet resultSet = preparedStatement.executeQuery();
			bigDecimal = new BigDecimal(resultSet.getString("Current_Balance"));
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return bigDecimal;
	}

	@Override
	public void setCurrentBalance(String uci, BigDecimal currentBalnce) throws IBSExceptions {
		Connection con = ConnectionProvider.getConnection();
		try {
			PreparedStatement preparedStatement = con.prepareStatement(QueryMapper.SET_BALANCE);
			preparedStatement.setBigDecimal(1, currentBalnce);
			preparedStatement.setBigDecimal(2, new BigDecimal(uci));
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
	}

}
