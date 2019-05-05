package com.sq.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ContentOperator;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ParsePDF {

    public static String pdf_path = "config/sample_table.pdf";
    public static String result_txt = "C:\\Users\\xunce\\Desktop\\result.txt";

    public static void parsePdf(String pdf, String txt) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        
        SqExtractStrategy parse = new SqExtractStrategy();
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
        try {
            parsePdf(pdf_path, result_txt);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
