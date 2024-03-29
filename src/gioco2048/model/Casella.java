package gioco2048.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

 
public final class Casella {
     
    private static final int LARGHEZZA_CASELLA = 75; //prima era 120
     
    private int valore;
     
    private Point posizione_casella;
     
    public Casella(int valore) {
        set_valore(valore);
    }
 
    public static int get_larghezza_casella() {
        return LARGHEZZA_CASELLA;
    }
     
    public int get_valore() {
        return valore;
    }
 
    public void set_valore(int value) {
        this.valore = value;
    }
     
    public boolean is_valore_zero() {
        if(valore == 0)
            return true;
        else
            return false;
    }
     
    public void set_posizione_casella(int x, int y) {
        this.posizione_casella = new Point(x,y);
    }
  
    public void disegna_casella(Graphics g) {
        if (valore == 0) {
            g.setColor(new Color(ColorUtil.color_vuota)); //prima era Color.DARK_GREY
            g.fillRoundRect(posizione_casella.x, posizione_casella.y, LARGHEZZA_CASELLA, LARGHEZZA_CASELLA, 15, 15); // per contenitori celle stondate
        } else {   
            //Font font = g.getFont();
            Font font = new Font("Bebas Neue Regular", Font.PLAIN, 28);
            FontRenderContext frc = new FontRenderContext(null, true, true);

            String valore_stringa = Integer.toString(valore);
            BufferedImage image = crea_immagine(font, frc, LARGHEZZA_CASELLA, valore_stringa);
            g.drawImage(image, posizione_casella.x, posizione_casella.y, null);
        }
    }
    
    private BufferedImage crea_immagine(Font font, FontRenderContext frc, int larghezza_casella, String s) { 
 
        Font largeFont = font.deriveFont((float) (larghezza_casella / 4));
        
        Rectangle2D r = largeFont.getStringBounds(s, frc); //
        
        int rWidth = (int) Math.round(r.getWidth());    //larghezza of rectangle
        int rHeight = (int) Math.round(r.getHeight());  //altezza of rectangle
        
        
        BufferedImage image = new BufferedImage(larghezza_casella, larghezza_casella, BufferedImage.TYPE_INT_RGB);
         
        Graphics gg = image.getGraphics();
        gg.setColor(new Color(ColorUtil.color_griglia));
        gg.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        gg.setColor(ColorUtil.get_colore_casella(valore));
        //la disegna stondata
        gg.fillRoundRect(0, 0, image.getWidth(), image.getHeight(), 25, 25); 

        
        int x = (larghezza_casella / 2) - (rWidth / 2);
        int y = (larghezza_casella / 2) - (rHeight / 2) + 18;
        
        gg.setFont(largeFont);
        
        
        gg.setColor(ColorUtil.get_colore_testo(valore));
        gg.drawString(s, x, y);
        gg.dispose();
        return image;
    } 
 
}