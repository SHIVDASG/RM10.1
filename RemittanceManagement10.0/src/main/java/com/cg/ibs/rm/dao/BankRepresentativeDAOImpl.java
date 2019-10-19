package com.cg.ibs.rm.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.ui.Type;
import com.cg.ibs.rm.util.ConnectionProvider;
import com.cg.ibs.rm.util.QueryMapper;

public class BankRepresentativeDAOImpl implements BankRepresentativeDAO {

	public Set<String> getRequests() throws IBSExceptions {
		Set<String> ucis = new HashSet<>();
		Connection connection = ConnectionProvider.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(QueryMapper.UCI_LIST_FOR_ADMIN1);
				PreparedStatement statement2 = connection.prepareStatement(QueryMapper.UCI_LIST_FOR_ADMIN2);
				ResultSet resultSet = statement.executeQuery();
				ResultSet resultSet2 = statement2.executeQuery();) {
			while (resultSet.next()) {
				ucis.add(resultSet.getString("uci"));
			}
			while (resultSet2.next()) {
				ucis.add(resultSet2.getString("uci"));
			}
		} catch (SQLException e) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return ucis;
	}

	public Set<Beneficiary> getBeneficiaryDetails(String uci) throws IBSExceptions {
		Set<Beneficiary> unapprovedBeneficiaries = new HashSet<>();
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement statement = con.prepareStatement(QueryMapper.UNAPP_BENEFECIARIS);) {
			statement.setBigDecimal(1, new BigDecimal(uci));
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					Beneficiary beneficiary = new Beneficiary();
					beneficiary.setAccountName(resultSet.getString("Account_Name"));
					beneficiary.setAccountNumber(new BigInteger(resultSet.getString("Account_Number")));
					beneficiary.setBankName(resultSet.getString("bank_name"));
					beneficiary.setIfscCode(resultSet.getString("Ifsc_code"));
					beneficiary.setType(Type.valueOf(resultSet.getString("Type")));
					unapprovedBeneficiaries.add(beneficiary);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return unapprovedBeneficiaries;
	}

	public Set<CreditCard> getCreditCardDetails(String uci) throws IBSExceptions {
		Set<CreditCard> unapprovedCards = new HashSet<>();
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(QueryMapper.UNAPP_CARDS);) {
			statement.setBigDecimal(1, new BigDecimal(uci));
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					CreditCard card = new CreditCard();
					card.setcreditCardNumber(new BigInteger(resultSet.getBigDecimal("credit_card_num").toString()));
					card.setcreditDateOfExpiry(resultSet.getDate("credit_expiry_date").toString());
					card.setnameOnCreditCard(resultSet.getString("name_on_cred_card"));
					unapprovedCards.add(card);
				}

			}
		} catch (SQLException e) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return unapprovedCards;

	}

	@Override
	public boolean copyCreditCardDetails(String uci, CreditCard card) throws IBSExceptions {
		boolean check = false;
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.SAVING_CARDS_INTO_FINAL);) {
			statement.setBigDecimal(1, new BigDecimal(card.getcreditCardNumber()));
			statement.setString(2, "INVALID");
			statement.setString(3, card.getnameOnCreditCard());
			statement.setInt(4, -1);
			statement.setInt(5, -1);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(card.getcreditDateOfExpiry(), dateTimeFormatter);
			statement.setDate(6, Date.valueOf(date));
			statement.setBigDecimal(7, new BigDecimal(uci));
			statement.setString(8, "INVALID");
			statement.setInt(9, -1);
			statement.setInt(10, -1);
			statement.setInt(11, -1);

			if (statement.executeUpdate() > 0) {
				check = true;
			}
		} catch (SQLException e) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return check;
	}

	@Override
	public void copyBeneficiaryDetails(String uci, Beneficiary beneficiary) throws IBSExceptions {
		boolean result = false;
		Connection con = ConnectionProvider.getConnection();
		try (PreparedStatement statement = con.prepareStatement(QueryMapper.SAVING_BENEFICIARIES_INTO_FINAL);) {
			statement.setBigDecimal(1, new BigDecimal(beneficiary.getAccountNumber()));
			statement.setString(2, beneficiary.getAccountName());
			statement.setString(3, beneficiary.getIfscCode());
			statement.setString(4, beneficiary.getBankName());
			statement.setString(5, beneficiary.getType().toString());
			statement.setBigDecimal(6, new BigDecimal(uci));
			if (statement.executeUpdate() > 0) {
				result = true;
			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
	}

	@Override
	public Set<String> getUCIList() throws IBSExceptions {
		Connection con = ConnectionProvider.getConnection();
		Set<String> uciList = new HashSet<>();
		try (PreparedStatement statement = con.prepareStatement("SELECT UCI FROM CUSTOMERS");
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				uciList.add(set.getString("uci"));
			}
		} catch (SQLException exp) {
			exp.printStackTrace();
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}

		return uciList;
	}

	public boolean deleteBeneficiaryDetails(BigInteger accountNumber) throws IBSExceptions {
		boolean result = false;
		Connection connection = ConnectionProvider.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_TEMP_BENEFICIARY);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(accountNumber));
			try (ResultSet getCard = preparedStatement.executeQuery();) {
				if (getCard.next()) {
					try (PreparedStatement preparedStatement2 = connection
							.prepareStatement(QueryMapper.DELETE_TEMP_BENEFICIARY);) {
						preparedStatement2.setBigDecimal(1, new BigDecimal(accountNumber));
						int check = preparedStatement2.executeUpdate();
						if (check > 0) {
							result = true;
						}
					}
				}
			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return result;
	}

	public boolean deleteCreditCardDetails(BigInteger cardNumber) throws IBSExceptions {
		boolean result = false;
		Connection connection = ConnectionProvider.getConnection();
		try (PreparedStatement preparedStatement2 = connection
				.prepareStatement(QueryMapper.DELETE_TEMP_CARD_DETAILS);) {
			preparedStatement2.setBigDecimal(1, new BigDecimal(cardNumber));
			int check = preparedStatement2.executeUpdate();
			if (check > 0) {
				result = true;
			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return result;
	}
}
