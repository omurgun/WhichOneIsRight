package com.omurgun.whichoneisright.Model;

public class Question {

    private String optionA;
    private String optionB;
    private String optionTrue;

    public Question(String optionA, String optionB, String optionTrue) {
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionTrue = optionTrue;
    }

    public String getOptionA() {
        return optionA;
    }
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }
    public String getOptionB() {
        return optionB;
    }
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }
    public String getOptionTrue() {
        return optionTrue;
    }
    public void setOptionTrue(String optionTrue) {
        this.optionTrue = optionTrue;
    }
}
