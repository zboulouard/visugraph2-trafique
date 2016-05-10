import java.io.*;
import java.util.*;

public class NetRepertoire {
	public String Chemin;
	public NetRepertoire(String chemin){
		this.Chemin = chemin;
	}
public static void listeRepertoire(File repertoire){
	File[] list = repertoire.listFiles();
	if (list != null){
		for (int i =0 ; i < list.length; i++){
		System.out.println(list[i].getName().toLowerCase());
		}
	}
}
}
