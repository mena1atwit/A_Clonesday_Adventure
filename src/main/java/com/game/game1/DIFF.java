package com.game.game1;

public enum DIFF {
    HARD("PlayerRed.png", "element_red_polygon.png"),
    MEDIUM("PlayerBlue.png", "element_red_polygon.png"),
    EASY("PlayerGreen.png", "element_red_polygon.png");
    private String urlDifficulty;
    private String urlLife;


    private DIFF(String urlDifficulty, String urlLife) {
        this.urlDifficulty = urlDifficulty;
        this.urlLife = urlLife;
    }

    public String getCharacter() {
        return this.urlDifficulty;
    }

    public String getLife(){
        return this.urlLife;
    }
}


