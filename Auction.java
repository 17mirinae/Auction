import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Auction {
	String productId = null;
	String productName = null;
	String productWay = null;
	int moneyDB;

	public Auction(Statement stmt, String memidIDTF) {
		JFrame Auction = new JFrame("경매 프로그램"); // 경매 frame 생성
		Auction.setLocation(600, 400); // 위치 지정
		Auction.setPreferredSize(new Dimension(600, 450));

		Container AuctionPane = Auction.getContentPane(); // container 생성
		AuctionPane.setLayout(null); // container의 레이아웃 설정

		JPanel AuctionGoods = new JPanel(); // 경매 물품 패널
		AuctionGoods.setSize(400, 400);
		AuctionGoods.setLocation(0, 0);
		AuctionGoods.setBackground(new Color(0, 0, 0));

		JPanel InputMoney = new JPanel(); // 돈 입력 패널
		InputMoney.setLocation(400, 0);
		InputMoney.setSize(200, 400);
		InputMoney.setBackground(new Color(255, 255, 255));
		InputMoney.setLayout(null);

		/*
		 * 돈을 입력하는 패널
		 */
		JLabel userID = new JLabel(memidIDTF + " 님");
		userID.setLocation(10, 20);
		userID.setSize(100, 20);

		JLabel MyMoney = new JLabel("내 입금액: "); // 내 입금액 Label
		MyMoney.setLocation(10, 60);
		MyMoney.setSize(80, 20);

		JLabel SuccessMoney = new JLabel(); // 내 입금액 Success
		SuccessMoney.setLocation(90, 60);
		SuccessMoney.setSize(100, 20);

		JLabel MaxMoney = new JLabel("현재 입찰액: ");
		MaxMoney.setLocation(10, 90);
		MaxMoney.setSize(80, 20);

		JLabel MaxMoneyField = new JLabel();
		MaxMoneyField.setLocation(90, 90);
		MaxMoneyField.setSize(100, 20);

		JLabel NowProduct = new JLabel("현재 경매물품: ");
		NowProduct.setLocation(20, 140);
		NowProduct.setSize(90, 20);

		JLabel Product = new JLabel();
		Product.setLocation(20, 170);
		Product.setSize(155, 20);

		JLabel Money = new JLabel("돈 입력: "); // Label 생성
		Money.setLocation(20, 250);
		Money.setSize(60, 20);

		JTextField MoneyField = new JTextField(); // 돈 입력하는 Field 생성
		MoneyField.setText("0");
		MoneyField.setLocation(80, 250);
		MoneyField.setSize(85, 20);

		JButton Upload = new JButton("입금");
		Upload.setLocation(20, 300);
		Upload.setSize(145, 40);

		InputMoney.add(userID);
		InputMoney.add(MyMoney);
		InputMoney.add(SuccessMoney);
		InputMoney.add(MaxMoney);
		InputMoney.add(MaxMoneyField);
		InputMoney.add(NowProduct);
		InputMoney.add(Product);
		InputMoney.add(Money); // 돈 입력 패널에 Label 추가
		InputMoney.add(MoneyField); // 돈 입력 패널에 돈 입력 Field 생성
		InputMoney.add(Upload);

		String productQuery = "SELECT productNo, productName, productWay FROM productlist WHERE productNo = (SELECT min(productNo) FROM productlist);";
		System.out.println(productQuery);

		try {
			ResultSet rs = stmt.executeQuery(productQuery);

			if (rs.next()) {
				productId = rs.getString("productNo");
				productName = rs.getString("productName");
				productWay = rs.getString("productWay");

				Product.setText(productName);
			} else {
				JOptionPane.showMessageDialog(null, "현재 진행되고 있는 경매 물품이 없습니다", "경매 물품 없음", JOptionPane.ERROR_MESSAGE);
				return;
			}

		} catch (SQLException ex1) {
			ex1.printStackTrace();
		}

		// 현재 입찰 최고금액 출력
		String query = "SELECT amount FROM account;";
		System.out.println(query);
		ResultSet rs;
		try {
			rs = stmt.executeQuery(query);

			if (rs.next()) {
				moneyDB = rs.getInt("amount"); // DB의 금액
				MaxMoneyField.setText(String.valueOf(moneyDB) + " 원");
			} else {
				query = "INSERT INTO account (amount) values ('100');";
				stmt.executeUpdate(query);
				rs.next();

				moneyDB = rs.getInt("amount");
				MaxMoneyField.setText(String.valueOf(moneyDB) + " 원");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		TimeLabel Time = new TimeLabel(Auction, InputMoney, stmt, MaxMoneyField, productName, productId);
		Time.setLocation(20, 210);
		Time.setSize(145, 40);

		// 입금 버튼 눌렀을 시 Action
		Upload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UploadAction(stmt, MoneyField, productId, memidIDTF, SuccessMoney, MaxMoneyField);
			}
		});

		/*
		 * 경매 물품 패널
		 */
		JLabel image = new JLabel(new ImageIcon(productWay));
		AuctionGoods.add(image);

		AuctionPane.add(AuctionGoods); // container에 경매 물품 패널 추가
		AuctionPane.add(InputMoney); // container에 돈 입력 패널 추가

		Auction.pack(); // 사이즈에 맞게 조정
		Auction.setVisible(true); // 눈에 보이기
	}
}
