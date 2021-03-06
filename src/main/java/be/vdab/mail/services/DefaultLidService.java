package be.vdab.mail.services;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.mailing.LidMailing;
import be.vdab.mail.repositories.LidRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@Transactional
class DefaultLidService implements LidService{
    private final LidRepository lidRepository;
    private final LidMailing lidMailing;

    public DefaultLidService(LidRepository lidRepository, LidMailing lidMailing) {
        this.lidRepository = lidRepository;
        this.lidMailing = lidMailing;
    }

    @Override
    public void registreer(Lid lid, String ledenUrl){
        lidRepository.save(lid);
        lidMailing.stuurMailNaRegistratie(lid, ledenUrl);
    }

    @Override
    @Transactional(readOnly = true) //let erop dat je juiste transational importeert
    public Optional<Lid> findById(long id) {
        return lidRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 5000)
    public void stuurMailMetAantalLeden() {
        lidMailing.stuurMailMetAantalLeden(lidRepository.count());
    }
}
