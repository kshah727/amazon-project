package com.sqa.ks;

import java.sql.*;
import java.util.*;

import org.testng.*;
import org.testng.annotations.*;

import com.sqa.ks.helpers.*;

public class DataHelperTest {

	public static Object[][] evalDatabaseTable(String driverClassString,
			String databaseStringUrl, String username, String password, String tableName)
			throws ClassNotFoundException, SQLException {
		return evalDatabaseTable(driverClassString, databaseStringUrl, username, password,
				tableName, 0, 0);
	}

	public static Object[][] evalDatabaseTable(String driverClassString,
			String databaseStringUrl, String username, String password, String tableName,
			int rowOffset, int colOffset) throws ClassNotFoundException, SQLException {
		Object[][] myData;
		ArrayList<Object> myArrayData = new ArrayList<Object>();
		Class.forName(driverClassString);
		Connection dbconn = DriverManager.getConnection(databaseStringUrl, username, password);
		Statement stmt = dbconn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from " + tableName);
		int numOfColumns = rs.getMetaData().getColumnCount();
		int curRow = 1;
		while (rs.next()) {
			if (curRow > rowOffset) {
				Object[] rowData = new Object[numOfColumns - colOffset];
				for (int i = 0, j = colOffset; i < rowData.length; i++) {
					rowData[i] = rs.getString(i + colOffset + 1);
				}
				myArrayData.add(rowData);
			}
			curRow++;
		}
		myData = new Object[myArrayData.size()][];
		for (int i = 0; i < myData.length; i++) {
			myData[i] = (Object[]) myArrayData.get(i);
		}
		rs.close();
		stmt.close();
		dbconn.close();
		return myData;
	}

	// @DataProvider
	public Object[][] dp() {
		return new Object[][] { new Object[] { 3, 5, 4, 1 },
				new Object[] { 3.1, 5.55, 4.1, 1.3 }, new Object[] { "3", "5", "4", "1" },
				new Object[] { '3', '5', '4', '1' } };
	}

	@Test
	public void testNonStaticDisplayData() {
		String expected =
				"3	5	4	1	\n3.1	5.55	4.1	1.3	\n3	5	4	1	\n3	5	4	1	\n";
		DataHelper dh = new DataHelper(dp());
		String actual = dh.displayData();
		// System.out.println(actual);
		Assert.assertEquals(actual, expected, "does not work correctly");
	}

	@Test// (dataProvider = "dp")
	public void testStaticDisplayData() {
		Object[][] data = dp();
		String expected =
				"3	5	4	1	\n3.1	5.55	4.1	1.3	\n3	5	4	1	\n3	5	4	1	\n";
		String actual = DataHelper.displayData(data);
		Assert.assertEquals(actual, expected, "does not work correctly");
	}
}
