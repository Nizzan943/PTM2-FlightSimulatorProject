package Model;

import java.io.Serializable;

public class UserSettings implements Serializable {

    public UserSettings() {
    }
    private String assosicateName;
    private int max = 1000000;
    private int min = -1000000;
    private String realName;


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
