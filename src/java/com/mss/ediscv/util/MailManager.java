/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;
import java.io.File;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author miracle1
 */
public class MailManager {
    private static final String SMTP_AUTH_USER = "mscvp_alerts@miraclesoft.com";
     private static final String SMTP_AUTH_PWD  = "Plokam@001";
     private static final String SMTP_HOST  = "smtp.miraclesoft.com";
     private static final String PORT="587";
     
        public static String sendPwd(String email,String password,String userid,String name) {
        // SUBSTITUTE YOUR EMAIL ADDRESSES HERE!!!
        /** The to is used for storing the user mail id to send details. */
        String to = email;
        
        /** The from is used for storing the from address. */
        String from = "mscvp_alerts@miraclesoft.com";
        
        // SUBSTITUTE YOUR ISP'S MAIL SERVER HERE!!!
        
        /**The host is used for storing the IP address of mail */
    
        /**The props is instance variabel to <code>Properties</code> class */
        Properties props = new Properties();
        
        /* Here set smtp protocal to props
         */
        props.setProperty("mail.transport.protocol", "smtp");

        //**Here set the address of the host to props */
        props.setProperty("mail.host", SMTP_HOST);

        /**
         * Here set the authentication for the host *
         */
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", PORT);
        Authenticator auth = new SMTPAuthenticator();
       // Session mailSession = Session.getDefaultInstance(props, null);
        Session mailSession = Session.getDefaultInstance(props, auth);
        mailSession.setDebug(true);
        Transport transport;
        try {
            transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject("Miracle Supply Chain Visibility Portal Password Details");
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.addRecipient(Message.RecipientType.BCC,new InternetAddress("cjakkampudi@miraclesoft.com"));
            
            // This HTML mail have to 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");
            
            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            StringBuilder htmlText = new StringBuilder();
             htmlText.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
            htmlText.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
            htmlText.append("<head>");
            htmlText.append("  <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
            htmlText.append("  <title>Your password has been reset successfully&#33;&#33;</title>");
            htmlText.append("  <style type='text/css'>");

            htmlText.append(" body {");
            htmlText.append("  padding-top: 0 !important;");
            htmlText.append("  padding-bottom: 0 !important;");
            htmlText.append("   padding-top: 0 !important;");
            htmlText.append("  padding-bottom: 0 !important;");
            htmlText.append("   margin:0 !important;");
            htmlText.append("  width: 100% !important;");
            htmlText.append("  -webkit-text-size-adjust: 100% !important;");
            htmlText.append(" -ms-text-size-adjust: 100% !important;;");
            htmlText.append(" -webkit-font-smoothing: antialiased !important;");
            htmlText.append(" }");
            htmlText.append(" .tableContent img {");
            htmlText.append("   border: 0 !important;");
            htmlText.append("  display: block !important;");
            htmlText.append("   outline: none !important;");
            htmlText.append(" }");

            htmlText.append("a{");
            htmlText.append("color:#382F2E;");
            htmlText.append("}");

            htmlText.append("p, h1,h2,ul,ol,li,div{");
            htmlText.append("margin:0;");
            htmlText.append("padding:0;");
            htmlText.append("}");

            htmlText.append("h1,h2{");
            htmlText.append("font-weight: normal;");
            htmlText.append("  background:transparent !important;");
            htmlText.append("border:none !important;");
            htmlText.append("}");

            htmlText.append(".contentEditable h2.big,.contentEditable h1.big{");
            htmlText.append("  font-size: 26px !important;");
            htmlText.append("}");

            htmlText.append(".contentEditable h2.bigger,.contentEditable h1.bigger{");
            htmlText.append("font-size: 37px !important;");
            htmlText.append("}");

            htmlText.append("td,table{");
            htmlText.append("vertical-align: top;");
            htmlText.append("}");

            htmlText.append("td.middle{");
            htmlText.append("vertical-align: middle;");
            htmlText.append("}");

            htmlText.append(" a.link1{");
            htmlText.append("font-size:13px;");
            htmlText.append("color:#27A1E5;");
            htmlText.append("line-height: 24px;");
            htmlText.append("text-decoration:none;");
            htmlText.append("}");

            htmlText.append("a{");
            htmlText.append("text-decoration: none;");
            htmlText.append("}");

            htmlText.append(".link2{");
            htmlText.append("color:#fc3f3f;");
            htmlText.append("border-top:0px solid #fc3f3f;");
            htmlText.append("border-bottom:0px solid #fc3f3f;");
            htmlText.append("border-left:10px solid #fc3f3f;");
            htmlText.append("border-right:10px solid #fc3f3f;");
            htmlText.append("border-radius:1px;");
            htmlText.append("-moz-border-radius:5px;");
            htmlText.append("-webkit-border-radius:5px;");
            htmlText.append("background:#fc3f3f;");
            htmlText.append("}");

            htmlText.append(".link3{");
            htmlText.append("color:#555555;");
            htmlText.append("border:1px solid #cccccc;");
            htmlText.append("padding:10px 18px;");
            htmlText.append("border-radius:3px;");
            htmlText.append("-moz-border-radius:3px;");
            htmlText.append("-webkit-border-radius:3px;");
            htmlText.append("background:#ffffff;");
            htmlText.append("}");

            htmlText.append(".link4{");
            htmlText.append("color:#27A1E5;");
            htmlText.append("line-height: 24px;");
            htmlText.append("}");

            htmlText.append("h2,h1{");
            htmlText.append("line-height: 20px;");
            htmlText.append("}");

            htmlText.append("p{");
            htmlText.append("font-size: 14px;");
            htmlText.append("line-height: 21px;");
            htmlText.append(" color:#AAAAAA;");
            htmlText.append("}");

            htmlText.append(".contentEditable li{");
            htmlText.append("}");

            htmlText.append(".appart p{");
            htmlText.append("}");

            htmlText.append(".bgItem{");
            htmlText.append("background:#ffffff;");
            htmlText.append("}");

            htmlText.append(".bgBody{");
            htmlText.append("background: #0d416b;");
            htmlText.append("}");

            htmlText.append("img {");
            htmlText.append("outline:none;");
            htmlText.append("text-decoration:none;");
            htmlText.append("-ms-interpolation-mode: bicubic;");
            htmlText.append("width: auto;");
            htmlText.append("max-width: 100%;");
            htmlText.append("clear: both;");
            htmlText.append("display: block;");
            htmlText.append("float: none;");
            htmlText.append("}");
            htmlText.append("</style>");

            htmlText.append("<script type='colorScheme' class='swatch active'>");
            htmlText.append("{");
            htmlText.append("'name':'Default',");
            htmlText.append("'bgBody':'ffffff',");
            htmlText.append("'link':'27A1E5',");
            htmlText.append("'color':'AAAAAA',");
            htmlText.append("'bgItem':'ffffff',");
            htmlText.append("'title':'444444'");
            htmlText.append("}");

            htmlText.append("</script>");

            htmlText.append("</head>");
            htmlText.append("<body paddingwidth='0' paddingheight='0' bgcolor='#d1d3d4' style='padding-top: 0; padding-bottom: 0; padding-top: 0; padding-bottom: 0; background-repeat: repeat; width: 100% !important; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-font-smoothing: antialiased;' offset='0' toppadding='0' leftpadding='0' data-gr-c-s-loaded='true'>");
            htmlText.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' class='tableContent bgBody' align='center' style='font-family:Helvetica, sans-serif;'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td align='center'>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td class='bgItem' align='center'>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td class='movableContentContainer' align='center'>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px' height='20'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px'>");
            htmlText.append("<table width='650' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentImageEditable'>");
            htmlText.append("<div class='contentEditable'>");
            htmlText.append("<a href='http://www.miraclesoft.com/index.php' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/logo.png' alt='Logo' height='45' data-default='placeholder' data-max-width='200'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td valign='middle' style='vertical-align: middle;'>");
            htmlText.append("</td>");
            htmlText.append("<td valign='middle' style='vertical-align: middle;' width='150'>");
            htmlText.append("<br>");
            htmlText.append("<table width='300' border='0' cellpadding='0' cellspacing='0' align='right' style='text-align: right; font-size: 13px; border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='fullCenter'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td height='55' valign='middle' width='100%' style='font-family: Open Sans; color:#232527;'>");
            htmlText.append("<span style='font-family: 'proxima_nova_rgregular', Open Sans; font-weight: normal;'>");
            htmlText.append("<a href='http://www.miraclesoft.com/company/about-us.php' target='_blank' style='text-decoration: none; color:#ffffff;' class='underline'>");
            htmlText.append("Company");
            htmlText.append("</a>");
            htmlText.append("</span>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<span style='font-family: 'proxima_nova_rgregular', Open Sans; font-weight: normal;'>");
            htmlText.append("<a href='http://www.miraclesoft.com/careers/' target='_blank' style='text-decoration: none; color:#ffffff;' class='underline'> Careers </a>");
            htmlText.append("</span>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='580' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='border: 0px solid #ffffff; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px'>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='660' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#00aae7; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:px'>");
            htmlText.append("<table width='630' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td height='15'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: left;'>");
            htmlText.append("<h2 style='font-size: 25px;'>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("<b>Password Reset </b>");
            htmlText.append("</font>");
            htmlText.append("</h2>");
            htmlText.append("<br>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<p>");
            htmlText.append("</p>");
            htmlText.append("<p>");
            htmlText.append("</p>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td height='5'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;'>");
            htmlText.append("<br>");
            htmlText.append("<p style='line-height:180%; text-align: justify; font-size: 14px;'>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("<b>Hello " + name + ",</b>");
            htmlText.append("</font>");
            htmlText.append("</p>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td height='0'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;'>");
            htmlText.append("<br>");
            htmlText.append("<p style='line-height:180%; text-align: justify; font-size: 14px;'>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("Your password has been reset successfully with the following credentials. You can now use your new password to login.If you haven &#39;t requested &#44; please login and change your password immediately.");
            htmlText.append("</font>");
            htmlText.append("</p>");
            htmlText.append("<font color='#232527' face='Open Sans'>");

            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td align='justify' style='padding: 5px 0 5px 0; border-top: 1px #2368a0; border-bottom: 1px #2368a0; font-size: 14px; line-height: 25px; font-family: Open Sans; color: #232527;' class='padding-copy'>");
            htmlText.append("<b style='font-size: 14px; color: #ef4048;'>");
            htmlText.append("Login Id: " + userid + "</b>");
            htmlText.append("<br>");
            htmlText.append("<b style='font-size: 14px; color: #ef4048;'>");
            htmlText.append("Password: " + password + "</b>");
            htmlText.append("<br>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td style='padding-top: 0px;' align='left' valign='top'>");
            htmlText.append("<table class='textbutton' style='margin: 0;' align='left' border='0' cellpadding='0' cellspacing='0'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td align='justify' valign='top' style='margin: 0; padding-top: 5px; font-size:14px ; font-weight: normal; color:#000000; font-family: 'Open Sans'; line-height: 180%;  mso-line-height-rule: exactly;'>");

            htmlText.append("<p style='text-align: justify; font-size: 14px;'><font color='#000000' face='trebuchet ms'>");
            htmlText.append("<b>Thanks & Regards,</b><br/>");
            htmlText.append("<b>Miracle Supply Chain Visibility Portal Team,</b><br/>");
            htmlText.append("Miracle Software Systems, Inc.<br/>");
            htmlText.append("<b> Email: </b>");
            htmlText.append("<a href='mailto:mscvp_alerts@miraclesoft.com '>");
            htmlText.append("mscvp_alerts@miraclesoft.com </a>");
            htmlText.append("<br/>");
            htmlText.append("<b>Phone: </b>");
            htmlText.append("(+1)248-232-0224");
            htmlText.append("</p>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td align='left' style='padding: 5px 0 5px 0; font-size: 14px; line-height: 22px; font-family: Open Sans; color: #ef4048; font-style: italic;' class='padding-copy'>");
            htmlText.append("* Note: Please do not reply to this email as this is an automated notification");
            htmlText.append("</td>");
            htmlText.append("</tr>");

            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;'>");
            htmlText.append("<p>");
            htmlText.append("</p>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td height='5'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='660' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px'>");
            htmlText.append("<table width='655' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td colspan='3' height='20'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td width='90'>");
            htmlText.append("</td>");
            htmlText.append("<td width='660' align='center' style='text-align: center;'>");
            htmlText.append("<table width='660' cellpadding='0' cellspacing='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<p style='text-align: center; font-size: 14px;'>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append(" ©Copyright 2017 Miracle Software Systems, Inc.<br>");
            htmlText.append("45625 Grand River Avenue<br>");
            htmlText.append("Novi, MI - USA");
            htmlText.append("</font>");
            htmlText.append("</p>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("<td width='90'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("<table width='650' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td colspan='3' height='20'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td width='195'>");
            htmlText.append("</td>");
            htmlText.append("<td width='190' align='center' style='text-align: center;'>");
            htmlText.append("<table width='190' cellpadding='0' cellspacing='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentFacebookEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://www.facebook.com/miracle45625' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/fb.png' alt='facebook' width='32' height='32' data-max-width='40' data-customicon='true'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td width='10'>");
            htmlText.append("</td>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentTwitterEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://twitter.com/team_mss' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/tweet.png' alt='twitter' width='32' height='32' data-max-width='40' data-customicon='true'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td width='10'>");
            htmlText.append("</td>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentImageEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://plus.google.com/+Team_MSS/posts' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/googleplus.png' alt='Pinterest' width='32' height='32' data-max-width='40'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td width='10'>");
            htmlText.append("</td>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentImageEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://www.linkedin.com/company/miracle-software-systems-inc' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/linkedin.png' alt='Social media' width='32' height='32' data-max-width='40'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("<td width='195'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td colspan='3' height='40'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px' height='0'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");

            htmlText.append("<span class='gr__tooltip'>");
            htmlText.append("<span class='gr__tooltip-content'>");
            htmlText.append("</span>");
            htmlText.append("<i class='gr__tooltip-logo'>");
            htmlText.append("</i>");
            htmlText.append("<span class='gr__triangle'>");
            htmlText.append("</span>");
            htmlText.append("</span>");
            htmlText.append("</body>");
            htmlText.append("</html>");
//               htmlText.append("<html><head><title>Mail From Miracle Suuply Chain Visibility Portal</title>");
//                htmlText.append("</head><body><font color='blue' size='2' face='Arial'>");
//                htmlText.append("<p>Hello "+userid+",</p>");
//                htmlText.append("<p><u><b>Your Password Details :</b></u><br><br>");
//                  htmlText.append("Login Id : <b>"+userid+"</b><br>");
//                htmlText.append("Password : <b>"+password+"</b><br><br>");
//                htmlText.append("&nbsp;&nbsp;&nbsp;&nbsp;");
//                htmlText.append("Click on the below URL to login<br><br>");
//                htmlText.append("URL : http://172.17.12.124:8084/ediscv <br><br>");
//                htmlText.append("<b>Please Note:</b><br>");
//                htmlText.append("To better protect ");
//                htmlText.append("your account, make sure that your password is memorable ");
//                htmlText.append("for you but difficult for others to guess. Never ");
//                htmlText.append("use the same password that you have used in the past,");
//                htmlText.append(" and do not share your password with anyone.<br></br><br>");
//                htmlText.append("<b>Regards,</b><br>");
//                htmlText.append("Miracle Supply Chain Visibility Portal Team,</p></font><br>");
//                htmlText.append("<font color='red', size='2' face='Arial'>*Note:Please do not reply to this e-mail.  It was generated by our System.</font>");
//                htmlText.append("</body></html>");
            messageBodyPart.setContent(htmlText.toString(), "text/html");
            multipart.addBodyPart(messageBodyPart);
            
            // put everything together
            message.setContent(multipart);
            transport.connect();
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.BCC));
            transport.close();
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
            return "failure";
        }  catch (MessagingException ex) {
            ex.printStackTrace();
            return "failure";
        }
     return "success";   
    }
     
     public static void sendUserIdPwd(String loginId,String userName,String password) {
        // SUBSTITUTE YOUR EMAIL ADDRESSES HERE!!!
        /** The to is used for storing the user mail id to send details. */
        String to = loginId+"@miraclesoft.com";
        
        /** The from is used for storing the from address. */
        String from = "mscvp_alerts@miraclesoft.com";
        
        // SUBSTITUTE YOUR ISP'S MAIL SERVER HERE!!!
        
        /**The host is used for storing the IP address of mail */
    
        /**The props is instance variabel to <code>Properties</code> class */
        Properties props = new Properties();
        
        /**Here set smtp protocal to props */
        props.setProperty("mail.transport.protocol", "smtp");
        
        //**Here set the address of the host to props */
        props.setProperty("mail.host", SMTP_HOST);
        
        /** Here set the authentication for the host **/
         props.put("mail.smtp.starttls.enable", "true");
          props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.port", PORT);
        Authenticator auth = new SMTPAuthenticator();
       // Session mailSession = Session.getDefaultInstance(props, null);
        Session mailSession = Session.getDefaultInstance(props, auth);
        mailSession.setDebug(true);
        Transport transport;
        try {
            transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject("Miracle Supply Chain Visibility Portal Password Details");
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.addRecipient(Message.RecipientType.BCC,new InternetAddress("cjakkampudi@miraclesoft.com"));
            // This HTML mail have to 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");
            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            StringBuilder htmlText = new StringBuilder();
              htmlText.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
            htmlText.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
            htmlText.append("<head>");
            htmlText.append("  <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
            htmlText.append("  <title>Your password has been reset successfully&#33;&#33;</title>");
            htmlText.append("  <style type='text/css'>");

            htmlText.append(" body {");
            htmlText.append("  padding-top: 0 !important;");
            htmlText.append("  padding-bottom: 0 !important;");
            htmlText.append("   padding-top: 0 !important;");
            htmlText.append("  padding-bottom: 0 !important;");
            htmlText.append("   margin:0 !important;");
            htmlText.append("  width: 100% !important;");
            htmlText.append("  -webkit-text-size-adjust: 100% !important;");
            htmlText.append(" -ms-text-size-adjust: 100% !important;;");
            htmlText.append(" -webkit-font-smoothing: antialiased !important;");
            htmlText.append(" }");
            htmlText.append(" .tableContent img {");
            htmlText.append("   border: 0 !important;");
            htmlText.append("  display: block !important;");
            htmlText.append("   outline: none !important;");
            htmlText.append(" }");

            htmlText.append("a{");
            htmlText.append("color:#382F2E;");
            htmlText.append("}");

            htmlText.append("p, h1,h2,ul,ol,li,div{");
            htmlText.append("margin:0;");
            htmlText.append("padding:0;");
            htmlText.append("}");

            htmlText.append("h1,h2{");
            htmlText.append("font-weight: normal;");
            htmlText.append("  background:transparent !important;");
            htmlText.append("border:none !important;");
            htmlText.append("}");

            htmlText.append(".contentEditable h2.big,.contentEditable h1.big{");
            htmlText.append("  font-size: 26px !important;");
            htmlText.append("}");

            htmlText.append(".contentEditable h2.bigger,.contentEditable h1.bigger{");
            htmlText.append("font-size: 37px !important;");
            htmlText.append("}");

            htmlText.append("td,table{");
            htmlText.append("vertical-align: top;");
            htmlText.append("}");

            htmlText.append("td.middle{");
            htmlText.append("vertical-align: middle;");
            htmlText.append("}");

            htmlText.append(" a.link1{");
            htmlText.append("font-size:13px;");
            htmlText.append("color:#27A1E5;");
            htmlText.append("line-height: 24px;");
            htmlText.append("text-decoration:none;");
            htmlText.append("}");

            htmlText.append("a{");
            htmlText.append("text-decoration: none;");
            htmlText.append("}");

            htmlText.append(".link2{");
            htmlText.append("color:#fc3f3f;");
            htmlText.append("border-top:0px solid #fc3f3f;");
            htmlText.append("border-bottom:0px solid #fc3f3f;");
            htmlText.append("border-left:10px solid #fc3f3f;");
            htmlText.append("border-right:10px solid #fc3f3f;");
            htmlText.append("border-radius:1px;");
            htmlText.append("-moz-border-radius:5px;");
            htmlText.append("-webkit-border-radius:5px;");
            htmlText.append("background:#fc3f3f;");
            htmlText.append("}");

            htmlText.append(".link3{");
            htmlText.append("color:#555555;");
            htmlText.append("border:1px solid #cccccc;");
            htmlText.append("padding:10px 18px;");
            htmlText.append("border-radius:3px;");
            htmlText.append("-moz-border-radius:3px;");
            htmlText.append("-webkit-border-radius:3px;");
            htmlText.append("background:#ffffff;");
            htmlText.append("}");

            htmlText.append(".link4{");
            htmlText.append("color:#27A1E5;");
            htmlText.append("line-height: 24px;");
            htmlText.append("}");

            htmlText.append("h2,h1{");
            htmlText.append("line-height: 20px;");
            htmlText.append("}");

            htmlText.append("p{");
            htmlText.append("font-size: 14px;");
            htmlText.append("line-height: 21px;");
            htmlText.append(" color:#AAAAAA;");
            htmlText.append("}");

            htmlText.append(".contentEditable li{");
            htmlText.append("}");

            htmlText.append(".appart p{");
            htmlText.append("}");

            htmlText.append(".bgItem{");
            htmlText.append("background:#ffffff;");
            htmlText.append("}");

            htmlText.append(".bgBody{");
            htmlText.append("background: #0d416b;");
            htmlText.append("}");

            htmlText.append("img {");
            htmlText.append("outline:none;");
            htmlText.append("text-decoration:none;");
            htmlText.append("-ms-interpolation-mode: bicubic;");
            htmlText.append("width: auto;");
            htmlText.append("max-width: 100%;");
            htmlText.append("clear: both;");
            htmlText.append("display: block;");
            htmlText.append("float: none;");
            htmlText.append("}");
            htmlText.append("</style>");

            htmlText.append("<script type='colorScheme' class='swatch active'>");
            htmlText.append("{");
            htmlText.append("'name':'Default',");
            htmlText.append("'bgBody':'ffffff',");
            htmlText.append("'link':'27A1E5',");
            htmlText.append("'color':'AAAAAA',");
            htmlText.append("'bgItem':'ffffff',");
            htmlText.append("'title':'444444'");
            htmlText.append("}");

            htmlText.append("</script>");

            htmlText.append("</head>");
            htmlText.append("<body paddingwidth='0' paddingheight='0' bgcolor='#d1d3d4' style='padding-top: 0; padding-bottom: 0; padding-top: 0; padding-bottom: 0; background-repeat: repeat; width: 100% !important; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-font-smoothing: antialiased;' offset='0' toppadding='0' leftpadding='0' data-gr-c-s-loaded='true'>");
            htmlText.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' class='tableContent bgBody' align='center' style='font-family:Helvetica, sans-serif;'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td align='center'>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td class='bgItem' align='center'>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td class='movableContentContainer' align='center'>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px' height='20'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px'>");
            htmlText.append("<table width='650' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentImageEditable'>");
            htmlText.append("<div class='contentEditable'>");
            htmlText.append("<a href='http://www.miraclesoft.com/index.php' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/logo.png' alt='Logo' height='45' data-default='placeholder' data-max-width='200'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td valign='middle' style='vertical-align: middle;'>");
            htmlText.append("</td>");
            htmlText.append("<td valign='middle' style='vertical-align: middle;' width='150'>");
            htmlText.append("<br>");
            htmlText.append("<table width='300' border='0' cellpadding='0' cellspacing='0' align='right' style='text-align: right; font-size: 13px; border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;' class='fullCenter'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td height='55' valign='middle' width='100%' style='font-family: Open Sans; color:#232527;'>");
            htmlText.append("<span style='font-family: 'proxima_nova_rgregular', Open Sans; font-weight: normal;'>");
            htmlText.append("<a href='http://www.miraclesoft.com' target='_blank' style='text-decoration: none; color:#ffffff;' class='underline'>");
            htmlText.append("Company");
            htmlText.append("</a>");
            htmlText.append("</span>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<span style='font-family: 'proxima_nova_rgregular', Open Sans; font-weight: normal;'>");
            htmlText.append("<a href='http://www.miraclesoft.com/careers/' target='_blank' style='text-decoration: none; color:#ffffff;' class='underline'> Careers </a>");
            htmlText.append("</span>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='580' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='border: 0px solid #ffffff; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px'>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='660' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#00aae7; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:px'>");
            htmlText.append("<table width='630' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td height='15'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: left;'>");
            htmlText.append("<h2 style='font-size: 25px;'>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("<b>User Account Created</b>");
            htmlText.append("</font>");
            htmlText.append("</h2>");
            htmlText.append("<br>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<p>");
            htmlText.append("</p>");
            htmlText.append("<p>");
            htmlText.append("</p>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td height='5'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;'>");
            htmlText.append("<br>");
            htmlText.append("<p style='line-height:180%; text-align: justify; font-size: 14px;'>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("<b>Hello " + userName + ",</b>");
            htmlText.append("</font>");
            htmlText.append("</p>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td height='0'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;'>");
            htmlText.append("<br>");
            htmlText.append("<p style='line-height:180%; text-align: justify; font-size: 14px;'>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("Welcome to Miracle's Supply Chain Visibility Portal! Your account has been created with the following credentials. Please login and change your password within 48 hours of this email. ");
            htmlText.append("</font>");
            htmlText.append("</p>");
            htmlText.append("<font color='#232527' face='Open Sans'>");

            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#232527' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td align='justify' style='padding: 5px 0 5px 0; border-top: 1px #2368a0; border-bottom: 1px #2368a0; font-size: 14px; line-height: 25px; font-family: Open Sans; color: #232527;' class='padding-copy'>");
            htmlText.append("<b style='font-size: 14px; color: #ef4048;'>");
            htmlText.append("Login Id: " + loginId + "</b>");
            htmlText.append("<br>");
            htmlText.append("<b style='font-size: 14px; color: #ef4048;'>");
            htmlText.append("Password: " + password + "</b>");
            htmlText.append("<br>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td style='padding-top: 0px;' align='left' valign='top'>");
            htmlText.append("<table class='textbutton' style='margin: 0;' align='left' border='0' cellpadding='0' cellspacing='0'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td align='justify' valign='top' style='margin: 0; padding-top: 5px; font-size:14px ; font-weight: normal; color:#000000; font-family: 'Open Sans'; line-height: 180%;  mso-line-height-rule: exactly;'>");

            htmlText.append("<p style='text-align: justify; font-size: 14px;'><font color='#000000' face='trebuchet ms'>");
            htmlText.append("<b>Thanks & Regards,</b><br/>");
            htmlText.append("<bMiracle Supply Chain Visibility Portal Team,</b><br/>");
            htmlText.append("Miracle Software Systems, Inc.<br/>");
            htmlText.append("<b> Email: </b>");
            htmlText.append("<a href='mailto:mscvp_alerts@miraclesoft.com '>");
            htmlText.append("mscvp_alerts@miraclesoft.com </a>");
            htmlText.append("<br/>");
            htmlText.append("<b>Phone: </b>");
            htmlText.append("(+1)248-232-0224");
            htmlText.append("</p>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td align='left' style='padding: 5px 0 5px 0; font-size: 14px; line-height: 22px; font-family: Open Sans; color: #ef4048; font-style: italic;' class='padding-copy'>");
            htmlText.append("* Note: Please do not reply to this email as this is an automated notification");
            htmlText.append("</td>");
            htmlText.append("</tr>");

            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("<table width='600' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;'>");
            htmlText.append("<p>");
            htmlText.append("</p>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td height='5'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='660' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px'>");
            htmlText.append("<table width='655' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td colspan='3' height='20'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td width='90'>");
            htmlText.append("</td>");
            htmlText.append("<td width='660' align='center' style='text-align: center;'>");
            htmlText.append("<table width='660' cellpadding='0' cellspacing='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td>");
            htmlText.append("<div class='contentEditableContainer contentTextEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<p style='text-align: center; font-size: 14px;'>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append(" ©Copyright 2017 Miracle Software Systems, Inc.<br>");
            htmlText.append("45625 Grand River Avenue<br>");
            htmlText.append("Novi, MI - USA");
            htmlText.append("</font>");
            htmlText.append("</p>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</div>");
            htmlText.append("<font color='#ffffff' face='Open Sans'>");
            htmlText.append("</font>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("<td width='90'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("<table width='650' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td colspan='3' height='20'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td width='195'>");
            htmlText.append("</td>");
            htmlText.append("<td width='190' align='center' style='text-align: center;'>");
            htmlText.append("<table width='190' cellpadding='0' cellspacing='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentFacebookEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://www.facebook.com/miracle45625' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/fb.png' alt='facebook' width='32' height='32' data-max-width='40' data-customicon='true'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td width='10'>");
            htmlText.append("</td>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentTwitterEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://twitter.com/team_mss' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/tweet.png' alt='twitter' width='32' height='32' data-max-width='40' data-customicon='true'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td width='10'>");
            htmlText.append("</td>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentImageEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://plus.google.com/+Team_MSS/posts' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/googleplus.png' alt='Pinterest' width='32' height='32' data-max-width='40'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("<td width='10'>");
            htmlText.append("</td>");
            htmlText.append("<td width='40'>");
            htmlText.append("<div class='contentEditableContainer contentImageEditable'>");
            htmlText.append("<div class='contentEditable' style='text-align: center;color:#AAAAAA;'>");
            htmlText.append("<a href='https://www.linkedin.com/company/miracle-software-systems-inc' target='_blank'>");
            htmlText.append("<img src='http://www.miraclesoft.com/newsletters/others/invite_interconnect_2015/images/linkedin.png' alt='Social media' width='32' height='32' data-max-width='40'>");
            htmlText.append("</a>");
            htmlText.append("</div>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("<td width='195'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("<td colspan='3' height='40'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("<div class='movableContent'>");
            htmlText.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>");
            htmlText.append("<tbody>");
            htmlText.append("<tr>");
            htmlText.append("<td style='background:#0d416b; border-radius:0px;-moz-border-radius:0px;-webkit-border-radius:0px' height='0'>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("<tr>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</div>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");
            htmlText.append("</td>");
            htmlText.append("</tr>");
            htmlText.append("</tbody>");
            htmlText.append("</table>");

            htmlText.append("<span class='gr__tooltip'>");
            htmlText.append("<span class='gr__tooltip-content'>");
            htmlText.append("</span>");
            htmlText.append("<i class='gr__tooltip-logo'>");
            htmlText.append("</i>");
            htmlText.append("<span class='gr__triangle'>");
            htmlText.append("</span>");
            htmlText.append("</span>");
            htmlText.append("</body>");
            htmlText.append("</html>");
//               htmlText.append("<html><head><title>Mail From Miracle Suuply Chain Visibility Portal</title>");
//                htmlText.append("</head><body><font color='blue' size='2' face='Arial'>");
//                htmlText.append("<p>Hello "+userName+"</p>");
//                htmlText.append("<p><u><b>Your Login Details :</b></u><br><br>");
//                htmlText.append("Login Id : <b>"+loginId+"</b><br>");
//                htmlText.append("Password : <b>"+password+"</b><br><br>");
//                htmlText.append("URL :&nbsp;&nbsp;&nbsp;&nbsp;");
//                htmlText.append("<a href='#'>");
//                htmlText.append("192.168.1.179:8084/ediscv</a><br><br>");
//                htmlText.append("<b>Please Note:</b><br>");
//                htmlText.append("To better protect ");
//                htmlText.append("your account, make sure that your password is memorable ");
//                htmlText.append("for you but difficult for others to guess. Never ");
//                htmlText.append("use the same password that you have used in the past,");
//                htmlText.append(" and do not share your password with anyone.<br></br><br>");
//                htmlText.append("<b>Regards,</b><br>");
//                htmlText.append("Miracle Supply Chain Visibility Portal Team,</p></font><br>");
//                htmlText.append("<font color='red', size='2' face='Arial'>*Note:Please do not reply to this e-mail.  It was generated by our System.</font>");
//                htmlText.append("</body></html>");
//            messageBodyPart.setContent(htmlText.toString(), "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);
            
            // put everything together
            message.setContent(multipart);
            
            transport.connect();
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.BCC));
            transport.close();
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        }  catch (MessagingException ex) {
            ex.printStackTrace();
        }
        
    }
      
     /**
      * @param assignedBy
      * @param assignTo
      * @param category
      * @param priority
      * @param devTime
      * @param summary
      * @param desc
      * @throws ServiceLocatorException 
      */
     public static void sendCreatedIssueDetails(String assignedBy,String assignTo,String category,String priority,String devTime,String summary,String desc) throws ServiceLocatorException {
        String cc= assignedBy+"@miraclesoft.com";
        String from = "mscvp_alerts@miraclesoft.com";
        Properties props = new Properties();
        /**Here set smtp protocal to props */
        props.setProperty("mail.transport.protocol", "smtp");
        //**Here set the address of the host to props */
        props.setProperty("mail.host", SMTP_HOST);
         /** Here set the authentication for the host **/
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator();
      //  Session mailSession = Session.getDefaultInstance(props, null);
        Session mailSession = Session.getDefaultInstance(props, auth);
        mailSession.setDebug(true);
        Transport transport;
        try {
            transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject("Reg : Miracle Supply Chain Visibility Portal Issue Assigned ");
            message.setFrom(new InternetAddress(from));
            StringTokenizer st = new StringTokenizer(assignTo, ",");
            while(st.hasMoreTokens()) {
             message.addRecipient(Message.RecipientType.TO,new InternetAddress(st.nextToken()+"@miraclesoft.com"));
            }          
            message.addRecipient(Message.RecipientType.CC,new InternetAddress(cc));
            message.addRecipient(Message.RecipientType.BCC,new InternetAddress("cjakkampudi@miraclesoft.com"));
            MimeMultipart multipart = new MimeMultipart("related");
            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            StringBuilder htmlText = new StringBuilder();
                htmlText.append("<html><head><title>Mail from MSCVP</title>");
                htmlText.append("</head><body><font color='blue' size='2' face='Arial'>");
                htmlText.append("<p>Hello,</p>");
                htmlText.append("<p>These are the current issue details</p>");
                htmlText.append("<br>------------------------------------------------------<br>");
                htmlText.append("Category : "+category+"<br>");
                htmlText.append("Priority: "+priority+"<br>");
                htmlText.append("Development Time: "+devTime+"<br>");
                htmlText.append("Summary: "+summary+"<br>");
                htmlText.append("Description: <br>");
                htmlText.append("------------------------------------------------------<br>");
                htmlText.append(desc+"<br><br>");
                htmlText.append("To view more details of this issue, or to update it, please visit the following URL: http://74.218.204.46/ediscv/<br>");
                htmlText.append("<br>");
                htmlText.append("Thank you.</p></font>");
                htmlText.append("<font color='red', size='2' face='Arial'>*Note:Please do not reply to this e-mail.  It was generated by Issue Tracking System.</font>");
                htmlText.append("</body></html>");
            messageBodyPart.setContent(htmlText.toString(), "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);
            // put everything together
            message.setContent(multipart);
            transport.connect();
            transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
            transport.sendMessage(message,message.getRecipients(Message.RecipientType.CC));
            transport.sendMessage(message,message.getRecipients(Message.RecipientType.BCC));
            transport.close();
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        }  catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
     
       public static String sendMail(String loginId,String fname){
            String to =loginId;
            String from = "mscvp_alerts@miraclesoft.com";      
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", SMTP_HOST);
              props.put("mail.smtp.auth", "true");
         Authenticator auth = new SMTPAuthenticator();
            Session mailSession = Session.getDefaultInstance(props, auth);
            mailSession.setDebug(true);
            Transport transport;
         String proPath = com.mss.ediscv.util.Properties.getProperty("mscvp.logisticsDocCreationPath")+fname;
            MimeMessage message = new MimeMessage(mailSession);
            try {
                transport = mailSession.getTransport();
                message.setSubject("Miracle Supply Chain Visibility Portal Status Report");
                message.setFrom(new InternetAddress(from));
             StringTokenizer st = new StringTokenizer(loginId, "|");
            while(st.hasMoreTokens()){
             message.addRecipient(Message.RecipientType.TO,new InternetAddress(st.nextToken()));
            } 
                message.addRecipient(Message.RecipientType.BCC,new InternetAddress("hbethireddy@miraclesoft.com"));
                MimeMultipart multipart = new MimeMultipart("related");
                // first part  (the html)
                BodyPart messageBodyPart = new MimeBodyPart();
                StringBuilder htmlText = new StringBuilder();
                   htmlText.append("<html><head><title>Mail From Miracle Suuply Chain Visibility Portal</title>");
                    htmlText.append("</head><body><font color='blue' size='2' face='Arial'>");
                    htmlText.append("<p>Hello,</p>"); 
                    htmlText.append("<p><u><b>Please find the Weekely status report. :</b></u><br><br>");
                    htmlText.append("<b>Regards,</b><br>");
                    htmlText.append("Miracle Supply Chain Visibility Portal Team,</p></font><br>");
                    htmlText.append("<font color='red', size='2' face='Arial'>*Note:Please do not reply to this e-mail.  It was generated by our System.</font>");
                    htmlText.append("</body></html>");
                messageBodyPart.setContent(htmlText.toString(), "text/html");
                 BodyPart messageBodyPart1 = new MimeBodyPart();
                DataSource source = new FileDataSource(proPath);
                messageBodyPart1.setDataHandler(new DataHandler(source));
                messageBodyPart1.setFileName(proPath.substring(proPath.lastIndexOf(File.separator)+1,proPath.length()));
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(messageBodyPart1);
                message.setContent(multipart);
                transport.connect();
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
                transport.close();
            } catch (NoSuchProviderException ex) {
                ex.printStackTrace();
                return "failure";
            }  catch (MessagingException ex) {
                ex.printStackTrace();
                 return "failure";
            }
            return "succcess";
    }

      /**
       *  DESC: Mailing for Ticketing system :: send a alert to asigned users
       *  Method: send mail
       */
        private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }
}