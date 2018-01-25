/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebike;

import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;


public class CapteurSim extends Frame {
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
   Label lbl=new Label(); 

  CommandeGStreet cgs=new CommandeGStreet();

  private static googlebike.CapteurSim cp=null;

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


private CapteurSim(){
// System.setProperty("java.library.path", "D:\\tempo\\googlebike\\lib"); 
 
 System.out.println("capteur créé");
 
 setSize(300, 300);
   add(lbl);
   this.setLocationByPlatform(true);
   setVisible(true);
   
    WindowListener listener = new WindowAdapter() {
      public void windowClosing(WindowEvent w) {
        System.exit(0);
      }
    };
   addWindowListener(listener);
 }

public static googlebike.CapteurSim getInstance(){
   if (cp==null) return cp=new googlebike.CapteurSim();
    else return cp;
}

public void setDimRoue(float dimRoue){
    this.dimRoue=dimRoue;
}

public String isGauche(){
    if(false) return "go";
    else return "gn";
}

public String isDroite(){
     if(false) return "do";
else return "dn";
}

public boolean isFrein(){
    return false;
}


public void init(){

    
    System.out.println("fin d'initialisation");

}
 public void tourne(){
    cgs.start();
    File f=new File("vitesse.dta");
    int passage=0;
    int n1=0,n2=0;
     System.out.println("compteur simple démarre1");
    n1 =0;
    int incre=6;
    
    do{
        passage++;
    FileWriter fout=null;

    if(passage%8==0){
      
     passage=0;
    
      n2=n1+incre;
      // System.out.println("n1="+n1+" n2="+n2);
      dist=(n2-n1)*dimRoue;
     dtot+=(int)dist;
     vit= dist/(delai*8)*3600 ;
     n1=n2;
      cgs.setValeur((int)vit);
     }
   cgs.setDroite(isDroite().equals("do"));
   cgs.setGauche(isGauche().equals("go"));
  try{
      fout=new FileWriter(f);
      fout.write(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
      fout.close();
      lbl.setText(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
     }
   catch(Exception e){System.out.println("erreur d'écriture"+e);}
    //System.out.println(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
  
     try{
      Thread.sleep(delai);
     }catch(Exception e2){}
     

  }while(true);

 }

 public static void main(String args[]){

     googlebike.CapteurSim cp=googlebike.CapteurSim.getInstance();
     cp.setDimRoue((float)2.1);
     cp.setDelai(200);
     cp.setRebond(50);
     cp.init();
     cp.tourne();

    }

}
