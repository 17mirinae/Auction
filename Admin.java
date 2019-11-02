import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Admin {
	public Admin(Statement stmt) {
		JFrame AdminFrame = new JFrame("관리자");
		AdminFrame.setLocation(600, 400);
		Container AdminPane = AdminFrame.getContentPane();

		JPanel AdminPanel = new JPanel();
		AdminPane.add(AdminPanel);
		AdminPanel.setBackground(new Color(255, 255, 255));
		AdminPanel.setLayout(null);

		AdminFrame.setPreferredSize(new Dimension(415, 600));

		JLabel ProductName = new JLabel("추가할 제품 이름");
		ProductName.setLocation(50, 150);
		ProductName.setSize(130, 20);

		JTextField ProductNameTF = new JTextField("제품 이름");
		ProductNameTF.setLocation(180, 150);
		ProductNameTF.setSize(170, 20);

		JLabel ProductWay = new JLabel("사진 이름과 확장명");
		ProductWay.setLocation(50, 180);
		ProductWay.setSize(130, 20);

		JTextField ProductWayTF = new JTextField("사진 이름과 확장명");
		ProductWayTF.setLocation(180, 180);
		ProductWayTF.setSize(170, 20);

		JButton Accept = new JButton("추가");
		Accept.setLocation(50, 230);
		Accept.setSize(140, 40);

		JButton LogOut = new JButton("로그아웃");
		LogOut.setLocation(210, 230);
		LogOut.setSize(140, 40);

		JLabel Time = new JLabel("경매 종료 시간: ");
		Time.setLocation(50, 350);
		Time.setSize(150, 20);

		JTextField SetTime = new JTextField("ex) 1998-11-06 00:00:00");
		SetTime.setLocation(200, 350);
		SetTime.setSize(150, 20);

		JButton UploadTime = new JButton("시간 설정");
		UploadTime.setLocation(200, 390);
		UploadTime.setSize(150, 40);

		Accept.addActionListener(new ActionListener() {
			@Override // 제품 추가 버튼
			public void actionPerformed(ActionEvent e) {
				String inputQuery = "INSERT INTO productlist (productName, productWay) values ('"
						+ ProductNameTF.getText() + "', '" + ProductWayTF.getText() + "');";
				System.out.println(inputQuery);
				try {
					stmt.executeUpdate(inputQuery);
					JOptionPane.showConfirmDialog(null, "추가되었습니다", "성공", JOptionPane.PLAIN_MESSAGE);
					ProductNameTF.setText("");
					ProductWayTF.setText("");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "추가 되지 않았습니다", "오류", JOptionPane.ERROR_MESSAGE);
					ProductNameTF.setText("");
					ProductWayTF.setText("");
					e1.printStackTrace();
				}
			}
		});

		LogOut.addActionListener(new ActionListener() {
			@Override // 로그아웃 버튼
			public void actionPerformed(ActionEvent e) {
				AdminFrame.dispose();
				new AdminLogin(stmt);
			}
		});

		UploadTime.addActionListener(new ActionListener() {
			@Override // 시간 설정 버튼
			public void actionPerformed(ActionEvent e) {
				String query = "SELECT time FROM clock;";
				String timeDB = null;
				// time from database
				String timeInput = SetTime.getText();
				// time from textfield
				String inputTimeQuery = null;

				try {
					ResultSet rs = stmt.executeQuery(query);
					if (rs.next()) {
						timeDB = rs.getString("time");
						String outputTimeQuery = "DELETE FROM clock WHERE time = '" + timeDB + "';";

						stmt.executeUpdate(outputTimeQuery);

						if (timeInput.equals("")) {
							inputTimeQuery = "INSERT INTO clock (time) values ('1998-11-06 00:00:00');";
							JOptionPane.showConfirmDialog(null, "1998년 11월 6일 0시로 설정되었습니다", "자정 설정",
									JOptionPane.PLAIN_MESSAGE);
							SetTime.setText("");
						} else {
							inputTimeQuery = "INSERT INTO clock (time) values ('" + timeInput + "');";
						}

						stmt.executeUpdate(inputTimeQuery);

						JOptionPane.showConfirmDialog(null, "설정 성공", "설정 성공", JOptionPane.PLAIN_MESSAGE);
						SetTime.setText("");
					} else {
						inputTimeQuery = "INSERT INTO clock (time) values ('1998-11-06 00:00:00');";
						stmt.executeUpdate(inputTimeQuery);
						JOptionPane.showConfirmDialog(null, "1998년 11월 6일 0시로 설정되었습니다", "자정 설정",
								JOptionPane.PLAIN_MESSAGE);
						SetTime.setText("");
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		});

		AdminPanel.add(ProductName);
		AdminPanel.add(ProductNameTF);
		AdminPanel.add(ProductWay);
		AdminPanel.add(ProductWayTF);
		AdminPanel.add(Accept);
		AdminPanel.add(LogOut);
		AdminPanel.add(Time);
		AdminPanel.add(SetTime);
		AdminPanel.add(UploadTime);

		AdminFrame.pack();
		AdminFrame.setVisible(true);
	}
}
