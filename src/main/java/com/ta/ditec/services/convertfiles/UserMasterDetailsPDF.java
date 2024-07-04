package com.ta.ditec.services.convertfiles;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ta.ditec.services.model.UserMasterDetails;

public class UserMasterDetailsPDF {

	public void generateumdPDF(List<UserMasterDetails> resp, HttpServletResponse response)
			throws IOException {

		
		
		String dept=resp.get(1).getUserName();
		
		PDDocument document = new PDDocument();
		PDPage page2 = new PDPage();
		document.addPage(page2);

		PDFont font = PDType1Font.HELVETICA;
		PDPageContentStream pageContentStream = new PDPageContentStream(document, page2);
		pageContentStream.beginText();
		
		pageContentStream.setFont(PDType1Font.TIMES_BOLD, 20);
		pageContentStream.newLineAtOffset(50, 600);
		
		pageContentStream.drawString("User Master Details");
		
		pageContentStream.endText();
		
		
		pageContentStream.addRect(50, 500, 115 ,40);
	
		pageContentStream.beginText();
		pageContentStream.newLineAtOffset(60,510 );
		pageContentStream.setFont(PDType1Font.TIMES_BOLD, 10);
	
		pageContentStream.showText("Username");
		pageContentStream.endText();
		
		
		pageContentStream.addRect(165, 500, 115 ,40);
		pageContentStream.beginText();
		pageContentStream.newLineAtOffset(175, 510);
		pageContentStream.setFont(PDType1Font.TIMES_BOLD, 10);
		pageContentStream.showText("MobileNumber");
		pageContentStream.endText();
		
		
		pageContentStream.addRect(280, 500, 115 ,40);
		pageContentStream.beginText();
		pageContentStream.newLineAtOffset(290, 510);
		pageContentStream.setFont(PDType1Font.TIMES_BOLD, 10);
		pageContentStream.showText("UserID");
		pageContentStream.endText();
		
		

		int pageheight = (int) page2.getTrimBox().getHeight();
		int pagewidth = (int) page2.getTrimBox().getWidth();

		int initX = 50;
		int initY = 500;
		int cellHeight = 20;
		int cellWidth = 115;
		
		

			
		int rowCount = resp.size()-1;
		int colCount = 3;	

		
		
		
		for (int i= 0; i <= rowCount; i++) {	
			
				
			for (int j = 0; j <colCount; j++) {
				
				
				
				pageContentStream.addRect(initX, initY, cellWidth, -cellHeight);

				pageContentStream.beginText();
				pageContentStream.newLineAtOffset(initX + 10, initY - cellHeight + 10);
				pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
				if(j==0) {
			  pageContentStream.showText(resp.get(i).getUserName());
				}
				if(j==1) {
					 pageContentStream.showText(resp.get(i).getUserMobileNumber());
				}
				if(j==2) {
					 pageContentStream.showText(resp.get(i).getUserId());
				}
				
				
				
			//	System.out.println(usermasterdetails.get(j).getUserName());
				pageContentStream.endText();

				initX += cellWidth;
				}
						initX = 50;
			initY -= cellHeight;
		}
	
		
	
		pageContentStream.stroke();

		PDImageXObject logo2 = PDImageXObject.createFromFile("src/main/webapp/assets/image/logo_diatec.png", document);
		pageContentStream.drawImage(logo2, 20,700,550,50);

		pageContentStream.close();

		System.out.println("Image Created");



		document.save(response.getOutputStream());
		document.close();

	}

	
}
