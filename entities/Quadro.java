package entities;

public class Quadro {
    private int id;

    private int sencondChance;

    public Quadro(int id) {
        this.id = id;
    }

    public int getid() {
        return this.id;
    }

    public void addSecondChance(){
        sencondChance = 1;
    }

    public void removeSecondChance(){
        sencondChance = 0;
    }

    public int getSecondChance(){
        return sencondChance;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Quadro){
            return this.id == ((Quadro)obj).id;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
