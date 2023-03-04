

public class Computer {
 
	private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Computer(){
        score = 0;
    }
    public static void computerTurn(int py, int px, int computerY, int computerX, String[][] gameArray, Player player, Computer computer) {
        boolean IsThereATrap = Game.IsThereATrap(computerY, computerX, gameArray);
        boolean IsThereAWarp = Game.IsThereAWarp(computerY, computerX, gameArray);
        if (IsThereAWarp) {
            gameArray[computerY][computerX] = " ";
            player.setScore(player.getScore() + 300);
        } else {
            if (computerX < px && computerX != 53 && !gameArray[computerY][computerX + 1].equals("#") && !IsThereATrap && !gameArray[computerY][computerX + 1].equals("C")
                    && !gameArray[computerY][computerX + 1].equals("=")) {
                if (gameArray[computerY][computerX + 1].equals("P")) {
                    player.setLife(player.getLife() - 1);
                    player.setInitialStart(true);
                } else if (gameArray[computerY][computerX + 1].matches("\\d")) {
                    int number = Integer.parseInt(gameArray[computerY][computerX + 1]);
                    if (number == 1)
                        computer.setScore(computer.getScore() + 2);
                    else if (number == 2)
                        computer.setScore(computer.getScore() + 10);
                    else if (number == 3)
                        computer.setScore(computer.getScore() + 30);
                    else if (number == 4)
                        computer.setScore(computer.getScore() + 100);
                    else if (number == 5)
                        computer.setScore(computer.getScore() + 300);
                }
                gameArray[computerY][computerX] = " ";
                gameArray[computerY][computerX + 1] = "C";
            } else if (computerX > px && computerX != 1 && !gameArray[computerY][computerX - 1].equals("#") && !IsThereATrap && !gameArray[computerY][computerX - 1].equals("C")
                    && !gameArray[computerY][computerX - 1].equals("=")) {
                if (gameArray[computerY][computerX - 1].equals("P")) {
                    player.setLife(player.getLife() - 1);
                    player.setInitialStart(true);
                } else if (gameArray[computerY][computerX - 1].matches("\\d")) {
                    int number = Integer.parseInt(gameArray[computerY][computerX - 1]);
                    if (number == 1)
                        computer.setScore(computer.getScore() + 2);
                    else if (number == 2)
                        computer.setScore(computer.getScore() + 10);
                    else if (number == 3)
                        computer.setScore(computer.getScore() + 30);
                    else if (number == 4)
                        computer.setScore(computer.getScore() + 100);
                    else if (number == 5)
                        computer.setScore(computer.getScore() + 300);
                }
                gameArray[computerY][computerX] = " ";
                gameArray[computerY][computerX - 1] = "C";
            } else if (computerY < py && computerY != 21 && !gameArray[computerY + 1][computerX].equals("#") && !IsThereATrap && !gameArray[computerY + 1][computerX].equals("C")
                    && !gameArray[computerY + 1][computerX].equals("=")) {
                if (gameArray[computerY + 1][computerX].equals("P")) {
                    player.setLife(player.getLife() - 1);
                    player.setInitialStart(true);
                } else if (gameArray[computerY + 1][computerX].matches("\\d")) {
                    int number = Integer.parseInt(gameArray[computerY + 1][computerX]);
                    if (number == 1)
                        computer.setScore(computer.getScore() + 2);
                    else if (number == 2)
                        computer.setScore(computer.getScore() + 10);
                    else if (number == 3)
                        computer.setScore(computer.getScore() + 30);
                    else if (number == 4)
                        computer.setScore(computer.getScore() + 100);
                    else if (number == 5)
                        computer.setScore(computer.getScore() + 300);
                }
                gameArray[computerY][computerX] = " ";
                gameArray[computerY + 1][computerX] = "C";
            } else if (computerY > py && computerY != 1 && !gameArray[computerY - 1][computerX].equals("#") && !IsThereATrap && !gameArray[computerY - 1][computerX].equals("C")
                    && !gameArray[computerY - 1][computerX].equals("=")) {
                if (gameArray[computerY - 1][computerX].equals("P")) {
                    player.setLife(player.getLife() - 1);
                    player.setInitialStart(true);
                } else if (gameArray[computerY - 1][computerX].matches("\\d")) {
                    int number = Integer.parseInt(gameArray[computerY - 1][computerX]);
                    if (number == 1)
                        computer.setScore(computer.getScore() + 2);
                    else if (number == 2)
                        computer.setScore(computer.getScore() + 10);
                    else if (number == 3)
                        computer.setScore(computer.getScore() + 30);
                    else if (number == 4)
                        computer.setScore(computer.getScore() + 100);
                    else if (number == 5)
                        computer.setScore(computer.getScore() + 300);
                }
                gameArray[computerY][computerX] = " ";
                gameArray[computerY - 1][computerX] = "C";
            }

        }
    }

}
