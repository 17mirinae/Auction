import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class UploadAction {
	public UploadAction(Statement stmt, JTextField MoneyField, String productId, String memidIDTF, JLabel SuccessMoney,
			JLabel MaxMoneyField) {
		String query = "SELECT amount FROM account;";
		System.out.println(query);
		try {
			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				int moneyDB = rs.getInt("amount"); // DB의 금액
				int moneyAmount = Integer.parseInt(MoneyField.getText()); // 입금된 금액

				if (moneyAmount > moneyDB) {
					String inputQuery = "INSERT INTO inputinfo (productId, memId, uploadedMoney) values ('" + productId
							+ "', '" + memidIDTF + "', '" + moneyAmount + "');";
					stmt.executeUpdate(inputQuery);
					String outputMoneyQuery = "DELETE FROM account WHERE amount = " + moneyDB + ";";
					String inputMoneyQuery = "INSERT INTO account (amount) values ('" + moneyAmount + "');";
					stmt.executeUpdate(outputMoneyQuery);
					stmt.executeUpdate(inputMoneyQuery);
					JOptionPane.showConfirmDialog(null, "입금 성공", "입금 성공", JOptionPane.PLAIN_MESSAGE);

					SuccessMoney.setText(String.valueOf(moneyAmount) + " 원");
					MaxMoneyField.setText(String.valueOf(moneyAmount) + " 원");
					MoneyField.setText("0");
				} else {
					JOptionPane.showMessageDialog(null, "현 입찰액과 적거나 같습니다", "입금 실패", JOptionPane.ERROR_MESSAGE);
					SuccessMoney.setText("        원");
					MaxMoneyField.setText(String.valueOf(moneyDB) + " 원");
					MoneyField.setText("0");
				}
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
}
