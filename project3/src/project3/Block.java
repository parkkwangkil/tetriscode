package project3;

import java.awt.Shape;
import java.util.Random;

public class Block {
	enum TetrisBlock { // 테트리스의 블록 형태
		NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape
	};

	private int blocks[][]; // 테트리스의 블록의 좌표[][][]
	private int blockTable[][][]; // 테트리스 테이블의 좌표 [][][]
	private TetrisBlock currentBlock; // 테트리스의 현재 블록의 이미지
	private int rotation;

	public Block() {
		blocks = new int[4][2];
		setShape(currentBlock.NoShape);

	}

	void setShape(TetrisBlock shape) {
		// 현재 블록의 형태를 입력
		// 기능 : 테트리스의 블록을 변형 하는 입력 값을 삽입
		// [] currentPiece 값을 넣어준다. 제일 처음 인덱스
		// [][] rotation 회전 값
		// ordinal : 원소에 저장된 순서를 정수로 변환 값
		blockTable = new int[][][] { { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, // NoShape
				{ { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, // SquareShape
				{ { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, // LShape
				{ { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, // Line
				{ { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
				{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } } };

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; ++j) {
				blocks[i][j] = blockTable[shape.ordinal()][i][j];
			}
		}
		currentBlock = shape;

	}

	public void setRandomShape() {
		// abs 매개변수 숫자의 반환 값 숫자의 절대값을 반환
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % 6 + 1;
		TetrisBlock[] values = TetrisBlock.values();
		setShape(values[x]);
	}

	public int getX(int i) {
		// 테트리스 블록의 좌표인 blocks에 i 값을 대입 rotation은 0 값을 대입
		// 테트리스 i번쨰 블록의 x 좌표를 가져 옴
		return blocks[i][0];
	}

	public int getY(int i) {
		// 위와 설명은 동일
		// i 번째 블록의 Y 좌표를 가져 온다.
		return blocks[i][1];
	}

	public TetrisBlock getShape() {
		return currentBlock;
	}

	private void setX(int i, int x) {
		// 블록의 x 좌표, i 값에 따라 블록 선택이 가능
		blocks[i][0] = x;
	}

	private void setY(int i, int y) {
		// 블록의 y 좌표, i 값에 따라 블록 선택 가능
		blocks[i][0] = y;
	}

	public int minX() {
		// X 좌표의 가장 작은 값
		int m = blocks[0][0];// 첫 번째 X 좌표
		for (int i = 0; i < 4; i++) {
		}
		return m;
	}

	public int minY() {
		// Y 좌표의 가장 작은 값
		int m = blocks[0][1]; // 첫번쨰 Y 좌표
		for (int i = 0; i < 4; i++) {
		}
		return m;
	}

	public Block rotateRight() {
		// //블록의 좌표를 오른쪽으로 돌린다.
		if (currentBlock == TetrisBlock.SquareShape)
			return this;

		Block result = new Block();
		result.currentBlock = currentBlock;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, getY(i));
			result.setY(i, -getX(i));
		}
		return result;
	}

	public Block rotateLeft() {
		// 블록을 왼쪽으로 돌려서 반환한다.
		if (currentBlock == TetrisBlock.SquareShape)
			return this;

		Block result = new Block();
		result.currentBlock = currentBlock;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, -getY(i));
			result.setY(i, getX(i));
		}
		return result;
	}
}