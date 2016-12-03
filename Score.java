import java.io.*;

//Class for files.


/**
 * Class to save and retrieve top scores.
 */
public class Score {


    /**
     * Method to update high score.
     */
    public void updateScores(int score) {

        //Holds high score.
        int fileScore = readScore();


        //Calculate highscore.
        if (score > fileScore) {
            fileScore = score;
        }


        //Write binary data to file.
        try {

            FileOutputStream fstream = new FileOutputStream("Score.dat");
            DataOutputStream outputFile = new DataOutputStream(fstream);

            outputFile.writeInt(fileScore);

            outputFile.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    /**
     * Method that returns the high score.
     */
    public int readScore() {

        //Holds high score.
        int fileScore = 0;

        //Flag to stop reading file.
        boolean endOfFile = false;


        //Read binary data, or create if not there.
        try {

            FileInputStream fstream = new FileInputStream("Score.dat");
            DataInputStream inputFile = new DataInputStream(fstream);

            while (!endOfFile) {
                try {

                    fileScore = inputFile.readInt();

                } catch (EOFException e) {

                    endOfFile = true;
                }
            }

            inputFile.close();

        } catch (IOException e) {
            try {

                FileOutputStream fstream = new FileOutputStream("Score.dat");
                DataOutputStream outputFile = new DataOutputStream(fstream);

                outputFile.writeInt(0);

                outputFile.close();

            } catch (IOException ex) {

                e.printStackTrace();
            }
        }

        return fileScore;
    }

}
