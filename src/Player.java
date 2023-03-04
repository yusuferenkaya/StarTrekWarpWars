
public class Player {
	
	private int life;
    private int score;
    private double energy;
    private Stack backpack;
    private boolean energyActivated;
    private boolean initialStart;
    
    public Player(){
        life = 5;
        score = 0;
        energy = 50;
        backpack = new Stack(10);
        energyActivated = true;
        initialStart = false;
    }
    public int getLife() {
        return life;
    }

    public int getScore() {
        return score;
    }

    public double getEnergy() {
        return energy;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isInitialStart() {
        return initialStart;
    }

    public void setInitialStart(boolean initialStart) {
        this.initialStart = initialStart;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public Object[] getBackpackElements(){
        return backpack.getElements();
    }

    public Stack getBackpack(){
        return backpack;
    }

    public boolean isEnergyActivated(){
        return energyActivated;
    }
    public void setEnergyActivatedTrue(){
        energyActivated = true;
    }

    public void setEnergyActivatedFalse(){
       energyActivated = false;
    }
    
    public void eliminateGameElement(String[][] gameArray,int py, int px){
        if (gameArray[py][px].matches("\\d") && !getBackpack().isFull()){
            if(gameArray[py][px].equals(getBackpack().peek())) {
                if (getBackpack().peek().equals("1")) {
                    getBackpack().push(gameArray[py][px]);
                } else {
                    String poppedElement = (String) getBackpack().pop();
                    if (poppedElement.equals("2")) {
                        setEnergyActivatedTrue();
                        setEnergy(10);
                    } else if (poppedElement.equals("3")) {
                        getBackpack().push("=");
                    } else if (poppedElement.equals("4")) {
                        setEnergyActivatedTrue();
                        setEnergy(240);

                    } else if (poppedElement.equals("5")) {
                        getBackpack().push("*");
                    }
                }
            }
            else{
                String peekedElement = (String) getBackpack().peek();
                if (getBackpack().isEmpty() || !peekedElement.matches("\\d"))
                    getBackpack().push(gameArray[py][px]);
                else if (!getBackpack().isEmpty())
                    getBackpack().pop();
            }
            int number = Integer.parseInt(gameArray[py][px]);
            if (number == 1)
                setScore(getScore() + 1);
            else if (number == 2)
                setScore(getScore() + 5);
            else if (number == 3)
                setScore(getScore() + 15);
            else if (number == 4)
                setScore(getScore() + 50);
            else if (number == 5)
                setScore(getScore() + 150);
            gameArray[py][px] = " ";
        }

    }
}