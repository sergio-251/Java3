package player;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class Player extends OutputStream implements Serializable {
    private String firstName;
    private String lastName;
    private int score = 0;

    public Player(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addScore(int scoreAdd){
        this.score += scoreAdd;
    }

    public int getScore(){
        return score;
    }

    @Override
    public void write(int b) throws IOException {

    }
}
