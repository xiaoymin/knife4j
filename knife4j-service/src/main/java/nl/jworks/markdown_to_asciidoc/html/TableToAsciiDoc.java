package nl.jworks.markdown_to_asciidoc.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableToAsciiDoc {
    public static String convert(String html) {
        if(!html.startsWith("<table")) {
            throw new IllegalArgumentException("No table found in HTML: " + html);
        }

        Document doc = Jsoup.parse(html);
        Element table = doc.select("table").get(0); //select the first table.

        StringBuilder result = new StringBuilder();

        result.append("|===");
        result.append('\n');

        Elements rows = table.select("tr");

        for (Element row : rows) {
            // table headers
            result.append(buildAsciiDocRow(row, "th"));
            if(row.select("th").size() > 0) {
                result.append('\n');
            }

            // table data
            result.append(buildAsciiDocRow(row, "td"));
            result.append('\n');
        }

        result.append("|===");
        result.append('\n');

        return result.toString();
    }

    private static String buildAsciiDocRow(Element row, String query) {
        Elements columns = row.select(query);
        StringBuilder dataRow = new StringBuilder();
        for (Element col : columns) {
            dataRow.append('|').append(applyBasicFormatting(col)).append(' ');
        }
        return dataRow.toString().trim();
    }

    private static String applyBasicFormatting(Element element) {

        String result = element.ownText();

        for (Element child : element.children()) {
            if("code".equals(child.tagName())) {
                result = "`" + child.ownText() + "`";
            } else if("b".equals(child.tagName()) || "strong".equals(child.tagName())) {
                result = "*" + child.ownText() + "*";
            } else if("i".equals(child.tagName()) || "em".equals(child.tagName())) {
                result = "_" + child.ownText() + "_";
            } else if("a".equals(child.tagName())) {
                result = child.attr("href") + "[" + child.ownText() + "]";
            }
        }

        return  result;
    }
}
