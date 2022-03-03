package lightweight.lightchess.server.net;

import lightweight.lightchess.server.tournament.Tournament;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ProcessCommands implements Runnable {
    Scanner scan = new Scanner(System.in);
    String cmd;
    Tournament tournament;

    public ProcessCommands(Tournament t){
        tournament = t;
    }

    public void create_open_tournament(){
        getInput(scan);
    }

    void getInput(Scanner scan){
        System.out.println("Enter name of tournament");
        String name = scan.nextLine();
        System.out.println("Enter start time yyyy-mm-ddThh:mm");
        String dateString = scan.nextLine();
        LocalDateTime startTime = LocalDateTime.parse(dateString);
        System.out.println("Enter duration in minutes");
        int duration = scan.nextInt();
        LocalDateTime endTime = startTime.plusMinutes(duration);

        tournament.startTime = startTime;
        tournament.endTime = endTime;
        tournament.name = name;
    }





    @Override
    public void run() {
        while(true){
            cmd = scan.nextLine();

            switch (cmd){
                case "create_open_tournament" -> {
                    create_open_tournament();
                }

                case "get_tournament_details" -> {
                    System.out.println(tournament.get_tournament_details());
                }

                default -> {
                    System.out.println("Invalid command");
                }
            }
        }
    }
}
