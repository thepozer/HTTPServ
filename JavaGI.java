// Package
package pozer.Server.HTTP.JGI;

// Importation
import java.util.*;

// Classe Principale
public abstract class JavaGI {
	protected Properties Prop = null;

  // Founiture des donn�es de travail
  public void setData(Properties p){
		Prop = p;
  }

  // R�cup�ration de l'ent�te
  public String getHead() {
		return null;
  }

  // R�cup�ration du r�sultat
  public String getMimeType(){
		return "text/plain";
	}

  public int getSizeData() {
		return 12;
  }

  public String getData(){
    return "Class JavaGI";
  }
}
