import enigma.core.Enigma;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import enigma.console.TextAttributes;



public class Game {
    String[][] gameArray = new String[23][55];
    public KeyListener klis;
    public static int keypr;
    public static int rkey;
    Random random = new Random();
    CircularQueue inputQueue = new CircularQueue(15);
    int py, px;
    int time = 0;
    int timeCounter = 0;
    int gameFlag = 0;
    public static enigma.console.Console cn = Enigma.getConsole("Star Trek Warp Wars");
    public static String[] inputQueueGenerator = new String[40];
    String[][] deviceArray = new String[25][3];
    Player player = new Player();
    Computer computer = new Computer();
    
    public enigma.console.TextWindow gameWindow;
	public static TextAttributes blue = new TextAttributes(Color.cyan, Color.black);
	public static TextAttributes red = new TextAttributes(Color.MAGENTA, Color.black);
	public static TextAttributes green = new TextAttributes(Color.GREEN, Color.black);
	public static TextAttributes yellow = new TextAttributes(Color.yellow, Color.black);



    public Game() throws Exception{
        klis=new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if(keypr==0) {
                    keypr=1;
                    rkey=e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);
        readMaze();
        formInputQueueGenerator();
        startInputQueue();
        for(short i = 0;i<deviceArray.length;i++)
            deviceArray[i] = null;
        fillInputQueue();
        executeGame();

    }

    public void readMaze() throws FileNotFoundException {

        File mazeFile = new File("src/map.txt");
        Scanner scanner = new Scanner(mazeFile);
        scanner.useDelimiter("(?<=.)");
        int j = 0;
        while(scanner.hasNextLine()){
            for (int i = 0;i<gameArray[j].length;i++){

                gameArray[j][i] = scanner.next().trim();
                if (gameArray[j][i].equals(""))
                    gameArray[j][i] = " ";
            }
            j++;
        }

    }

    public void printMaze() throws CloneNotSupportedException {
        cn.getTextWindow().setCursorPosition(0,0);
       
        for (int i = 0;i<gameArray.length;i++){
            for (int j = 0;j<gameArray[i].length;j++){
                System.out.print(gameArray[i][j]);
            }
            System.out.println();
        }
        short j1 = 60;
        cn.getTextWindow().setCursorPosition(j1,5);
        cn.getTextWindow().output((String.valueOf("j1")), blue);
        for(Object element : inputQueue.getElements()){
            if(element != null){
                System.out.print(element);
                cn.getTextWindow().setCursorPosition(++j1,5);
                cn.getTextWindow().output((String.valueOf(element)), blue);
            }

        }

        System.out.println();

        int i1 = 7;
        int i1temp = i1+9;
        cn.getTextWindow().setCursorPosition(60,i1);
        
        for(int i = 0;i<11;i++){
            System.out.println("|   |");
            cn.getTextWindow().setCursorPosition(60,i1++);
            }
        System.out.println("+---+");
        cn.getTextWindow().setCursorPosition(60,i1++);
        cn.getTextWindow().output((String.valueOf("P.Backpack")), red);
        for(int i = 0;i<10;i++){
            cn.getTextWindow().setCursorPosition(62,i1temp--);
            System.out.print(" ");
        }
        i1 = 7;
        i1temp = i1+9;


        {
            for(Object element : player.getBackpackElements()){
                cn.getTextWindow().setCursorPosition(62,i1temp);
                if (element == null)
                    System.out.print(" ");
                else{
                    System.out.print(element);
                    cn.getTextWindow().setCursorPosition(62,i1temp);
                    cn.getTextWindow().output((String.valueOf(element)), red);                   
                }
                i1temp--;
            }
        }

        cn.getTextWindow().setCursorPosition(60,20);
        int playerEnergy = (int) player.getEnergy(); 
        cn.getTextWindow().setCursorPosition(60,20);
        cn.getTextWindow().output("P.Energy : ", green);       
        System.out.println(playerEnergy);
        cn.getTextWindow().setCursorPosition(60,21);
        cn.getTextWindow().output("P.Score : ", yellow);
        System.out.println(player.getScore());
        cn.getTextWindow().setCursorPosition(60,22);
        cn.getTextWindow().output("P.Life : " ,red);
        System.out.println(player.getLife());
        cn.getTextWindow().setCursorPosition(60,23);
        cn.getTextWindow().output("C.Score : " , green);   
        System.out.println(computer.getScore());
        cn.getTextWindow().setCursorPosition(60,24);
        cn.getTextWindow().output("Time : ", yellow);
        System.out.print(time);


    }

    public void startInputQueue(){
        String[] startInputQueue = new String[20];
        int startInputQueueCounter = 0;
        for(int i = 0;i<5;i++){
            startInputQueue[startInputQueueCounter++] = "1";
        }
        for (int i = 0;i<3;i++){
            startInputQueue[startInputQueueCounter++] = "2";
        }
        for (int i = 0;i<3;i++){
            startInputQueue[startInputQueueCounter++] = "3";
        }
        for (int i = 0;i<3;i++){
            startInputQueue[startInputQueueCounter++] = "4";
        }
        for (int i = 0;i<3;i++){
            startInputQueue[startInputQueueCounter++] = "5";
        }
        startInputQueue[startInputQueueCounter++] = "=";
        startInputQueue[startInputQueueCounter++] = "*";
        startInputQueue[startInputQueueCounter] = "C";
        startInputQueueCounter = 0;
        while(startInputQueueCounter<20){
            int[][] availableIndices = new int[900][2];
            int availableIndicesCounter = 0;
            for (int i = 0; i < gameArray.length; i++) {
                for (int j = 0; j < gameArray[i].length; j++) {
                    if (gameArray[i][j].equals(" ") && i != 5 && j != 5) {
                        availableIndices[availableIndicesCounter][0] = i;
                        availableIndices[availableIndicesCounter++][1] = j;
                    }
                }
            }
            int randIndex = random.nextInt(availableIndicesCounter);
            int[] availableIndex = availableIndices[randIndex];
            int availableIndexRow = availableIndex[0];
            int availableIndexColumn = availableIndex[1];
            String elementToPlace = (String) startInputQueue[startInputQueueCounter];
            if(elementToPlace.equals("=") || elementToPlace.equals("*")) {
                for (short i = 0; i < deviceArray.length; i++) {
                    if (deviceArray[i] == null) {
                        deviceArray[i] = new String[3];
                        deviceArray[i][0] = String.valueOf(availableIndexRow);
                        deviceArray[i][1] = String.valueOf(availableIndexColumn);
                        deviceArray[i][2] = String.valueOf(25);
                        break;
                    }
                }
            }
            gameArray[availableIndexRow][availableIndexColumn] = elementToPlace;
            startInputQueueCounter++;
        }
    }

    public void formInputQueueGenerator() {
        for (int i = 0; i < inputQueueGenerator.length; i++) {
            if (i <= 11) 
                inputQueueGenerator[i] = "1";
            else if (i <= 19)
                inputQueueGenerator[i] = "2";
            else if (i <= 25)
                inputQueueGenerator[i] = "3";
            else if (i <= 30)
                inputQueueGenerator[i] = "4";
            else if (i <= 34)
                inputQueueGenerator[i] = "5";
            else if (i <= 36)
                inputQueueGenerator[i] = "=";
            else if (i <= 37)
                inputQueueGenerator[i] = "*";
            else if (i <= 39)
                inputQueueGenerator[i] = "C";

        }
    }

     public void fillInputQueue() {
        if (inputQueue.isActivated()){
            int size1 = inputQueue.size();
            while (!inputQueue.isFull()) {
                int randIndex = random.nextInt(40);               
                inputQueue.enqueue(inputQueueGenerator[randIndex]);
                size1 = inputQueue.size();
            }
        }

        }

    public void placeElements(){
        while(!inputQueue.isEmpty()){
            int[][] availableIndices = new int[900][2];
            int availableIndicesCounter = 0;
            for (int i = 0;i<gameArray.length;i++){
                for (int j = 0;j<gameArray[i].length;j++){
                    if (gameArray[i][j].equals(" ")){
                        availableIndices[availableIndicesCounter][0] = i;
                        availableIndices[availableIndicesCounter++][1] = j;
                    }
                }
            }
            int randIndex = random.nextInt(availableIndicesCounter);
            int[] availableIndex = availableIndices[randIndex];
            int availableIndexRow = availableIndex[0];
            int availableIndexColumn = availableIndex[1];
            String peekedElement = (String) inputQueue.dequeue();
            if(peekedElement.equals("=") || peekedElement.equals("*")) {
                for (short i = 0; i < deviceArray.length; i++) {
                    if (deviceArray[i] == null) {
                        deviceArray[i] = new String[3];
                        deviceArray[i][0] = String.valueOf(availableIndexRow);
                        deviceArray[i][1] = String.valueOf(availableIndexColumn);
                        deviceArray[i][2] = String.valueOf(25);
                        break;
                    }
                }
            }
            gameArray[availableIndexRow][availableIndexColumn] = peekedElement;

        }


    }

    public void placeAnElement() {
        int[][] availableIndices = new int[900][2];
        int availableIndicesCounter = 0;
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[i].length; j++) {
                if (gameArray[i][j].equals(" ") && i != 5 && j != 5) {
                    availableIndices[availableIndicesCounter][0] = i;
                    availableIndices[availableIndicesCounter++][1] = j;
                }
            }
        }
        int randIndex = random.nextInt(availableIndicesCounter);
        int[] availableIndex = availableIndices[randIndex];
        int availableIndexRow = availableIndex[0];
        int availableIndexColumn = availableIndex[1];
        
        String peekedElement = (String) inputQueue.dequeue();
        
        if(peekedElement.equals("=") || peekedElement.equals("*")) {
            for (short i = 0; i < deviceArray.length; i++) {
                if (deviceArray[i] == null) {
                    deviceArray[i] = new String[3];
                    deviceArray[i][0] = String.valueOf(availableIndexRow);
                    deviceArray[i][1] = String.valueOf(availableIndexColumn);
                    deviceArray[i][2] = String.valueOf(25);
                    break;
                }
            }
        }
        gameArray[availableIndexRow][availableIndexColumn] = peekedElement;
        
    }

    public void moveANumber() {
        int[][] available4and5s = new int[50][2];
        int available4and5sCounter = 0;
        for (int i = 0; i < gameArray.length; i++) {
            for (int j = 0; j < gameArray[i].length; j++) {
                if (gameArray[i][j].equals("4") || gameArray[i][j].equals("5")) {
                    available4and5s[available4and5sCounter][0] = i;
                    available4and5s[available4and5sCounter++][1] = j;
                }
            }
        }
        int randIndex = random.nextInt(available4and5sCounter);
        int[] availableIndex = available4and5s[randIndex];
        int availableIndexRow = availableIndex[0];
        int availableIndexColumn = availableIndex[1];
        boolean IsThereATrap = IsThereATrap(availableIndexRow, availableIndexColumn, gameArray);
        boolean IsThereAWarp = IsThereAWarp(availableIndexRow, availableIndexColumn, gameArray);
        if (IsThereAWarp){
            if(gameArray[availableIndexRow][availableIndexColumn].equals("4"))
                player.setScore(player.getScore() + 50);
            else if(gameArray[availableIndexRow][availableIndexColumn].equals("5"))
                player.setScore(player.getScore() + 150);
            gameArray[availableIndexRow][availableIndexColumn] = " ";
        }
        else {
            int[][] possibleMovementIndices = new int[4][2];
            int possibleMovementIndicesCounter = 0;
            if (!gameArray[availableIndexRow + 1][availableIndexColumn].equals("#") &&
                    !gameArray[availableIndexRow + 1][availableIndexColumn].matches("\\d")
                    && !gameArray[availableIndexRow + 1][availableIndexColumn].equals("P") &&
                    !gameArray[availableIndexRow + 1][availableIndexColumn].equals("=") &&
                    !gameArray[availableIndexRow + 1][availableIndexColumn].equals("*") && !gameArray[availableIndexRow + 1][availableIndexColumn].equals("C") &&
                    !IsThereATrap) {
                possibleMovementIndices[possibleMovementIndicesCounter][0] = availableIndexRow + 1;
                possibleMovementIndices[possibleMovementIndicesCounter++][1] = availableIndexColumn;

            }
            if (!gameArray[availableIndexRow - 1][availableIndexColumn].equals("#") &&
                    !gameArray[availableIndexRow - 1][availableIndexColumn].matches("\\d") &&
                    !gameArray[availableIndexRow - 1][availableIndexColumn].equals("P")
                    && !gameArray[availableIndexRow - 1][availableIndexColumn].equals("*") && !gameArray[availableIndexRow - 1][availableIndexColumn].equals("=") &&
                    !gameArray[availableIndexRow - 1][availableIndexColumn].equals("C") && !IsThereATrap) {
                possibleMovementIndices[possibleMovementIndicesCounter][0] = availableIndexRow - 1;
                possibleMovementIndices[possibleMovementIndicesCounter++][1] = availableIndexColumn;

            }
            if (!gameArray[availableIndexRow][availableIndexColumn + 1].equals("#") &&
                    !gameArray[availableIndexRow][availableIndexColumn + 1].matches("\\d")
                    && !gameArray[availableIndexRow][availableIndexColumn + 1].equals("P") &&
                    !gameArray[availableIndexRow][availableIndexColumn + 1].equals("*")
                    && !gameArray[availableIndexRow][availableIndexColumn + 1].equals("=") && !gameArray[availableIndexRow][availableIndexColumn + 1].equals("C")
                    && !IsThereATrap) {
                possibleMovementIndices[possibleMovementIndicesCounter][0] = availableIndexRow;
                possibleMovementIndices[possibleMovementIndicesCounter++][1] = availableIndexColumn + 1;
            }
            if (!gameArray[availableIndexRow][availableIndexColumn - 1].equals("#") &&
                    !gameArray[availableIndexRow][availableIndexColumn - 1].matches("\\d") &&
                    !gameArray[availableIndexRow][availableIndexColumn - 1].equals("P") && !gameArray[availableIndexRow][availableIndexColumn - 1].equals("*")
                    && !gameArray[availableIndexRow][availableIndexColumn - 1].equals("=") && !gameArray[availableIndexRow][availableIndexColumn - 1].equals("C") &&
                    !IsThereATrap) {
                possibleMovementIndices[possibleMovementIndicesCounter][0] = availableIndexRow;
                possibleMovementIndices[possibleMovementIndicesCounter++][1] = availableIndexColumn - 1;
            }
            if (possibleMovementIndicesCounter != 0) {
                int randomPossibleMovement = random.nextInt(possibleMovementIndicesCounter);
                int chosenMovementRow = possibleMovementIndices[randomPossibleMovement][0];
                int chosenMovementColumn = possibleMovementIndices[randomPossibleMovement][1];
                String tempValue = gameArray[availableIndexRow][availableIndexColumn];
                gameArray[availableIndexRow][availableIndexColumn] = " ";
                gameArray[chosenMovementRow][chosenMovementColumn] = tempValue;
            } else {
                moveANumber();
            }
        }
    }

    public static boolean IsThereAWarp(int i, int j,String[][] gameArray){
        for(int i1 = i-1;i1<i+2;i1++){
            for(int j1 = j-1;j1<j+2;j1++){
                if(gameArray[i1][j1].equals("*")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean IsThereATrap(int i, int j,String[][] gameArray){
        for(int i1 = i-1;i1<i+2;i1++){
            for(int j1 = j-1;j1<j+2;j1++){
                if(gameArray[i1][j1].equals("=")){
                    return true;
                }
            }
        }
        return false;
    }

    public void executeGame() throws InterruptedException, CloneNotSupportedException {
        gameArray[5][5] = "P";        
        py = 5;
        px = 5;
        boolean timeReached = false;
        while (player.getLife() != 0) {
            printMaze();
            boolean backpackIsFull = player.getBackpack().isFull();
            boolean backpackIsEmpty = player.getBackpack().isEmpty();
            boolean isEnergyActivated = player.isEnergyActivated();
            if (keypr == 1) {
                gameArray[py][px] = " ";
                String[] temp = new String[3];
                // if keyboard button pressed
                char rckey = (char) rkey;
                if (rckey=='&' && ((!gameArray[py -1][px].equals("#")) && ((gameArray[py -1][px].matches("\\d") && !backpackIsFull) || gameArray[py -1][px].equals(" ")))){
                    py--;
                    temp[0] = gameArray[py][px];
                    temp[1] = String.valueOf(py);
                    temp[2] = String.valueOf(px);
                    if(isEnergyActivated && py != 1 && ((!gameArray[py -1][px].equals("#")) && ((gameArray[py -1][px].matches("\\d") && !backpackIsFull) || gameArray[py -1][px].equals(" ")))){
                        py--;}
                }
                else if (rckey=='(' && (!gameArray[py +1][px].equals("#") && ((gameArray[py +1][px].matches("\\d") && !backpackIsFull) || gameArray[py +1][px].equals(" ")))){
                    py++;
                    temp[0] = gameArray[py][px];
                    temp[1] = String.valueOf(py);
                    temp[2] = String.valueOf(px);
                    if(isEnergyActivated && py != 21 && (!gameArray[py +1][px].equals("#") && ((gameArray[py +1][px].matches("\\d") && !backpackIsFull) || gameArray[py +1][px].equals(" ")))){
                        py++;}
                }
                else if (rckey=='%' && (!gameArray[py][px-1].equals("#") && ((gameArray[py][px-1].matches("\\d") && !backpackIsFull) || gameArray[py][px-1].equals(" ")))){
                    px--;
                    temp[0] = gameArray[py][px];
                    temp[1] = String.valueOf(py);
                    temp[2] = String.valueOf(px);
                    if(isEnergyActivated && px != 1 && (!gameArray[py][px-1].equals("#") && ((gameArray[py][px-1].matches("\\d") && !backpackIsFull) || gameArray[py][px-1].equals(" ")))){
                        px--;}
                }
                else if (rckey=='\'' && (!gameArray[py][px+1].equals("#") && ((gameArray[py][px+1].matches("\\d") && !backpackIsFull) || gameArray[py][px+1].equals(" ")))){
                    px++;
                    temp[0] = gameArray[py][px];
                    temp[1] = String.valueOf(py);
                    temp[2] = String.valueOf(px);
                    if(isEnergyActivated && px != 53 && (!gameArray[py][px+1].equals("#") && ((gameArray[py][px+1].matches("\\d") && !backpackIsFull) || gameArray[py][px+1].equals(" ")))){
                        px++;}
                }
                if (rckey == 'W' && gameArray[py-1][px].equals(" ") && !backpackIsEmpty){
                    String peekedElement = (String) player.getBackpack().peek();
                    if (peekedElement.matches("\\d")){
                        player.getBackpack().pop();
                    }
                    else{
                        String poppedElement = (String) player.getBackpack().pop();
                        gameArray[py-1][px] = poppedElement;
                        for(short i = 0;i<deviceArray.length;i++){
                            if (deviceArray[i] == null){
                                deviceArray[i] = new String[3];
                                deviceArray[i][0] = String.valueOf(py-1);
                                deviceArray[i][1] = String.valueOf(px);
                                deviceArray[i][2] = String.valueOf(25);
                                break;
                            }
                        }
                    }
                }
                else if (rckey == 'S' && gameArray[py+1][px].equals(" ") && !backpackIsEmpty){
                    String peekedElement = (String) player.getBackpack().peek();
                    if (peekedElement.matches("\\d")){
                        player.getBackpack().pop();
                    }
                    else{
                        String poppedElement = (String) player.getBackpack().pop();
                        gameArray[py+1][px] = poppedElement;
                        for(short i = 0;i<deviceArray.length;i++){
                            if (deviceArray[i] == null){
                                deviceArray[i] = new String[3];
                                deviceArray[i][0] = String.valueOf(py+1);
                                deviceArray[i][1] = String.valueOf(px);
                                deviceArray[i][2] = String.valueOf(25);
                                break;
                            }
                        }
                    }

                }
                else if (rckey == 'A' && gameArray[py][px-1].equals(" ") && !backpackIsEmpty) {
                    String peekedElement = (String) player.getBackpack().peek();
                    if (peekedElement.matches("\\d")) {
                        player.getBackpack().pop();
                    } else {
                        String poppedElement = (String) player.getBackpack().pop();
                        gameArray[py][px-1] = poppedElement;
                        for(short i = 0;i<deviceArray.length;i++){
                            if (deviceArray[i] == null){
                                deviceArray[i] = new String[3];
                                deviceArray[i][0] = String.valueOf(py);
                                deviceArray[i][1] = String.valueOf(px-1);
                                deviceArray[i][2] = String.valueOf(25);
                                break;
                            }
                        }
                    }
                }
                else if (rckey == 'D' && gameArray[py][px+1].equals(" ") && !backpackIsEmpty) {
                    String peekedElement = (String) player.getBackpack().peek();
                    if (peekedElement.matches("\\d")) {
                        player.getBackpack().pop();
                    } else {
                        String poppedElement = (String) player.getBackpack().pop();
                        gameArray[py][px+1] = poppedElement;
                        for(short i = 0;i<deviceArray.length;i++){
                            if (deviceArray[i] == null){
                                deviceArray[i] = new String[3];
                                deviceArray[i][0] = String.valueOf(py);
                                deviceArray[i][1] = String.valueOf(px+1);
                                deviceArray[i][2] = String.valueOf(25);
                                break;
                            }
                        }
                    }
                }
                if(temp[0] != null && temp[0].matches("\\d")){
                    player.eliminateGameElement(gameArray,Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
                }
                if (gameArray[py][px].matches("\\d") && !player.getBackpack().isFull()){
                    if(gameArray[py][px].equals(player.getBackpack().peek())){
                        if (player.getBackpack().peek().equals("1")){
                            player.getBackpack().push(gameArray[py][px]);
                        }
                        else {
                            String poppedElement = (String) player.getBackpack().pop();
                            if (poppedElement.equals("2")) {
                                player.setEnergyActivatedTrue();
                                player.setEnergy(10);
                            } else if (poppedElement.equals("3")) {
                                player.getBackpack().push("=");
                            } else if (poppedElement.equals("4")) {
                                player.setEnergyActivatedTrue();
                                player.setEnergy(240);

                            } else if (poppedElement.equals("5")) {
                                player.getBackpack().push("*");
                            }
                        }
                    }
                    else{
                        String peekedElement = (String) player.getBackpack().peek();
                        if (player.getBackpack().isEmpty() || !peekedElement.matches("\\d"))
                            player.getBackpack().push(gameArray[py][px]);
                        else if (!player.getBackpack().isEmpty())
                            player.getBackpack().pop();
                    }
                    int number = Integer.parseInt(gameArray[py][px]);
                    if (number == 1)
                        player.setScore(player.getScore() + 1);
                    else if (number == 2)
                        player.setScore(player.getScore() + 5);
                    else if (number == 3)
                        player.setScore(player.getScore() + 15);
                    else if (number == 4)
                        player.setScore(player.getScore() + 50);
                    else if (number == 5)
                        player.setScore(player.getScore() + 150);
                }
                gameArray[py][px] = "P";
                keypr = 0;
            }
            if(gameArray[py-1][px].equals("C") || gameArray[py+1][px].equals("C") || gameArray[py][px-1].equals("C") || gameArray[py][px+1].equals("C")){
                if(player.getBackpack().size() >= 2){
                    player.getBackpack().pop();
                    player.getBackpack().pop();
                }
                else if(player.getBackpack().size() == 1)
                    player.getBackpack().pop();
            }
            if(player.isEnergyActivated())
                player.setEnergy(player.getEnergy()-0.5);
            if(Math.round(player.getEnergy()) == 0){
                player.setEnergyActivatedFalse();
            }
            timeCounter += 1;
            if (timeCounter == 2){
                time += 1;
                timeReached = true;
                timeCounter = 0;
            }
            if ((time >= 3 && time % 3 == 0) && timeReached){
                placeAnElement();
                fillInputQueue();
                timeReached = false;
            }
            moveANumber();
            moveANumber();
            moveANumber();
            for(short j = 0;j<deviceArray.length;j++){
                    if (deviceArray[j] != null){
                        int pyOfDevice = Integer.parseInt(deviceArray[j][0]);
                        int pxOfDevice = Integer.parseInt(deviceArray[j][1]);
                        if(gameArray[pyOfDevice][pxOfDevice].equals("*")) {
                            for (int i1 = pyOfDevice - 1; i1 < pyOfDevice + 2; i1++) {
                                for (int j1 = pxOfDevice - 1; j1 < pxOfDevice + 2; j1++) {
                                    if (gameArray[i1][j1].equals("1") || gameArray[i1][j1].equals("2") || gameArray[i1][j1].equals("3")) {
                                        if(gameArray[i1][j1].equals("1"))
                                            player.setScore(player.getScore() + 1);
                                        else if (gameArray[i1][j1].equals("2"))
                                            player.setScore(player.getScore() + 5);
                                        else if (gameArray[i1][j1].equals("3"))
                                            player.setScore(player.getScore() + 15);
                                        gameArray[i1][j1] = " ";
                                    }
                                }
                            }
                        }
                        double deviceDuration = Double.parseDouble(deviceArray[j][2]);
                        deviceArray[j][2] = String.valueOf(deviceDuration-0.5);
                        if (Math.round(deviceDuration) == 0){
                            gameArray[pyOfDevice][pxOfDevice] = " ";
                            deviceArray[j] = null;
                        }
                    }
                }
            int[][] availableComputers = new int[10][2];
            int availableComputersCounter = 0;
            for (int i = 0;i<gameArray.length;i++){
                for (int j = 0;j<gameArray[i].length;j++){
                    if (gameArray[i][j].equals("C")){
                        availableComputers[availableComputersCounter][0] = i;
                        availableComputers[availableComputersCounter++][1] = j;
                    }
                }
            }
            if(availableComputersCounter != 0){
                for(short i = 0;i<availableComputersCounter;i++){
                    Computer.computerTurn(py,px,availableComputers[i][0],availableComputers[i][1],gameArray,player,computer);
                }
            }
            if(player.isInitialStart()){
                px = 5;
                py = 5;
                gameArray[py][px] = "P";
                player.setInitialStart(false);
            }
            Thread.sleep(500);
        }
        cn.getTextWindow().setCursorPosition(0,0);
        for(int i = 0;i<35;i++){
            for(int j =0;j<75;j++){
                System.out.print(" ");
            }
        }
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼\n" +
                "███▀▀▀██┼███▀▀▀███┼███▀█▄█▀███┼██▀▀▀\n" +
                "██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼█┼┼┼██┼██┼┼┼\n" +
                "██┼┼┼▄▄▄┼██▄▄▄▄▄██┼██┼┼┼▀┼┼┼██┼██▀▀▀\n" +
                "██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██┼┼┼\n" +
                "███▄▄▄██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██▄▄▄\n" +
                "┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼\n" +
                "███▀▀▀███┼▀███┼┼██▀┼██▀▀▀┼██▀▀▀▀██▄┼\n" +
                "██┼┼┼┼┼██┼┼┼██┼┼██┼┼██┼┼┼┼██┼┼┼┼┼██┼\n" +
                "██┼┼┼┼┼██┼┼┼██┼┼██┼┼██▀▀▀┼██▄▄▄▄▄▀▀┼\n" +
                "██┼┼┼┼┼██┼┼┼██┼┼█▀┼┼██┼┼┼┼██┼┼┼┼┼██┼\n" +
                "███▄▄▄███┼┼┼─▀█▀┼┼─┼██▄▄▄┼██┼┼┼┼┼██▄");

        System.out.println("YOUR GAME SCORE IS : " + (player.getScore() - computer.getScore()));
    }




}
