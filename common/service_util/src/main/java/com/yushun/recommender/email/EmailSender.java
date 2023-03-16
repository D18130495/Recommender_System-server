package com.yushun.recommender.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {
    @Value("${spring.mail.username}")
    private static String adminEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendRegisterVerificationCode(String recipient, String code) throws MessagingException {
        if(recipient == null) {
            return false;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("Finterest <990415zys@gmail.com>");
        helper.setTo(recipient);
        helper.setSubject("verification-code");

        String html = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <title>verification-code</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    \n" +
                "<div class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=\"mailContentContainer\" style=\"\">\n" +
                "\n" +
                "<style type=\"text/css\">\n" +
                ".qmbox body {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "background: #fff;\n" +
                "font-family: \"Verdana, Arial, Helvetica, sans-serif\";\n" +
                "font-size: 14px;\n" +
                "line-height: 24px;\n" +
                "}\n" +
                "\n" +
                ".qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "}\n" +
                "\n" +
                ".qmbox img {\n" +
                "border: none;\n" +
                "}\n" +
                "\n" +
                ".qmbox .contaner {\n" +
                "margin: 0 auto;\n" +
                "}\n" +
                "\n" +
                ".qmbox .content {\n" +
                "margin: 4px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .biaoti {\n" +
                "padding: 6px;\n" +
                "color: #000;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xtop, .qmbox .xbottom {\n" +
                "display: block;\n" +
                "font-size: 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "display: block;\n" +
                "overflow: hidden;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                "height: 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "border-left: 1px solid #BCBCBC;\n" +
                "border-right: 1px solid #BCBCBC;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1 {\n" +
                "margin: 0 5px;\n" +
                "background: #BCBCBC;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb2 {\n" +
                "margin: 0 3px;\n" +
                "border-width: 0 2px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb3 {\n" +
                "margin: 0 2px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb4 {\n" +
                "height: 2px;\n" +
                "margin: 0 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xboxcontent {\n" +
                "display: block;\n" +
                "border: 0 solid #BCBCBC;\n" +
                "border-width: 0 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .line {\n" +
                "margin-top: 6px;\n" +
                "border-top: 1px dashed #B9B9B9;\n" +
                "padding: 4px;\n" +
                "font-size: 8px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .neirong {\n" +
                "padding: 15px;\n" +
                "color:#444343;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".qmbox .font_lightblue {\n" +
                "color:#318CF7;\n" +
                "font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".qmbox .font_gray {\n" +
                "color: #888;\n" +
                "font-size: 12px;\n" +
                "}\n" +
                "</style>\n" +
                "<div class=\"contaner\">\n" +
                "    <div class=\"content\">\n" +
                "        <b class=\"xtop\"></b>\n" +
                "        <b class=\"xb1\"></b>\n" +
                "        <b class=\"xb2\"></b>\n" +
                "        <b class=\"xb3\"></b>\n" +
                "        <b class=\"xb4\"></b>\n" +
                "        \n" +
                "    <div class=\"xboxcontent\"> \n" +
                "        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-bottom:20px\"></p>\n" +
                "        <br>\n" +
                "        <p class=\"biaoti\"> \n" +
                "            <b>Dear user!</b> \n" +
                "        </p>\n" +
                "    <div class=\"neirong\">\n" +
                "        <p>\n" +
                "            <b>You are registering an account, please enter the following verification code complete the operation:</b> \n" +
                "            <br><br>\n" +
                "            <div align=\"center\">\n" +
                "            <span class=\"font_lightblue\">\n" +
                "            <span id=\"yzm\" t=\"7\" style=\" z-index: 1; position: static;font-size: 27px\">\n" +
                                code +
                "            </span>\n" +
                "                <br><br>\n" +
                "            </span>\n" +
                "            </div>\n" +
                "            <p style=\" margin:10px auto 10px auto;font-size:15px;color:#999;\">\n" +
                "                Please keep your verification code safe!\n" +
                "            </p>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "        <div class=\"line\">\n" +
                "            <p style=\"text-align:center; margin:10px auto 10px auto;font-size:12px;color:#999;\">\n" +
                "                This is a system email, please do not reply.<br>\n" +
                "              The verification code is valid for five minutes.\n" +
                "          </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "        <b class=\"xbottom\"></b>\n" +
                "        <b class=\"xb4\"></b>\n" +
                "        <b class=\"xb3\"></b>\n" +
                "        <b class=\"xb2\"></b>\n" +
                "        <b class=\"xb1\"></b>\n" +
                "        \n" +
                "    </div>\n" +
                "</div>\n" +
                "    <style type=\"text/css\">\n" +
                "    .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                "    display: none !important;\n" +
                "    }\n" +
                "    </style>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        helper.setText(html, true);

        try {
            javaMailSender.send(message);

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean sendPassword(String recipient, String password) throws MessagingException {
        if(recipient == null) {
            return false;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("Finterest <990415zys@gmail.com>");
        helper.setTo(recipient);
        helper.setSubject("verification-code");

        String html = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <title>New-Password</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    \n" +
                "<div class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=\"mailContentContainer\" style=\"\">\n" +
                "\n" +
                "<style type=\"text/css\">\n" +
                ".qmbox body {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "background: #fff;\n" +
                "font-family: \"Verdana, Arial, Helvetica, sans-serif\";\n" +
                "font-size: 14px;\n" +
                "line-height: 24px;\n" +
                "}\n" +
                "\n" +
                ".qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "}\n" +
                "\n" +
                ".qmbox img {\n" +
                "border: none;\n" +
                "}\n" +
                "\n" +
                ".qmbox .contaner {\n" +
                "margin: 0 auto;\n" +
                "}\n" +
                "\n" +
                ".qmbox .content {\n" +
                "margin: 4px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .biaoti {\n" +
                "padding: 6px;\n" +
                "color: #000;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xtop, .qmbox .xbottom {\n" +
                "display: block;\n" +
                "font-size: 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "display: block;\n" +
                "overflow: hidden;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                "height: 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "border-left: 1px solid #BCBCBC;\n" +
                "border-right: 1px solid #BCBCBC;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1 {\n" +
                "margin: 0 5px;\n" +
                "background: #BCBCBC;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb2 {\n" +
                "margin: 0 3px;\n" +
                "border-width: 0 2px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb3 {\n" +
                "margin: 0 2px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb4 {\n" +
                "height: 2px;\n" +
                "margin: 0 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xboxcontent {\n" +
                "display: block;\n" +
                "border: 0 solid #BCBCBC;\n" +
                "border-width: 0 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .line {\n" +
                "margin-top: 6px;\n" +
                "border-top: 1px dashed #B9B9B9;\n" +
                "padding: 4px;\n" +
                "font-size: 8px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .neirong {\n" +
                "padding: 15px;\n" +
                "color:#444343;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".qmbox .font_lightblue {\n" +
                "color:#318CF7;\n" +
                "font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".qmbox .font_gray {\n" +
                "color: #888;\n" +
                "font-size: 12px;\n" +
                "}\n" +
                "</style>\n" +
                "<div class=\"contaner\">\n" +
                "    <div class=\"content\">\n" +
                "        <b class=\"xtop\"></b>\n" +
                "        <b class=\"xb1\"></b>\n" +
                "        <b class=\"xb2\"></b>\n" +
                "        <b class=\"xb3\"></b>\n" +
                "        <b class=\"xb4\"></b>\n" +
                "        \n" +
                "    <div class=\"xboxcontent\"> \n" +
                "        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-bottom:20px\"></p>\n" +
                "        <br>\n" +
                "        <p class=\"biaoti\"> \n" +
                "            <b>Dear user!</b> \n" +
                "        </p>\n" +
                "    <div class=\"neirong\">\n" +
                "        <p>\n" +
                "            <b>You are require a new password:</b> \n" +
                "            <br><br>\n" +
                "            <div align=\"center\">\n" +
                "            <span class=\"font_lightblue\">\n" +
                "            <span id=\"yzm\" t=\"7\" style=\" z-index: 1; position: static;font-size: 27px\">\n" +
                                password +
                "            </span>\n" +
                "                <br><br>\n" +
                "            </span>\n" +
                "            </div>\n" +
                "            <p style=\" margin:10px auto 10px auto;font-size:15px;color:#999;\">\n" +
                "                Please keep your password safe!\n" +
                "            </p>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "        <div class=\"line\">\n" +
                "            <p style=\"text-align:center; margin:10px auto 10px auto;font-size:12px;color:#999;\">\n" +
                "                This is a system email, please do not reply.<br>\n" +
                "          </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "        <b class=\"xbottom\"></b>\n" +
                "        <b class=\"xb4\"></b>\n" +
                "        <b class=\"xb3\"></b>\n" +
                "        <b class=\"xb2\"></b>\n" +
                "        <b class=\"xb1\"></b>\n" +
                "        \n" +
                "    </div>\n" +
                "</div>\n" +
                "    <style type=\"text/css\">\n" +
                "    .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                "    display: none !important;\n" +
                "    }\n" +
                "    </style>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        helper.setText(html, true);

        try {
            javaMailSender.send(message);

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean sendChangePasswordVerificationCode(String recipient, String code) throws MessagingException {
        if(recipient == null) {
            return false;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("Finterest <990415zys@gmail.com>");
        helper.setTo(recipient);
        helper.setSubject("verification-code");

        String html = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <title>verification-code</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    \n" +
                "<div class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=\"mailContentContainer\" style=\"\">\n" +
                "\n" +
                "<style type=\"text/css\">\n" +
                ".qmbox body {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "background: #fff;\n" +
                "font-family: \"Verdana, Arial, Helvetica, sans-serif\";\n" +
                "font-size: 14px;\n" +
                "line-height: 24px;\n" +
                "}\n" +
                "\n" +
                ".qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "}\n" +
                "\n" +
                ".qmbox img {\n" +
                "border: none;\n" +
                "}\n" +
                "\n" +
                ".qmbox .contaner {\n" +
                "margin: 0 auto;\n" +
                "}\n" +
                "\n" +
                ".qmbox .content {\n" +
                "margin: 4px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .biaoti {\n" +
                "padding: 6px;\n" +
                "color: #000;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xtop, .qmbox .xbottom {\n" +
                "display: block;\n" +
                "font-size: 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "display: block;\n" +
                "overflow: hidden;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                "height: 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "border-left: 1px solid #BCBCBC;\n" +
                "border-right: 1px solid #BCBCBC;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb1 {\n" +
                "margin: 0 5px;\n" +
                "background: #BCBCBC;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb2 {\n" +
                "margin: 0 3px;\n" +
                "border-width: 0 2px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb3 {\n" +
                "margin: 0 2px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xb4 {\n" +
                "height: 2px;\n" +
                "margin: 0 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .xboxcontent {\n" +
                "display: block;\n" +
                "border: 0 solid #BCBCBC;\n" +
                "border-width: 0 1px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .line {\n" +
                "margin-top: 6px;\n" +
                "border-top: 1px dashed #B9B9B9;\n" +
                "padding: 4px;\n" +
                "font-size: 8px;\n" +
                "}\n" +
                "\n" +
                ".qmbox .neirong {\n" +
                "padding: 15px;\n" +
                "color:#444343;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".qmbox .font_lightblue {\n" +
                "color:#318CF7;\n" +
                "font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".qmbox .font_gray {\n" +
                "color: #888;\n" +
                "font-size: 12px;\n" +
                "}\n" +
                "</style>\n" +
                "<div class=\"contaner\">\n" +
                "    <div class=\"content\">\n" +
                "        <b class=\"xtop\"></b>\n" +
                "        <b class=\"xb1\"></b>\n" +
                "        <b class=\"xb2\"></b>\n" +
                "        <b class=\"xb3\"></b>\n" +
                "        <b class=\"xb4\"></b>\n" +
                "        \n" +
                "    <div class=\"xboxcontent\"> \n" +
                "        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-bottom:20px\"></p>\n" +
                "        <br>\n" +
                "        <p class=\"biaoti\"> \n" +
                "            <b>Dear user!</b> \n" +
                "        </p>\n" +
                "    <div class=\"neirong\">\n" +
                "        <p>\n" +
                "            <b>You are changing password, please enter the following verification code complete the operation:</b> \n" +
                "            <br><br>\n" +
                "            <div align=\"center\">\n" +
                "            <span class=\"font_lightblue\">\n" +
                "            <span id=\"yzm\" t=\"7\" style=\" z-index: 1; position: static;font-size: 27px\">\n" +
                                code +
                "            </span>\n" +
                "                <br><br>\n" +
                "            </span>\n" +
                "            </div>\n" +
                "            <p style=\" margin:10px auto 10px auto;font-size:15px;color:#999;\">\n" +
                "                Please keep your verification code safe!\n" +
                "            </p>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "        <div class=\"line\">\n" +
                "            <p style=\"text-align:center; margin:10px auto 10px auto;font-size:12px;color:#999;\">\n" +
                "                This is a system email, please do not reply.<br>\n" +
                "              The verification code is valid for five minutes.\n" +
                "          </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "        <b class=\"xbottom\"></b>\n" +
                "        <b class=\"xb4\"></b>\n" +
                "        <b class=\"xb3\"></b>\n" +
                "        <b class=\"xb2\"></b>\n" +
                "        <b class=\"xb1\"></b>\n" +
                "        \n" +
                "    </div>\n" +
                "</div>\n" +
                "    <style type=\"text/css\">\n" +
                "    .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                "    display: none !important;\n" +
                "    }\n" +
                "    </style>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        helper.setText(html, true);

        try {
            javaMailSender.send(message);

            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
