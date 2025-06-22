package io.github.alexandradew.image_analyzer.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class OcrService {

    @Value("${tessdata.path}")
    private String tessdataPath;

    @Value("${tessdata.lang}")
    private String tessdataLang;

    private String extractTextFromImage(BufferedImage image) throws TesseractException {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdataPath);
        tesseract.setLanguage(tessdataLang);
        return tesseract.doOCR(image);
    }

    public String extractFromImage(File imageFile) throws IOException {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                throw new IOException("Could not read image file: " + imageFile.getName());
            }

            return extractTextFromImage(image);
        } catch (TesseractException e) {
            throw new RuntimeException("OCR failed: " + e.getMessage());
        }
    }

    public String extractFromPdf(File pdfFile) throws IOException {
        StringBuilder finalText = new StringBuilder();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                String pageText = extractTextFromImage(image);
                finalText.append("\n--- Page ").append(i + 1).append(" ---\n").append(pageText);
            }
        } catch (TesseractException e) {
            throw new RuntimeException("OCR failed: " + e.getMessage());
        }

        return finalText.toString();
    }

}
