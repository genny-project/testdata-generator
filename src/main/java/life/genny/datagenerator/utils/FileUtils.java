package life.genny.datagenerator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import org.jboss.logging.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * A static utility class used for standard operations 
 * involving a file.
 * 
 * @author Amrizal Fajar
 */
public class FileUtils {

    private static final Logger LOG = Logger.getLogger(FileUtils.class);
    
    private static final String ABSOLUTE_PATH = Thread.currentThread().getContextClassLoader()
            .getResource("application.properties").getPath()
            .replace("target/classes/application.properties", "");
    public static final String SAMPLE_PATH = "sample-data/";
    public static final String TEXT_PARAGRAPH_REGEX = "(\\w{5,20}[ ]{1}){80,120}";

    /**
     * Generate random file with random characters
     * as it's content.
     * 
     * @return The generated file
     */
    public static FileInputStream generateFile() {
        return FileUtils.generateFile(UUID.randomUUID().toString());
    }

    /**
     * Generate random file with random characters
     * as it's content.
     * 
     * @param filename The file name
     * @return The generated file
     */
    public static FileInputStream generateFile(String filename) {
        try {
            File file = getRandomSampleFile();
            if (file != null)
                return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("Error Getting random file: " + e.getMessage());
            e.printStackTrace();
        }

        String path = ABSOLUTE_PATH + SAMPLE_PATH;
        createDirectory(path);
        File fileObj = new File(path + filename + ".pdf");

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileObj));
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            int totalParagraph = DataFakerUtils.randInt(2, 4);
            for (int i = 0; i < totalParagraph; i++)
                document.add(new Paragraph(DataFakerUtils.randStringFromRegex(TEXT_PARAGRAPH_REGEX), font));

            document.close();
            pdfWriter.close();

            return new FileInputStream(fileObj);
        } catch (DocumentException | FileNotFoundException e) {
            LOG.error("Error generating file: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * Private methods to import random file from specific path
     * 
     * @return The random file
     */
    private static File getRandomSampleFile() {
        String path = ABSOLUTE_PATH + SAMPLE_PATH;
        createDirectory(path);
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        return fileList.length > 10
                ? fileList[DataFakerUtils.randInt(fileList.length)]
                : null;
    }

    /**
     * Create directory if not exists
     * 
     * @param path The directory path
     */
    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
    }

}
