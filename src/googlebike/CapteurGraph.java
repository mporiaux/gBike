/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebike;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.*;



public class CapteurGraph extends Frame implements ActionListener,MouseListener {
static{
    System.loadLibrary("K8055D");
}
  ICLibrary cl =null;
  float dimRoue=(float)2.1;
  float vit=0;
  float dist=0;
  int tTot=0;
  int rebond=50;
  long delai=2000;
  long dtot=0;
  boolean gauche=false;
  boolean droite=false;
  boolean frein=false;
  int npass=4;
  TextField tfVit=new TextField(); 
  TextField tfDist=new TextField();
  CommandeGStreet cgs=new CommandeGStreet();
  LecteurAltitude lalt=new LecteurAltitude();
  int depv=0,deph=0;
  BufferedImage image=null;
  public int alt=0,altold=0;
  JLabel lbValeur=new JLabel("--------------");

  JButton vplus=new JButton("^");
  JButton vmoins=new JButton("v");
  JButton hplus=new JButton("->");
  JButton hmoins=new JButton("<-");
  JButton go=new JButton("GO");
  // JLabel zone=new JLabel();
Robot robot=null;
    Cible cible=new Cible();
  
  private static googlebike.CapteurGraph cp=null;
int pente=0;
float distold=0;
JLabel etipente=new JLabel("Pente:");
JLabel lbpente=new JLabel("0%");
Point p=new Point(1,1);
    public void setDelai(long delai) {
        this.delai = delai;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public void setDtot(long dtot) {
        this.dtot = dtot;
    }

    public void setRebond(int rebond) {
        this.rebond = rebond;
    }

    public void setNpass(int npass){
        this.npass=npass;
    }

    public long getDelai() {
        return delai;
    }

    public float getDimRoue() {
        return dimRoue;
    }

    public float getDist() {
        return dist;
    }

    public long getDtot() {
        return dtot;
    }

    public int getRebond() {
        return rebond;
    }

    public int gettTot() {
        return tTot;
    }

    public float getVit() {
        return vit;
    }


private CapteurGraph(){

   WindowListener listener = new WindowAdapter() {
    public void windowClosing(WindowEvent w) {
        System.exit(0);
      }
    };
   addWindowListener(listener);
 cl =ICLibrary.instance;
 long v=0;
 cl.CloseDevice();
  v = cl.OpenDevice(0);
 System.out.println("valeur carte="+v);
   if(v==-1){
       System.out.println("erreur de connexion");
       System.exit(0);
   }
 System.out.println("capteur créé");
 setLayout(new GridLayout(4,4,5,5));
 setSize(800, 75);
   Label lbVit=new Label("Vitesse ");
   add(lbVit);
   add(tfVit);
   Label lbDist=new Label("Distance ");
   add(lbDist);
   add(tfDist);
   add(lbValeur);
  // add(zone);
   add(etipente);
   add(lbpente);
   add(vmoins);
   add(vplus);
   add(hmoins);
   add(hplus);
   add(go);
     vmoins.addActionListener(this);
       vplus.addActionListener(this);
       hmoins.addActionListener(this);
       hplus.addActionListener(this);
       go.addActionListener(this);
       lbValeur.setFont(new Font("TimesRoman",Font.BOLD+Font.ITALIC,20));
   
   Font f = new Font("Serif", Font.PLAIN, 20);
   lbVit.setFont(f);
   lbDist.setFont(f);
   tfVit.setFont(f);
   tfDist.setFont(f);
    
       
      try{
        robot = new Robot();
        }
      catch(Exception e){}
      addMouseListener(this);
   this.setLocationByPlatform(true);
   setVisible(true);
   // cible.pack();
  //  cible.setVisible(true);
 
  
 }

public static googlebike.CapteurGraph getInstance(){
   if (cp==null) return cp=new googlebike.CapteurGraph();
    else return cp;
}

public void setDimRoue(float dimRoue){
    this.dimRoue=dimRoue;
}

public boolean isGauche(){
   return cl.ReadDigitalChannel(2);
}

public boolean isDroite(){
    return cl.ReadDigitalChannel(3);
}

public boolean isFrein(){
   return cl.ReadDigitalChannel(4);
}


public void init(){

    
   System.out.println("debut d'initialisation");
    cl.ResetCounter(1);
    cl.SetCounterDebounceTime(1,rebond);
    System.out.println("fin d'initialisation");

}
 public void tourne(){
     cgs.start();
      DecimalFormat formater1 = new DecimalFormat("#0 km/h");
       DecimalFormat formater2 = new DecimalFormat("#0.000 km");
    int passage=0;
    int n1=0,n2=0;
    System.out.println("compteur simple démarre1");
  n1 =(int)cl.ReadCounter(1);
   
   do{
       /*on fait des calculs 1 fois tous les 4 passages pour 
         réduire les délais de mesure et permettre de changer de direction 
         plus rapidement*/
    passage++;
    if(passage%npass==0){
    passage=0;
   n2=(int)cl.ReadCounter(1);
  //   n2=n1+3;
   
    dist=(n2-n1)*dimRoue;
    if(dtot<0)dtot=0;
    dtot+=(int)dist;
    vit= dist/(delai*npass)*3600 ;
    cgs.setValeur((int)vit); 
    altold=alt;
    alt=lalt.getAltitude();
    lbValeur.setText(""+alt);
    if(altold!=alt){
        
        pente=(int)((alt-altold)/(dtot-distold)*100);
        distold=dtot;
        lbpente.setText(pente+"%");
    }
    tfVit.setText(formater1.format(vit));
    tfDist.setText(formater2.format(dtot/1000.0));
      n1=n2;
      
     }
     
      
      cgs.setDroite(isDroite());
      cgs.setGauche(isGauche());
 /*  try{
      File f=new File("vitesse.dta");
      FileWriter  fout=new FileWriter(f);
      fout.write(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
      fout.close();
     }
   catch(Exception e){System.out.println("erreur d'écriture"+e);}
  */   
    
  
     try{
      Thread.sleep(delai);
     }catch(Exception e2){}
     

  }while(true);

 }


 
 
 public void actionPerformed(ActionEvent e) {
    
         if(e.getSource()==vmoins)  p.y--;
         if(e.getSource()==vplus)p.y++;
         if(e.getSource()==hmoins) p.x--;
         if(e.getSource()==hplus)p.x++;
         if(e.getSource()==go){
             lalt.start();
             this.tourne();
         }
       /*  Rectangle  posRect= cible.getBounds();
         lalt.rectX=posRect.x+deph;
         lalt.rectY=posRect.y+depv;*
         
         */
         
         affichage();
       
    }
 
 public void affichage(){
     lalt.rectX=p.x;
    lalt.rectY=p.y;
     image = robot.createScreenCapture(new Rectangle(lalt.rectX,lalt.rectY,100,20));
         ImageIcon img = new ImageIcon(image);
       //  zone.setIcon(img);
         
         lalt.lecture();
         alt=lalt.getAltitude();
         lbValeur.setText(""+alt);
 }
 
 public void recherche(){
    do{
         Rectangle r=cible.getBounds();
         r.y+=50;
         r.x+=10;
         r.height=20;
         r.width=50;
         cible.setVisible(false);
         try{
             Thread.sleep(250);
         }
         catch(Exception e){}
         image = robot.createScreenCapture(r);
         ImageIcon img = new ImageIcon(image);
         cible.zone.setIcon(img);
        // cible.setPreferredSize(new Dimension(100,100));
         cible.setVisible(true);
         try{
             Thread.sleep(2500);
         }
         catch(Exception e){}
    }     while(true);
 }
 
  public static void main(String args[]){

     googlebike.CapteurGraph cp=googlebike.CapteurGraph.getInstance();
     cp.setDimRoue((float)2.1);
     cp.setDelai(500);
     cp.setRebond(50);
     cp.setNpass(10);
     cp.init();
     cp.recherche();
     // cp.tourne();
 }

    public void mouseClicked(MouseEvent e) {
        
        p= e.getLocationOnScreen();
        System.out.println("p="+p);
       affichage();
    }

    public void mousePressed(MouseEvent e) {
     
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
      
    }

    public void mouseExited(MouseEvent e) {
       
    }

}
