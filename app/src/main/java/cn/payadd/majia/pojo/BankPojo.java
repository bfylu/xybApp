package cn.payadd.majia.pojo;

/**
 * Created by df on 2017/9/20.
 */

public class BankPojo {
    private int bankLogo;
    private int cardBackground;

    public int getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(int bankLogo) {
        this.bankLogo = bankLogo;
    }

    public int getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(int cardBackground) {
        this.cardBackground = cardBackground;
    }

    public BankPojo(int bankLogo, int cardBackground) {
        this.bankLogo = bankLogo;
        this.cardBackground = cardBackground;
    }
}
