package pkw.models;


import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "wybory")
public class Election {
    @Id
    @GeneratedValue(generator="ElectionId")
    @SequenceGenerator(name="ElectionId",sequenceName="ISEQ$$_92789")
    private int id;

    @Column(name = "data_utworzenia")
    private Date creationDate;

    @Transient
    private String electionTypeName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_glosowania")
    private Date votingDate;

    @NotNull
    @Column(name = "typ_wyborow_id")
    private int electionType;

    @Column(name = "id_tworcy")
    private int creatorId;

    public String getElectionTypeName() {
        return electionTypeName;
    }

    public void setElectionTypeName(String electionTypeName) {
        this.electionTypeName = electionTypeName;
    }

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
