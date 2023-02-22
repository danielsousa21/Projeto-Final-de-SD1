package com.ventoapp.eventoapp.repository;

import com.ventoapp.eventoapp.models.Convidado;
import com.ventoapp.eventoapp.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvidadoRepository extends JpaRepository<Convidado, String> {
    Iterable<Convidado> findByEvento(Evento evento);
    Convidado findByRg(String rg);
}
