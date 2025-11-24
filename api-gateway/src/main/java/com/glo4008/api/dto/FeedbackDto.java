package com.glo4008.api.dto;

public class FeedbackDto {
  private boolean isCorrect;
  private String sentence;
  private float polarity;

  public FeedbackDto() {
  }

  public boolean isCorrect() {
    return isCorrect;
  }

  public void setCorrect(boolean correct) {
    isCorrect = correct;
  }

  public String getSentence() {
    return sentence;
  }

  public void setSentence(String sentence) {
    this.sentence = sentence;
  }

  public float getPolarity() {
    return polarity;
  }

  public void setPolarity(float polarity) {
    this.polarity = polarity;
  }
}
