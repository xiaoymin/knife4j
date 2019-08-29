package nl.jworks.markdown_to_asciidoc.code;


/**
 * A really basic way of detecting code types.
 */
public class Linguist {

    public String detectLanguage(String text) {
        if(text.startsWith("<")) {
            return "html";
        } else if(text.endsWith(";")) {
            return "java";
        } else {
            return  "groovy";
        }
    }
}
