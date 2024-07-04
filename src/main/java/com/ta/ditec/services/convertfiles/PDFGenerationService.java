//package com.ta.ditec.services.convertfiles;
//
//import java.awt.Color;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//import org.springframework.stereotype.Service;
//import org.vandeseer.easytable.TableDrawer;
//import org.vandeseer.easytable.settings.VerticalAlignment;
//import org.vandeseer.easytable.structure.Row;
//import org.vandeseer.easytable.structure.Table;
//import org.vandeseer.easytable.structure.cell.TextCell;
//
//@Service
//public class PDFGenerationService {
//
//	private static final float HEADER_TABLE_LOCATION_X = 35;
//	private static final float HEADER_TABLE_LOCATION_Y = 550;
//	private static final int MAX_ROWS_PER_PAGE = 5;
//
//	public byte[] generatePDF() throws IOException {
//		try (PDDocument document = new PDDocument()) {
//			List<List<String>> userRoles = createDummyData();
//			int totalRows = userRoles.size();
//
//			if (totalRows > 0) {
//				int pages = (int) Math.ceil(totalRows / (double) MAX_ROWS_PER_PAGE);
//
//				for (int i = 0; i < pages; i++) {
//					PDPage page = new PDPage();
//					document.addPage(page);
//					try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
//							PDPageContentStream.AppendMode.APPEND, true, true)) {
//						addHeader(document, page);
//						// Draw header table on each page
//						drawHeaderTable(contentStream, page.getMediaBox().getWidth(), 760);
//						drawFooterTable(contentStream, page.getMediaBox().getWidth(), 320);
//						addFooter(document, page);
//						addContentToPage(document, page, userRoles, i);
//					}
//				}
//			}
//
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			document.save(outputStream);
//			return outputStream.toByteArray();
//		}
//	}
//
//	private void addHeader(PDDocument document, PDPage page) throws IOException {
//		Path headerimagePath = Paths.get("src", "main", "webapp", "assets", "image", "logo_diatec.png");
//		Path imagePath = Paths.get("src", "main", "webapp", "assets", "image", "MicrosoftTeams-image.png");
//		File headerimage = headerimagePath.toFile();
//
//		try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
//				PDPageContentStream.AppendMode.APPEND, true, true)) {
//
//			int pageWidth = 612; // Example: If working with A4 size, the width is typically 612 units
//			float startX = pageWidth - 300; // Assuming the width of the header image is 300 units
//
//			// Draw header line
//			contentStream.setStrokingColor(Color.BLUE);
//			contentStream.setLineWidth(14);
//			contentStream.moveTo(0, 785); // Assuming the top border should be at Y-axis position 770
//			contentStream.lineTo(pageWidth, 785); // Draw line across the page width
//			contentStream.stroke();
//
//			// Header image
//			PDImageXObject headerImage = PDImageXObject.createFromFileByContent(headerimage, document);
//			contentStream.drawImage(headerImage, 60, 725, 300, 50); // Adjust coordinates and size as needed
//
//			drawHeaderTable(contentStream, 0, 0);
//
//			// Header text
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 45);
//			contentStream.newLineAtOffset(50, 670); // Position below the header image
//			contentStream.showText("INVOICE");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//			contentStream.newLineAtOffset(50, 645); // Position below the header image
//			contentStream.showText("Detiles");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 623); // Position below the header image
//			contentStream.showText("Customer Name");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 598); // Position below the header image
//			contentStream.showText("email");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 573); // Position below the header image
//			contentStream.showText("Phone");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 553); // Position below the header image
//			contentStream.showText("Address");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//			contentStream.newLineAtOffset(400, 640); // Position below the header image
//			contentStream.showText("Address");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA, 9);
//
//			// Set the position for the text
//			contentStream.newLineAtOffset(400, 620);
//			// Show the text
//			contentStream.showText("Directorate of Information Technology Electronics ");
//			contentStream.newLineAtOffset(0, -15);
//			contentStream.showText("Communication(DITEC)Behind CM Block Assam");
//			contentStream.newLineAtOffset(0, -15);
//			contentStream.showText("Secretariat,Dispur,Guwahati-781006, Assam");
//			contentStream.newLineAtOffset(0, -15);
//			contentStream.showText("");
//			// End the content stream
//			contentStream.endText();
//			// Close the content stream
//			contentStream.close();
//
//		}
//	}
//
//	private void addFooter(PDDocument document, PDPage page) throws IOException {
//
//		try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
//				PDPageContentStream.AppendMode.APPEND, true, true)) {
//
//			int pageWidth = 612; // Example: If working with A4 size, the width is typically 612 units
//			float startX = pageWidth - 300; // Assuming the width of the header image is 300 units
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//			contentStream.newLineAtOffset(50, 325); // Position below the header image
//			contentStream.showText("Payment Detiles");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 300); // Position below the header image
//			contentStream.showText("Account Name");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 275); // Position below the header image
//			contentStream.showText("Account Number");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 250); // Position below the header image
//			contentStream.showText("Bank Name");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 225); // Position below the header image
//			contentStream.showText("IFSC Code");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(50, 200); // Position below the header image
//			contentStream.showText("Amount in Words");
//			contentStream.endText();
//
//			contentStream.beginText();
//			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//			contentStream.newLineAtOffset(250, 60); // Position below the header image
//			contentStream.showText("Thank You For using e-KYC Service");
//			contentStream.endText();
//
//			contentStream.setStrokingColor(Color.BLUE);
//			contentStream.setLineWidth(14);
//			contentStream.moveTo(0, 5); // Assuming the footer border should be at Y-axis position 30
//			contentStream.lineTo(pageWidth, 5); // Draw line across the page width
//			contentStream.stroke();
//
//		}
//	}
//
//	private void addContentToPage(PDDocument document, PDPage page, List<List<String>> userRoles, int pageIndex)
//			throws IOException {
//		try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
//				PDPageContentStream.AppendMode.APPEND, true, true)) {
//
//			int start = pageIndex * MAX_ROWS_PER_PAGE;
//			int end = Math.min(start + MAX_ROWS_PER_PAGE, userRoles.size());
//
//			List<List<String>> pageUserRoles = userRoles.subList(start, end);
//
//			drawActiveCelUserRolesTable(contentStream, pageUserRoles);
//		}
//	}
//
//	Color backgroundColor = new Color(20, 189, 238); // RGB values for the color #14bdee
//
//	private void drawActiveCelUserRolesTable(PDPageContentStream contentStream, List<List<String>> userRoles)
//			throws IOException {
//		// Draw table header
//		Table activeUsersHeader = Table.builder().addColumnsOfWidth(80, 100, 130, 120, 100).padding(10)
//				.addRow(Row.builder()
//						.add(TextCell.builder().text("SL.no").verticalAlignment(VerticalAlignment.MIDDLE)
//								.textColor(Color.white).backgroundColor(backgroundColor).build())
//						.add(TextCell.builder().text("Service Name").textColor(Color.white)
//								.backgroundColor(backgroundColor).build())
//						.add(TextCell.builder().text("Each transaction").textColor(Color.white)
//								.backgroundColor(backgroundColor).build())
//						.add(TextCell.builder().text("Total Transaction").textColor(Color.white)
//								.backgroundColor(backgroundColor).build())
//						.add(TextCell.builder().text("Amount").build()).textColor(Color.white)
//						.backgroundColor(backgroundColor).build())
//				.build();
//
//		TableDrawer headerDrawer = TableDrawer.builder().contentStream(contentStream).startX(HEADER_TABLE_LOCATION_X)
//				.startY(HEADER_TABLE_LOCATION_Y - 60).table(activeUsersHeader).build();
//
//		headerDrawer.draw();
//
//		Table.TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(80, 100, 130, 120, 100);
//
//		for (List<String> userRole : userRoles) {
//			tableBuilder.addRow(Row.builder().add(TextCell.builder().text(userRole.get(0)).borderWidthLeft(1).borderWidthRight(1).build()) 
//					.add(TextCell.builder().text(userRole.get(1)).borderWidthRight(1).build())
//					.add(TextCell.builder().text(userRole.get(2)).borderWidthRight(1).build())
//					.add(TextCell.builder().text(userRole.get(3)).borderWidthRight(1).build())
//					.add(TextCell.builder().text(userRole.get(4)).build()) // No border for the last cell
//					.build());
//		}
//
//		Table activeUsersRows = tableBuilder.build();
//		TableDrawer rowDrawer = TableDrawer.builder().contentStream(contentStream).startX(HEADER_TABLE_LOCATION_X)
//				.startY(HEADER_TABLE_LOCATION_Y - 89).table(activeUsersRows).build();
//
//		rowDrawer.draw();
//	}
//
//	private List<List<String>> createDummyData() {
//		List<List<String>> userRoles = new ArrayList<>();
//		for (int i = 0; i < 13; i++) { // Creating 13 dummy rows for demonstration
//			List<String> userRole = new ArrayList<>();
//			userRole.add("1" + i);
//			userRole.add("AUA" + i);
//			userRole.add("0.5" + i);
//			userRole.add("450");
//			userRole.add("225");
//			userRoles.add(userRole);
//		}
//		return userRoles;
//	}
//
//	private void drawHeaderTable(PDPageContentStream contentStream, float pageWidth, float yPosition)
//			throws IOException {
//		// Calculate the X position to align the table beside the logo
//		float startX = 380; // Adjust as needed
//		// Define font size
//		int fontSize = 9;
//		// Draw table
//		Table headerTable = Table.builder().addColumnsOfWidth(100, 100) // Define column widths
//				.addRow(Row.builder().add(TextCell.builder().text("Date").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("10/06/2024").borderWidth(1).fontSize(fontSize).build()).build())
//				.addRow(Row.builder()
//						.add(TextCell.builder().text("Invoice Date").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("01/16/2024").borderWidth(1).fontSize(fontSize).build()).build())
//				.addRow(Row.builder()
//						.add(TextCell.builder().text("Invoice Number").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("#DITEC1234").borderWidth(1).fontSize(fontSize).build()).build())
//				.addRow(Row.builder().add(TextCell.builder().text("GSTN").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("GST1234").fontSize(fontSize).borderWidth(1).build()).build())
//				.build();
//		TableDrawer tableDrawer = TableDrawer.builder().contentStream(contentStream).startX(startX).startY(yPosition)
//				.table(headerTable).build();
//		tableDrawer.draw();
//	}
//
//	private void drawFooterTable(PDPageContentStream contentStream, float pageWidth, float yPosition)
//			throws IOException {
//		float startX = 380; // Adjust as needed
//		// Define font size
//		int fontSize = 9;
//		// Draw table
//		Table headerTable = Table.builder().addColumnsOfWidth(100, 100) // Define column widths
//				.addRow(Row.builder()
//						.add(TextCell.builder().text("Sub total").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("1000").borderWidth(1).fontSize(fontSize).build()).build())
//				.addRow(Row.builder().add(TextCell.builder().text("SGST-9%").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("90").borderWidth(1).fontSize(fontSize).build()).build())
//				.addRow(Row.builder().add(TextCell.builder().text("CGST-9%").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("90").borderWidth(1).fontSize(fontSize).build()).build())
//				.addRow(Row.builder().add(TextCell.builder().text("IGST-18%").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("").fontSize(fontSize).borderWidth(1).build()).build())
//				.addRow(Row.builder().add(TextCell.builder().text("Total").fontSize(fontSize).borderWidth(1).build())
//						.add(TextCell.builder().text("1180").fontSize(fontSize).borderWidth(1).build()).build())
//				.build();
//		TableDrawer tableDrawer = TableDrawer.builder().contentStream(contentStream).startX(startX).startY(yPosition)
//				.table(headerTable).build();
//		tableDrawer.draw();
//	}
//
//}

package com.ta.ditec.services.convertfiles;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.ta.ditec.services.response.DitecReportsServiceResponse;

@Service
public class PDFGenerationService {

	private static final float HEADER_TABLE_LOCATION_X = 35;
	private static final float HEADER_TABLE_LOCATION_Y = 550;
	private static final int MAX_ROWS_PER_PAGE = 1;

	public byte[] generatePDF() throws IOException {
		try (PDDocument document = new PDDocument()) {
			List<List<String>> userRoles = createDummyData();
			int totalRows = userRoles.size();

			if (totalRows > 0) {
				int pages = (int) Math.ceil(totalRows / (double) MAX_ROWS_PER_PAGE);

				for (int i = 0; i < pages; i++) {
					PDPage page = new PDPage();
					document.addPage(page);
					try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
							PDPageContentStream.AppendMode.APPEND, true, true)) {
						addHeader(document, page);
						// Draw header table on each page
						drawHeaderTable(contentStream, page.getMediaBox().getWidth(), 760);
//						drawFooterTable(contentStream, page.getMediaBox().getWidth(), 320);
						addFooter(document, page);
						addContentToPage(document, page, userRoles, i);
					}
				}
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			document.save(outputStream);
			return outputStream.toByteArray();
		}
	}

	private void addHeader(PDDocument document, PDPage page) throws IOException {
		Path headerimagePath = Paths.get("src", "main", "webapp", "assets", "image", "logo_diatec.png");
		Path imagePath = Paths.get("src", "main", "webapp", "assets", "image", "MicrosoftTeams-image.png");
		File headerimage = headerimagePath.toFile();

		try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
				PDPageContentStream.AppendMode.APPEND, true, true)) {

			int pageWidth = 612; // Example: If working with A4 size, the width is typically 612 units
			float startX = pageWidth - 300; // Assuming the width of the header image is 300 units

			// Draw header line
			contentStream.setStrokingColor(Color.BLUE);
			contentStream.setLineWidth(14);
			contentStream.moveTo(0, 785); // Assuming the top border should be at Y-axis position 770
			contentStream.lineTo(pageWidth, 785); // Draw line across the page width
			contentStream.stroke();

			// Header image
			PDImageXObject headerImage = PDImageXObject.createFromFileByContent(headerimage, document);
			contentStream.drawImage(headerImage, 60, 725, 300, 50); // Adjust coordinates and size as needed

			drawHeaderTable(contentStream, 0, 0);

			// Header text
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 45);
			contentStream.newLineAtOffset(50, 670); // Position below the header image
			contentStream.showText("INVOICE");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
			contentStream.newLineAtOffset(50, 645); // Position below the header image
			contentStream.showText("Details");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 623); // Position below the header image
			contentStream.showText("Customer Name  : ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 598); // Position below the header image
			contentStream.showText("Email : ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 573); // Position below the header image
			contentStream.showText("Phone : ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 553); // Position below the header image
			contentStream.showText("Address : ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
			contentStream.newLineAtOffset(300, 640); // Position below the header image
			contentStream.showText("Address");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 9);

			// Set the position for the text
			contentStream.newLineAtOffset(300, 620);
			// Show the text
			contentStream.showText("Directorate of Information Technology Electronics ");
			contentStream.newLineAtOffset(0, -15);
			contentStream.showText("Communication(DITEC)Behind CM Block Assam");
			contentStream.newLineAtOffset(0, -15);
			contentStream.showText("Secretariat,Dispur,Guwahati-781006, Assam");
			contentStream.newLineAtOffset(0, -15);
			contentStream.showText("");
			// End the content stream
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(300, 570); // Position below the header image
			contentStream.showText("Email : ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 9);
			contentStream.newLineAtOffset(340, 570); // Position below the header image
			contentStream.showText("ditec_support@assam.gov.in");
			contentStream.endText();

			// Close the content stream
			contentStream.close();

		}
	}

	private void addFooter(PDDocument document, PDPage page) throws IOException {

		try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
				PDPageContentStream.AppendMode.APPEND, true, true)) {

			int pageWidth = 612; // Example: If working with A4 size, the width is typically 612 units
			float startX = pageWidth - 300; // Assuming the width of the header image is 300 units

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
			contentStream.newLineAtOffset(50, 325); // Position below the header image
			contentStream.showText("Payment Detiles");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 300); // Position below the header image
			contentStream.showText("Account Name : DITEC");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 275); // Position below the header image
			contentStream.showText("Account Number : 12000124559000");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 250); // Position below the header image
			contentStream.showText("Bank Name : State Bank of India");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 225); // Position below the header image
			contentStream.showText("IFSC Code : SBI12345");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(50, 200); // Position below the header image
			contentStream.showText("Amount in Words : ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
			contentStream.newLineAtOffset(250, 60); // Position below the header image
			contentStream.showText("Thank You For using e-KYC Service");
			contentStream.endText();

			contentStream.setStrokingColor(Color.BLUE);
			contentStream.setLineWidth(14);
			contentStream.moveTo(0, 5); // Assuming the footer border should be at Y-axis position 30
			contentStream.lineTo(pageWidth, 5); // Draw line across the page width
			contentStream.stroke();

		}
	}

	private void addContentToPage(PDDocument document, PDPage page, List<List<String>> userRoles, int pageIndex)
			throws IOException {
		try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
				PDPageContentStream.AppendMode.APPEND, true, true)) {

			int start = pageIndex * MAX_ROWS_PER_PAGE;
			int end = Math.min(start + MAX_ROWS_PER_PAGE, userRoles.size());

			List<List<String>> pageUserRoles = userRoles.subList(start, end);

			float endX = 100f; // Example value for end X coordinate
			float endY = 500f; // Example value for end Y coordinate
			List<String> header = Arrays.asList("Column 1", "Column 2", "Column 3", "Column 1", "Column 1"); // Example
																												// header

//			drawActiveCelUserRolesTable(contentStream, pageUserRoles, endX, endY, document, header);

//			drawActiveCelUserRolesTable(contentStream, pageUserRoles, endX, endY, document);
		}
	}

	Color backgroundColor = new Color(20, 189, 238); // RGB values for the color #14bdee

	private List<List<String>> createDummyData() {
		List<List<String>> userRoles = new ArrayList<>();
		for (int i = 0; i < 13; i++) { // Creating 13 dummy rows for demonstration
			List<String> userRole = new ArrayList<>();
			userRole.add("1" + i);
			userRole.add("AUA" + i);
			userRole.add("0.5" + i);
			userRole.add("450");
			userRole.add("225");
			userRoles.add(userRole);
		}
		return userRoles;
	}

	private void drawHeaderTable(PDPageContentStream contentStream, float pageWidth, float yPosition)
			throws IOException {
		// Calculate the X position to align the table beside the logo
		float startX = 380; // Adjust as needed
		// Define font size
		int fontSize = 9;
		// Draw table
		Table headerTable = Table.builder().addColumnsOfWidth(100, 100) // Define column widths
				.addRow(Row.builder().add(TextCell.builder().text("Date").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("").borderWidth(1).fontSize(fontSize).build()).build())
				.addRow(Row.builder()
						.add(TextCell.builder().text("Invoice Date").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("").borderWidth(1).fontSize(fontSize).build()).build())
				.addRow(Row.builder()
						.add(TextCell.builder().text("Invoice Number").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("").borderWidth(1).fontSize(fontSize).build()).build())
				.addRow(Row.builder().add(TextCell.builder().text("GSTN").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("").fontSize(fontSize).borderWidth(1).build()).build())
				.build();
		TableDrawer tableDrawer = TableDrawer.builder().contentStream(contentStream).startX(startX).startY(yPosition)
				.table(headerTable).build();
		tableDrawer.draw();
	}

	private void drawFooterTable(PDPageContentStream contentStream, float pageWidth, float yPosition)
			throws IOException {
		float startX = 380; // Adjust as needed
		// Define font size
		int fontSize = 9;
		// Draw table
		Table headerTable = Table.builder().addColumnsOfWidth(100, 100) // Define column widths
				.addRow(Row.builder()
						.add(TextCell.builder().text("Sub total").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("1000").borderWidth(1).fontSize(fontSize).build()).build())
				.addRow(Row.builder().add(TextCell.builder().text("SGST-9%").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("90").borderWidth(1).fontSize(fontSize).build()).build())
				.addRow(Row.builder().add(TextCell.builder().text("CGST-9%").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("90").borderWidth(1).fontSize(fontSize).build()).build())
				.addRow(Row.builder().add(TextCell.builder().text("IGST-18%").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("").fontSize(fontSize).borderWidth(1).build()).build())
				.addRow(Row.builder().add(TextCell.builder().text("Total").fontSize(fontSize).borderWidth(1).build())
						.add(TextCell.builder().text("1180").fontSize(fontSize).borderWidth(1).build()).build())
				.build();
		TableDrawer tableDrawer = TableDrawer.builder().contentStream(contentStream).startX(startX).startY(yPosition)
				.table(headerTable).build();
		tableDrawer.draw();
	}

	private static final Color WHITE = Color.WHITE;
	private static final Color DARK_GRAY = new Color(64, 64, 64);
	private static final Color GRAY = new Color(128, 128, 128);
	private static final Color GRAY_LIGHT_2 = new Color(192, 192, 192);
	private static final String HELVETICA = "Helvetica";
//	private static final HorizontalAlignment CENTER = 1;

	private static final float MARGIN = 50f;

	public static void drawActiveCelUserRolesTable(PDPageContentStream contentStream, List<List<String>> userRoles,
			float x, float y, PDDocument document, List<String> header) throws IOException {

		final int rows = userRoles.size();
		final int cols = userRoles.get(0).size();
		final float rowHeight = 20f;
		final float tableWidth = document.getPage(0).getMediaBox().getWidth() - MARGIN - MARGIN;
		final float tableHeight = rowHeight * (rows + 1); // Header row included
		final float colWidth = tableWidth / (float) cols;
		final float cellMargin = 5f;

		// Draw top line
		contentStream.moveTo(MARGIN, y);
		contentStream.lineTo(MARGIN + tableWidth, y);
		contentStream.stroke();

		// Draw header
		contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
		float headerTextX = MARGIN + cellMargin;
		float headerTextY = y - 15;
		for (int i = 0; i < header.size(); i++) {
			String text = header.get(i);
			contentStream.beginText();
			contentStream.newLineAtOffset(headerTextX, headerTextY);
			contentStream.showText(text);
			contentStream.endText();
			headerTextX += colWidth;
		}

		// Draw the rows and row lines
		float nextY = y - rowHeight;
		// Draw top horizontal line for the table
		contentStream.moveTo(MARGIN, y);
		contentStream.lineTo(MARGIN + tableWidth, y);
		contentStream.stroke();

		for (int i = 0; i <= rows; i++) {
			// Draw horizontal line for the row
			contentStream.moveTo(MARGIN, nextY);
			contentStream.lineTo(MARGIN + tableWidth, nextY);
			contentStream.stroke();

			// Draw cells for the row
			float nextX = MARGIN + cellMargin;
			for (int j = 0; j < cols; j++) {
				String text = (i < rows) ? userRoles.get(i).get(j) : ""; // Text for cells, or empty string for last
																			// (empty) row
				contentStream.beginText();
				contentStream.newLineAtOffset(nextX, nextY - 15); // Adjust vertical position for text
				contentStream.showText(text);
				contentStream.endText();
				nextX += colWidth;
			}

			nextY -= rowHeight; // Move to the next row
		}

		// Draw bottom horizontal line for the table
		contentStream.moveTo(MARGIN, y - tableHeight);
		contentStream.lineTo(MARGIN + tableWidth, y - tableHeight);
		contentStream.stroke();

		// Draw the columns
		float nextX = MARGIN;
		for (int i = 0; i <= cols; i++) {
			contentStream.moveTo(nextX, y);
			contentStream.lineTo(nextX, y - tableHeight);
			contentStream.stroke();
			nextX += colWidth;
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
