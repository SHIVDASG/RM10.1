package com.cg.ibs.rm.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.ui.Type;
import com.cg.ibs.rm.util.ConnectionProvider;
import com.cg.ibs.rm.util.QueryMapper;

public class BeneficiaryDAOImpl implements BeneficiaryDAO {
	@Override
	public Set<Beneficiary> getDetails(String uci) throws IBSExceptions {// returns set of beneficiaries for a given uci
		Set<Beneficiary> beneficiaries = new HashSet<>();
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement statement = con.prepareStatement(QueryMapper.GET_ADDED_BENEFICIARIES);) {
			statement.setBigDecimal(1, new BigDecimal(uci));
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					Beneficiary beneficiary = new Beneficiary();
					beneficiary.setAccountName(resultSet.getString("Account_Name"));
					beneficiary.setAccountNumber(new BigInteger(resultSet.getString("Account_Number")));
					beneficiary.setBankName(resultSet.getString("bank_name"));
					beneficiary.setIfscCode(resultSet.getString("Ifsc_code"));
					beneficiary.setType(Type.valueOf(resultSet.getString("Type")));
					beneficiaries.add(beneficiary);
				}
			}
		} catch (SQLException e) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return beneficiaries;
	}

	@Override
	public void copyDetails(String uci, Beneficiary beneficiary) throws IBSExceptions {// copying values into the final
																						// beneficiary database
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement2 = con.prepareStatement(QueryMapper.CHECK_BENEFICIARY);) {
			statement2.setBigDecimal(1, new BigDecimal(beneficiary.getAccountNumber()));
			try (PreparedStatement statement = con.prepareStatement(QueryMapper.ADD_BENEFICIARY);
					ResultSet rs = statement2.executeQuery()) {
				if (rs.next()) {
					throw new IBSExceptions(ExceptionMessages.ERROR3);
				} else {
					statement.setBigDecimal(1, new BigDecimal(beneficiary.getAccountNumber()));
					statement.setString(2, beneficiary.getAccountName());
					statement.setString(3, beneficiary.getIfscCode());
					statement.setString(4, beneficiary.getBankName());
					statement.setString(5, beneficiary.getType().toString());
					statement.setBigDecimal(6, new BigDecimal(uci));
					statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
					statement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
	}

	@Override
	public Beneficiary getBeneficiary(String uci, BigInteger accountNumber) throws IBSExceptions {// returns beneficiary
																									// object
		Beneficiary beneficiary = null;
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.GET_BENEFICIARY)) {
			statement.setBigDecimal(1, new BigDecimal(accountNumber));
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					beneficiary = new Beneficiary();
					beneficiary.setAccountName(resultSet.getString("Account_Name"));
					beneficiary.setAccountNumber(new BigInteger(resultSet.getString("Account_Number")));
					beneficiary.setBankName(resultSet.getString("bank_name"));
					beneficiary.setIfscCode(resultSet.getString("Ifsc_code"));
					beneficiary.setType(Type.valueOf(resultSet.getString("Type")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return beneficiary;
	}

	@Override
	public boolean updateDetails(String uci, Beneficiary beneficiary1) throws IBSExceptions {
		boolean check = false;
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.GET_BENEFICIARY);) {
			statement.setBigDecimal(1, new BigDecimal(beneficiary1.getAccountNumber()));
			try (PreparedStatement statement1 = con.prepareStatement(QueryMapper.UPDATE_BENEFICIARY);
					ResultSet resultSet1 = statement.executeQuery();) {
				if (!resultSet1.next()) {
					check = false;
					throw new IBSExceptions(ExceptionMessages.ERROR3);

				} else {
					statement1.setBigDecimal(1, new BigDecimal(beneficiary1.getAccountNumber()));
					statement1.setString(2, beneficiary1.getAccountName());
					statement1.setString(3, beneficiary1.getIfscCode());
					statement1.setString(4, beneficiary1.getBankName());
					statement1.setString(5, beneficiary1.getType().toString());
					statement1.setBigDecimal(6, new BigDecimal(uci));
					statement1.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
					statement1.setBigDecimal(8, new BigDecimal(beneficiary1.getAccountNumber()));
					if (statement1.executeUpdate() > 0) {
						check = true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return check;

	}

	@Override
	public boolean deleteDetails(String uci, BigInteger accountNumber) throws IBSExceptions {
		boolean check = true;
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.DELETE_BENEFICIARY);) {
			statement.setBigDecimal(1, new BigDecimal(accountNumber));
			if (statement.executeUpdate() > 0) {
				check = false;
			}

		} catch (SQLException e) {

			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return check;

	}

}
