package carsharing;


public class Main {
    public static void main(String[] args) {
        DisplayManager program = new DisplayManager();
        program.prepareProgram();
        program.start();
        program.stop();
    }
}