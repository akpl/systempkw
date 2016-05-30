package pkw;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Elleander on 27/05/2016.
 */
public class PDFGenerator {
    PDDocument document;
    PDPageContentStream contentStream;
    PDPage page;
    /**
     * Magic.
     */
    public PDFGenerator()
    {
        document= new PDDocument();
    }

    /**
     * Magic.
     * @throws IOException
     */
    public void addPage() {
        page = new PDPage();
        document.addPage(page);
        try {
            if(contentStream!=null)
            {
                contentStream.endText();
                contentStream.close();
                contentStream=null;}
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.moveTextPositionByAmount( 10, 770 );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a line of text to the PDF and move the cursor down.
     * @param line Line to add
     */
    public void addLine(String line)
    {
        PDFont font = PDType1Font.HELVETICA;
        try {
//            font = PDTrueTypeFont.loadTTF(document, "Arial.ttf");
            contentStream.setFont(font, 12);
            contentStream.drawString(line);
            contentStream.moveTextPositionByAmount(0, -12);
        }
        catch (IOException e) {
        e.printStackTrace();
    }
    }

    /**
     * Create a byte array containing the whole PDF document
     * @return Byte array with PDF contents
     */
    public byte[] returnByteArray() {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        try {
            if(contentStream!=null)
            {
                contentStream.endText();
                contentStream.close();
                contentStream=null;}

            document.save(ByteStream);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (COSVisitorException e) {
            e.printStackTrace();
        }
        byte[] ByteArray = ByteStream.toByteArray();
        return ByteArray;
    }

}
