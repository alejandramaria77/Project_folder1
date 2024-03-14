import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Election {
    private List<Candidate> candidates;
    private List<Ballot> ballots;
    private List<String> eliminatedCandidates;
    private int totalBallots;
    private int blankBallots;
    private int invalidBallots;

    public Election() {
        this.candidates = new ArrayList<>();
        this.ballots = new ArrayList<>();
        this.eliminatedCandidates = new ArrayList<>();
        this.totalBallots = 0;
        this.blankBallots = 0;
        this.invalidBallots = 0;
    }

    public void readCandidates(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                candidates.add(new Candidate(id, name));
            }
        }
    }

    public void readBallots(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int ballotNum = 1;
            while ((line = br.readLine()) != null) {
                Ballot ballot = new Ballot(ballotNum++);
                String[] parts = line.split(",");
                if (parts.length == 1) {
                    blankBallots++;
                } else if (!isValidBallot(parts)) {
                    invalidBallots++;
                } else {
                    for (int i = 1; i < parts.length; i++) {
                        String[] voteParts = parts[i].split(":");
                        int candidateId = Integer.parseInt(voteParts[0]);
                        int rank = Integer.parseInt(voteParts[1]);
                        ballot.addVote(candidateId, rank);
                    }
                    ballots.add(ballot);
                }
                totalBallots++;
            }
        }
    }

    private boolean isValidBallot(String[] parts) {
        for (int i = 1; i < parts.length; i++) {
            String[] voteParts = parts[i].split(":");
            if (voteParts.length != 2)
                return false;
            int candidateId = Integer.parseInt(voteParts[0]);
            int rank = Integer.parseInt(voteParts[1]);
            if (candidateId < 1 || candidateId > candidates.size() || rank < 1 || rank > candidates.size())
                return false;
            if (i != rank)
                return false;
        }
        return true;
    }

    private int getTotalVotesForCandidate(int candidateId) {
        int totalVotes = 0;
        for (Ballot ballot : ballots) {
            if (ballot.getVotes().containsKey(candidateId) && ballot.getVotes().get(candidateId) == 1) {
                totalVotes++;
            }
        }
        return totalVotes;
    }

    private Candidate getLowestCandidate() {
        Candidate lowestCandidate = null;
        int lowestVotes = Integer.MAX_VALUE;
        for (Candidate candidate : candidates) {
            if (candidate.isActive()) {
                int totalVotes = getTotalVotesForCandidate(candidate.getId());
                if (totalVotes < lowestVotes) {
                    lowestCandidate = candidate;
                    lowestVotes = totalVotes;
                }
            }
        }
        return lowestCandidate;
    }

    private boolean hasWinner() {
        for (Candidate candidate : candidates) {
            if (candidate.isActive() && getTotalVotesForCandidate(candidate.getId()) > totalBallots / 2) {
                return true;
            }
        }
        return false;
    }

    private void eliminateCandidate(Candidate candidate) {
        candidate.setActive(false);
        eliminatedCandidates.add(candidate.getName());
        for (Ballot ballot : ballots) {
            if (ballot.getVotes().containsKey(candidate.getId())) {
                int rank = ballot.getVotes().get(candidate.getId());
                for (int candidateId : ballot.getVotes().keySet()) {
                    if (candidate.isActive() && ballot.getVotes().get(candidateId) > rank) {
                        ballot.getVotes().put(candidateId, ballot.getVotes().get(candidateId) - 1);
                    }
                }
            }
        }
    }

    public void runElection() {
        while (!hasWinner()) {
            Candidate lowestCandidate = getLowestCandidate();
            if (lowestCandidate == null) {
                System.out.println("No active candidates remaining.");
                return;
            }
            eliminateCandidate(lowestCandidate);
        }
        for (Candidate candidate : candidates) {
            if (candidate.isActive()) {
                System.out.println("Winner: " + candidate.getName());
                return;
            }
        }
    }

    public void writeResults(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Total Ballots: " + totalBallots + "\n");
            writer.write("Blank Ballots: " + blankBallots + "\n");
            writer.write("Invalid Ballots: " + invalidBallots + "\n");
            writer.write("Eliminated Candidates: " + String.join(", ", eliminatedCandidates) + "\n");
        }
    }
}