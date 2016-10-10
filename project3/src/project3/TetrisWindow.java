package project3;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class TetrisWindow extends JFrame {

	JLabel statusbar;

	public TetrisWindow() {
		statusbar = new JLabel("tetris"); // 게임 상태
		add(statusbar, BorderLayout.SOUTH); // 라벨 삽입 및 위치 설정

		Board board = new Board();
		add(board);
		board.start(); // 게임 시작
		System.out.println("add window and start game");
		
		//JPanel back = new JPanel();//배경 패널
		//add(back);
		
		setSize(300, 500); // 전체 창 크기 설정
		setResizable(false); // 창 크기 고정
		setTitle("Tetris!"); // 타이틀 삽입
		setDefaultCloseOperation(EXIT_ON_CLOSE); //종료 버튼 설정
	}

	public static void main(String[] args) {
		TetrisWindow tetrisWindow = new TetrisWindow();
		tetrisWindow.setLocationRelativeTo(null); // 모니터 화면 중앙에 위치
		tetrisWindow.setVisible(true); // show 명령어 대신 사용한다.
		System.out.println("show window");
	}

	/**
	 * 이 객체에서 제공하는 라벨을 반환한다. - 라벨은 게임 오버, 게임 점수들을 표기하여 주는 역활을 하고 있다.
	 * @return JLabel
	 */
	public JLabel getStatusbar() {
		return statusbar;
	}

}