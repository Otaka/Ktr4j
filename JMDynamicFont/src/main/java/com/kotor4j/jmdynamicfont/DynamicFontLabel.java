package com.kotor4j.jmdynamicfont;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.kotor4j.jmdynamicfont.Document.Line;
import com.kotor4j.jmdynamicfont.ShelfRectPacker.Rect;
import com.kotor4j.jmdynamicfont.exceptions.NotEnoughSpaceInField;
import gnu.trove.list.TCharList;
import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TCharArrayList;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * @author Dmitry
 */
public class DynamicFontLabel extends Node {

    private String text;
    private Alignment horizontalAlignment = Alignment.LEFT;
    private Alignment verticalAlignment = Alignment.TOP;
    private SimpleMesh tMesh;
    private DynamicFont font;
    private Geometry textGeometry;
    private Geometry backgroundGeometry;
    private boolean multiline = false;
    private int maxWidth;
    private int maxHeight;
    private Document document;

    public DynamicFontLabel(AssetManager assetManager, DynamicFont font) {
        this(assetManager, font, false, -1, -1);
    }

    public DynamicFontLabel(AssetManager assetManager, DynamicFont font, boolean multiline) {
        this(assetManager, font, multiline, -1, -1);
    }

    public DynamicFontLabel(AssetManager assetManager, DynamicFont font, boolean multiline, int maxWidth, int maxHeight) {
        this.multiline = multiline;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        setQueueBucket(RenderQueue.Bucket.Gui);
        setCullHint(CullHint.Never);
        Material tMaterial = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        tMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        tMaterial.setTexture("Texture", font.getTexture());
        tMesh = new SimpleMesh();
        textGeometry = new Geometry("DynamicFontLabelGeometry", tMesh);
        textGeometry.setLocalTranslation(0, 0, 0.001f);
        textGeometry.setMaterial(tMaterial);
        attachChild(textGeometry);
        this.font = font;
        setColor(ColorRGBA.White);

        backgroundGeometry = new Geometry();
        backgroundGeometry.setMesh(new Quad(1, 1));
        backgroundGeometry.setMaterial(new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md"));
    }

    public void showBackground(boolean visible) {
        if (visible) {
            if (backgroundGeometry.getParent() == null) {
                attachChild(backgroundGeometry);
            }
        } else if (backgroundGeometry.getParent() != null) {
            detachChild(backgroundGeometry);
        }
    }

    public void setBackgroundColor(ColorRGBA rgba) {
        showBackground(true);
        backgroundGeometry.getMaterial().setColor("Color", rgba);
    }

    public DynamicFontLabel setColor(ColorRGBA color) {
        textGeometry.getMaterial().setColor("Color", color);
        return this;
    }

    public void setText(String text) {
        if (!text.equals(this.text)) {
            this.text = text;
            document = null;
            recreateMesh();
        }
    }

    public boolean isMultiline() {
        return multiline;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    private Document getDocument() {
        if (document != null) {
            return document;
        }

        if (multiline == false) {
            document = createSinglelineDocument();
        } else {
            document = createMultilineDocument();
        }

        return document;
    }

    private Document createSinglelineDocument() {
        TCharList charsList = new TCharArrayList();
        TIntList widthsList = new TIntArrayList();
        Line line = new Line(charsList, widthsList);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        int currentLineWidth = 0;
        int elipsisWidth = font.getCharWidth('.') * 3;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int charWidth;
            switch (c) {
                case '\r': {
                    c = ' ';
                    charWidth = 0;
                    break;
                }
                case '\n': {
                    c = ' ';
                    charWidth = 0;
                    break;
                }
                case '\t': {
                    c = ' ';
                    int tabWidth = font.getCharWidth('0') * 4;
                    int tabFitsLength = (currentLineWidth / tabWidth) * tabWidth;
                    int difference = currentLineWidth - tabFitsLength;
                    charWidth = tabWidth - difference;
                    break;
                }
                default: {
                    charWidth = font.getCharWidth(c);
                    break;
                }
            }

            currentLineWidth += charWidth;
            if (maxWidth != -1 && (currentLineWidth + elipsisWidth > maxWidth)) {
                currentLineWidth -= charWidth;
                widthsList.add(font.getCharWidth('.'));
                widthsList.add(font.getCharWidth('.'));
                widthsList.add(font.getCharWidth('.'));
                currentLineWidth += elipsisWidth;
                charsList.add('.');
                charsList.add('.');
                charsList.add('.');
                break;
            }

            widthsList.add(charWidth);
            charsList.add(c);
        }

        line.lineWidth = currentLineWidth;
        if (maxWidth != -1) {
            switch (horizontalAlignment) {
                case LEFT:
                    line.lineOffset = 0;
                    break;
                case CENTER:
                    line.lineOffset = maxWidth / 2 - currentLineWidth / 2;
                    break;
                case RIGHT:
                    line.lineOffset = maxWidth - currentLineWidth;
                    break;
                default:
                    break;
            }
        }

        int verticalOffset = 0;
        if (maxHeight != -1) {
            int tMaxHeight = maxHeight;
            if (tMaxHeight < font.getFontHeightInPixels()) {
                tMaxHeight = font.getFontHeightInPixels();
            }

            switch (verticalAlignment) {
                case BOTTOM:
                    verticalOffset = tMaxHeight - font.getFontHeightInPixels();
                    break;
                case CENTER:
                    verticalOffset = tMaxHeight / 2 - font.getFontHeightInPixels() / 2;
                    break;
                case TOP:
                    verticalOffset = 0;
                    break;
                default:
                    break;
            }
        }

        Document doc = new Document(lines);
        doc.setTextVerticalOffset(verticalOffset);
        return doc;
    }

    private Document createMultilineDocument() {
        int fontHeight = font.getFontHeightInPixels();
        List<String> wordsList = parseText(text);
        TCharList charsList = new TCharArrayList();
        TIntList widthsList = new TIntArrayList();
        Line line = new Line(charsList, widthsList);

        List<Line> lines = new ArrayList<>();
        lines.add(line);
        int currentHeight = fontHeight;

        int heightLimit = maxHeight;
        if (heightLimit == -1) {
            heightLimit = Integer.MAX_VALUE;
        }
        int currentLineWidth = 0;
        OUTER:
        for (int i = 0; i < wordsList.size(); i++) {
            String word = wordsList.get(i);
            if (word.isEmpty()) {
                continue;
            }
            char firstChar = word.charAt(0);
            if (maxWidth == -1) {
                switch (firstChar) {
                    case '\n': {
                        line.lineWidth = currentLineWidth;
                        if ((currentHeight + fontHeight) > heightLimit) {
                            break;
                        }
                        currentHeight += fontHeight;

                        charsList = new TCharArrayList();
                        widthsList = new TIntArrayList();
                        line = new Line(charsList, widthsList);
                        currentLineWidth = 0;
                        lines.add(line);
                        break;
                    }
                    case '\t': {
                        int tabWidth = font.getCharWidth('0') * 4;
                        int tabFitsLength = (currentLineWidth / tabWidth) * tabWidth;
                        int difference = currentLineWidth - tabFitsLength;
                        int charWidth = tabWidth - difference;
                        widthsList.add(charWidth);
                        currentLineWidth += charWidth;
                        charsList.add(' ');
                        break;
                    }
                    default: {
                        //word
                        charsList.add(word.toCharArray());
                        int wordSize = word.length();
                        for (int j = 0; j < wordSize; j++) {
                            int charWidth = font.getCharWidth(word.charAt(j));
                            widthsList.add(charWidth);
                            currentLineWidth += charWidth;
                        }
                        break;
                    }
                }
            } else {//there is width limit
                switch (firstChar) {
                    case '\n': {
                        line.lineWidth = currentLineWidth;
                        if ((currentHeight + fontHeight) > heightLimit) {
                            break OUTER;
                        }
                        currentHeight += fontHeight;
                        charsList = new TCharArrayList();
                        widthsList = new TIntArrayList();
                        line = new Line(charsList, widthsList);
                        currentLineWidth = 0;
                        lines.add(line);
                        break;
                    }
                    case '\t': {
                        int tabWidth = font.getCharWidth('0') * 4;
                        int tabFitsLength = (currentLineWidth / tabWidth) * tabWidth;
                        int difference = currentLineWidth - tabFitsLength;
                        int charWidth = tabWidth - difference;
                        if (currentLineWidth + charWidth > maxWidth) {
                            //allocate new line(TODO:refactor it somehow, because of lot of repetitions here)
                            line.lineWidth = currentLineWidth;
                            if ((currentHeight + fontHeight) > heightLimit) {
                                break OUTER;
                            }
                            currentHeight += fontHeight;
                            charsList = new TCharArrayList();
                            widthsList = new TIntArrayList();
                            line = new Line(charsList, widthsList);
                            currentLineWidth = 0;
                            lines.add(line);
                        } else {
                            widthsList.add(charWidth);
                            currentLineWidth += charWidth;
                            charsList.add(' ');
                        }
                        break;
                    }
                    default: {
                        //it is word
                        int wordLengthInPixels = wordLength(word);
                        if (currentLineWidth + wordLengthInPixels > maxWidth) {
                            //allocate new line(TODO:refactor it somehow, because of lot of repetitions here)
                            line.lineWidth = currentLineWidth;
                            if ((currentHeight + fontHeight) > heightLimit) {
                                break OUTER;
                            }
                            currentHeight += fontHeight;
                            charsList = new TCharArrayList();
                            widthsList = new TIntArrayList();
                            line = new Line(charsList, widthsList);
                            currentLineWidth = 0;
                            lines.add(line);
                        }
                        int wordLength = word.length();
                        for (int j = 0; j < wordLength; j++) {
                            char c = word.charAt(j);
                            int charWidth = font.getCharWidth(c);
                            widthsList.add(charWidth);
                            currentLineWidth += charWidth;
                            charsList.add(c);
                        }
                        break;
                    }
                }
            }
        }

        line.lineWidth = currentLineWidth;
        removeTrailingSpacesInMultilineDocument(lines);

        //process alignment
        int foundMaximumWidth = 0;
        for (Line tLine : lines) {
            if (foundMaximumWidth < tLine.lineWidth) {
                foundMaximumWidth = tLine.lineWidth;
            }
        }

        switch (horizontalAlignment) {
            case LEFT:
                for (Line tLine : lines) {
                    tLine.lineOffset = 0;
                }
                break;
            case CENTER:
                for (Line tLine : lines) {
                    tLine.lineOffset = foundMaximumWidth / 2 - tLine.lineWidth / 2;
                }
                break;
            case RIGHT:
                for (Line tLine : lines) {
                    tLine.lineOffset = foundMaximumWidth - tLine.lineWidth;
                }
                break;
            default:
                throw new IllegalArgumentException("Horizontal alignment cannot have [" + horizontalAlignment + "] value");
        }

        int verticalOffset = 0;
        if (maxHeight != -1) {
            int maximumLinesHeight = lines.size() * fontHeight;

            switch (verticalAlignment) {
                case TOP: {
                    verticalOffset = 0;
                    break;
                }
                case CENTER: {
                    verticalOffset = maxHeight / 2 - maximumLinesHeight / 2;
                    break;
                }
                case BOTTOM: {
                    verticalOffset = maxHeight - maximumLinesHeight;
                    break;
                }
                default:
                    throw new IllegalArgumentException("Vertical alignment cannot have [" + verticalAlignment + "] value");
            }
        }

        Document doc = new Document(lines);
        doc.setMaximumWidth(foundMaximumWidth);
        doc.setTextVerticalOffset(verticalOffset);
        return doc;
    }

    private void removeTrailingSpacesInMultilineDocument(List<Line> lines) {
        for (Line line : lines) {
            for (int i = line.chars.size() - 1; i >= 0; i--) {
                if (!line.chars.isEmpty()) {
                    char firstSymbol = line.chars.get(i);
                    if (firstSymbol == ' ' || firstSymbol == '\t') {
                        line.chars.removeAt(i);
                        int charWidth = line.widths.removeAt(i);
                        line.lineWidth -= charWidth;

                    } else {
                        break;
                    }
                }
            }
        }
    }

    private int wordLength(String word) {
        int fullLength = 0;
        int wordSize = word.length();
        for (int i = 0; i < wordSize; i++) {
            fullLength += font.getCharWidth(word.charAt(i));
        }
        return fullLength;
    }

    /**
     * Parse text and automatically split long words that does not fit in width
     */
    private List<String> parseText(String text) {
        MutableInt tIndex = new MutableInt();
        MutableInt tokenWidth = new MutableInt();
        StringBuilder tempStringBuilder = new StringBuilder();
        List<String> resultList = new ArrayList<>();
        String token;
        int twospaceIntervalWidth = font.getCharWidth(' ');
        while ((token = Document.tokenize(text, tIndex.getValue(), tIndex, tokenWidth, font, tempStringBuilder)) != null) {
            if (maxWidth == -1) {
                resultList.add(token);
            } else {
                int wordLengthInPixels = wordLength(token);
                if (maxWidth < wordLengthInPixels) {
                    int length = token.length();
                    StringBuilder sb = new StringBuilder();
                    wordLengthInPixels = 0;
                    for (int i = 0; i < length; i++) {
                        char c = token.charAt(i);
                        int charWidth = font.getCharWidth(c);
                        if ((wordLengthInPixels + charWidth + twospaceIntervalWidth) > maxWidth) {
                            resultList.add(sb.toString());
                            sb.setLength(0);
                            wordLengthInPixels = 0;
                        }
                        wordLengthInPixels += charWidth;
                        sb.append(c);
                    }
                    if (sb.length() != 0) {
                        resultList.add(sb.toString());
                    }

                } else {
                    resultList.add(token);
                }

            }
        }
        return resultList;
    }

    private void recreateMesh() {
        Document tDocument = getDocument();
        try {
            for (Line line : tDocument.getLines()) {
                font.offerChars(line.chars.toArray());
            }
        } catch (NotEnoughSpaceInField ex) {
            throw new IllegalStateException("Not implemented yet");
        }

        TFloatList positionBuffer = new TFloatArrayList(100);//we have 4 vertices for one char
        TIntList indiciesBuffer = new TIntArrayList(100);
        TFloatList UVBuffer = new TFloatArrayList(100);

        int foundMaximumWidth = 0;
        int currentX = 0;
        int currentIndex = 0;
        int fontHeightInPixels = font.getFontHeightInPixels();

        int basicY = fontHeightInPixels + tDocument.getTextVerticalOffset();
        for (Line line : tDocument.getLines()) {
            int lineOffset = line.lineOffset;
            char[] charArray = line.chars.toArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                int charWidth = line.widths.get(i);
                if (!Character.isSpaceChar(c)) {
                    /*
                     * triangles layout for one char
                     *
                     * 0*   0****3
                     *  **    ***
                     *  ***    **
                     * 1****2  2*
                     */

                    positionBuffer.add(currentX + lineOffset);
                    positionBuffer.add(fontHeightInPixels - basicY);
                    positionBuffer.add(0);
                    //vertex 1
                    positionBuffer.add(currentX + lineOffset);
                    positionBuffer.add(0 - basicY);
                    positionBuffer.add(0);
                    //vertex 2
                    positionBuffer.add(currentX + charWidth + lineOffset);
                    positionBuffer.add(0 - basicY);
                    positionBuffer.add(0);
                    //vertex 3
                    positionBuffer.add(currentX + charWidth + lineOffset);
                    positionBuffer.add(fontHeightInPixels - basicY);
                    positionBuffer.add(0);

                    indiciesBuffer.add(currentIndex + 0);
                    indiciesBuffer.add(currentIndex + 1);
                    indiciesBuffer.add(currentIndex + 2);

                    indiciesBuffer.add(currentIndex + 0);
                    indiciesBuffer.add(currentIndex + 2);
                    indiciesBuffer.add(currentIndex + 3);
                    Rect rect = font.getCharRectangleInTexture(c);

                    calculateAndWriteUVPoint(rect.getX(), rect.getY(), font.getCurrentTextureSize(), UVBuffer);
                    calculateAndWriteUVPoint(rect.getX(), rect.getY2(), 512, UVBuffer);
                    calculateAndWriteUVPoint(rect.getX2(), rect.getY2(), 512, UVBuffer);
                    calculateAndWriteUVPoint(rect.getX2(), rect.getY(), 512, UVBuffer);
                    currentIndex += 4;
                } else if (c == '\t') {
                    charWidth = font.getCharWidth(' ') * 2;
                } else if (c == '\n' || c == '\r') {
                    charWidth = font.getCharWidth(' ');
                }

                currentX += charWidth;
            }
            basicY += fontHeightInPixels;
            if (currentX > foundMaximumWidth) {
                foundMaximumWidth = currentX;
            }
            currentX = 0;
        }

        tMesh.setVertexData(currentIndex, positionBuffer.toArray(), null, indiciesBuffer.toArray(), UVBuffer.toArray());

        int backgroundWidth = foundMaximumWidth;
        int backgroundHeight = fontHeightInPixels * document.getLines().size();
        if (maxWidth != -1) {
            backgroundWidth = maxWidth;
        }
        if (maxHeight != -1) {
            if (maxHeight > fontHeightInPixels) {
                backgroundHeight = maxHeight;
            }
        }
        backgroundGeometry.setLocalScale(backgroundWidth, backgroundHeight, 0);
        backgroundGeometry.setLocalTranslation(0, -backgroundHeight, 0);
    }

    private void calculateAndWriteUVPoint(int x, int y, int textureSize, TFloatList buffer) {
        float u = (float) x / (float) textureSize;
        float v = (float) (y) / (float) textureSize;
        buffer.add(u);
        buffer.add(v);
    }

    public Alignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public Alignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setAlignment(Alignment horizontalAlignment, Alignment verticalAlignment) {
        if (!(horizontalAlignment == Alignment.CENTER || horizontalAlignment == Alignment.LEFT || horizontalAlignment == Alignment.RIGHT)) {
            throw new IllegalArgumentException("Horizontal alignment cannot accept [" + horizontalAlignment + "]");
        }
        if (!(verticalAlignment == Alignment.CENTER || verticalAlignment == Alignment.TOP || verticalAlignment == Alignment.BOTTOM)) {
            throw new IllegalArgumentException("Vertical alignment cannot accept [" + verticalAlignment + "]");
        }
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        if (text != null) {
            recreateMesh();
        }
    }
}
