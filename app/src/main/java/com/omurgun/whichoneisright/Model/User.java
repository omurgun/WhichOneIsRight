package com.omurgun.whichoneisright.Model;

public class User  implements Comparable<User>{

    private String username;
    private String maxScore;

    public User(String username, String maxScore) {
        this.username = username;
        this.maxScore = maxScore;
    }

    public String getMaxScore() {
        return maxScore;
    }
    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public int compareTo(User o) {
        if(Integer.parseInt(maxScore)==Integer.parseInt(o.getMaxScore()))
            return 0;
        else if(Integer.parseInt(maxScore)<Integer.parseInt(o.getMaxScore()))
            return 1;
        else
            return -1;
    }
}
