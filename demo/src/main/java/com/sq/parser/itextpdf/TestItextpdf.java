package com.sq.parser.itextpdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ContentOperator;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class TestItextpdf {

    public static void parsePdf(String pdf, String txt, String saveImagesDir) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        
        SqExtractStrategy parse = new SqExtractStrategy(saveImagesDir);
        HashMap<String, ContentOperator> listen_action = new HashMap<String, ContentOperator>();
        listen_action.put("m", parse); // listen Move action
        listen_action.put("l", parse); // listen LineTo action
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            SqExtractStrategy strategy = parser.processContent(i, parse, listen_action);
            String mytext = strategy.getResultantText();
            // System.out.println(mytext);
            out.println(mytext);
        }
        reader.close();
        out.flush();
        out.close();
    }

    public static void main(String[] args) {

        final String inputPdf = "config/sample_table.pdf";
        final String outputDir = "C:\\Users\\xunce\\Desktop\\pdf\\";
        final String saveText = outputDir + "result.txt";
        final String saveImagesDir = outputDir;
        try {
            parsePdf(inputPdf, saveText, saveImagesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
