package com.carballeira.practica1.utils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReportGenerating {
    public void generateReport(Connection conn) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream("REPORTS/reporte_completo.pdf"));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título principal
            Paragraph mainTitle = new Paragraph("REPORTE COMPLETO DE USUARIOS Y LIBROS").setFontSize(18).setTextAlignment(TextAlignment.CENTER);
            document.add(mainTitle);
            document.add(new Paragraph("\n\n"));

            // Sección Usuarios
            document.add(new Paragraph("LISTADO DE USUARIOS").setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            Table userTable = new Table(4); // 4 columnas
            userTable.setWidth(UnitValue.createPercentValue(100));

            userTable.addCell(new Cell().add(new Paragraph("Nombre")).setTextAlignment(TextAlignment.CENTER));
            userTable.addCell(new Cell().add(new Paragraph("Email")).setTextAlignment(TextAlignment.CENTER));
            userTable.addCell(new Cell().add(new Paragraph("Teléfono")).setTextAlignment(TextAlignment.CENTER));
            userTable.addCell(new Cell().add(new Paragraph("Admin")).setTextAlignment(TextAlignment.CENTER));

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NOMBRE, EMAIL, TELEFONO, ADMIN FROM USUARIOS");

            while (rs.next()) {
                userTable.addCell(new Cell().add(new Paragraph(rs.getString("NOMBRE"))));
                userTable.addCell(new Cell().add(new Paragraph(rs.getString("EMAIL"))));
                userTable.addCell(new Cell().add(new Paragraph(rs.getString("TELEFONO"))));
                userTable.addCell(new Cell().add(new Paragraph(rs.getString("ADMIN"))));
            }

            document.add(userTable);
            document.add(new Paragraph("\n\n"));

            // Sección Libros
            document.add(new Paragraph("LISTADO DE LIBROS").setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            Table bookTable = new Table(5); // 5 columnas
            bookTable.setWidth(UnitValue.createPercentValue(100));

            bookTable.addCell(new Cell().add(new Paragraph("Título")).setTextAlignment(TextAlignment.CENTER));
            bookTable.addCell(new Cell().add(new Paragraph("Autor")).setTextAlignment(TextAlignment.CENTER));
            bookTable.addCell(new Cell().add(new Paragraph("Año")).setTextAlignment(TextAlignment.CENTER));
            bookTable.addCell(new Cell().add(new Paragraph("Disponible")).setTextAlignment(TextAlignment.CENTER));
            bookTable.addCell(new Cell().add(new Paragraph("Usuario Reservado")).setTextAlignment(TextAlignment.CENTER));

            rs = stmt.executeQuery("SELECT Titulo, Autor, AñoPublicacion, Disponible, EmailUsuarioReservado FROM LIBROS");

            while (rs.next()) {
                bookTable.addCell(new Cell().add(new Paragraph(rs.getString("Titulo"))));
                bookTable.addCell(new Cell().add(new Paragraph(rs.getString("Autor"))));
                bookTable.addCell(new Cell().add(new Paragraph(rs.getString("AñoPublicacion"))));
                bookTable.addCell(new Cell().add(new Paragraph(rs.getInt("Disponible") == 1 ? "Sí" : "No")));
                bookTable.addCell(new Cell().add(new Paragraph(rs.getString("EmailUsuarioReservado") != null ? rs.getString("EmailUsuarioReservado") : "N/A")));
            }

            document.add(bookTable);

            document.close();
            System.out.println("Reporte generado exitosamente en un solo archivo PDF.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateFilteredReport(Connection conn, String table, String field, String filterValue) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream("REPORTS/reporte_filtrado.pdf"));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título principal
            Paragraph mainTitle = new Paragraph("REPORTE FILTRADO DE " + table.toUpperCase()).setFontSize(18).setTextAlignment(TextAlignment.CENTER);
            document.add(mainTitle);
            document.add(new Paragraph("\n\n"));

            // Crear la tabla según la tabla seleccionada
            if (table.equals("Usuarios")) {
                document.add(new Paragraph("LISTADO DE USUARIOS FILTRADO").setFontSize(16).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n"));

                Table userTable = new Table(4); // 4 columnas
                userTable.setWidth(UnitValue.createPercentValue(100));

                userTable.addCell(new Cell().add(new Paragraph("Nombre")).setTextAlignment(TextAlignment.CENTER));
                userTable.addCell(new Cell().add(new Paragraph("Email")).setTextAlignment(TextAlignment.CENTER));
                userTable.addCell(new Cell().add(new Paragraph("Telefono")).setTextAlignment(TextAlignment.CENTER));
                userTable.addCell(new Cell().add(new Paragraph("Admin")).setTextAlignment(TextAlignment.CENTER));

                String query = "SELECT NOMBRE, EMAIL, TELEFONO, ADMIN FROM USUARIOS WHERE " + field + " LIKE '%" + filterValue + "%'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    userTable.addCell(new Cell().add(new Paragraph(rs.getString("NOMBRE"))));
                    userTable.addCell(new Cell().add(new Paragraph(rs.getString("EMAIL"))));
                    userTable.addCell(new Cell().add(new Paragraph(rs.getString("TELEFONO"))));
                    userTable.addCell(new Cell().add(new Paragraph(rs.getString("ADMIN"))));
                }

                document.add(userTable);
            } else if (table.equals("Libros")) {
                document.add(new Paragraph("LISTADO DE LIBROS FILTRADO").setFontSize(16).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n"));

                Table bookTable = new Table(5); // 5 columnas
                bookTable.setWidth(UnitValue.createPercentValue(100));

                bookTable.addCell(new Cell().add(new Paragraph("Titulo")).setTextAlignment(TextAlignment.CENTER));
                bookTable.addCell(new Cell().add(new Paragraph("Autor")).setTextAlignment(TextAlignment.CENTER));
                bookTable.addCell(new Cell().add(new Paragraph("Año")).setTextAlignment(TextAlignment.CENTER));
                bookTable.addCell(new Cell().add(new Paragraph("Disponible")).setTextAlignment(TextAlignment.CENTER));
                bookTable.addCell(new Cell().add(new Paragraph("Usuario Reservado")).setTextAlignment(TextAlignment.CENTER));

                String query = "SELECT Titulo, Autor, AñoPublicacion, Disponible, EmailUsuarioReservado FROM LIBROS WHERE " + field + " LIKE '%" + filterValue + "%'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    bookTable.addCell(new Cell().add(new Paragraph(rs.getString("Titulo"))));
                    bookTable.addCell(new Cell().add(new Paragraph(rs.getString("Autor"))));
                    bookTable.addCell(new Cell().add(new Paragraph(rs.getString("AñoPublicacion"))));
                    bookTable.addCell(new Cell().add(new Paragraph(rs.getInt("Disponible") == 1 ? "Sí" : "No")));
                    bookTable.addCell(new Cell().add(new Paragraph(rs.getString("EmailUsuarioReservado") != null ? rs.getString("EmailUsuarioReservado") : "N/A")));
                }

                document.add(bookTable);
            }

            document.close();
            System.out.println("Reporte filtrado generado exitosamente en un solo archivo PDF.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
