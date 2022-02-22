package ro.mta.licenta.badea.temporalUse;

public class SenderText {
    private static String data=new String();

    public SenderText(){}

    public static String getData() {
        return data;
    }

    public static void setData(String data) {
        SenderText.data = data;
    }
}
