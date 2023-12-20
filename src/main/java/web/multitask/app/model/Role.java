package web.multitask.app.model;
;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "roles")
@Getter
public class Role {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NonNull
    private String descripcion;

}