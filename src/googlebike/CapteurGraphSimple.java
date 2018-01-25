/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebike;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.*;



public class CapteurGraphSimple extends Frame implements ActionListener,MouseListener {
static{
    System.loadLibrary("K8055D");
}
  ICLibrary cl =null;
  float dimRoue=(float)2.1;
  float vit=0;
  float dist=0;
  int tTot=0;
  //int rebond=50;
  int rebond=10;
  long delai=2000;
  long dtot=0;
  boolean gauche=false;
  boolean droite=false;
  boolean frein=false;
  int npass=4;
  TextField tfVit=new TextField(); 
  TextField tfDist=new TextField();
  CommandeGStreet cgs=new CommandeGStreet();
 
  int depv=0,deph=0;
  BufferedImage image=null;
  public int alt=0,altold=0;
    JButton go=new JButton("GO");
    JButton stop=new JButton("STOP");
 
Robot robot=null;
     
  private static googlebike.CapteurGraphSimple cp=null;
int pente=0;
float distold=0;

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


private CapteurGraphSimple(){

   WindowListener listener = new WindowAdapter() {
    public void windowClosing(WindowEvent w) {
        System.exit(0);
      }
    };
   addWindowListener(listener);
 /*cl =ICLibrary.instance;
 long v=0;
 cl.CloseDevice();
  v = cl.OpenDevice(0);
 System.out.println("valeur carte="+v);
   if(v==-1){
       System.out.println("erreur de connexion");
       System.exit(0);
   }
 System.out.println("capteur créé");*/
 setLayout(new GridLayout(6,1,5,5));
 setSize(100, 200);
   Label lbVit=new Label("Vitesse ");
   add(lbVit);
   add(tfVit);
   Label lbDist=new Label("Distance ");
   add(lbDist);
   add(tfDist);
  
   add(go);
   go.addActionListener(this);
   add(stop);
   stop.addActionListener(this);
   
   Font f = new Font("Serif", Font.PLAIN, 20);
   lbVit.setFont(f);
   lbDist.setFont(f);
   tfVit.setFont(f);
   tfDist.setFont(f);
    
   /*    
      try{
        robot = new Robot();
        }
      catch(Exception e){}*/
   addMouseListener(this);
   this.setLocationByPlatform(true);
   setVisible(true);
   
 }

public static googlebike.CapteurGraphSimple getInstance(){
   if (cp==null) return cp=new googlebike.CapteurGraphSimple();
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
     
     
     
     
     try{
   //  cgs.start();
     
     }
     catch(Exception e){
         
     }
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
    // n2=n1+2;
        System.out.println(n1+"  "+n2); 
    dist=(n2-n1)*dimRoue;
    if(dtot<0)dtot=0;
    dtot+=(int)dist;
    vit= dist/(delai*npass)*3600 ;
    cgs.setValeur((int)vit); //division par 3 pour des raisons d'affichage
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

public void tourne2(){
    DecimalFormat formater1 = new DecimalFormat("#0 km/h");
       DecimalFormat formater2 = new DecimalFormat("#0.000 km"); 
      try{
    cgs.start();
     
     }
     catch(Exception e){
         
     }  
   File fin=new File("vitesse.dta");
do{
      String ph="0";
      try{
      FileReader in = new FileReader(fin);
      Scanner sc=new Scanner(in);
      while(sc.hasNext()) {
       ph=sc.nextLine();
         // System.out.println("recup de "+ph);
      }
      in.close();
      }
      catch(Exception e){
          System.out.println("erreur de lecture "+e);
      }
      try{
      String res[]=ph.split(":");
      float v=Float.parseFloat(res[0]);
      dist=(int)Float.parseFloat(res[1]);
      boolean g = Boolean.valueOf(res[2]);
      boolean d = Boolean.valueOf(res[3]);
      System.out.println("lecture :"+v+"   "+dist+" "+g+"  "+d);
          
    cgs.setValeur((int)v); 
    tfVit.setText(formater1.format(v));
    tfDist.setText(formater2.format(dist/1000.0)); 
    cgs.setDroite(d);
    cgs.setGauche(g);
    if(g & d)System.exit(0);
      }
     catch (Exception e){
          System.out.println("erreur d'échantillonnage "+e);
     }
      
         try{
       Thread.sleep(1000);
     }catch(Exception e2){}
     

  }while(true);  
 }

 
 
 public void actionPerformed(ActionEvent e) {
    
        
         if(e.getSource()==go){
               cgs.setPause(false);
            this.tourne2();
         
            cgs.setPause(false);
         }
         
         if(e.getSource()==stop){
           System.exit(0);
         }
              
    }
 
  public static void main(String args[]){

     googlebike.CapteurGraphSimple cp=googlebike.CapteurGraphSimple.getInstance();
    /* cp.setDimRoue((float)2.1);
     cp.setDelai(500);
     cp.setRebond(50);
     cp.setNpass(10);
     cp.init();*/
    // cp.tourne2();
     
 }

    public void mouseClicked(MouseEvent e) {
        
        p= e.getLocationOnScreen();
        System.out.println("p="+p);
       
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
