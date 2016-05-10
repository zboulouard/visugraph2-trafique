class Morphing extends  Graphe {

    int type; // graphe relatif a une instance (type=0) graphe differentiel (type=1)
    Morphing(boolean oriented){
	super(oriented);
	this.typeMat = 0;
	this.type = 0;
    }
}
