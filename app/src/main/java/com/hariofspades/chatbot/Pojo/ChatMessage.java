package com.hariofspades.chatbot.Pojo;

/**
 * Created by Hari on 11/05/16.
 */
public class ChatMessage {
    private boolean isImage, isMine;
    private String content;
    private String col_name;
    private String[] row_data;

    public ChatMessage(String message, boolean mine, boolean image) {
        content = message;
        isMine = mine;
        isImage = image;
    }

    public ChatMessage(String col,String[] row,boolean mine,boolean image)
    {
        col_name = col;
        row_data = row;
        isMine = mine;
        isImage = image;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public String getCol_name() {
        return col_name;
    }

    public void setCol_name(String col_name) {
        this.col_name = col_name;
    }

    public String[] getRow_data() {
        return row_data;
    }

    public void setRow_data(String[] row_data) {
        this.row_data = row_data;
    }
}
