package com.cakeme.backend.dto.cake;

public class CakeRequestDTO {
    private String shape;
    private String flavor;
    private String color1;
    private String color2;
    private String occasion;
    private String theme;
    private String text;

    // Getter & Setter
    public String getShape() { return shape; }
    public void setShape(String shape) { this.shape = shape; }
    public String getFlavor() { return flavor; }
    public void setFlavor(String flavor) { this.flavor = flavor; }
    public String getColor1() { return color1; }
    public void setColor1(String color1) { this.color1 = color1; }
    public String getColor2() { return color2; }
    public void setColor2(String color2) { this.color2 = color2; }
    public String getOccasion() { return occasion; }
    public void setOccasion(String occasion) { this.occasion = occasion; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

