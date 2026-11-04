package metropet_clinic.demo.repository;

import metropet_clinic.demo.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
 // Se crea el método para buscar las facturas asociadas a una mascota específica  
    List<Factura> findByMascotaId(Long mascotaId);
}