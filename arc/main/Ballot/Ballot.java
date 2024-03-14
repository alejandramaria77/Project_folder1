import java.util.HashMap;
import java.util.Map;

public class Ballot {
    private int ballotNum;
    private Map<Integer, Integer> votes;

    public Ballot(int ballotNum) {
        this.ballotNum = ballotNum;
        this.votes = new HashMap<>();
    }

    public int getBallotNum() {
        return ballotNum;
    }

    public Map<Integer, Integer> getVotes() {
        return votes;
    }

    public void addVote(int candidateId, int rank) {
        votes.put(candidateId, rank);
    }

    public boolean isValid(int numCandidates) {
        if (votes.isEmpty())
            return false;
        for (int i = 1; i <= numCandidates; i++) {
            if (!votes.containsKey(i))
                return false;
        }
        int maxRank = numCandidates;
        for (int rank : votes.values()) {
            if (rank > maxRank)
                return false;
            maxRank = rank;
        }
        return true;
    }
}

