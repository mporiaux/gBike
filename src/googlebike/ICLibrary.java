package googlebike;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrateur
 */

import com.sun.jna.*;


public interface ICLibrary extends Library{
    


    //public static final String winClib ="K8055DJVA";
    
    
 // public static final String winClib ="K8055fpc64.dll";
 public static final String winClib ="c:/K8055D.dll";
 
    public ICLibrary instance =(ICLibrary)Native.loadLibrary( winClib,ICLibrary.class);
    public long OpenDevice(long adr);
    public void CloseDevice();
    public boolean ReadDigitalChannel(long num );
    public void SetCounterDebounceTime(long nc,long dt);
    public long ReadCounter(long nc);
    public void ResetCounter(long nc);
}