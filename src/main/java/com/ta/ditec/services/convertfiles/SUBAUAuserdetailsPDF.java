package com.ta.ditec.services.convertfiles;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.InvoiceRequest;
import com.ta.ditec.services.utils.ConstantFilePath;

public class SUBAUAuserdetailsPDF {

//	private static final String UPLOAD_DIR = "/apps/SUBAUA/SUBAUA.pdf";

	public void subAUAPDF(List<SubAuaUser> subAUAuserdetails, HttpServletResponse response) throws IOException {

		PDDocument document = new PDDocument();

		// Create a new blank page and add it to the document
		PDPage page = new PDPage();
		document.addPage(page);
		PDFont font = PDType1Font.HELVETICA_BOLD;
		PDFont font2 = PDType1Font.HELVETICA;
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		contentStream.beginText();
		contentStream.setFont(font, 18);
		contentStream.moveTextPositionByAmount(80, 700);
		contentStream.drawString("DITEC - Sub-AUA Agency - Onboarding Enquiry Form ");

		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 668);
		contentStream.drawString("Organization Name");

		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 648);
		contentStream.drawString("Application Name");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 628);
		contentStream.drawString("Managerial Point of Contact Name");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 608);
		contentStream.drawString("Managerial Point of Contact E-mail");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 11);
		contentStream.moveTextPositionByAmount(110, 588);
		contentStream.drawString("Managerial Point of Contact No");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 568);
		contentStream.drawString("Technical Point of Contact Name");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 548);
		contentStream.drawString("Technical Point of Contact No");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 528);
		contentStream.drawString("Technical point of Contact E-mail");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 508);
		contentStream.drawString("Address");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 488);
		contentStream.drawString("Authentication Purpose");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 468);
		contentStream.drawString("KYC purpose");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 448);
		contentStream.drawString("Integration Approach");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 428);
		contentStream.drawString("Mode Of Authentication");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 408);
		contentStream.drawString("Mode Of ekyc transactions");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 388);
		contentStream.drawString("Application Environment");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 368);
		contentStream.drawString("RD Devices");
		contentStream.endText();

		contentStream.beginText();
		contentStream.setFont(font2, 12);
		contentStream.moveTextPositionByAmount(110, 348);
		contentStream.drawString("Other Services");
		contentStream.endText();

		contentStream.stroke();
		contentStream.drawLine(100, 680, 500, 680);

		contentStream.stroke();
		contentStream.drawLine(100, 660, 500, 660);
		contentStream.stroke();
		contentStream.drawLine(100, 640, 500, 640);
		contentStream.stroke();
		contentStream.drawLine(100, 620, 500, 620);
		contentStream.stroke();
		contentStream.drawLine(100, 600, 500, 600);
		contentStream.stroke();
		contentStream.drawLine(100, 580, 500, 580);
		contentStream.stroke();
		contentStream.drawLine(100, 560, 500, 560);
		contentStream.stroke();
		contentStream.drawLine(100, 540, 500, 540);
		contentStream.stroke();
		contentStream.drawLine(100, 520, 500, 520);
		contentStream.stroke();
		contentStream.drawLine(100, 500, 500, 500);
		contentStream.stroke();
		contentStream.drawLine(100, 480, 500, 480);
		contentStream.stroke();
		contentStream.drawLine(100, 460, 500, 460);
		contentStream.stroke();
		contentStream.drawLine(100, 440, 500, 440);
		contentStream.stroke();
		contentStream.drawLine(100, 420, 500, 420);
		contentStream.stroke();
		contentStream.drawLine(100, 400, 500, 400);
		contentStream.stroke();
		contentStream.drawLine(100, 380, 500, 380);
		contentStream.stroke();
		contentStream.drawLine(100, 360, 500, 360);
		contentStream.stroke();
		contentStream.drawLine(100, 340, 500, 340);

		contentStream.stroke();
		contentStream.drawLine(100, 680, 100, 340);
		contentStream.stroke();
		contentStream.drawLine(300, 680, 300, 340);
		contentStream.stroke();
		contentStream.drawLine(500, 680, 500, 340);

		contentStream.beginText();
		contentStream.setFont(font2, 10);
		contentStream.moveTextPositionByAmount(450, 288);
		contentStream.drawString("Authorized Signatory");
		contentStream.endText();

		contentStream.close();
		document.save("D://Enquiryform.pdf");
		document.save(response.getOutputStream());
		document.close();
	}

	public void editinvoice(InvoiceRequest req) throws IOException {

		File file = new File("D:/INVOICE.pdf");

		PDDocument document = PDDocument.load(file);

		PDPage page = document.getPage(0);

		document.addPage(page);

		PDPageContentStream contentStream = new PDPageContentStream(document, page,

				PDPageContentStream.AppendMode.APPEND, true);

		PDFont font2 = PDType1Font.HELVETICA;

		float red = 1.0f;
		float green = 0.0f;
		float blue = 0.0f;
		contentStream.setNonStrokingColor(red, green, blue);

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(190, 598);

		contentStream.showText(req.getInvoiceNo());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(190, 585);

		contentStream.showText(req.getDate());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(190, 570);

		contentStream.showText(req.getClient());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(190, 550);

		contentStream.showText(req.getAddress());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(190, 490);

		contentStream.showText(req.getEmail());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(210, 425);

		contentStream.showText(req.getDescription());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 425);

		contentStream.showText(req.getBillingCycle());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(210, 415);

		contentStream.showText(req.getDescription1());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 415);

		contentStream.showText(req.getBillingCycle1());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 399);

		contentStream.showText(req.getTotalperiod());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 386);

		contentStream.showText(req.getSubtotal());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 376);

		contentStream.showText(req.getSgst());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 365);

		contentStream.showText(req.getCgst());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(370, 352);

		contentStream.showText(req.getTotal());

		contentStream.endText();

		contentStream.close();

		document.save("D:/editedinvoice.pdf");

		document.close();

	}

	public void readtext(SubAuaUser res) throws IOException, MessagingException {

		ConstantFilePath filepath = new ConstantFilePath();
		String sts = "";

		File file = new File(sts);

		PDDocument document = PDDocument.load(file);

		PDPage page = document.getPage(0);

		PDPageContentStream contentStream = new PDPageContentStream(document, page,

				PDPageContentStream.AppendMode.APPEND, true);

		PDFont font2 = PDType1Font.HELVETICA;

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(400, 655);

		contentStream.showText(res.getOrganisationName());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(400, 640);

		contentStream.showText(res.getSubAuaId());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(400, 625);

		contentStream.showText(res.getAddress());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(400, 625);

		contentStream.showText("");

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(400, 610);

		contentStream.showText("");

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 10);

		contentStream.newLineAtOffset(400, 595);

		contentStream.showText(res.getManagementName());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 580);

		contentStream.showText(res.getManagementDesignationName());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 565);

		contentStream.showText(res.getManagementMobilenumber());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 550);

		contentStream.showText(res.getManagementEmail());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 535);

		contentStream.showText(res.getTechnicalName());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 520);

		contentStream.showText(res.getTechnicalDesignationName());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 505);

		contentStream.showText(res.getTechnicalMobilenumber());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 490);

		contentStream.showText(res.getTechnicalEmail());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 475);

		contentStream.showText(res.getAuthenticationPurpose());

		contentStream.endText();

		contentStream.beginText();

		contentStream.setFont(font2, 12);

		contentStream.newLineAtOffset(400, 460);

		contentStream.showText(res.getKycpurpose());

		contentStream.endText();

		contentStream.close();

		try {
//			document.save(filepath.getDownloadPdf() + res.getSubAuaId() + ".pdf");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}

//		document.save(response.getOutputStream());

		document.close();
	}

}
