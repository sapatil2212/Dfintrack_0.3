package zenith_expense_tracker_nov_v1.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.entity.Bill;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    // Define colors - refined color palette
    private static final Color LIGHT_GREEN = new Color(237, 252, 246); // Lighter mint green background
    private static final Color PRIMARY_TEXT = new Color(33, 37, 41);   // Dark text
    private static final Color ACCENT_GREEN = new Color(38, 166, 133); // Green accent color
    private static final Color PURPLE_ACCENT = new Color(103, 58, 113); // Zenith purple
    private static final Color LIGHT_GRAY = new Color(240, 240, 240); // Light gray for separators
    private static final Color DARK_GRAY = new Color(120, 120, 120); // Dark gray for secondary text

    // Font setup - Using smaller font sizes
    private static Font regularFont;
    private static Font boldFont;
    private static Font semiBoldFont;
    private static Font lightFont;

    // Logo paths
    private static final String DFIN_LOGO_PATH = "src/main/resources/static/logo.png";
    private static final String ZENITH_LOGO_PATH = "src/main/resources/static/zenith.png";

    public byte[] generateBillPdf(Bill bill) throws Exception {
        Document document = new Document(PageSize.A4, 20, 20, 30, 30); // Reduced margins
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Initialize fonts with smaller sizes
            initializeFonts();

            // Add logo header
            addModernHeader(document);

            // Add confirmation box
            addConfirmationBox(document);

            // Add booking details header
            addSectionTitle(document, "Booking Details");

            // Add booking ID
            addBookingId(document, bill);

            // Add guest info in 3-column layout
            addGuestInfoGrid(document, bill);

            // Add price summary in styled box
            addPriceSummary(document, bill);

            // Add terms and conditions
            addTermsAndConditions(document);

            // Add footer
            addModernFooter(document, writer);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            document.close();
            throw e;
        }
    }

    private void initializeFonts() {
        // Create fonts with smaller sizes
        regularFont = new Font(Font.HELVETICA, 8, Font.NORMAL, PRIMARY_TEXT); // Reduced from 9 to 8
        boldFont = new Font(Font.HELVETICA, 8, Font.BOLD, PRIMARY_TEXT); // Reduced from 9 to 8
        semiBoldFont = new Font(Font.HELVETICA, 8, Font.BOLD, PRIMARY_TEXT); // Reduced from 9 to 8
        lightFont = new Font(Font.HELVETICA, 8, Font.NORMAL, DARK_GRAY); // Reduced from 9 to 8
    }

    private void addModernHeader(Document document) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);

        try {
            headerTable.setWidths(new float[]{1, 1});

            // First cell - DFinTrack with logo
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setPaddingBottom(5); // Reduced padding from 10 to 5

            try {
                // Add actual logo image
                Image dfinLogo = Image.getInstance(DFIN_LOGO_PATH);
                dfinLogo.scaleToFit(80, 30);
                leftCell.addElement(dfinLogo);

            } catch (IOException e) {
                // Fallback if image can't be loaded
                Font logoFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED);
                Paragraph logoPlaceholder = new Paragraph("DFinTrack", logoFont);
                leftCell.addElement(logoPlaceholder);

                Font taglineFont = new Font(Font.HELVETICA, 5, Font.NORMAL, DARK_GRAY); // Reduced from 6 to 5
                Paragraph tagline = new Paragraph("Control Your Finances Digitally", taglineFont);
                leftCell.addElement(tagline);
            }

            headerTable.addCell(leftCell);

            // Second cell - INVOICE heading (positioned at top right)
            Font invoiceFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(20, 27, 77)); // Reduced from 20 to 18
            Paragraph invoice = new Paragraph("INVOICE", invoiceFont);
            invoice.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell rightCell = new PdfPCell();
            rightCell.addElement(invoice);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.setVerticalAlignment(Element.ALIGN_TOP);
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setPaddingBottom(10); // Added spacing below "INVOICE" text
            headerTable.addCell(rightCell);

            document.add(headerTable);

        } catch (DocumentException e) {
            throw new DocumentException("Error creating header: " + e.getMessage());
        }
    }

    private void addConfirmationBox(Document document) throws DocumentException {
        PdfPTable confirmTable = new PdfPTable(2);
        confirmTable.setWidthPercentage(100);

        try {
            confirmTable.setWidths(new float[]{1, 1});

            // Left cell - Thank you message
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBackgroundColor(LIGHT_GREEN);

            // Add checkmark icon and thank you text
            Font checkmarkFont = new Font(Font.ZAPFDINGBATS, 16, Font.NORMAL, ACCENT_GREEN); // Reduced from 20 to 16
            Paragraph checkIcon = new Paragraph("✓", checkmarkFont);
            checkIcon.setAlignment(Element.ALIGN_LEFT);
            checkIcon.setSpacingAfter(2); // Reduced spacing after checkmark

            Font thankYouFont = new Font(Font.HELVETICA, 9, Font.BOLD, PRIMARY_TEXT); // Reduced from 10 to 9
            Paragraph thankYou = new Paragraph("Thank you for your trust!", thankYouFont);
            thankYou.setAlignment(Element.ALIGN_LEFT);
            thankYou.setSpacingAfter(2); // Reduced spacing after thank you message

            Font subTextFont = new Font(Font.HELVETICA, 5, Font.NORMAL, PRIMARY_TEXT); // Reduced from 6 to 5
            Paragraph subText = new Paragraph(
                    "We are happy to serve you. This is a digitally generated invoice.",
                    subTextFont
            );
            subText.setAlignment(Element.ALIGN_LEFT);

            leftCell.addElement(checkIcon);
            leftCell.addElement(thankYou);
            leftCell.addElement(subText);
            leftCell.setPadding(5); // Reduced padding from 10 to 5
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // Right cell with Zenith logo (aligned to extreme right)
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBackgroundColor(LIGHT_GREEN);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            rightCell.setPadding(5); // Reduced padding from 10 to 5
            rightCell.setBorder(Rectangle.NO_BORDER);

            try {
                // Add actual Zenith logo image
                Image zenithLogo = Image.getInstance(ZENITH_LOGO_PATH);
                zenithLogo.scaleToFit(50, 15); // Reduced size from 80x30 to 50x15
                zenithLogo.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(zenithLogo);
            } catch (IOException e) {
                // Fallback if image can't be loaded
                Font zenithLogoFont = new Font(Font.HELVETICA, 14, Font.BOLD, PURPLE_ACCENT); // Reduced from 16 to 14
                Paragraph zenithText = new Paragraph("Zenith", zenithLogoFont);
                zenithText.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(zenithText);

                Font hospitalityFont = new Font(Font.HELVETICA, 5, Font.BOLD, PURPLE_ACCENT); // Reduced from 6 to 5
                Paragraph hospitality = new Paragraph("HOSPITALITY", hospitalityFont);
                hospitality.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(hospitality);
            }

            confirmTable.addCell(leftCell);
            confirmTable.addCell(rightCell);
            confirmTable.setSpacingAfter(10); // Reduced spacing from 15 to 10

            document.add(confirmTable);

        } catch (DocumentException e) {
            throw new DocumentException("Error creating confirmation box: " + e.getMessage());
        }
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD, PRIMARY_TEXT); // Reduced from 16 to 14
        Paragraph sectionTitle = new Paragraph(title, titleFont);
        sectionTitle.setSpacingAfter(8);
        document.add(sectionTitle);
    }

    private void addBookingId(Document document, Bill bill) throws DocumentException {
        Font bookingIdFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.GRAY); // Reduced from 14 to 12
        Paragraph bookingId = new Paragraph(" " + formatBillNumber(bill.getBillNumber()), bookingIdFont);
        bookingId.setSpacingAfter(15);
        document.add(bookingId);
    }

    private String formatBillNumber(String billNumber) {
        return billNumber;
    }

    private void addGuestInfoGrid(Document document, Bill bill) throws DocumentException {
        PdfPTable infoGrid = new PdfPTable(3);
        infoGrid.setWidthPercentage(100);
        infoGrid.setSpacingBefore(10);
        infoGrid.setSpacingAfter(15);
        try {
            // Set column widths
            infoGrid.setWidths(new float[]{2, 2, 2});

            // Add all fields
            addInfoColumn(infoGrid, "Booking Number", bill.getBookingNumber() != null ? bill.getBookingNumber().toString() : "N/A");
            addInfoColumn(infoGrid, "Guest Name", bill.getGuestName());
            addInfoColumn(infoGrid, "Phone Number", bill.getPhoneNumber());
            addInfoColumn(infoGrid, "Email", bill.getEmail());
            addInfoColumn(infoGrid, "Check-in Date", bill.getCheckInDate().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));
            addInfoColumn(infoGrid, "Check-out Date", bill.getCheckOutDate().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));
            addInfoColumn(infoGrid, "Number of Rooms", String.valueOf(bill.getNumberOfRooms()));
            addInfoColumn(infoGrid, "Billing Type", bill.getBillingType().toString());
            addInfoColumn(infoGrid, "Booking Status", bill.getBookingStatusEnum().toString());
            addInfoColumn(infoGrid, "Payment Mode", bill.getPaymentMode() != null ? bill.getPaymentMode().toString() : "N/A");
            addInfoColumn(infoGrid, "Occupancy Type", bill.getOccupancyType() != null ? bill.getOccupancyType().toString() : "N/A");
            addInfoColumn(infoGrid, "Booking Date & Time", bill.getBookingDateTime() != null ? bill.getBookingDateTime().toString() : "N/A");

            document.add(infoGrid);

        } catch (DocumentException e) {
            throw new DocumentException("Error creating guest info grid: " + e.getMessage());
        }
    }

    private void addInfoColumn(PdfPTable table, String label, String value) {
        if (value == null) {
            value = "N/A"; // Handle null values
        }

        Font labelFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY); // Label font
        Font valueFont = new Font(Font.HELVETICA, 9, Font.BOLD, PRIMARY_TEXT); // Value font

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(8); // Add spacing below the cell

        Paragraph labelPara = new Paragraph(label, labelFont);
        Paragraph valuePara = new Paragraph(value, valueFont);

        cell.addElement(labelPara);
        cell.addElement(valuePara);

        table.addCell(cell);
    }

    private void addPriceSummary(Document document, Bill bill) throws DocumentException {
        // Create outer table with light green background
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);
        PdfPCell outerCell = new PdfPCell();
        outerCell.setBackgroundColor(LIGHT_GREEN);
        outerCell.setBorder(Rectangle.NO_BORDER);
        outerCell.setPadding(20);

        // Summary title
        Font summaryTitleFont = new Font(Font.HELVETICA, 12, Font.BOLD, PRIMARY_TEXT); // Reduced from 14 to 12
        Paragraph summaryTitle = new Paragraph("Your Price Summary", summaryTitleFont);
        summaryTitle.setSpacingAfter(12);
        outerCell.addElement(summaryTitle);

        // Create price breakdown table
        PdfPTable priceTable = new PdfPTable(2);
        priceTable.setWidthPercentage(100);

        try {
            priceTable.setWidths(new float[]{3, 1});

            // Add booking amount
            addPriceRow(priceTable, "Booking Amount", "Rs. " + bill.getBookingAmount() + "/-");

            // Add GST
            addPriceRow(priceTable, "SGST", "Rs. " + bill.getStayGSTAmount() + "/-");

            // Add Food Tax
            addPriceRow(priceTable, "Food Tax", "Rs. " + bill.getFoodGSTAmount() + "/-");

            outerCell.addElement(priceTable);

            // Add horizontal line
            LineSeparator line = new LineSeparator();
            line.setLineColor(Color.LIGHT_GRAY);
            line.setPercentage(100);
            line.setLineWidth(0.5f);

            Paragraph linePara = new Paragraph();
            linePara.add(new Chunk(line));
            linePara.setSpacingBefore(12);
            linePara.setSpacingAfter(12);
            outerCell.addElement(linePara);

            // Add total amount row
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);

            try {
                totalTable.setWidths(new float[]{3, 1});

                // Calculate total
                BigDecimal total = bill.getTotalAmount();

                // TO PAY:
                Font toPayFont = new Font(Font.HELVETICA, 12, Font.BOLD, ACCENT_GREEN); // Reduced from 14 to 12
                PdfPCell toPayCell = new PdfPCell(new Phrase("TO PAY", toPayFont));
                toPayCell.setBorder(Rectangle.NO_BORDER);
                totalTable.addCell(toPayCell);

                // Total amount
                Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD, ACCENT_GREEN); // Reduced from 14 to 12
                PdfPCell amountCell = new PdfPCell(new Phrase("Rs. " + total + "/-", totalFont));
                amountCell.setBorder(Rectangle.NO_BORDER);
                amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalTable.addCell(amountCell);

                outerCell.addElement(totalTable);

            } catch (DocumentException e) {
                throw new DocumentException("Error creating total amount row: " + e.getMessage());
            }

            outerTable.addCell(outerCell);
            outerTable.setSpacingAfter(20);

            document.add(outerTable);

        } catch (DocumentException e) {
            throw new DocumentException("Error creating price summary: " + e.getMessage());
        }
    }

    private void addPriceRow(PdfPTable table, String label, String amount) {
        Font labelFont = new Font(Font.HELVETICA, 9, Font.NORMAL, PRIMARY_TEXT); // Reduced from 10 to 9
        Font amountFont = new Font(Font.HELVETICA, 9, Font.NORMAL, PRIMARY_TEXT); // Reduced from 10 to 9

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(8);
        table.addCell(labelCell);

        PdfPCell amountCell = new PdfPCell(new Phrase(amount, amountFont));
        amountCell.setBorder(Rectangle.NO_BORDER);
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amountCell.setPaddingBottom(8);
        table.addCell(amountCell);
    }

    private void addTermsAndConditions(Document document) throws DocumentException {
        Font titleFont = new Font(Font.HELVETICA, 9, Font.BOLD, PRIMARY_TEXT); // Reduced from 10 to 9
        Font contentFont = new Font(Font.HELVETICA, 7, Font.NORMAL, Color.GRAY); // Reduced from 8 to 7

        Paragraph termsTitle = new Paragraph("Terms & Conditions", titleFont);
        termsTitle.setSpacingAfter(4);
        document.add(termsTitle);

        // Add terms as numbered list
        Paragraph terms = new Paragraph();
        terms.setIndentationLeft(15);

        terms.add(new Chunk("1. This is a digital copy of the booking confirmation and not the final document.\n", contentFont));
        terms.add(new Chunk("2. The details mentioned are subject to change.\n", contentFont));
        terms.add(new Chunk("3. Please review the details carefully. If there are any discrepancies or changes, contact us immediately.\n", contentFont));

        document.add(terms);

        // Add important information
        Paragraph infoTitle = new Paragraph("Important Information", titleFont);
        infoTitle.setSpacingBefore(8);
        infoTitle.setSpacingAfter(4);
        document.add(infoTitle);

        Paragraph info = new Paragraph();
        info.setIndentationLeft(15);

        info.add(new Chunk("• Standard Check-in: 12:00 PM\n", contentFont));
        info.add(new Chunk("• Standard Check-out: 11:00 AM\n", contentFont));
        info.add(new Chunk("• Please carry a valid ID proof\n", contentFont));
        info.add(new Chunk("• Early check-in subject to availability\n", contentFont));

        document.add(info);
    }

    private void addModernFooter(Document document, PdfWriter writer) throws DocumentException {
        Font footerFont = new Font(Font.HELVETICA, 7, Font.NORMAL, DARK_GRAY); // Reduced from 8 to 7

        // Create a table for the footer to ensure alignment
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);

        PdfPCell footerCell = new PdfPCell();
        footerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph footer = new Paragraph();
        footer.setAlignment(Element.ALIGN_CENTER);

        Chunk email = new Chunk("Email: help@digiworldinfotech.com | ", footerFont);
        Chunk phone = new Chunk("Phone: +91 883 055 3868, +91 820 841 5943", footerFont);

        footer.add(email);
        footer.add(phone);

        footerCell.addElement(footer);
        footerTable.addCell(footerCell);

        // Add horizontal line above the footer
        LineSeparator line = new LineSeparator();
        line.setLineColor(Color.LIGHT_GRAY);
        line.setPercentage(100);
        line.setLineWidth(0.5f);

        Paragraph linePara = new Paragraph();
        linePara.add(new Chunk(line));
        linePara.setSpacingBefore(10); // Adjust spacing above the line
        linePara.setSpacingAfter(5); // Adjust spacing below the line
        document.add(linePara);

        // Add the footer table to the document
        document.add(footerTable);
    }
}