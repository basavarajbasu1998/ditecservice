package com.ta.ditec.services.convertfiles;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import com.ta.ditec.services.response.DitecReportsServiceResponse;

@Component
public class UserMasterDetailsCSV {

	public void writeStudentsToCsv(List<DitecReportsServiceResponse> students, OutputStream outputStream) {
		try (OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Sl no", "Sub AUA ID", "Date",
						"AuaOtp", "AuaBio", "auatotal", "kuaotp", "kuabio", "kuatotal"))) {
			for (DitecReportsServiceResponse student : students) {
				printer.printRecord(student.getSlNo(), student.getAgencyCode(), student.getDate(),
						student.getOtpYCountAua(), student.getBioYCountAua(), student.getBioYCountAua(),
						student.getEkycTotalCountAua(), student.getOtpYCountKua(), student.getBioYCountKua(),
						student.getEkycTotalCountKua());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] generatePdf(List<DitecReportsServiceResponse> transactions) throws IOException {
		try (PDDocument document = new PDDocument()) {
			int columnsPerPage = 5;
			List<String> headers = Arrays.asList("Sl.no", "SubAuaId", "Date", "AuaOtp", "AuaBio", "auatotal", "kuaotp",
					"kuabio", "kuatotal");

			int totalHeaders = headers.size();
			int totalPages = (int) Math.ceil((double) totalHeaders / columnsPerPage);

			for (int currentPage = 0; currentPage < totalPages; currentPage++) {
				PDPage page = new PDPage(PDRectangle.A4);
				document.addPage(page);

				try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
					drawTableHeader(contentStream, headers, currentPage, columnsPerPage);

					float margin = 50;
					float yStart = page.getMediaBox().getHeight() - margin - 20; // Start y position below header
					float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
					float columnWidth = tableWidth / columnsPerPage;
					float yPosition = yStart;
					float rowHeight = 20;

					for (DitecReportsServiceResponse transaction : transactions) {
						drawTableRow(contentStream, transaction, currentPage, columnsPerPage, yPosition, rowHeight,
								columnWidth);
						yPosition -= rowHeight;
					}
				}
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			document.save(outputStream);
			return outputStream.toByteArray();
		}
	}

	private void drawTableHeader(PDPageContentStream contentStream, List<String> headers, int currentPage,
			int columnsPerPage) throws IOException {
		float margin = 50;
		float yStart = PDRectangle.A4.getHeight() - margin;
		float tableWidth = PDRectangle.A4.getWidth() - 2 * margin;
		float columnWidth = tableWidth / columnsPerPage;
		float yPosition = yStart;
		float rowHeight = 20;

		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
		contentStream.setNonStrokingColor(Color.black);

		// Draw header background
		contentStream.fillRect(margin, yPosition, tableWidth, rowHeight);

		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setLineWidth(1f);

		// Draw header cells with borders
		for (int i = currentPage * columnsPerPage, j = 0; i < headers.size() && j < columnsPerPage; i++, j++) {
			String header = headers.get(i);
			contentStream.beginText();
			contentStream.newLineAtOffset(margin + j * columnWidth + (columnWidth / 2f - header.length() * 3f),
					yPosition + rowHeight / 3f);
			contentStream.showText(header);
			contentStream.endText();
			contentStream.moveTo(margin + (j + 1) * columnWidth, yPosition);
			contentStream.lineTo(margin + (j + 1) * columnWidth, yPosition - rowHeight);
			contentStream.stroke();
		}
	}

	private void drawTableRow(PDPageContentStream contentStream, DitecReportsServiceResponse transaction,
			int currentPage, int columnsPerPage, float yPosition, float rowHeight, float columnWidth)
			throws IOException {
		float margin = 50;

		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setLineWidth(1f);

		// Draw row cells with borders
		contentStream.moveTo(margin, yPosition);
		contentStream.lineTo(margin + columnsPerPage * columnWidth, yPosition);
		contentStream.stroke();

		List<String> headers = Arrays.asList("Sl.no", "SubAuaId", "Date", "AuaOtp", "AuaBio", "auatotal", "kuaotp",
				"kuabio", "kuatotal");

		for (int i = currentPage * columnsPerPage, j = 0; i < headers.size() && j < columnsPerPage; i++, j++) {
			String header = headers.get(i);
			String text = getFieldValue(transaction, header);
			contentStream.setNonStrokingColor(Color.BLACK);
			contentStream.beginText();
			contentStream.newLineAtOffset(margin + j * columnWidth + 2, yPosition - rowHeight + 5);
			contentStream.showText(text);
			contentStream.endText();
			contentStream.moveTo(margin + (j + 1) * columnWidth, yPosition);
			contentStream.lineTo(margin + (j + 1) * columnWidth, yPosition - rowHeight);
			contentStream.stroke();
		}
	}

	private String getFieldValue(DitecReportsServiceResponse transaction, String header) {
		switch (header) {
		case "Sl.no":
			return transaction.getSlNo().toString();
		case "SubAuaId":
			return transaction.getAgencyCode();
		case "Date":
			return transaction.getDate().toString();
		case "AuaOtp":
			return String.valueOf(transaction.getOtpYCountAua());
		case "AuaBio":
			return String.valueOf(transaction.getBioYCountAua());
		case "auatotal":
			return String.valueOf(transaction.getEkycTotalCountAua());
		case "kuaotp":
			return String.valueOf(transaction.getOtpYCountKua());
		case "kuabio":
			return String.valueOf(transaction.getBioYCountKua());
		case "kuatotal":
			return String.valueOf(transaction.getEkycTotalCountKua());
		default:
			return "";
		}
	}

}
