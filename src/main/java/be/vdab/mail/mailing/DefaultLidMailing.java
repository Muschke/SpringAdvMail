package be.vdab.mail.mailing;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.exceptions.KanMailNietZendenException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
class DefaultLidMailing implements LidMailing {
    private final JavaMailSender sender;
    DefaultLidMailing(JavaMailSender sender) {
        this.sender = sender;
    }
    @Override
    public void stuurMailNaRegistratie(Lid lid, String ledenUrl) {
        try {
            //var message = new SimpleMailMessage(); --> lege mail zonder opmaak
            //message.setTo(lid.getEmailAdres());
            //message.setSubject("Geregistreerd");
            //message.setText("Je bent nu lid. Je nummer is:" + lid.getId());
            var message = sender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setTo(lid.getEmailAdres());
            helper.setSubject("Geregistreerd");
            //helper.setText("<h1>Je bent nu lid.</h1>Je nummer is:" + lid.getId(), true); --> we gaan deze vervangen om ook hyperlink mee te geven
            var urlVanDeLidInfo = ledenUrl + "/" + lid.getId();
            var tekst = "<h1>Je bent nu lid.</h1>Je nummer is:" + lid.getId() + "." +
                    "Je ziet je info <a href='" + urlVanDeLidInfo + "'>hier</a>.";
            helper.setText(tekst, true);

            sender.send(message); //deze staat bij beide
            /*je moet ook extra messagingexception toevoegen*/
        } catch (MailException | MessagingException ex) {
            throw new KanMailNietZendenException(ex);
        }
    }
}
