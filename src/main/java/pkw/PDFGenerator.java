package pkw;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

/**
 * Created by Elleander on 27/05/2016.
 */
public class PDFGenerator {
    PDDocument document;
    PDPageContentStream contentStream;
    PDPage page;
    PDTrueTypeFont font;
    /**
     * Magic.
     */
    public PDFGenerator()
    {
        document= new PDDocument();
        try {
            font = PDTrueTypeFont.loadTTF(document, new File("src/main/resources/fonts/ARIALUNI.TTF"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
        contentStream.newLineAtOffset( 10, 770 );
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

        String purgedLine = Normalizer.normalize(line.replaceAll("ł", "l"), Normalizer.Form.NFD);
        purgedLine = purgedLine.replaceAll("[^\\p{ASCII}]", "");
        try {
            contentStream.setFont(font, 12);
            contentStream.showText(purgedLine);
            contentStream.newLineAtOffset(0, -12);
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
        }
        byte[] ByteArray = ByteStream.toByteArray();
        return ByteArray;
    }

}
