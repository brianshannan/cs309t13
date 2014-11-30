package edu.iastate.controllers;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.iastate.dao.MemberDao;
import edu.iastate.dao.MessageDao;
import edu.iastate.models.Member;
import edu.iastate.models.Message;

@Controller
@RequestMapping("/mail")
public class MailController {

    @RequestMapping(method = RequestMethod.GET)
    public String getMail(Model model, HttpSession session,
            @RequestParam(value = "inbox", required = false) String inbox,
            @RequestParam(value = "sentmail", required = false) String sentmail,
            @RequestParam(value = "drafts", required = false) String drafts,
            @RequestParam(value = "deleted", required = false) String deleted) {

        if (session.getAttribute("member") == null)
            return "redirect:../denied";

        Member member = (Member) session.getAttribute("member");
        Set<Message> messages;
        if (inbox != null)
            messages = member.getMail().getMessages();
        else if (sentmail != null)
            messages = member.getMail().getSentMail();
        else if (drafts != null)
            messages = member.getMail().getDrafts();
        else if (deleted != null)
            messages = member.getMail().getDeleted();
        else
            messages = member.getMail().getMessages();
        model.addAttribute("messages", messages);
        return "mail";
    }

    @RequestMapping(value = "/setMessagesAsViewed", method = RequestMethod.POST)
    public @ResponseBody void setNotificationsAsViewed(HttpSession session) {

        if (session.getAttribute("member") == null)
            return;

        // set up database access objects
        MessageDao messageDao = new MessageDao();

        Member member = (Member) session.getAttribute("member");
        Set<Message> unviewedMessages = member.getMail().getUnviewedMessages();
        for (Message unviewedMessage : unviewedMessages) {
            unviewedMessage.setViewed(true);
            messageDao.save(unviewedMessage);
        }
        session.setAttribute("member", new MemberDao().getMemberById(member.getId()));
    }

    @RequestMapping(value = "/doesRecipientExist", method = RequestMethod.GET)
    public @ResponseBody String doesRecipientExist(HttpSession session,
            @RequestParam(value = "recipient") String username) {
        if (session.getAttribute("member") == null)
            return "redirect:../denied";
        MemberDao memberDao = new MemberDao();
        Member member = memberDao.getMemberByUsername(username);
        String isValid;
        if (member == null) {
            isValid = "{ \"valid\": false }";
        } else {
            isValid = "{ \"valid\": true }";
        }
        return isValid;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public @ResponseBody void send(HttpSession session, @RequestParam(value = "recipient") String recipientUsername,
            @RequestParam(value = "subject") String subject, @RequestParam(value = "body") String body) {
        if (session.getAttribute("member") == null)
            return;
        MemberDao memberDao = new MemberDao();
        Member sender = (Member) session.getAttribute("member");
        Member recipient = memberDao.getMemberByUsername(recipientUsername);
        Message newMessage = new Message(subject, body, sender, recipient);
        new MessageDao().save(newMessage);
        session.setAttribute("member", memberDao.getMemberById(sender.getId()));
    }
}
