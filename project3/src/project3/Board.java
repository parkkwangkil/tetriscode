package project3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import project3.Block.TetrisBlock;

public class Board extends JPanel implements ActionListener {
	int boardwidth = 10; // 보드의 길이
	int boardheight = 10; // 보드의 높이
	Timer timer; // 타이머 swing

	boolean isFallingFinised = false; // 블록이 떨어지는지 확인
	boolean isStarted = false; // 시작
	boolean isPaused = false; // 정지 요청

	int locationX = 0; // 블록의 x 축
	int locationY = 0; // 블록의 y축
	Block pieceShape; // 현재 테트리스의 형태
	TetrisBlock[] board; // 블록의 위치와 어떤 것인지 확인

	private int blocks[][]; // 테트리스의 블록의 좌표[][][]
	private int blockTable[][][]; // 테트리스 테이블의 좌표 [][][]//

	int countLinesRemoved = 0; // 제거한 라인수
	JLabel statusbar;

	public Board() {
		setFocusable(true); // 이 객체가 구현하는 창이 포커스를 가진다.
		pieceShape = new Block();
		timer = new Timer(400, this); // actionPerformed를 400 ms 마다 실행 설정
		board = new TetrisBlock[boardwidth * boardheight];
		 addKeyListener(new TetrisKeyListener()); // 키를 등록 
		 clearBoard(); // 보드 초기화
	}

	@Override // 시작에 대한 액션
	public void actionPerformed(ActionEvent e) {
		if (isFallingFinised) {
			isFallingFinised = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	int squareWidth() { // 블록의 가로 길이
		return (int) getSize().getWidth() / boardwidth;
	}

	int squareHeight() { // 블록의 세로 길이
		return (int) getSize().getHeight() / boardheight;
	}

	TetrisBlock shapeAt(int x, int y) {
		return board[(y * boardwidth) + x];
	}

	public void start() { // 게임 시작 
		if (isStarted) { // 게임 시작 확인 
			return;
		}

		isStarted = true; // 게임이 시작이면 true
		isFallingFinised = false; // 게임 종료면 false
		countLinesRemoved = 0;
		clearBoard();

		newPiece();
		timer.start();
	}


	private void pause() { // 게임 중단 
		if (isPaused) { // 게임이 중단 되었는지 확인한다.
			return;
		}

		isPaused = isStarted;

		if (isPaused) {
			timer.stop();
			statusbar.setText("paused");
		} else {
			timer.start();
			statusbar.setText("score : " + countLinesRemoved);
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - boardheight * squareHeight();

		// 쌓여 있는 블록을 그린다.
		for (int nowHeight = 0; nowHeight < boardheight; ++nowHeight) {
			for (int nowWidth = 0; nowWidth < boardwidth; ++nowWidth) {

				TetrisBlock shape = shapeAt(nowWidth, boardheight - nowHeight - 1);
				if (shape != TetrisBlock.NoShape)
					drawSquare(g, 0 + nowWidth * squareWidth(), boardTop + nowHeight * squareHeight(), shape);
			}
		}

		// 현재 떨어지고 있는 테트리스 블록을 그린다.
		if (pieceShape.getShape() != TetrisBlock.NoShape) {
			// 테트리스 블록을 그린다.
			for (int i = 0; i < 4; ++i) {
				int x = locationX + pieceShape.getX(i);
				int y = locationY - pieceShape.getY(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (boardheight - y - 1) * squareHeight(),
						pieceShape.getShape());
			}
		}
	}

	private void dropDown() {
		int newY = locationY;
		while (newY > 0) {
			if (!tryMove(pieceShape, locationX, newY - 1))
				break;
			--newY;
		}
		pieceDropped();
	}

	private void pieceDropped() {
		for (int i = 0; i < 4; ++i) { // 테트리스 블록은 4개의 블록으로 이루어져 있다.
			int x = locationX + pieceShape.getX(i);
			int y = locationY - pieceShape.getY(i);
			board[(y * boardwidth) + x] = pieceShape.getShape();
			System.out.println("add : " + ((y * boardwidth) + x) + " shape : " + pieceShape.getShape());
		}

		removeFullLines();

		if (!isFallingFinised) {// 현재 블록의 상태 이다.
			newPiece();
		}
	}

	private void removeFullLines() {
		int countFullLines = 0; // 이 메소드 내에서 제거하는 라인의 수

		for (int i = boardheight - 1; i >= 0; --i) {
			boolean lineIsFull = true;// 라인에 대한 상태

			// 해당하는 라인에서 하나라도 빈 블록이 있는지 확인
			for (int j = 0; j < boardwidth; ++j) {
				if (shapeAt(j, i) == TetrisBlock.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++countFullLines;
				// 지워진 라인 위의 블록을 아래로 이동
				for (int k = i; k < boardheight - 1; ++k) {
					for (int j = 0; j < boardwidth; ++j) {
						board[(k * boardwidth) + j] = shapeAt(j, k + 1);
					}
				}
			}
		}

		if (countFullLines > 0) {
			countLinesRemoved += countFullLines;
			statusbar.setText("score : " + countLinesRemoved);
			isFallingFinised = true;
			pieceShape.setShape(TetrisBlock.NoShape);
			repaint();
		}

	}

	private boolean tryMove(Block newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.getX(i);
			int y = newY - newPiece.getY(i);
			System.out.println(newX + " " + newPiece.getX(i) + " " + newY + " " + newPiece.getY(i));
			// 테트리스 필드를 벗어나는 지 확인
			if (x < 0 || x >= boardwidth || y < 0 || y >= boardheight) {
				System.err.println("필드 x : " + x + " y : " + y);
				return false;
			}

			// 다른 테트리스 블록과 충돌하는 지 확인
			if (shapeAt(x, y) != TetrisBlock.NoShape) {
				System.out.println("블록");
				return false;
			}
		}

		pieceShape = newPiece;
		locationX = newX;
		locationY = newY;
		repaint();
		return true;
	}

	private void clearBoard() {
		for (int i = 0; i < boardheight * boardwidth; i++) {
			board[i] = TetrisBlock.NoShape;
		}
	}

	private void newPiece() {
		pieceShape.setRandomShape();

		// 블록이 생성되는 위치 설정 - 상단 중앙
		locationX = boardwidth / 2 + 1;
		locationY = boardheight - 1 + pieceShape.minY();
		System.out.println(pieceShape.getShape() + " : create location x : " + locationX + " y : " + locationY);

		if (!tryMove(pieceShape, locationX, locationY)) {
			pieceShape.setShape(TetrisBlock.NoShape);
			timer.stop();
			isStarted = false;
			statusbar.setText("game over");
			System.out.println("game over");
		}
	}

	private void oneLineDown() {
		if (!tryMove(pieceShape, locationX, locationY - 1)) {
			pieceDropped();
		}
	}

	private void drawSquare(Graphics g, int x, int y, TetrisBlock shape) {
		// 색 정의 모양과 같은 위치에 해당하는 것의 색이 정의되어 있다.
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0) };

		// 선택된 모양의 컬러 가져오기
		Color color = colors[shape.ordinal()];

		// 사각형의 내부
		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		// 사각형의 외부 상단, 왼쪽
		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y); // 왼쪽 선
		g.drawLine(x, y, x + squareWidth() - 1, y); // 상단 선

		// 사각형의 외부 하단, 오른쪽
		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);// 하단
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);// 오른쪽
	}

	class TetrisKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);

			if (!isStarted || pieceShape.getShape() == TetrisBlock.NoShape) {
				return;
			}

			int keycode = e.getKeyCode();

			if (keycode == 'p' || keycode == 'P') {
				pause();
				return;
			}

			if (isPaused)
				return;

			switch (keycode) {
			case KeyEvent.VK_LEFT:
				tryMove(pieceShape, locationX - 1, locationY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(pieceShape, locationX + 1, locationY);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(pieceShape.rotateRight(), locationX, locationY);
				break;
			case KeyEvent.VK_UP:
				tryMove(pieceShape.rotateLeft(), locationX, locationY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case 'd':
				oneLineDown();
				break;
			case 'D':
				oneLineDown();
				break;
			}
		}
	}

}
