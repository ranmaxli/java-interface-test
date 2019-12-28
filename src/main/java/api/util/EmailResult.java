package api.util;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.annotations.Test;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liran on 2017/1/18.
 */
public class EmailResult extends TestListenerAdapter{
    public static String workdir = System.getProperty("user.dir");
    String to = "";// 收件人
    String from = "";// 发件人
    String host = "";// smtp主机
    String username = "";
    String password = "";
    String filename = "";// 附件文件名
    String subject = "";// 邮件主题
    String content = "";// 邮件正文
    Vector file = new Vector();// 附件文件集合

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        resultSendMail();//发送邮件

    }

    @Override
    public List<ITestContext> getTestContexts() {
        Reporter.log("super.getTestContexts()");
        return super.getTestContexts();

    }

    /**
     * 方法说明：默认构造器
     */
    public EmailResult() {
    }

    /**
     * 方法说明：构造器，提供直接的参数传入
     */
    public EmailResult(String to, String from, String smtpServer, String username,
                       String password, String subject, String content) {
        this.to = to;
        this.from = from;
        this.host = smtpServer;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 方法说明：设置邮件服务器地址 输入参数：String host 邮件服务器地址名称 返回类型：
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 方法说明：设置登录服务器校验密码
     */
    public void setPassWord(String pwd) {
        this.password = pwd;
    }

    /**
     * 方法说明：设置登录服务器校验用户
     */
    public void setUserName(String usn) {
        this.username = usn;
    }

    /**
     * 方法说明：设置邮件发送目的邮箱
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * 方法说明：设置邮件发送源邮箱
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 方法说明：设置邮件主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 方法说明：设置邮件内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 方法说明：把主题转换为中文
     */
    public String transferChinese(String strText) {
        try {
            strText = MimeUtility.encodeText(new String(strText.getBytes(),
                    "UTF-8"), "GB2312", "B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strText;
    }

    /**
     * 方法说明：往附件组合中添加附件
     */
    public void attachfile(String fname) {
        file.addElement(fname);
    }
    @Test
    public void resultSendMail() {

        Date nowdate = new Date();
        SimpleDateFormat fmtdate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Properties prop = new Properties();
        try {
            // 读取属性文件a.properties
            InputStream in = new BufferedInputStream(new FileInputStream(
                    workdir + "\\src\\main\\resources\\email.properties"));
            prop.load(in); // /加载属性列表
            EmailResult emailResult = new EmailResult();
            emailResult.setHost(prop.getProperty("host"));
            emailResult.setUserName(prop.getProperty("userName"));
            emailResult.setPassWord(prop.getProperty("passWord"));
            emailResult.setTo(prop.getProperty("toEmail"));
            emailResult.setFrom(prop.getProperty("fromEmail"));
            emailResult.setSubject("（1001、1002、1003版本）java接口测试结果，执行时间:" + (fmtdate.format(nowdate)));
            emailResult.setContent("<span style=\"font-size:12px;color:red;\">接口自动化测试结果如下，具体的接口请求地址请查看附件output.html，执行失败的接口请开发在jira中查看缺陷记录<br>"+
                    "<br>(此邮件为自动发送，请勿回复)</span><br><br>"+
                    viewTable()+
                    "<font size=3>Test one：ranmaxli</Font>");
            emailResult.attachfile(workdir+"\\test-output\\html\\output.html");
            System.out.println(emailResult.sendMail());

            in.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * 方法说明：发送邮件 输入参数： 返回类型：boolean 成功为true，反之为false
     */
    public boolean sendMail() {

        // 构造mail session
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // 构造MimeMessage 并设定基本的值
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));

            // msg.addRecipients(Message.RecipientType.TO, address);
            // //这个只能是给一个人发送email
            msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(to));
            subject = transferChinese(subject);
            msg.setSubject(subject);

            // 构造Multipart
            Multipart mp = new MimeMultipart();

            // 向Multipart添加正文
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setContent(content, "text/html;charset=gb2312");

            // 向MimeMessage添加（Multipart代表正文）
            mp.addBodyPart(mbpContent);

            // 向Multipart添加附件
            Enumeration efile = file.elements();
            while (efile.hasMoreElements()) {

                MimeBodyPart mbpFile = new MimeBodyPart();
                filename = efile.nextElement().toString();
                FileDataSource fds = new FileDataSource(filename);
                mbpFile.setDataHandler(new DataHandler(fds));
                String filename = new String(fds.getName().getBytes(),
                        "ISO-8859-1");

                mbpFile.setFileName(filename);
                // 向MimeMessage添加（Multipart代表附件）
                mp.addBodyPart(mbpFile);
            }

            file.removeAllElements();
            // 向Multipart添加MimeMessage
            msg.setContent(mp);
            msg.setSentDate(new Date());
            msg.saveChanges();
            // 发送邮件

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }



    public static String viewTable() {

        try {
            String encoding = "UTF-8";
            String line = "";
            File file = new File(workdir+"\\test-output\\html\\overview.html");

            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);// 通过缓冲区提取数据
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    line = line + lineTxt;
                }
                return line;
            }
            System.out.println("line:"+line);

        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return null;

    }
}