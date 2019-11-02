import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.*;

public class SignUp {
	public SignUp(Statement stmt) {
		JFrame signUpFrame = new JFrame("회원가입"); // 회원가입 frame 생성
		signUpFrame.setLocation(600, 400); // frame의 위치 조정
		Container signUpPane = signUpFrame.getContentPane();

		JPanel signUpPanel = new JPanel();
		signUpPane.add(signUpPanel);
		signUpPanel.setBackground(new Color(255, 255, 255));
		signUpPanel.setLayout(null);

		signUpFrame.setPreferredSize(new Dimension(415, 600));

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

		JButton Accept = new JButton("가입");
		Accept.setLocation(50, 400);
		Accept.setSize(140, 40);

		JButton Back = new JButton("뒤로");
		Back.setLocation(210, 400);
		Back.setSize(140, 40);

		Accept.addActionListener(new ActionListener() { // add to the database

			@Override
			public void actionPerformed(ActionEvent e) {
				String memid = idtf.getText(); // memid에 입력
				String mempwd = new String(pwtf.getPassword()); // mempwd에 입력
				String query = "insert into memberinfo (memid, mempwd) values ('" + memid + "', '" + mempwd + "');";
				System.out.println(query);

				try {
					stmt.executeUpdate(query);
					JOptionPane.showConfirmDialog(null, "가입되었습니다", "성공", JOptionPane.PLAIN_MESSAGE);
					pwtf.setText("");
					idtf.setText("");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "이미 있는 아이디입니다", "오류", JOptionPane.ERROR_MESSAGE);
					pwtf.setText("");
					idtf.setText("");
					e1.printStackTrace();
				}

			}
			// 아이디 primary key로 지정해놓음
		});

		Back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				signUpFrame.dispose();
				new GUI();
			}
		});

		signUpPanel.add(id);
		signUpPanel.add(idtf);
		signUpPanel.add(pw);
		signUpPanel.add(pwtf);
		signUpPanel.add(Accept);
		signUpPanel.add(Back);

		signUpFrame.pack();
		signUpFrame.setVisible(true);
	}

}
