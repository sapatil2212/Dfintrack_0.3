package zenith_expense_tracker_nov_v1.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

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

    private void sendHtmlEmail(String toEmail, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true); // Set true for HTML content

        mailSender.send(mimeMessage);
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
        """.formatted(otp); // Use .formatted to inject OTP into the HTML
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
                        <p>Your password has been successfully changed. If you did not request this change, contact us immediately.</p>
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
