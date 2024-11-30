package com.saintolivetree.stripe_events_listener_service.service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(String templateName, Context context) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Generate HTML from Thymeleaf template
            String html = templateEngine.process(templateName, context);

            Document doc = Jsoup.parse(html);
            doc.outputSettings().syntax( Document.OutputSettings.Syntax.xml);
            // Render PDF using Flying Saucer
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(doc.html());
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();

           return outputStream.toByteArray();
            //return doc.html().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
