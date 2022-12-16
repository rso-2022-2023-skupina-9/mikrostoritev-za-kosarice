package si.fri.rso.skupina9.entities;

import javax.persistence.*;

@Entity
@Table(name = "izdelek_kosarica")
@NamedQueries(value = {
        @NamedQuery(name = "IzdelekKosarica.getAll", query = "SELECT izdelek_kosarica FROM IzdelekKosaricaEntity izdelek_kosarica")
})
public class IzdelekKosaricaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer izdelek_kosarica_id;

    private Integer izdelek_id;
}
