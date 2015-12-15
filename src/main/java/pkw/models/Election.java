package pkw.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Election {
    public String getElectionTypeName() {
        return electionTypeName;
    }

    public void setElectionTypeName(String electionTypeName) {
        this.electionTypeName = electionTypeName;
    }

    private int id;
    private Date creationDate;
    private String electionTypeName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date votingDate;

    @NotNull
    private int electionType;
    private int creatorId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getElectionType() {
        return electionType;
    }

    public void setElectionType(int electionType) {
        this.electionType = electionType;
    }

    public String getFormattedVotingDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(getVotingDate());
    }

    public String getFormattedCreationDate() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(getCreationDate());
    }
}
