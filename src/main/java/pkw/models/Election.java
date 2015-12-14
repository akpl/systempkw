package pkw.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Election {
    public enum ElectionType
    {
        Parlamentarne,
        Prezydenckie,
        Referendum
    }

    private int id;
    private Date creationDate;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date votingDate;

    @NotBlank
    private ElectionType electionType;
    private int creatorId;

    public int getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(Date votingDate) {
        this.votingDate = votingDate;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }
}
