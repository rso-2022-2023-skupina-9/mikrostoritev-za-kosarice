package si.fri.rso.skupina09.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rso.skupina09.converters.KosaricaConverter;
import si.fri.rso.skupina09.entities.IzdelekEntity;
import si.fri.rso.skupina09.entities.KosaricaEntity;
import si.fri.rso.skupina09.lib.Kosarica;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class KosaricaBean {

    private Logger logger = Logger.getLogger(KosaricaBean.class.getName());

    @Inject
    private EntityManager entityManager;

    public List<Kosarica> getKosarice() {
        TypedQuery<KosaricaEntity> query = entityManager.createNamedQuery("KosaricaEntity.getAll", KosaricaEntity.class);
        List<KosaricaEntity> result = query.getResultList();
        return result.stream().map(KosaricaConverter::toDto).collect(Collectors.toList());
    }

    @Timed
    public List<Kosarica> getKosarice(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(entityManager, KosaricaEntity.class, queryParameters).stream().map(KosaricaConverter::toDto).collect(Collectors.toList());
    }

    public Kosarica getKosarica(Integer id) {
        KosaricaEntity kosaricaEntity = entityManager.find(KosaricaEntity.class, id);
        if (kosaricaEntity == null) {
            throw new NotFoundException(String.format("Kosarica z id-jem: %d ne obstaja!", id));
        }
        Kosarica kosarica = KosaricaConverter.toDto(kosaricaEntity);
        return kosarica;
    }

    public Kosarica createKosarica(Kosarica kosarica) {
        KosaricaEntity kosaricaEntity = KosaricaConverter.toEntity(kosarica);
        try {
            beginTx();
            entityManager.persist(kosaricaEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        if (kosaricaEntity.getKosarica_id() == null) {
            throw new RuntimeException("Kosarica entity ni bil dodan!");
        }
        return KosaricaConverter.toDto(kosaricaEntity);
    }

    public Kosarica putKosarica(Integer id, Kosarica kosarica) {
        KosaricaEntity kosaricaEntity = entityManager.find(KosaricaEntity.class, id);
        if (kosaricaEntity == null) {
            return null;
        }
        KosaricaEntity updatedKosaricaEntity = KosaricaConverter.toEntity(kosarica);
        try {
            beginTx();
            updatedKosaricaEntity.setKosarica_id(kosaricaEntity.getKosarica_id());
            updatedKosaricaEntity = entityManager.merge(updatedKosaricaEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return KosaricaConverter.toDto(updatedKosaricaEntity);
    }

    public boolean deleteKosarica(Integer id) {
        KosaricaEntity kosaricaEntity = entityManager.find(KosaricaEntity.class, id);
        if (kosaricaEntity != null) {
            try {
                beginTx();
                entityManager.remove(kosaricaEntity);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public Kosarica addIzdelekToKosarica(Integer kosaricaId, Integer izdelekId) {
        KosaricaEntity kosaricaEntity = entityManager.find(KosaricaEntity.class, kosaricaId);
        if (kosaricaEntity == null) {
            return null;
        }
        IzdelekEntity izdelekEntity = entityManager.find(IzdelekEntity.class, izdelekId);
        if (izdelekEntity == null) {
            return null;
        }
        List<IzdelekEntity> trenutniIzdelki = kosaricaEntity.getIzdelki();
        trenutniIzdelki.add(izdelekEntity);
        kosaricaEntity.setIzdelki(trenutniIzdelki);
        KosaricaEntity updatedKosarica = kosaricaEntity;
        try {
            beginTx();
            updatedKosarica = entityManager.merge(kosaricaEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return KosaricaConverter.toDto(updatedKosarica);
    }

    public Kosarica deleteIzdelekFromKosarica(Integer kosaricaId, Integer izdelekId) {
        KosaricaEntity kosaricaEntity = entityManager.find(KosaricaEntity.class, kosaricaId);
        if (kosaricaEntity == null) {
            return null;
        }
        IzdelekEntity izdelekEntity = entityManager.find(IzdelekEntity.class, izdelekId);
        if (izdelekEntity == null) {
            return null;
        }
        List<IzdelekEntity> trenutniIzdelki = kosaricaEntity.getIzdelki();
        int indexToRemove = -1;
        for(int i = 0; i < trenutniIzdelki.size(); i++) {
            if (trenutniIzdelki.get(i).getIzdelekId() == izdelekEntity.getIzdelekId()) {
                indexToRemove = i;
                break;
            }
        }
        KosaricaEntity novaKosarica = kosaricaEntity;
        if (indexToRemove > -1) {
            trenutniIzdelki.remove(indexToRemove);
            novaKosarica.setIzdelki(trenutniIzdelki);
            try {
                beginTx();
                novaKosarica = entityManager.merge(novaKosarica);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        }
        return KosaricaConverter.toDto(novaKosarica);
    }

    private void beginTx() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}
