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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class CapteurGraphSimple3 extends Frame implements ActionListener,MouseListener {
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
  boolean pause=true;
  TextField tfVit=new TextField(); 
  TextField tfDist=new TextField();
  JSlider js=new JSlider();  
  CommandeGStreet3 cgsa =new CommandeGStreet3();
 CommandeGStreet3 cgsb =new CommandeGStreet3();
  int depv=0,deph=0;
  BufferedImage image=null;
  public int alt=0,altold=0;
    JButton go=new JButton("GO");
    JButton stop=new JButton("STOP");
 
Robot robot=null;
     
  private static googlebike.CapteurGraphSimple3 cp=null;
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


private CapteurGraphSimple3(){

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
 setLayout(new GridLayout(7,1,5,5));
 setSize(200, 300);
   Label lbVit=new Label("Vitesse ");
   add(lbVit);
   add(tfVit);
   Label lbDist=new Label("Distance ");
   add(lbDist);
   add(tfDist);
  
   
   add(js);
   js.setMaximum(8000);
   js.setValue(4000);
   js.addChangeListener(new ChangeListener(){
       public void stateChanged(ChangeEvent e){
           int val=js.getValue();
           cgsa.setCoeff(val);
       }
   });
   
  add(go);
   go.addActionListener(this);
   add(stop);
   stop.addActionListener(this);
   
   Font f = new Font("Serif", Font.PLAIN, 20);
   lbVit.setFont(f);
   lbDist.setFont(f);
   tfVit.setFont(f);
   tfDist.setFont(f);
   cgsa.setCoeff(2000);
   cgsb.setCoeff(300);
   /*    
      try{
        robot = new Robot();
        }
      catch(Exception e){}*/
   addMouseListener(this);
   this.setLocationByPlatform(true);
   setVisible(true);
   
 }

public static googlebike.CapteurGraphSimple3 getInstance(){
   if (cp==null) return cp=new googlebike.CapteurGraphSimple3();
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
public void tourne2(){
    DecimalFormat formater1 = new DecimalFormat("#0 km/h");
       DecimalFormat formater2 = new DecimalFormat("#0.000 km"); 
      try{
    cgsa.start();
     cgsb.start();
     }
     catch(Exception e){
         
     }  
   File fin=new File("c:/bike/vitesse.dta");
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
          
    cgsa.setValeur((int)v); 
    tfVit.setText(formater1.format(v));
    tfDist.setText(formater2.format(dist/1000.0)); 
    cgsb.setDroite(d);
    cgsb.setGauche(g);
    if(g & d) {
        cgsa.setPause(true);
        pause=true;
    }
      }
     catch (Exception e){
          System.out.println("erreur d'échantillonnage "+e);
     }
      
         try{
       Thread.sleep(300);
     }catch(Exception e2){}
     

  }while(!pause);  
 }

 
 
 public void actionPerformed(ActionEvent e) {
    
        
         if(e.getSource()==go){
               
             cgsa.setPause(false);
             cgsb.setPause(false);
             pause=false;
            this.tourne2();
         
            
         }
         
         if(e.getSource()==stop){
            cgsa.setPause(true);
             cgsb.setPause(true);  
             pause=true;
           //System.exit(0);
         }
              
    }
 
  public static void main(String args[]){

     googlebike.CapteurGraphSimple3 cp=googlebike.CapteurGraphSimple3.getInstance();
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
