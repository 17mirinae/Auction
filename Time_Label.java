import javax.swing.*;
import java.util.Date;
import java.awt.HeadlessException;
import java.awt.event.*;
import java.sql.*;
import java.text.*;

public class TimeLabel extends JLabel implements ActionListener {

	DateFormat Display = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public TimeLabel(JFrame Auction, JPanel InputMoney, Statement stmt, JLabel maxMoneyField, String productName,
			String productId) {
		Timer Tick = new Timer(1000, this); // 1초
		InputMoney.add(this);
		Tick.start();

		Tick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rs;
				ResultSet rs1;
				ResultSet rs2;
				int moneyDB;
				String getTimeQuery = "SELECT time FROM clock;";

				try {
					rs = stmt.executeQuery(getTimeQuery);
					if (rs.next()) { // 설정된 시간이 있다면
						Date DBTime = Display.parse(rs.getString("time"));
						Date NowTime = Display.parse(Display.format(new Date()));

						System.out.println(NowTime); // 내가 로그인한 시각이 계속 출력된다.

						if (DBTime.before(NowTime) || DBTime.equals(NowTime)) { // 설정한 시간과 같다면
							String moneyDBQuery = "SELECT amount FROM account;";
							rs2 = stmt.executeQuery(moneyDBQuery);
							if (rs2.next()) {
								moneyDB = rs2.getInt("amount");
								String query = "SELECT memId FROM inputinfo WHERE uploadedMoney = '" + moneyDB + "';";
								rs1 = stmt.executeQuery(query);
								if (rs1.next()) {
									String memIDDB = rs1.getString("memId");
									maxMoneyField.setText("경매종료");
									JOptionPane.showConfirmDialog(null, "경매가 종료되었습니다", "경매 종료",
											JOptionPane.PLAIN_MESSAGE);
									Auction.dispose(); // 프로그램은 종료된다
									Tick.stop(); // 시계는 멈추고
									query = "INSERT INTO resultlist (productId, productName, memId, date, finalMoney) values ('"
											+ productId + "', '" + productName + "', '" + memIDDB + "', '" + DBTime
											+ "', '" + moneyDB + "');";
									// 경매가 끝나고 난 결과 업데이트
									try {
										stmt.executeUpdate(query);
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							}
						} else { // 설정된 시간이 있지만 설정한 시간과 같지 않다면
							String query = "SELECT amount FROM account;";
							try {
								rs = stmt.executeQuery(query);
								if (rs.next()) {
									moneyDB = rs.getInt("amount"); // DB의 금액
									maxMoneyField.setText(String.valueOf(moneyDB) + " 원");
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} else { // 설정된 시간이 없다면
						maxMoneyField.setText("경매종료");
						JOptionPane.showConfirmDialog(null, "경매가 종료되었습니다", "경매 종료", JOptionPane.PLAIN_MESSAGE);
						Auction.dispose();
						Tick.stop();
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	public void actionPerformed(ActionEvent event) {
		setText(Display.format(new Date()));
	}
}
