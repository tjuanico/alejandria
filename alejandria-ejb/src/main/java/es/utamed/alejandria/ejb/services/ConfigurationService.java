package es.utamed.alejandria.ejb.services;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Singleton
public class ConfigurationService {

    @PersistenceContext(unitName="alejandriaPU")
    EntityManager em;

    private String entornoActual;

    @PostConstruct
    public void init() {
        this.entornoActual = System.getProperty("alejandria.entorno", "DES");
        System.out.println("Configuración cargada para el entorno: " + this.entornoActual);
    }

    public String getValor(String clave) {
        try {
            return em.createNamedQuery("ConfiguracionEntity.obtenerParClaveValor", String.class)
                    .setParameter("clave", clave)
                    .setParameter("entorno", this.entornoActual)
                    .getSingleResult();
        } catch (Exception e) {
            // Manejo de error si no existe la propiedad
            return null;
        }
    }
}
