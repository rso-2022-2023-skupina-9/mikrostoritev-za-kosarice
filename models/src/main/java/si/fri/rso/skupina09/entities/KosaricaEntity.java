package si.fri.rso.skupina09.entities;

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

    @ManyToMany(targetEntity = IzdelekEntity.class)
    private List<IzdelekEntity> izdelki;

    @Column(name = "ime")
    private String ime;

    public Integer getKosarica_id() {
        return kosarica_id;
    }

    public void setKosarica_id(Integer kosarica_id) {
        this.kosarica_id = kosarica_id;
    }

    public List<IzdelekEntity> getIzdelki() {
        return izdelki;
    }

    public void setIzdelki(List<IzdelekEntity> izdelki) {
        this.izdelki = izdelki;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
}
