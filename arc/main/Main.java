import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Election election = new Election();
        try {
            election.readCandidates("inputFiles/candidates.csv");
            election.readBallots("inputFiles/ballots.csv");
            election.runElection();
            election.writeResults("outputFiles/pepe_perez6.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
