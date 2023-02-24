package com.hk.im.client.service;
 
import java.io.File;
import java.util.List;
 
public interface MailService {
 
 
    void sendSimpleMail(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content);
 
    void sendMailWithAttachments(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content,
                                 List<File> files);
 
    void sendMailWithImage(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content,
                           String[] imagePaths, String[] imageId);
 
    void sendHtmlMailThymeLeaf(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content);
 
 
}