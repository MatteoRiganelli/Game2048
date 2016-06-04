package gioco2048.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gioco2048.model.ModelloGioco;
import gioco2048.view.SchermataBase;

public class StartGame implements ActionListener {
     
    private SchermataBase schermata_base;
     
    private ModelloGioco modello_gioco;
     
    public StartGame(SchermataBase frame, ModelloGioco model) {
        this.schermata_base = frame;
        this.modello_gioco = model;
    }
 
    //quando implemento l'interfaccia -ActionListener- il metodo che immplementà 
    //l'interfaccia, richiamerà in automatico il meotodo -actionPerformed-
    //infatti il costruttore si preoccuperà di istanziare le classi necessarie ovvero
    //Game2048Frame e Game2048Model e l'azione del metodo actionPerformed
    //permetterà di eseguire i metodi ad esse collegate, necessari per inizializzare 
    //il gioco
    @Override
    public void actionPerformed(ActionEvent event) {
        modello_gioco.imposta_punteggio();
        modello_gioco.inizializza_griglia();
        modello_gioco.set_puoi_muovere(true); // va a true appena clicco start
        modello_gioco.aggiungi_casella();
        modello_gioco.aggiungi_casella();
         
        schermata_base.repaint_griglia_panel();
        schermata_base.aggiorna_punteggio_panel();
    }
 
}
