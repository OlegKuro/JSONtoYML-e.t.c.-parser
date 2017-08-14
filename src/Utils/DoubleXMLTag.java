package Utils;

public class DoubleXMLTag {
    private String tag = null;

    public DoubleXMLTag(String tag, String inner){
        StringBuilder sb = new StringBuilder();
        sb.append(new XMLTag(tag));
        sb.append(inner);
        sb.append(new XMLTag("/" + tag));
        this.tag = sb.toString();
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }
}
