package com.sq.pdf;

import java.util.ArrayList;

import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.parser.ContentOperator;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class SqExtractStrategy implements TextExtractionStrategy, ContentOperator {

    private Vector lastStart;
    private Vector lastEnd;

    /** used to store the resulting String. */
    private final StringBuffer result = new StringBuffer();;

    /**
     * Creates a new text extraction renderer.
     */
    public SqExtractStrategy() {
    }

    /**
     * @since 5.0.1
     */
    public void beginTextBlock() {
        System.out.println("XXX beginTextBlock");
    }

    /**
     * @since 5.0.1
     */
    public void endTextBlock() {

        System.out.println("=== endTextBlock");
    }

    /**
     * Returns the result so far.
     * 
     * @return a String with the resulting text.
     */
    public String getResultantText() {
        return result.toString();
    }


    protected final void appendTextChunk(CharSequence text) {
        result.append(text);
    }
    /**
     * Captures text using a simplified algorithm for inserting hard returns and spaces
     * 
     * @param renderInfo render info
     */
    public void renderText(TextRenderInfo renderInfo) {
        boolean firstRender = result.length() == 0;
        boolean hardReturn = false;

        LineSegment segment = renderInfo.getBaseline();
        Vector start = segment.getStartPoint();
        Vector end = segment.getEndPoint();

        if (!firstRender) {
            Vector x0 = start;
            Vector x1 = lastStart;
            Vector x2 = lastEnd;

            // see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
            float dist = (x2.subtract(x1)).cross((x1.subtract(x0))).lengthSquared() / x2.subtract(x1).lengthSquared();

            float sameLineThreshold = 1f; // we should probably base this on the current font metrics, but 1 pt seems to
                                          // be sufficient for the time being
            if (dist > sameLineThreshold)
                hardReturn = true;

            // Note: Technically, we should check both the start and end positions, in case the angle of the text
            // changed without any displacement
            // but this sort of thing probably doesn't happen much in reality, so we'll leave it alone for now
        }

        if (hardReturn) {
            System.out.println("<< Hard Return >>");
            appendTextChunk("\n");
        } else if (!firstRender) {
            if (result.charAt(result.length() - 1) != ' ' && renderInfo.getText().length() > 0
                    && renderInfo.getText().charAt(0) != ' ') { // we only insert a blank space if the trailing
                                                                // character of the previous string wasn't a space, and
                                                                // the leading character of the current string isn't a
                                                                // space
                float spacing = lastEnd.subtract(start).length();
                if (spacing > renderInfo.getSingleSpaceWidth() / 2f) {
                    appendTextChunk("$");
                    System.out.println("Inserting implied space before '" + renderInfo.getText() + "'");
                }
            }
        } else {
            System.out.println("Displaying first string of content '" + renderInfo.getText() + "' :: x1 = ");
        }

        String text = renderInfo.getText();
        System.out.println("lastStart == " + start + " end == " + end + ", text = " + text);
       // System.out.println(
        //        "[" + renderInfo.getStartPoint() + "]->[" + renderInfo.getEndPoint() + "] " + renderInfo.getText());

        appendTextChunk(text);


        lastStart = start;
        lastEnd = end;

    }

    /**
     * no-op method - this renderer isn't interested in image events
     * 
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     * @since 5.0.1
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        // do nothing - we aren't tracking images in this renderer
        System.out.println("Image");
    }

    @Override
    public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands)
            throws Exception {
        float x = ((PdfNumber) operands.get(0)).floatValue();
        float y = ((PdfNumber) operands.get(1)).floatValue();
        System.out.println("===>" + operator.toString() + ", " + operands.size() + " x = " + x + ", y == " + y);
    }

}
