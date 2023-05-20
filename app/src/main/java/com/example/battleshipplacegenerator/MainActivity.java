package com.example.battleshipplacegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_SIZE = 10;
    private static final int[] SHIP_LENGTHS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    private Button[][] buttons;
    private int[][] grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(GRID_SIZE);
        gridLayout.setRowCount(GRID_SIZE);

        buttons = new Button[GRID_SIZE][GRID_SIZE];
        grid = new int[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Button button = new Button(this);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels / GRID_SIZE;
                layoutParams.height = getResources().getDisplayMetrics().widthPixels / GRID_SIZE;
                button.setLayoutParams(layoutParams);
                buttons[row][col] = button;
                gridLayout.addView(button);
            }
        }

        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateGrid();
            }
        });
    }

    private void generateGrid() {
        clearGrid();

        Random random = new Random();
        List<Integer> availableIndices = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            availableIndices.add(i);
        }

        for (int shipLength : SHIP_LENGTHS) {
            boolean isHorizontal = random.nextBoolean();

            int availableCount = availableIndices.size();
            boolean shipPlaced = false;

            while (!shipPlaced && availableCount > 0) {
                int randomIndex = random.nextInt(availableCount);
                int gridIndex = availableIndices.get(randomIndex);
                int row = gridIndex / GRID_SIZE;
                int col = gridIndex % GRID_SIZE;

                if (isHorizontal && col + shipLength <= GRID_SIZE) {
                    if (isValidPlacement(row, col, shipLength, true)) {
                        placeShip(row, col, shipLength, true);
                        shipPlaced = true;
                    }
                } else if (!isHorizontal && row + shipLength <= GRID_SIZE) {
                    if (isValidPlacement(row, col, shipLength, false)) {
                        placeShip(row, col, shipLength, false);
                        shipPlaced = true;
                    }
                }

                if (!shipPlaced) {
                    availableIndices.remove(randomIndex);
                    availableCount--;
                }
            }
        }

        updateGridUI();
        Toast.makeText(this, "Корабли расставлены!", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidPlacement(int row, int col, int shipLength, boolean isHorizontal) {
        if (isHorizontal) {
            for (int i = col; i < col + shipLength; i++) {
                if (grid[row][i] == 1 || hasAdjacentShip(row, i)) {
                    return false;
                }
            }
        } else {
            for (int i = row; i < row + shipLength; i++) {
                if (grid[i][col] == 1 || hasAdjacentShip(i, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasAdjacentShip(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < GRID_SIZE && j >= 0 && j < GRID_SIZE) {
                    if (grid[i][j] == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void placeShip(int row, int col, int shipLength, boolean isHorizontal) {
        if (isHorizontal) {
            for (int i = col; i < col + shipLength; i++) {
                grid[row][i] = 1;
            }
        } else {
            for (int i = row; i < row + shipLength; i++) {
                grid[i][col] = 1;
            }
        }
    }

    private void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = 0;
            }
        }
    }

    private void updateGridUI() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Button button = buttons[row][col];
                if (grid[row][col] == 1) {
                    button.setText("1");
                } else {
                    button.setText("");
                }
            }
        }
    }
}
