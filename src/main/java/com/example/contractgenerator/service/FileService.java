package com.example.contractgenerator.service;


import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final String SOURCE_DIR = "C:/Users/nurid/Desktop/templates/contract.docx";

    public ResponseEntity<InputStreamResource> copyAndModifyPdf(Map<String, String> placeholders) throws IOException {

        try {
            // Fayl joylashuvi
            FileInputStream fis = new FileInputStream(new File(SOURCE_DIR));

            // Hujjatni yuklash
            try (XWPFDocument document = new XWPFDocument(fis)) {

                // Hujjatni o'zgartirish
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    replacePlaceholdersInParagraph(paragraph, placeholders);
                }

                for (XWPFTable table : document.getTables()) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                replacePlaceholdersInParagraph(paragraph, placeholders);
                            }
                        }
                    }
                }

                // Hujjatni qaytarish uchun oqim tayyorlash
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.write(outputStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(ContentDisposition.attachment().filename("processed.docx").build());

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new InputStreamResource(inputStream));
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    private void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        String text = paragraph.getText();
        if (text != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                text = text.replace(entry.getKey(), entry.getValue());
            }
            for (int pos = paragraph.getRuns().size() - 1; pos >= 0; pos--) {
                paragraph.removeRun(pos);
            }
            paragraph.createRun().setText(text);
        }
    }

}


