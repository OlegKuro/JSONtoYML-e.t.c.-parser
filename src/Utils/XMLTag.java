package Utils;

public class XMLTag {
    private String tag = null;
    protected static String
            left = "<",
            right = ">";

    public XMLTag(String inner){
        StringBuilder sb = new StringBuilder();
        sb.append(left).append(inner).append(right);
        this.tag = sb.toString();
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }
}
