
package com.ta.ditec.services.convertfiles;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.ta.ditec.services.model.IntgrationInvoiceServiceCharges;
import com.ta.ditec.services.response.InvoiceResponse;
import com.ta.ditec.services.response.InvoiceServiceResponse;

public class InvoicePdf {
//	public static void editinvoice(InvoiceResponse req, HttpServletResponse response) throws IOException {
//		PDDocument document = new PDDocument();
//		int itemsPerPage = 10; // Adjust as needed
//		int totalPages = (int) Math.ceil((double) req.getIntgrationInvoiceServiceCharges().size() / itemsPerPage);
//		for (int currentPage = 0; currentPage < totalPages; currentPage++) {
//			PDPage page = new PDPage(PDRectangle.A4);
//			document.addPage(page);
//			PDPageContentStream contentStream = new PDPageContentStream(document, page);
//			float startY = 700;
//			Table table = createTable(req, currentPage, itemsPerPage);
//			RepeatedHeaderTableDrawer.builder().table(table).startX(60F).startY(600F).endY(100F).build()
//					.draw(() -> document, () -> page, 50f);
//
//			Table table2 = createTable(req, currentPage, itemsPerPage);
//			RepeatedHeaderTableDrawer.builder().table(table2).startX(60F).startY(600F).endY(100F).build()
//					.draw(() -> document, () -> page, 50f);
//			drawHeader(document, page, contentStream);
//			drawFooter(contentStream);
//			contentStream.close();
//		}
//		document.save("D://invoice/Enquiryform.pdf");
//		document.save(response.getOutputStream());
//		document.close();
//	}

//	public static void editinvoice(InvoiceResponse req, HttpServletResponse response) throws IOException {
//		PDDocument document = new PDDocument();
//		int itemsPerPage = 10; // Adjust as needed
//		int totalPages = (int) Math.ceil((double) req.getIntgrationInvoiceServiceCharges().size() / itemsPerPage);
//
//		for (int currentPage = 0; currentPage < totalPages; currentPage++) {
//			PDPage page = new PDPage(PDRectangle.A4);
//			document.addPage(page);
//			PDPageContentStream contentStream = new PDPageContentStream(document, page);
//			float startY = 700;
//
//			Table firstTable = createTable(req, currentPage, itemsPerPage);
//			RepeatedHeaderTableDrawer.builder().table(firstTable).startX(60F).startY(600F).endY(100F).build()
//					.draw(() -> document, () -> page, 50f);
//
//			// Create a second table (modify createSecondTable method to suit your needs)
//			Table secondTable = createFirstTable(req, currentPage, itemsPerPage);
//			RepeatedHeaderTableDrawer.builder().table(secondTable).startX(60F).startY(400F).endY(100F).build()
//					.draw(() -> document, () -> page, 50f);
//
//			drawHeader(document, page, contentStream);
//			drawFooter(contentStream);
//			contentStream.close();
//		}
//
//		document.save("D://invoice/Enquiryform.pdf");
//		document.close();
//	}

	public static void editinvoice(InvoiceResponse req, HttpServletResponse response) throws IOException {
		PDDocument document = new PDDocument();
		int itemsPerPage = 10; // Adjust as needed
		int totalPages = (int) Math.ceil((double) req.getIntgrationInvoiceServiceCharges().size() / itemsPerPage);

		for (int currentPage = 0; currentPage < totalPages; currentPage++) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			float startY = 700;

			Table firstTable = createTable(req, currentPage, itemsPerPage);
			float firstTableHeight = drawTable(firstTable, document, page, startY);

			if (firstTableHeight > 600) {
				// Create a new page for the remaining portion of the first table
				PDPage nextPage = new PDPage(PDRectangle.A4);
				document.addPage(nextPage);
				contentStream.close(); // Close the content stream of the current page
				contentStream = new PDPageContentStream(document, nextPage);
				startY = 700 - (firstTableHeight - 600); // Adjust startY for the remaining portion
				firstTable = createTable(req, currentPage, itemsPerPage); // Re-create the table for the new page
				drawTable(firstTable, document, nextPage, startY);
			}

			startY -= firstTableHeight + 20; // Adjust startY for the second table
			Table secondTable = createFirstTable(req, currentPage, itemsPerPage);
			drawTable(secondTable, document, page, startY);

			drawHeader(document, page, contentStream);
			drawFooter(contentStream);
			contentStream.close();
		}
		document.save(response.getOutputStream());
		document.save("D://invoice/Enquiryform.pdf");
		document.close();
	}

	// Other methods (createTable, createSecondTable, drawHeader, drawFooter) should
	// be implemented based on your requirements

	private static float drawTable(Table table, PDDocument document, PDPage page, float startY) throws IOException {
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		RepeatedHeaderTableDrawer.builder().table(table).startX(60F).startY(startY).endY(100F).build()
				.draw(() -> document, () -> page, 50f);
		contentStream.close();

		// Calculate and return the height of the drawn table
		return table.getHeight();
	}

	private static void drawHeader(PDDocument document, PDPage page, PDPageContentStream contentStream)
			throws IOException {
		Path imagePath = Paths.get("src", "main", "webapp", "assets", "image", "MicrosoftTeams-image.png");
		try (InputStream in = Files.newInputStream(imagePath)) {
			PDImageXObject image = PDImageXObject.createFromByteArray(document, Files.readAllBytes(imagePath), "image");
			float imageWidth = 50;
			float imageHeight = 55;
			contentStream.drawImage(image, 50, 790 - imageHeight, imageWidth, imageHeight);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
			contentStream.newLineAtOffset(150, 780);
			contentStream.setNonStrokingColor(100, 60, 0, 0);
			contentStream.showText("Directorate of Information Technology, Electronics ");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
			contentStream.newLineAtOffset(190, 761);
			// contentStream.setNonStrokingColor(100, 60, 0, 0);
			contentStream.setNonStrokingColor(239, 246, 255, 1);
			contentStream.showText("and  Communication (DITEC) Assam");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
			contentStream.newLineAtOffset(190, 700);
			contentStream.showText("and  Communication (DITEC) Assam");
			contentStream.endText();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Table createTable(InvoiceResponse invoiceResponse, int currentPage, int itemsPerPage) {
		List<IntgrationInvoiceServiceCharges> items = invoiceResponse.getIntgrationInvoiceServiceCharges();
		int startIndex = currentPage * itemsPerPage;
		int endIndex = Math.min((currentPage + 1) * itemsPerPage, items.size());
		List<IntgrationInvoiceServiceCharges> currentPageItems = items.subList(startIndex, endIndex);
		Table.TableBuilder tableBuilder = Table.builder().addColumnOfWidth(40).addColumnOfWidth(90).addColumnOfWidth(70)
				.addColumnOfWidth(70).addColumnOfWidth(80).addColumnOfWidth(80);
		addHeaders(tableBuilder);
		int startingSerialNumber = currentPage * itemsPerPage + 1;
		double totalAmount = 0;

		for (int i = 0; i < currentPageItems.size(); i++) {
			IntgrationInvoiceServiceCharges currentItem = currentPageItems.get(i);
			addRow(tableBuilder, startingSerialNumber + i, currentItem);
			String currentItemAmountStr = String.valueOf(currentItem.getAmount());
			double currentItemAmount = 0;

			try {
				currentItemAmount = Double.parseDouble(currentItemAmountStr);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			totalAmount += currentItemAmount;
		}
		addTotalRow(tableBuilder, totalAmount);

		return tableBuilder.build();
	}

	public static Table createFirstTable(InvoiceResponse invoiceResponse, int currentPage, int itemsPerPage) {
		List<IntgrationInvoiceServiceCharges> items = invoiceResponse.getIntgrationInvoiceServiceCharges();
		int startIndex = currentPage * itemsPerPage;
		int endIndex = Math.min((currentPage + 1) * itemsPerPage, items.size());
		List<IntgrationInvoiceServiceCharges> currentPageItems = items.subList(startIndex, endIndex);
		Table.TableBuilder tableBuilder = Table.builder().addColumnOfWidth(40).addColumnOfWidth(90).addColumnOfWidth(70)
				.addColumnOfWidth(70).addColumnOfWidth(80).addColumnOfWidth(80);
		addHeaders(tableBuilder);
		int startingSerialNumber = currentPage * itemsPerPage + 1;
		double totalAmount = 0;

		for (int i = 0; i < currentPageItems.size(); i++) {
			IntgrationInvoiceServiceCharges currentItem = currentPageItems.get(i);
			addRow(tableBuilder, startingSerialNumber + i, currentItem);
			String currentItemAmountStr = String.valueOf(currentItem.getAmount());
			double currentItemAmount = 0;

			try {
				currentItemAmount = Double.parseDouble(currentItemAmountStr);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			totalAmount += currentItemAmount;
		}
		addTotalRow(tableBuilder, totalAmount);

		return tableBuilder.build();
	}

	private static void addTotalRow(Table.TableBuilder builder, double totalAmount) {
		String totalLabel = "Total: " + String.valueOf(totalAmount);
		Row.RowBuilder rowBuilder = Row.builder();
		for (int i = 0; i < 5; i++) {
			rowBuilder.add(TextCell.builder().text("").borderWidth(1).fontSize(10).build());
		}
		rowBuilder.add(TextCell.builder().text(totalLabel).borderWidth(1).fontSize(10).build());
		if (rowBuilder.build().getCells().size() != 6) {
			System.err.println("Number of cells does not match with the column count");
			return;
		}
		builder.addRow(rowBuilder.build());
	}

	private static void addRow(Table.TableBuilder builder, int serialNumber, IntgrationInvoiceServiceCharges item) {
		String mainService = item.getMainService() != null ? item.getMainService() : "";
		String subService = item.getSubService() != null ? item.getSubService() : "";
		String hsnCode = item.getSubAuaId() != null ? item.getSubAuaId() : "";
		String billingCycle = item.getSubAuaId() != null ? item.getSubAuaId() : "";
		String amount = item.getAmount() != null ? String.valueOf(item.getAmount()) : "";
		builder.addRow(Row.builder()
				.add(TextCell.builder().text(String.valueOf(serialNumber)).borderWidth(1)
						.verticalAlignment(VerticalAlignment.MIDDLE).padding(5).fontSize(10).build()) // Serial
				.add(TextCell.builder().text(mainService).borderWidth(1).verticalAlignment(VerticalAlignment.MIDDLE)
						.padding(5).fontSize(10).build())
				.add(TextCell.builder().text(subService).borderWidth(1).verticalAlignment(VerticalAlignment.MIDDLE)
						.padding(5).fontSize(10).build())
				.add(TextCell.builder().text(hsnCode).borderWidth(1).verticalAlignment(VerticalAlignment.MIDDLE)
						.padding(5).fontSize(10).build())
				.add(TextCell.builder().text(billingCycle).borderWidth(1).verticalAlignment(VerticalAlignment.MIDDLE)
						.padding(5).fontSize(10).build())
				.add(TextCell.builder().text(amount).borderWidth(1).verticalAlignment(VerticalAlignment.MIDDLE)
						.padding(5).fontSize(10).build())
				.build());
	}

	private static void addHeaders(Table.TableBuilder builder) {
		float headerCellHeight = 45;
		builder.addRow(Row.builder()
				.add(TextCell.builder().text("SL.No").backgroundColor(Color.BLUE).textColor(Color.WHITE).borderWidth(1)
						.minHeight(headerCellHeight).verticalAlignment(VerticalAlignment.MIDDLE).build())
				.add(TextCell.builder().text("MainService").backgroundColor(Color.BLUE).textColor(Color.WHITE)
						.borderWidth(1).minHeight(headerCellHeight).verticalAlignment(VerticalAlignment.MIDDLE).build())
				.add(TextCell.builder().text("Subservice").backgroundColor(Color.BLUE).textColor(Color.WHITE)
						.borderWidth(1).minHeight(headerCellHeight).verticalAlignment(VerticalAlignment.MIDDLE).build())
				.add(TextCell.builder().text("HSNCode").backgroundColor(Color.BLUE).textColor(Color.WHITE)
						.borderWidth(1).minHeight(headerCellHeight).verticalAlignment(VerticalAlignment.MIDDLE).build())
				.add(TextCell.builder().text("BillingCycle").backgroundColor(Color.BLUE).textColor(Color.WHITE)
						.borderWidth(1).minHeight(headerCellHeight).verticalAlignment(VerticalAlignment.MIDDLE).build())
				.add(TextCell.builder().text("Amount").backgroundColor(Color.BLUE).textColor(Color.WHITE).borderWidth(1)
						.minHeight(headerCellHeight).verticalAlignment(VerticalAlignment.MIDDLE).build())
				.build());
	}

	private static void drawFooter(PDPageContentStream contentStream) throws IOException {
		contentStream.beginText();
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
		contentStream.newLineAtOffset(50, 50);
		contentStream.showText("Footer Content");
		contentStream.endText();
	}

//	public static void eachserviceinvoice(InvoiceServiceResponse req, HttpServletResponse response) throws IOException {
//		try (PDDocument document = PDDocument.load(new File("D:\\invoice\\Invoice D2.pdf"))) {
//			PDPage page = document.getPage(0); // Access the first page, change the index if needed
//
//			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
//					PDPageContentStream.AppendMode.APPEND, true)) {
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 670);
//				contentStream.showText(req.getInvoiceNumber());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 655);
//				contentStream.showText(req.getInvoiceDate());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 640);
//				contentStream.showText(req.getGstNumber());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 520);
//				contentStream.showText(req.getInvoiceDate());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(150, 527);
//				contentStream.showText(req.getManagementName());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 485);
//				contentStream.showText(req.getGstNumber());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(120, 505);
//				contentStream.showText(req.getManagementEmail());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(120, 487);
//				contentStream.showText(req.getManagementMobilenumber());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(120, 470);
//				contentStream.showText(req.getAddress());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(90, 387);
//				contentStream.showText("1");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(90, 373);
//				contentStream.showText("2");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(220, 387);
//				contentStream.showText("Authaction");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(220, 373);
//				contentStream.showText("e-Kyc");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(330, 387);
//				contentStream.showText("GE12345");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(330, 373);
//				contentStream.showText("GE12345");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(380, 387);
//				contentStream.showText(req.getBillingcycle());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(380, 373);
//				contentStream.showText(req.getBillingcycle());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 387);
////				contentStream.showText(String.valueOf(req.getAua()));
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 373);
////				contentStream.showText(String.valueOf(req.getKua()));
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(145, 286);
//				contentStream.showText("DITEC ASSAM");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(145, 259);
//				contentStream.showText("SBI0011012456");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(130, 230);
//				contentStream.showText("State Bank Of India");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(130, 217);
//				contentStream.showText("SBI000159");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 314);
//				contentStream.showText(String.valueOf(req.getSubtotal()));
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 300);
//				contentStream.showText(String.valueOf(req.getSgst()));
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 289);
//				contentStream.showText(String.valueOf(req.getCgst()));
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 274);
//				contentStream.showText("--");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 252);
//				contentStream.showText("--");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(470, 232);
//				contentStream.showText(String.valueOf(req.getTotal()));
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(165, 188);
//				contentStream.showText(req.getAmountword());
//				contentStream.endText();
//
//			}
//
//			// Save the modified document and send it as response
////			document.save(response.getOutputStream());
////			response.setContentType("application/force-download");
////			String filename = "D://invoice/" + req.getManagementName() + " invoice" + ".pdf";
////			document.save(filename);
//
//			document.save(response.getOutputStream());
//
////			
//		}
//	}

	public static void eachserviceinvoice(InvoiceServiceResponse req, HttpServletResponse response) throws IOException {
		try (PDDocument document = PDDocument.load(new File("D:\\invoice\\Invoice D3.pdf"))) {
			PDPage page = document.getPage(0); // Access the first page, change the index if needed

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
					PDPageContentStream.AppendMode.APPEND, true)) {

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA, 8);
//				contentStream.newLineAtOffset(130, 671);
//				contentStream.showText(req.getInvoiceNumber());
//				contentStream.endText();
//				
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA, 8);
//				contentStream.newLineAtOffset(490, 671);
//				contentStream.showText(req.getInvoiceDate());
//				contentStream.endText();
//				
//				
//				
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA, 8);
//				contentStream.newLineAtOffset(140, 651);
//				contentStream.showText(req.getOrganisationName());
//				contentStream.endText();
				
				

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 655);
//				contentStream.showText(req.getInvoiceDate());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 640);
//				contentStream.showText(req.getGstNumber());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 523);
//				contentStream.showText(req.getInvoiceDate());
//				contentStream.endText();

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(150, 530);
//				contentStream.showText(req.getManagementName());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(420, 485);
//				contentStream.showText(req.getGstNumber());
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(120, 504);
//				contentStream.showText(req.getManagementEmail());
//				contentStream.endText();

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(120, 487);
//				contentStream.showText(req.getManagementMobilenumber());
//				contentStream.endText();

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//				contentStream.newLineAtOffset(120, 472);
//				contentStream.showText(req.getAddress());
//				contentStream.endText();

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(90, 385);
//				contentStream.showText("1");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(90, 371);
//				contentStream.showText("2");
//				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
				contentStream.newLineAtOffset(120, 471);
				contentStream.showText("Authaction");
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
				contentStream.newLineAtOffset(120, 430);
				contentStream.showText("e-Kyc");
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(330, 385);
				contentStream.showText(String.valueOf(req.getAuthTrns()));
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(330, 370);
				contentStream.showText(String.valueOf(req.getEkycTrns()));
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(380, 385);
				if (req.getBillingcycle() != null) {
					contentStream.showText(req.getBillingcycle());
				} else {
					contentStream.showText("--");
				}
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(380, 370);
				if (req.getBillingcycle() != null) {
					contentStream.showText(req.getBillingcycle());
				} else {
					contentStream.showText("--");
				}
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(470, 385);
				if (req.getAua() != null) {
					contentStream.showText(String.valueOf(req.getAua()));
				} else {
					contentStream.showText("--");
				}
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(470, 370);
				if (req.getKua() != null) {
					contentStream.showText(String.valueOf(req.getKua()));
				} else {
					contentStream.showText("--");
				}
				contentStream.endText();

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(145, 283);
//				contentStream.showText("DITEC ASSAM");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(145, 256);
//				contentStream.showText("SBI0011012456");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(130, 229);
//				contentStream.showText("State Bank Of India");
//				contentStream.endText();
//
//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
//				contentStream.newLineAtOffset(130, 213);
//				contentStream.showText("SBI000159");
//				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(470, 312);
				if (req.getSubtotal() != null) {
					contentStream.showText(String.valueOf(req.getSubtotal()));
				} else {
					contentStream.showText("--");
				}
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
				contentStream.newLineAtOffset(470, 298);
				if (req.getSgst() != null) {
					contentStream.showText(String.valueOf(req.getSgst()));
				}
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA, 8);
				contentStream.newLineAtOffset(470, 285);
				if (req != null) {
					contentStream.showText(String.valueOf(req.getCgst()));
				}
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA, 8);
				contentStream.newLineAtOffset(470, 272);
				contentStream.showText("--");
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA, 8);
				contentStream.newLineAtOffset(470, 252);
				contentStream.showText("--");
				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA, 8);
				contentStream.newLineAtOffset(470, 229);
				if (req.getTotal() != null) {
					contentStream.showText(String.valueOf(req.getTotal()));
				} else {
					contentStream.showText("--");
				}
				contentStream.endText();

//				contentStream.beginText();
//				contentStream.setFont(PDType1Font.HELVETICA, 8);
//				contentStream.newLineAtOffset(165, 186);
//				contentStream.showText(req.getAmountword());
//				contentStream.endText();
			}

			// Save the modified document and send it as response
			document.save(response.getOutputStream());
		}
	}

}
