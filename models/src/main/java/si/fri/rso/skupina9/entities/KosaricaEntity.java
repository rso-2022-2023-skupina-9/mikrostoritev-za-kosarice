package si.fri.rso.skupina9.entities;

import si.fri.rso.skupina9.lib.IzdelekKosarica;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "kosarica")
@NamedQueries(value = {
        @NamedQuery(name = "KosaricaEntity.getAll", query = "SELECT kosarica FROM KosaricaEntity kosarica")
})
public class KosaricaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kosarica_id;

    @OneToMany(mappedBy = "izdelek_kosarica_id")
    private List<IzdelekKosaricaEntity> list_izdelek_kosarica;
}
