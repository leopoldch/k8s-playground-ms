package com.glo4008.api.dto;

public class PolarityDto {

    private String sentence;
    private float polarity;

    public PolarityDto() {
    }

    public PolarityDto(String sentence, float polarity) {
        this.sentence = sentence;
        this.polarity = polarity;
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

    @Override
    public String toString() {
        return "PolarityDto{" +
                "sentence='" + sentence + '\'' +
                ", polarity=" + polarity +
                '}';
    }
}
