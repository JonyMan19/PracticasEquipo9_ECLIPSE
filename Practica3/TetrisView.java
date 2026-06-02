package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

public class TetrisView extends View {

    private static final int COLS = 10, ROWS = 20;
    private static final int DELAY = 500;

    private int[][] board = new int[ROWS][COLS];
    private int[][] currentPiece;
    private int pieceX, pieceY;
    private int currentColor;
    private int score = 0;
    private boolean gameOver = false;
    private Paint paint = new Paint();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable gameLoop;

    private static final int[][][] PIECES = {
        {{ 1,1,1,1 }},
        {{ 1,1 },{ 1,1 }},
        {{ 0,1,0 },{ 1,1,1 }},
        {{ 1,0 },{ 1,0 },{ 1,1 }},
        {{ 0,1 },{ 0,1 },{ 1,1 }},
        {{ 0,1,1 },{ 1,1,0 }},
        {{ 1,1,0 },{ 0,1,1 }}
    };

    private static final int[] COLORS = {
        Color.CYAN, Color.YELLOW, Color.MAGENTA,
        Color.parseColor("#FF7700"), Color.BLUE,
        Color.GREEN, Color.RED
    };

    public TetrisView(Context context) {
        super(context);
        init();
    }

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        spawnPiece();
        gameLoop = new Runnable() {
            @Override
            public void run() {
                if (!gameOver) {
                    update();
                    invalidate();
                    handler.postDelayed(gameLoop, DELAY);
                }
            }
        };
        handler.post(gameLoop);
    }

    private void spawnPiece() {
        int idx = (int)(Math.random() * PIECES.length);
        currentPiece = PIECES[idx];
        currentColor = COLORS[idx];
        pieceX = COLS / 2 - currentPiece[0].length / 2;
        pieceY = 0;
        if (collides(currentPiece, pieceX, pieceY)) gameOver = true;
    }

    public void rotatePiece() {
        int rows = currentPiece.length, cols = currentPiece[0].length;
        int[][] rotated = new int[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                rotated[c][rows - 1 - r] = currentPiece[r][c];
        if (!collides(rotated, pieceX, pieceY)) currentPiece = rotated;
    }

    public void moveLeft()  { if (!collides(currentPiece, pieceX - 1, pieceY)) pieceX--; }
    public void moveRight() { if (!collides(currentPiece, pieceX + 1, pieceY)) pieceX++; }
    public void moveDown()  { update(); invalidate(); }

    private boolean collides(int[][] piece, int px, int py) {
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] == 0) continue;
                int nx = px + c, ny = py + r;
                if (nx < 0 || nx >= COLS || ny >= ROWS) return true;
                if (board[ny][nx] != 0) return true;
            }
        }
        return false;
    }

    private void update() {
        if (!collides(currentPiece, pieceX, pieceY + 1)) {
            pieceY++;
            return;
        }
        for (int r = 0; r < currentPiece.length; r++)
            for (int c = 0; c < currentPiece[r].length; c++)
                if (currentPiece[r][c] != 0)
                    board[pieceY + r][pieceX + c] = currentColor;
        clearLines();
        spawnPiece();
    }

    private void clearLines() {
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++)
                if (board[r][c] == 0) { full = false; break; }
            if (!full) continue;
            for (int i = r; i > 0; i--)
                board[i] = board[i - 1].clone();
            board[0] = new int[COLS];
            score += 100;
            r++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Calcula celda ajustada a ancho Y alto
        float cellW = getWidth()  / (float) COLS;
        float cellH = getHeight() / (float) ROWS;
        float cell  = Math.min(cellW, cellH);

        // Offsets para centrar el tablero
        float offsetX = (getWidth()  - cell * COLS) / 2f;
        float offsetY = (getHeight() - cell * ROWS) / 2f;

        // Fondo
        canvas.drawColor(Color.parseColor("#111111"));

        // Bloques fijos
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (board[r][c] != 0)
                    drawCell(canvas, c, r, board[r][c], cell, offsetX, offsetY);

        // Pieza activa
        for (int r = 0; r < currentPiece.length; r++)
            for (int c = 0; c < currentPiece[r].length; c++)
                if (currentPiece[r][c] != 0)
                    drawCell(canvas, pieceX + c, pieceY + r, currentColor, cell, offsetX, offsetY);

        // Game Over
        if (gameOver) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(cell * 2);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME OVER", getWidth() / 2f, offsetY + (ROWS / 2f) * cell, paint);
        }
    }

    private void drawCell(Canvas canvas, int col, int row, int color,
                          float cell, float offsetX, float offsetY) {
        float left   = offsetX + col * cell + 1;
        float top    = offsetY + row * cell + 1;
        float right  = offsetX + (col + 1) * cell - 1;
        float bottom = offsetY + (row + 1) * cell - 1;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public int  getScore()   { return score; }
    public void pauseGame()  { handler.removeCallbacks(gameLoop); }
    public void resumeGame() { if (!gameOver) handler.post(gameLoop); }
} 