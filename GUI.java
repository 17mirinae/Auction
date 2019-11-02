import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

//
// SignUp 창과 Aution 창을 클래스로 분리하였음, MySQL의 DB 이름이 member이고 사용자 root, 암호 wkqkdjvmf, 테이블 이름이 members 일 때 동작함
// 로그인에서 id, pwd 확인 코드 추가했음, SignUp에서 사용자 추가되도록 했음
//

public class GUI {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/members?useSSL=false&allowPublicKeyRetrieval=true";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "wkqkdjvmf";

	Connection conn = null;
	Statement stmt = null;

	public GUI() {
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("경매 프로그램"); // 처음 창, 이름은 경매 프로그램
		Container LoginContentPane = frame.getContentPane(); // Pane만들기
		frame.setPreferredSize(new Dimension(500, 650)); // 처음 창 (frame)의 크기
		frame.setLocation(600, 200); // 처음 창 (frame)의 위치

		JPanel LoginPanel = new JPanel(); // 처음 창에 넣을 패널 생성
		LoginContentPane.add(LoginPanel); // loginContentPane에 LoginPanel추가
		LoginPanel.setBackground(new Color(255, 255, 255)); // LoginPanel의 배경 색을 흰색으로 설정
		LoginPanel.setLayout(null);

		JLabel ID = new JLabel("아이디: "); // "아이디: " 생성
		ID.setLocation(100, 200);
		ID.setSize(100, 20);

		JTextField IDTF = new JTextField(); // 아이디를 입력하는 TextField 생성
		IDTF.setLocation(200, 200);
		IDTF.setSize(200, 20);

		JLabel PW = new JLabel("비밀번호: "); // "비밀번호: " 생성
		PW.setLocation(100, 250);
		PW.setSize(100, 20);

		JPasswordField PWTF = new JPasswordField(); // 비밀번호를 입력하는 PasswordField 생성
		PWTF.setLocation(200, 250);
		PWTF.setSize(200, 20);

		JButton LoginButton = new JButton("로그인"); // 로그인 버튼 생성
		LoginButton.setLocation(100, 400);
		LoginButton.setSize(130, 40);

		JButton SignUpButton = new JButton("회원가입"); // 회원가입 버튼 생성
		SignUpButton.setLocation(250, 400);
		SignUpButton.setSize(130, 40);

		JButton AdminButton = new JButton("관리자 로그인");
		AdminButton.setLocation(100, 450);
		AdminButton.setSize(280, 40);

		LoginPanel.add(ID); // "아이디" 라벨 패널에 추가
		LoginPanel.add(IDTF); // "아이디" 입력칸 패널에 추가

		LoginPanel.add(PW); // "비밀번호" 라벨 패널에 추가
		LoginPanel.add(PWTF); // "비밀번호" 입력칸 패널에 추가

		LoginPanel.add(LoginButton); // "로그인" 버튼 패널에 추가
		LoginPanel.add(SignUpButton); // "회원가입" 버튼 패널에 추가
		LoginPanel.add(AdminButton); // "관리자 로그인" 버튼 패널에 추가

		LoginButton.addActionListener(new ActionListener() { // 로그인 버튼 기능
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String memidIDTF = IDTF.getText();
				String query = "SELECT mempwd FROM memberinfo WHERE memid = '" + memidIDTF + "';";
				System.out.println(query);
				try {
					ResultSet rspw = stmt.executeQuery(query);

					if (rspw.next()) {
						String mempwdDB = rspw.getString("mempwd");
						String mempwdPWTF = new String(PWTF.getPassword());

						if (mempwdDB.equals(mempwdPWTF)) {
							frame.dispose();
							new Auction(stmt, memidIDTF); // 전달해야 할 인자가 있으면 추가할 것
						} else {
							JOptionPane.showMessageDialog(null, "존재하지 않는 아이디거나 비밀번호가 틀렸습니다", "오류",
									JOptionPane.ERROR_MESSAGE);
							PWTF.setText("");
							IDTF.setText("");
						}
					} else {
						JOptionPane.showMessageDialog(null, "존재하지 않는 아이디거나 비밀번호가 틀렸습니다", "오류",
								JOptionPane.ERROR_MESSAGE);
						PWTF.setText("");
						IDTF.setText("");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}); // 로그인 버튼 기능

		SignUpButton.addActionListener(new ActionListener() {
			@Override // 회원가입 소스
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				new SignUp(stmt);
			}
		}); // 회원가입 버튼 기능

		AdminButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new AdminLogin(stmt);
			}
		});

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new GUI();
	}
}
