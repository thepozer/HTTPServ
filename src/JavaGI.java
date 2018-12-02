// Package
package pozer.Server.HTTP.JGI;

// Importation
import java.util.*;

// Classe Principale
public abstract class JavaGI {
	protected Properties Prop = null;

  // Founiture des données de travail
  public void setData(Properties p){
		Prop = p;
  }

  // Récupération de l'entête
  public String getHead() {
		return null;
  }

  // Récupération du résultat
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
