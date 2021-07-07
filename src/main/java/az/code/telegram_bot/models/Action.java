package az.code.telegram_bot.models;

import az.code.telegram_bot.models.enums.ActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    ActionType type;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    Question question;
    @ManyToOne
    @JoinColumn(name = "next_id")
    Question nextQuestion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "action", fetch = FetchType.EAGER)
    Set<ActionTranslate> actionTranslates;
}




