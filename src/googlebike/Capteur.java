package googlebike;
import java.io.*;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.awt.*;



public class Capteur //extends Frame
{
    
static{
   // System.loadLibrary("K8055fpc64");
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
   Label lbl=new Label(); 

  private static Capteur cp=null;

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


private Capteur(){
// System.setProperty("java.library.path", "D:\\tempo\\googlebike\\lib"); 
 cl =ICLibrary.instance;
   cl.CloseDevice();
   long v = cl.OpenDevice(0);
   System.out.println("valeur carte="+v);
   if(v==-1){
       System.out.println("erreur de connexion");
       System.exit(0);
   }
 System.out.println("capteur créé");
  /* setSize(300, 300);
   add(lbl);
   this.setLocationByPlatform(true);
   setVisible(true);*/

 }

public static Capteur getInstance(){
   if (cp==null) return cp=new Capteur();
    else return cp;
}

public void setDimRoue(float dimRoue){
    this.dimRoue=dimRoue;
}

public String isGauche(){
    if(cl.ReadDigitalChannel(2)) return "go";
    else return "gn";
}

public String isDroite(){
     if(cl.ReadDigitalChannel(3)) return "do";
else return "dn";
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
  /*  File f=new File("vitesse.dta");
    int passage=0;
    int n1=0,n2=0;
     System.out.println("compteur simple démarre1");
    n1 =(int)cl.ReadCounter(1);
  
    
  do{
        passage++;
    FileWriter fout=null;

    if(passage%4==0){
      
     passage=0;
    
      n2=(int)cl.ReadCounter(1);
       System.out.println("n1="+n1+" n2="+n2);
      dist=(n2-n1)*dimRoue;
     dtot+=(int)dist;
     vit= dist/(delai*4)*3600 ;
     
     n1=n2;
     }
   
   try{
      fout=new FileWriter(f);
      fout.write(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
       System.out.println(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
      lbl.setText(vit+":"+dtot);
      fout.close();
     }
   catch(Exception e){System.out.println("erreur d'écriture"+e);}
    //System.out.println(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
     try{
      Thread.sleep(delai);
     }catch(Exception e2){}
     

  }while(true);*/
    
     File f=new File("vitesse.dta");
    cl.ResetCounter(1);
    cl.SetCounterDebounceTime(1,50);
int n1,n2;
     System.out.println("compteur simple démarre");
    n1 =(int)cl.ReadCounter(1);
 do{
    FileWriter fout=null;
     n2=(int)cl.ReadCounter(1);
     float dist=(n2-n1)*dimRoue;
     dtot+=(int)dist;
     vit= dist/delai*3600 ;
   try{
      fout=new FileWriter(f);
      fout.write(vit+":"+dtot+"\n");
       System.out.println(n1+" "+n2+"  "+vit+"\n");
     fout.close();
     }
   catch(Exception e){System.out.println("erreur d'écriture"+e);}

   try{
      Thread.sleep(delai);
     }catch(Exception e2){}
     n1=n2;

  }while(true);

 


 }

 public static void main(String args[]){

     Capteur cp=Capteur.getInstance();
     cp.setDimRoue((float)2.1);
     cp.setDelai(500);
     cp.setRebond(2);
     cp.init();
     cp.tourne();

    }

}
