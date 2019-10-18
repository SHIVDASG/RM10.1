package com.cg.ibs.rm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionProvider {

	private static Connection connection;

	public static Connection getConnection() {

		if (null == connection) {
			ResourceBundle bundle = ResourceBundle.getBundle("ibs");
			String url = bundle.getString("url");
			String user = bundle.getString("user");
			String password = bundle.getString("password");

			try {
				connection = DriverManager.getConnection(url, user, password);
			} catch (SQLException exception) {
				System.out.println(exception.getMessage());
			}
		}
		return connection;
	}
}
