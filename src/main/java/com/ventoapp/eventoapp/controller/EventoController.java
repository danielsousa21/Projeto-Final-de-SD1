package com.ventoapp.eventoapp.controller;

import com.ventoapp.eventoapp.models.Convidado;
import com.ventoapp.eventoapp.models.Evento;
import com.ventoapp.eventoapp.repository.ConvidadoRepository;
import com.ventoapp.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    ConvidadoRepository convidadoRepository;

    @GetMapping("/cadastrarEvento")
    private String form(){
        return "evento/formEvento";
    }

    @PostMapping("/cadastrarEvento")
    private String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Campos Invalidos!");
            return "redirect:/cadastrarEvento";
        }
        eventoRepository.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento Cadastrado");
        return "redirect:/cadastrarEvento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEventos(){
        ModelAndView modelAndView = new ModelAndView("index");
        Iterable<Evento> eventos = eventoRepository.findAll();
        modelAndView.addObject("eventos", eventos);
        return modelAndView;
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo){
        Evento evento = eventoRepository.findByCodigo(codigo);
        ModelAndView modelAndView = new ModelAndView("evento/detalhesEvento");
        modelAndView.addObject("evento", evento);

        Iterable<Convidado> convidados = convidadoRepository.findByEvento(evento);
        modelAndView.addObject("convidados", convidados);

        return modelAndView;

    }


    @RequestMapping("/deletarEvento")
    public String deletarEvento(long codigo){
        Evento evento = eventoRepository.findByCodigo(codigo);
        eventoRepository.delete(evento);
        return "redirect:/eventos";
    }


    @RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Campos Invalidos!");
            return "redirect:/{codigo}";
        }
        Evento evento = eventoRepository.findByCodigo(codigo);
        convidado.setEvento(evento);
        convidadoRepository.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado Cadastrado");
        return "redirect:/{codigo}";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg){
        Convidado convidado = convidadoRepository.findByRg(rg);
        convidadoRepository.delete(convidado);

        Evento evento = convidado.getEvento();
        long codigoLong = evento.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/" + codigo;
    }

}
