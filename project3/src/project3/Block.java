package project3;

import java.awt.Shape;
import java.util.Random;

public class Block {
	enum TetrisBlock { // ��Ʈ������ ��� ����
		NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape
	};

	private int blocks[][]; // ��Ʈ������ ����� ��ǥ[][][]
	private int blockTable[][][]; // ��Ʈ���� ���̺��� ��ǥ [][][]
	private TetrisBlock currentBlock; // ��Ʈ������ ���� ����� �̹���
	private int rotation;

	public Block() {
		blocks = new int[4][2];
		setShape(currentBlock.NoShape);

	}

	void setShape(TetrisBlock shape) {
		// ���� ����� ���¸� �Է�
		// ��� : ��Ʈ������ ����� ���� �ϴ� �Է� ���� ����
		// [] currentPiece ���� �־��ش�. ���� ó�� �ε���
		// [][] rotation ȸ�� ��
		// ordinal : ���ҿ� ����� ������ ������ ��ȯ ��
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
		// abs �Ű����� ������ ��ȯ �� ������ ���밪�� ��ȯ
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % 6 + 1;
		TetrisBlock[] values = TetrisBlock.values();
		setShape(values[x]);
	}

	public int getX(int i) {
		// ��Ʈ���� ����� ��ǥ�� blocks�� i ���� ���� rotation�� 0 ���� ����
		// ��Ʈ���� i���� ����� x ��ǥ�� ���� ��
		return blocks[i][0];
	}

	public int getY(int i) {
		// ���� ������ ����
		// i ��° ����� Y ��ǥ�� ���� �´�.
		return blocks[i][1];
	}

	public TetrisBlock getShape() {
		return currentBlock;
	}

	private void setX(int i, int x) {
		// ����� x ��ǥ, i ���� ���� ��� ������ ����
		blocks[i][0] = x;
	}

	private void setY(int i, int y) {
		// ����� y ��ǥ, i ���� ���� ��� ���� ����
		blocks[i][0] = y;
	}

	public int minX() {
		// X ��ǥ�� ���� ���� ��
		int m = blocks[0][0];// ù ��° X ��ǥ
		for (int i = 0; i < 4; i++) {
		}
		return m;
	}

	public int minY() {
		// Y ��ǥ�� ���� ���� ��
		int m = blocks[0][1]; // ù���� Y ��ǥ
		for (int i = 0; i < 4; i++) {
		}
		return m;
	}

	public Block rotateRight() {
		// //����� ��ǥ�� ���������� ������.
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
		// ����� �������� ������ ��ȯ�Ѵ�.
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