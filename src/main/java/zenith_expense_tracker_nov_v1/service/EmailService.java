package zenith_expense_tracker_nov_v1.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import zenith_expense_tracker_nov_v1.entity.Bill;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBillEmail(String toEmail, Bill bill, byte[] pdfContent) throws MessagingException {
        String subject = "Your Bill from Zenith Expense Tracker";
        String content = generateBillEmailContent(bill);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        // Attach the PDF bill
        String fileName = bill.getBillNumber() + ".pdf";
        helper.addAttachment(fileName, new ByteArrayResource(pdfContent));

        mailSender.send(mimeMessage);
    }

    public void sendOtp(String toEmail, String otp) throws MessagingException {
        String subject = "Your OTP for Password Reset";
        String content = generateOtpEmailContent(otp);
        sendHtmlEmail(toEmail, subject, content);
    }

    public void sendPasswordChangeConfirmation(String toEmail) throws MessagingException {
        String subject = "Password Changed Successfully";
        String content = generatePasswordChangeEmailContent();
        sendHtmlEmail(toEmail, subject, content);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    private String generateBillEmailContent(Bill bill) {
        return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
            body {
                font-family: 'Poppins', sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f9fafb;
            }
            .container {
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                padding: 30px;
                border-radius: 12px;
                border: 1px solid #e5e7eb;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            }
            .header {
                text-align: center;
                padding: 20px 0;
                background: linear-gradient(135deg, #6d28d9, #8b5cf6);
                border-radius: 12px 12px 0 0;
                color: #ffffff;
            }
            .header h2 {
                margin: 0;
                font-size: 22px;
                font-weight: 600;
            }
            .content {
                padding: 20px 0;
            }
            .content h1 {
                font-size: 20px;
                font-weight: 600;
                color: #111827;
                margin-bottom: 15px;
            }
            .content p {
                font-size: 14px;
                color: #374151;
                margin-bottom: 12px;
                line-height: 1.5;
            }
            .content .bill-details {
                background-color: #f9fafb;
                padding: 15px;
                border-radius: 8px;
                border: 1px solid #e5e7eb;
                margin-top: 10px;
                margin-bottom: 20px;
            }
            .content .bill-details p {
                margin: 8px 0;
                font-size: 13px;
                color: #4b5563;
            }
            .content .bill-details p strong {
                color: #111827;
            }
            .footer {
                text-align: center;
                font-size: 12px;
                color: #6b7280;
                padding: 20px 0;
                border-top: 1px solid #e5e7eb;
                margin-top: 20px;
            }
            .footer p {
                margin: 5px 0;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h2>Zenith Hospitality Services</h2>
            </div>
            <div class="content">
                <h1>Your Bill Information</h1>
                <p>Dear %s,</p>
                <p>Thank you for choosing Zenith Hospitality Services. Below are the details of your bill:</p>
                
                <div class="bill-details">
                    <p><strong>Bill Number:</strong> %s</p>
                    <p><strong>Check-in Date:</strong> %s</p>
                    <p><strong>Check-out Date:</strong> %s</p>
                    <p><strong>Total Amount:</strong> %s</p>
                </div>

                <p>Find a copy of the invoice attached with this email.</p>
                <p>If you have any questions or need further assistance, please feel free to contact our support team.</p>
                <p>We look forward to serving you again!</p>
            </div>
            <div class="footer">
                <p>&copy; 2025 Zenith Hospitality Services. All Rights Reserved.</p>
                <p>Contact Us: zenithhospitalityservices@gmail.com | +91 90040 95581 / +91 74000 57303</p>
            </div>
        </div>
    </body>
    </html>
    """.formatted(
                bill.getGuestName(),
                bill.getBillNumber(),
                bill.getCheckInDate().toLocalDate(),
                bill.getCheckOutDate().toLocalDate(),
                bill.getTotalAmount()
        );
    }
    private String generateOtpEmailContent(String otp) {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: 'Poppins', sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f4f4f9;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                }
                .header {
                    text-align: center;
                    padding: 10px 0;
                    background-color: #4caf50;
                    border-radius: 8px 8px 0 0;
                    color: #ffffff;
                }
                .header img {
                    width: 100px;
                }
                .content {
                    padding: 20px;
                    text-align: center;
                }
                .content h1 {
                    font-size: 24px;
                    margin-bottom: 10px;
                    color: #333333;
                }
                .content p {
                    font-size: 16px;
                    color: #555555;
                    margin-bottom: 20px;
                }
                .content .otp {
                    font-size: 28px;
                    color: #4caf50;
                    font-weight: bold;
                }
                .footer {
                    text-align: center;
                    font-size: 14px;
                    color: #777777;
                    padding: 10px 0;
                    border-top: 1px solid #dddddd;
                    margin-top: 20px;
                }
                .footer p {
                    margin: 5px 0;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="https://yourcompany.com/logo.png" alt="Company Logo">
                    <h2>Zenith Expense Tracker</h2>
                </div>
                <div class="content">
                    <h1>OTP for Password Reset</h1>
                    <p>Use the following OTP to reset your password. It is valid for 2 minutes.</p>
                    <div class="otp">%s</div>
                </div>
                <div class="footer">
                    <p>&copy; 2024 Zenith Expense Tracker. All Rights Reserved.</p>
                    <p>Contact Us: support@zenithtracker.com | +91 8830553868</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(otp);
    }

    private String generatePasswordChangeEmailContent() {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: 'Poppins', sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f4f4f9;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                }
                .header {
                    text-align: center;
                    padding: 10px 0;
                    background-color: #4caf50;
                    border-radius: 8px 8px 0 0;
                    color: #ffffff;
                }
                .header img {
                    width: 100px;
                }
                .content {
                    padding: 20px;
                    text-align: center;
                }
                .content h1 {
                    font-size: 24px;
                    margin-bottom: 10px;
                    color: #333333;
                }
                .content p {
                    font-size: 16px;
                    color: #555555;
                    margin-bottom: 20px;
                }
                .footer {
                    text-align: center;
                    font-size: 14px;
                    color: #777777;
                    padding: 10px 0;
                    border-top: 1px solid #dddddd;
                    margin-top: 20px;
                }
                .footer p {
                    margin: 5px 0;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="https://yourcompany.com/logo.png" alt="Company Logo">
                    <h2>Zenith Expense Tracker</h2>
                </div>
                <div class="content">
                    <h1>Password Change Confirmation</h1>
                    <p>Your password has been successfully changed.</p>
                </div>
                <div class="footer">
                    <p>&copy; 2024 Zenith Expense Tracker. All Rights Reserved.</p>
                    <p>Contact Us: support@zenithtracker.com | +91 8830553868</p>
                </div>
            </div>
        </body>
        </html>
        """;
    }
}