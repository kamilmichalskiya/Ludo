package pl.lodz.chinczyk.player.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.chinczyk.playergame.model.PlayerGame;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;
    private String nick;
    @OneToMany
    private Set<PlayerGame> playerGameSet;
}
