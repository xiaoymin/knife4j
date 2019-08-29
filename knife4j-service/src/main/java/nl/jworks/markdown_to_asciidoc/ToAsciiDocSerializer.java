package nl.jworks.markdown_to_asciidoc;

import nl.jworks.markdown_to_asciidoc.code.Linguist;
import nl.jworks.markdown_to_asciidoc.html.TableToAsciiDoc;

import nl.jworks.markdown_to_asciidoc.util.Joiner;
import org.parboiled.common.StringUtils;
import org.pegdown.LinkRenderer;
import org.pegdown.Printer;
import org.pegdown.ast.*;

import java.util.*;

import static org.parboiled.common.Preconditions.checkArgNotNull;

public class ToAsciiDocSerializer implements Visitor {
    public static final String HARD_LINE_BREAK_MARKDOWN = "  \n";
    protected String source;
    protected Printer printer = new Printer();
    protected final Map<String, ReferenceNode> references = new HashMap<String, ReferenceNode>();
    protected final Map<String, String> abbreviations = new HashMap<String, String>();
    protected final LinkRenderer linkRenderer = new LinkRenderer();

    protected TableNode currentTableNode;
    protected int currentTableColumn;
    protected boolean inTableHeader;

    protected char listMarker;
    protected int listLevel = 0;
    protected int blockQuoteLevel = 0;

    // Experimental feature.
    protected boolean autoDetectLanguageType;
    protected Linguist linguist = new Linguist();
    
    protected RootNode rootNode;

    public ToAsciiDocSerializer(RootNode rootNode) {
        this(rootNode, null);
    }

    public ToAsciiDocSerializer(RootNode rootNode, String source) {
    	this.printer = new Printer();
        this.linguist = new Linguist();
        this.autoDetectLanguageType = false;
        checkArgNotNull(rootNode, "rootNode");
        this.rootNode = rootNode;
        this.source = source;
    }

    public String toAsciiDoc() {
    	cleanAst(rootNode);
        rootNode.accept(this);
        String result = normalizeWhitelines(printer.getString());
        printer.clear();
        return result;
    }

    public void visit(RootNode node) {
        for (ReferenceNode refNode : node.getReferences()) {
            visitChildren(refNode);
            references.put(normalize(printer.getString()), refNode);
            printer.clear();
        }
        for (AbbreviationNode abbrNode : node.getAbbreviations()) {
            visitChildren(abbrNode);
            String abbr = printer.getString();
            printer.clear();
            abbrNode.getExpansion().accept(this);
            String expansion = printer.getString();
            abbreviations.put(abbr, expansion);
            printer.clear();
        }
        visitChildren(node);
    }

    public void visit(AbbreviationNode node) {
    }

    public void visit(AutoLinkNode node) {
        printLink(linkRenderer.render(node));
    }

    public void visit(BlockQuoteNode node) {
        printer.println().println();

        blockQuoteLevel += 4;

        repeat('_', blockQuoteLevel);
        printer.println();
        visitChildren(node);
        printer.println().println();
        repeat('_', blockQuoteLevel);

        blockQuoteLevel -= 4;

        printer.println();
    }

    public void visit(BulletListNode node) {
        char prevListMarker = listMarker;
        listMarker = '*';
        
        listLevel = listLevel + 1;
        visitChildren(node);
        listLevel = listLevel - 1;
        
        listMarker = prevListMarker;
    }

    public void visit(CodeNode node) {
        printer.print('`');
        printer.printEncoded(node.getText());
        printer.print('`');
    }

    public void visit(DefinitionListNode node) {
        printer.println();
        visitChildren(node);
    }
    
    public void visit(DefinitionTermNode node) {
        visitChildren(node);
        printer.indent(2);
        printer.print("::").println();
    }

    public void visit(DefinitionNode node) {
        visitChildren(node);
        if (printer.indent > 0) {
            printer.indent(-2);
        }
        printer.println();
    }

    public void visit(ExpImageNode node) {
    	String text = printChildrenToString(node);
    	LinkRenderer.Rendering imageRenderer = linkRenderer.render(node, text);
    	Node linkNode;
    	if ((linkNode = findParentNode(node, rootNode)) instanceof ExpLinkNode) {
    		printImageTagWithLink(imageRenderer, linkRenderer.render((ExpLinkNode) linkNode, null));
    	}
    	else {
          printImageTag(linkRenderer.render(node, text));
    	}
    }

    public void visit(ExpLinkNode node) {
        String text = printChildrenToString(node);
        if (text.startsWith("image:")) {
        	printer.print(text);
        }
        else {
        	printLink(linkRenderer.render(node, text));
        }
    }

    public void visit(HeaderNode node) {
        printer.println().println();
        repeat('=', node.getLevel());
        printer.print(' ');
        visitChildren(node);
        printer.println().println();
    }

    private void repeat(char c, int times) {
        for (int i = 0; i < times; i++) {
            printer.print(c);
        }
    }

    public void visit(HtmlBlockNode node) {
        String text = node.getText();
        if (text.length() > 0) printer.println();

        if(text.startsWith("<table")) {
            printer.print(TableToAsciiDoc.convert(text));
            printer.println();
        }

        //printer.print(text);
    }

    public void visit(InlineHtmlNode node) {
        printer.print(node.getText());
    }

    public void visit(ListItemNode node) {
        printer.println();
        repeat(listMarker, listLevel);
        printer.print(" ");

        visitChildren(node);
    }

    public void visit(MailLinkNode node) {
        printLink(linkRenderer.render(node));
    }

    public void visit(OrderedListNode node) {
        char prevListMarker = listMarker;
        listMarker = '.';

        listLevel = listLevel + 1;
        visitChildren(node);
        listLevel = listLevel - 1;
        
        listMarker = prevListMarker;
    }

    public void visit(ParaNode node) {
    	if (!isListItemText(node)) {
    		printer.println().println();
    	}
        visitChildren(node);
        printer.println().println();
    }

    public void visit(QuotedNode node) {
        switch (node.getType()) {
            case DoubleAngle:
                printer.print("«");
                visitChildren(node);
                printer.print("»");
                break;
            case Double:
                printer.print("\"");
                visitChildren(node);
                printer.print("\"");
                break;
            case Single:
                printer.print("'");
                visitChildren(node);
                printer.print("'");
                break;
        }
    }

    public void visit(ReferenceNode node) {
        // reference nodes are not printed
    }

    public void visit(RefImageNode node) {
        String text = printChildrenToString(node);
        String key = node.referenceKey != null ? printChildrenToString(node.referenceKey) : text;
        ReferenceNode refNode = references.get(normalize(key));
        if (refNode == null) { // "fake" reference image link
            printer.print("![").print(text).print(']');
            if (node.separatorSpace != null) {
                printer.print(node.separatorSpace).print('[');
                if (node.referenceKey != null) printer.print(key);
                printer.print(']');
            }
        } else printImageTag(linkRenderer.render(node, refNode.getUrl(), refNode.getTitle(), text));
    }

    public void visit(RefLinkNode node) {
        String text = printChildrenToString(node);
        String key = node.referenceKey != null ? printChildrenToString(node.referenceKey) : text;
        ReferenceNode refNode = references.get(normalize(key));
        if (refNode == null) { // "fake" reference link
            printer.print('[').print(text).print(']');
            if (node.separatorSpace != null) {
                printer.print(node.separatorSpace).print('[');
                if (node.referenceKey != null) printer.print(key);
                printer.print(']');
            }
        } else printLink(linkRenderer.render(node, refNode.getUrl(), refNode.getTitle(), text));
    }

    public void visit(SimpleNode node) {
        switch (node.getType()) {
            case Apostrophe:
                printer.print("'");
                break;
            case Ellipsis:
                printer.print("…");
                break;
            case Emdash:
                printer.print("—");
                break;
            case Endash:
                printer.print("–");
                break;
            case HRule:
                printer.println().print("'''");
                break;
            case Linebreak:
                // look for length of span to detect hard line break (2 trailing spaces plus endline)
                // necessary because Pegdown doesn't distinguish between a hard line break and a normal line break
                if (source != null && source.substring(node.getStartIndex(), node.getEndIndex()).startsWith(HARD_LINE_BREAK_MARKDOWN)) {
                    printer.print(" +").println();
                }
                else {
                    // QUESTION should we fold or preserve soft line breaks? (pandoc emits a space here)
                    printer.println();
                }
                break;
            case Nbsp:
                printer.print("{nbsp}");
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public void visit(StrongEmphSuperNode node) {
        if (node.isClosed()) {
            if (node.isStrong()) {
                printNodeSurroundedBy(node, "*");
            } else {
                printNodeSurroundedBy(node, "_");
            }
        } else {
            //sequence was not closed, treat open chars as ordinary chars
            printer.print(node.getChars());
            visitChildren(node);
        }
    }

    public void visit(StrikeNode node) {
        printer.print("[line-through]").print('#');
        visitChildren(node);
        printer.print('#');
    }

    public void visit(TableBodyNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableCaptionNode node) {
        printer.println().print("<caption>");
        visitChildren(node);
        printer.print("</caption>");
    }

    public void visit(TableCellNode node) {
//        String tag = inTableHeader ? "th" : "td";
        List<TableColumnNode> columns = currentTableNode.getColumns();
        TableColumnNode column = columns.get(Math.min(currentTableColumn, columns.size() - 1));

        String pstr = printer.getString();
        if (pstr.length() > 0) {
            if (pstr.endsWith("\n") || pstr.endsWith(" ")) {
                printer.print("|");
            }
            else {
                printer.print(" |");
            }
        }
        else {
            printer.print("|");
        }
        column.accept(this);
        if (node.getColSpan() > 1) printer.print(" colspan=\"").print(Integer.toString(node.getColSpan())).print('"');
        visitChildren(node);

        currentTableColumn += node.getColSpan();
    }

    public void visit(TableColumnNode node) {
        // nothing here yet
    }

    public void visit(TableHeaderNode node) {
        inTableHeader = true;
//        printIndentedTag(node, "thead");

        visitChildren(node);

        inTableHeader = false;
    }

    private boolean ifColumnsHaveAlignmentSpecified(List<TableColumnNode> columns) {
        for (TableColumnNode column : columns) {
            if(column.getAlignment() != TableColumnNode.Alignment.None) {
                return true;
            }
        }
        return false;
    }

    private String getColumnAlignment(List<TableColumnNode> columns) {

        List<String> result = new ArrayList<String>();

        for (TableColumnNode column : columns) {
            switch (column.getAlignment()) {
                case None:
                case Left:
                    result.add("<");
                    break;
                case Right:
                    result.add(">");
                    break;
                case Center:
                    result.add("^");
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        return Joiner.join(result, ",");
    }


    public void visit(TableNode node) {
        currentTableNode = node;

        List<TableColumnNode> columns = node.getColumns();

        if(ifColumnsHaveAlignmentSpecified(columns)) {
            printer.print("[cols=\"");
            printer.print(getColumnAlignment(columns));
            printer.print("\"]");
            printer.println();
        }

        printer.print("|===");
        visitChildren(node);
        printer.println();
        printer.print("|===");
        printer.println();

        currentTableNode = null;
    }

    public void visit(TableRowNode node) {
        currentTableColumn = 0;

        printer.println();

        visitChildren(node);
//        printIndentedTag(node, "tr");

        if(inTableHeader) {
            printer.println();
        }
    }

    public void visit(VerbatimNode node) {
        printer.println();

        String type = node.getType();
        String text = node.getText();

        if (autoDetectLanguageType) {
            type = linguist.detectLanguage(text);
        }

        if (!type.isEmpty()) {
            printer.print("[source," + type + "]");
        }

        printer.println();
        repeat('-', 4);
        printer.println();
        printer.print(text);
        repeat('-', 4);
        printer.println().println();
    }

    public void visit(WikiLinkNode node) {
        printLink(linkRenderer.render(node));
    }

    public void visit(TextNode node) {
        if (abbreviations.isEmpty()) {
            printer.print(node.getText());
        } else {
            printWithAbbreviations(node.getText());
        }
    }

    public void visit(SpecialTextNode node) {
        printer.printEncoded(node.getText());
    }

    public void visit(SuperNode node) {
        visitChildren(node);
    }

    public void visit(Node node) {
        throw new RuntimeException("Don't know how to handle node " + node);
    }

    // helpers

    protected void visitChildren(AbstractNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }
    
    
    /**
     * Removes superfluous nodes from the tree.
     */
    protected void cleanAst(Node node) {
    	List<Node> children = node.getChildren();
    	for (int i = 0, len = children.size(); i < len; i++) {
    		Node c = children.get(i);
    		if (c instanceof RootNode) {
    			children.set(i, c.getChildren().get(0));
    		}
    		else if (c.getClass().equals(SuperNode.class) && c.getChildren().size() == 1) {
    			children.set(i, c.getChildren().get(0));
    		}
    		
    		cleanAst(c);
    	}
    }

    protected void printNodeSurroundedBy(AbstractNode node, String token) {
        printer.print(token);
        visitChildren(node);
        printer.print(token);
    }

    protected void printImageTag(LinkRenderer.Rendering rendering) {
        printer.print("image:");
        printer.print(rendering.href);
        printer.print('[');
        printTextWithQuotesIfNeeded(printer, rendering.text);
        printer.print(']');
    }
    
    protected void printImageTagWithLink(LinkRenderer.Rendering image, LinkRenderer.Rendering link) {
    	printer.print("image:").print(image.href).print('[');
    	if (image.text != null && !image.text.isEmpty()) {
    		printTextWithQuotesIfNeeded(printer, image.text);
	    	printer.print(',');
    	}

    	printer.print("link=").print(link.href).print(']');
    }

    protected void printLink(LinkRenderer.Rendering rendering) {
        String uri = rendering.href;
        String text = rendering.text;

        if (uri.startsWith("#")) {
            printer.print("<<").print(uri.substring(1)).print(',').print(text).print(">>");
        } else {
            if (!uri.contains("://")) {
                uri = "link:" + uri;
            }
            printer.print(uri);
            if (!uri.equals(text)) {
                printer.print('[');
                printTextWithQuotesIfNeeded(printer, rendering.text);
                printer.print(']');
            }
        }
    }

    protected String printChildrenToString(SuperNode node) {
        Printer priorPrinter = printer;
        printer = new Printer();
        visitChildren(node);
        String result = printer.getString();
        printer = priorPrinter;
        return result;
    }

    protected String normalize(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            switch (c) {
                case ' ':
                case '\n':
                case '\t':
                    continue;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    protected String normalizeWhitelines(String text) {
        // replace all double or more empty lines with single empty lines
        return text.replaceAll("(?m)^[ \t]*\r?\n{2,}", "\n").trim();
    }
    
    protected void printTextWithQuotesIfNeeded(Printer p, String text) {
    	if (text != null && !text.isEmpty()) {
    		if (text.contains(",")) {
    			p.print('"').print(text).print('"');
    		}
    		else {
    			p.print(text);
    		}
    	}
    }

    protected void printWithAbbreviations(String string) {
        Map<Integer, Map.Entry<String, String>> expansions = null;

        for (Map.Entry<String, String> entry : abbreviations.entrySet()) {
            // first check, whether we have a legal match
            String abbr = entry.getKey();

            int ix = 0;
            while (true) {
                int sx = string.indexOf(abbr, ix);
                if (sx == -1) break;

                // only allow whole word matches
                ix = sx + abbr.length();

                if (sx > 0 && Character.isLetterOrDigit(string.charAt(sx - 1))) continue;
                if (ix < string.length() && Character.isLetterOrDigit(string.charAt(ix))) {
                    continue;
                }

                // ok, legal match so save an expansions "task" for all matches
                if (expansions == null) {
                    expansions = new TreeMap<Integer, Map.Entry<String, String>>();
                }
                expansions.put(sx, entry);
            }
        }

        if (expansions != null) {
            int ix = 0;
            for (Map.Entry<Integer, Map.Entry<String, String>> entry : expansions.entrySet()) {
                int sx = entry.getKey();
                String abbr = entry.getValue().getKey();
                String expansion = entry.getValue().getValue();

                printer.printEncoded(string.substring(ix, sx));
                printer.print("<abbr");
                if (StringUtils.isNotEmpty(expansion)) {
                    printer.print(" title=\"");
                    printer.printEncoded(expansion);
                    printer.print('"');
                }
                printer.print('>');
                printer.printEncoded(abbr);
                printer.print("</abbr>");
                ix = sx + abbr.length();
            }
            printer.print(string.substring(ix));
        } else {
            printer.print(string);
        }
    }
 
    protected Node findParentNode(Node target, Node from) {
    	if (target.equals(rootNode)) {
    		return null;
    	}
    	
    	Node candidate;
    	
    	for (Node c : from.getChildren()) {   		
    		if (target.equals(c)) {
    			return from;
    		}
    		else if ((candidate = findParentNode(target, c)) != null) {
    			return candidate;
    		}
    	}
    	
    	return null;
    }
    
    protected boolean isFirstChild(Node parent, Node child) {
    	return child.equals(parent.getChildren().get(0));
    }
    
    protected boolean isListItemText(Node node) {
    	if (listLevel == 0) {
    		return false;
    	}
    	else {
    		Node parent = findParentNode(node, rootNode);
    		return (parent instanceof ListItemNode && isFirstChild(parent, node));
    	}
    }
}
