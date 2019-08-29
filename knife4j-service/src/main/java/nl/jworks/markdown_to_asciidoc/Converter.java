package nl.jworks.markdown_to_asciidoc;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;

import java.io.*;

public class Converter {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("markdown_to_asciidoc: Please specify a file to convert");
            return;
        }

        File input = new File(args[0]);
        if (!input.exists()) {
            System.err.println("markdown_to_asciidoc: Cannot find the specified file to convert");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            try {
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                reader.close();
                System.out.println(convertMarkdownToAsciiDoc(buffer.toString().trim()));
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("markdown_to_asciidoc: An error occurred while reading the input file");
        }
    }

    public static String convertMarkdownToAsciiDoc(String markdown) {
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        // insert blank line before fenced code block if necessary
        if (markdown.contains("```")) {
            markdown = markdown.replaceAll("(?m)(?<!\n\n)(\\s*)```(\\w*\n)((?:\\1[^\n]*\n)+)\\1```", "\n$1```$2$3$1```");
        }
        char[] markDown = markdown.toCharArray();
        RootNode rootNode = processor.parseMarkdown(markDown);
        return new ToAsciiDocSerializer(rootNode, markdown).toAsciiDoc();
    }
}
