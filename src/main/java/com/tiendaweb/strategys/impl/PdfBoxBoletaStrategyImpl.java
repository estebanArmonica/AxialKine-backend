package com.tiendaweb.strategys.impl;

import com.tiendaweb.models.Producto;
import com.tiendaweb.models.transbank.TransbankConfirmation;

import com.tiendaweb.strategys.GeneradorBoletaStrategy;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import com.tiendaweb.models.transbank.Compra;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PdfBoxBoletaStrategyImpl implements GeneradorBoletaStrategy {

    @Override
    public ByteArrayOutputStream generar(Compra compra) {
        try(PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            TransbankConfirmation confirmation = new TransbankConfirmation();
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            Producto producto = compra.getItem().getProducto();


            // cargamos la imagen del logo
            File logo = new File("src/main/resources/static/images/axialkine-logo.jpg");
            if(!logo.exists()){
                throw new RuntimeException("El archivo de imagen no se encontró en la ruta especificada: " + logo.getAbsolutePath());
            }

            PDImageXObject logoFile = PDImageXObject.createFromFile(logo.getAbsolutePath(), document);
            contentStream.drawImage(logoFile, 50, 700, 50, 50);

            // Encabezado
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(120, 720);
            contentStream.showText("AXIAL KINE");
            contentStream.endText();

            // RUT empresa
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(120, 700);
            contentStream.showText("RUT: 12.345.678-9");
            contentStream.endText();

            // Línea separadora
            contentStream.moveTo(50, 690);
            contentStream.lineTo(500, 690);
            contentStream.stroke();

            // Título de la boleta
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(140, 530);
            contentStream.showText("BOLETA ELECTRÓNICA N° " + compra.getBuyOrder());
            contentStream.endText();

            // Dirección y Comuna
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 640);
            contentStream.showText("Dirección    : Edificio Cordillera - Balmaceda 371");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Comuna       : Puente Alto");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Fecha        : " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                                    "   Hora: "+
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(("HH:MM"))));
            contentStream.endText();

            // Línea separadora
            contentStream.moveTo(50, 550);
            contentStream.lineTo(500, 550);
            contentStream.stroke();

            // Detalles del producto
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 590);
            contentStream.showText("CÓDIGO PRD     DESCRIPCIÓN                    MONTO");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 570);
            contentStream.showText(producto.getCodigo() + "               " + producto.getNombre() + "               $" + producto.getPrecio());
            contentStream.endText();

            // Línea separadora
            contentStream.moveTo(50, 550);
            contentStream.lineTo(500, 550);
            contentStream.stroke();

            // Timbre electrónico
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(180, 530);
            contentStream.showText("TIMBRE ELECTRÓNICO SII");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 510);
            contentStream.showText("Res 140 año 2010. Verifique documento en");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("www.sii.cl");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("www.axialkine.com");
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.out.println("Datos guardados: " + outputStream);
            document.save(outputStream);
            return outputStream;
        } catch(IOException e) {
            throw new RuntimeException("Error al generar boleta PDF: " + e.getMessage());
        }
    }
}
