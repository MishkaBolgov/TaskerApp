package mbolg.tasker.recognizer;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.ArrayList;

@Root(strict = false)
public class RecResponse {
    @Attribute(name = "success")
    private String success;

    @ElementList(name = "variant", inline = true, required = false)
    private ArrayList<Var> variants;

    public RecResponse() {

    }

    public String getRecognizedText() {
        if(variants == null)
            return "";
        if (variants.size() == 0)
            return "";
        else return variants.get(0).getText();
    }

    @Override
    public String toString() {
        return "RecResponse{" +
                "success='" + success + '\'' +
                ", variants=" + variants +
                '}';
    }
}

@Root(name = "variant")
class Var {
    @Text
    private String text;

    public String getText() {
        return text;
    }

    public Var() {
    }
}
