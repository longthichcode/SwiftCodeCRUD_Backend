package com.project.DTO;

public class ExcelError {
    private int row;
    private int column;
    private String message;

    // Constructor
    public ExcelError(int row, int column, String message) {
        this.row = row;
        this.column = column;
        this.message = message;
    }

    // Getters and Setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}