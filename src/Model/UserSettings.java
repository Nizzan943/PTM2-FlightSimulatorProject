package Model;

import java.io.Serializable;

public class UserSettings implements Serializable {

    public UserSettings() {
    }

    private String realName;
    private int max;
    private int min;
    private String assosicateName;


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAssosicateName() {
        return assosicateName;
    }

    public void setAssosicateName(String assosicateName) {
        this.assosicateName = assosicateName;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }


}