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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AdminLogin {
	public AdminLogin(Statement stmt) {
		JFrame adminFrame = new JFrame("관리자 로그인"); // 관리자 로그인 frame 생성
		adminFrame.setLocation(600, 400); // frame의 위치 조정
		Container adminPane = adminFrame.getContentPane();

		JPanel adminPanel = new JPanel();
		adminPane.add(adminPanel);
		adminPanel.setBackground(new Color(255, 255, 255));
		adminPanel.setLayout(null);

		adminFrame.setPreferredSize(new Dimension(415, 600));

		JLabel id = new JLabel("아이디: ");
		id.setLocation(50, 200);
		id.setSize(100, 20);

		JTextField idtf = new JTextField(); // id's textfield
		idtf.setLocation(150, 200);
		idtf.setSize(200, 20);

		JLabel pw = new JLabel("비밀번호: ");
		pw.setLocation(50, 250);
		pw.setSize(100, 20);

		JPasswordField pwtf = new JPasswordField(); // pwd's textfield
		pwtf.setLocation(150, 250);
		pwtf.setSize(200, 20);

		JButton Accept = new JButton("로그인");
		Accept.setLocation(50, 400);
		Accept.setSize(140, 40);

		JButton Back = new JButton("뒤로");
		Back.setLocation(210, 400);
		Back.setSize(140, 40);

		Accept.addActionListener(new ActionListener() {
			@Override // 관리자 로그인
			public void actionPerformed(ActionEvent e) {
				String adminId = idtf.getText();
				String query = "SELECT adminPwd FROM admininfo WHERE adminId = '" + adminId + "';";
				System.out.println(query);
				try {
					ResultSet rspw = stmt.executeQuery(query);

					if (rspw.next()) {
						String adminPwdDB = rspw.getString("adminPwd");
						String adminPwdpwtf = new String(pwtf.getPassword());

						if (adminPwdDB.equals(adminPwdpwtf)) {
							adminFrame.dispose();
							new Admin(stmt);
						} else {
							JOptionPane.showMessageDialog(null, "존재하지 않는 아이디거나 비밀번호가 틀렸습니다", "오류",
									JOptionPane.ERROR_MESSAGE);
							pwtf.setText("");
							idtf.setText("");
						}
					} else {
						JOptionPane.showMessageDialog(null, "존재하지 않는 아이디거나 비밀번호가 틀렸습니다", "오류",
								JOptionPane.ERROR_MESSAGE);
						pwtf.setText("");
						idtf.setText("");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		Back.addActionListener(new ActionListener() {
			@Override // 뒤로
			public void actionPerformed(ActionEvent arg0) {
				adminFrame.dispose();
				new GUI();
			}
		});

		adminPanel.add(id);
		adminPanel.add(idtf);
		adminPanel.add(pw);
		adminPanel.add(pwtf);
		adminPanel.add(Accept);
		adminPanel.add(Back);

		adminFrame.pack();
		adminFrame.setVisible(true);
	}
}
