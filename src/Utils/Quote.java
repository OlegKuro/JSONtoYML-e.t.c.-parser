package Utils;

public class Quote {
    private String content = null;
    private static String q = "\"";

    public Quote(String inner){
        StringBuilder sb = new StringBuilder();
        sb.append(q).append(inner).append(q);
        this.content = sb.toString();
    }

    @Override
    public String toString() {
        return this.content.toString();
    }
}
