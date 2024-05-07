package game1;

public class Score implements Comparable<Score>{ // object to store relationship between score and name and allow duplicate 'keys' in a list
    public int score;
    public String name;
    boolean old;

    public Score(int score, String name, boolean old){
        this.score = score;
        this.name = name;
        this.old = old;
    }

    @Override
    public int compareTo(Score o) {
        if(score > o.score){
            return -1;
        }
        else if(score < o.score){
            return 1;
        }
        else {
            if(old){
                return -1;
            }
            else if(o.old){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
}
