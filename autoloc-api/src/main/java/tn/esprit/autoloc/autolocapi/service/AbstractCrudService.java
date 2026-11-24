package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/** Shared CRUD and bean validation for the small domain services. */
public abstract class AbstractCrudService<T> extends GenericServiceImpl<T, Long> {
    private final String entityName;
    private final Method idGetter;

    protected AbstractCrudService(JpaRepository<T, Long> repository, Class<T> type, String entityName) {
        super(repository);
        this.entityName = entityName;
        try {
            this.idGetter = type.getMethod("getId" + type.getSimpleName());
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("Entité sans getter d'identifiant : " + type.getName(), ex);
        }
    }

    @Override protected String getEntityName() { return entityName; }

    @Override
    protected Long getId(T entity) {
        if (entity == null) return null;
        try { return (Long) idGetter.invoke(entity); }
        catch (ReflectiveOperationException ex) { throw new IllegalStateException(ex); }
    }

    @Override
    protected void setFields(T target, T source) {
        for (Field field : source.getClass().getDeclaredFields()) {
            if (isStaticOrSynthetic(field) || isIdentifierField(field, source.getClass().getSimpleName())) continue;
            try {
                Object value = readProperty(source, field.getName());
                if (value != null && (!(value instanceof Collection<?> collection) || !collection.isEmpty())) {
                    String name = field.getName();
                    String suffix = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    Method setter = java.util.Arrays.stream(target.getClass().getMethods())
                            .filter(m -> m.getName().equals("set" + suffix) && m.getParameterCount() == 1)
                            .findFirst().orElse(null);
                    if (setter != null) setter.invoke(target, value);
                }
            } catch (ReflectiveOperationException ex) {
                throw new IllegalStateException("Impossible de mettre à jour " + field.getName(), ex);
            }
        }
    }

    @Override
    public boolean check(T entity, boolean isNew) {
        if (entity == null || (isNew ? getId(entity) != null : getId(entity) == null)) return false;
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (isStaticOrSynthetic(field) || isValidationExemptField(field)) continue;
            if (!isValidField(entity, field)) return false;
        }
        return true;
    }

    private boolean isValidField(T entity, Field field) {
        try {
            Object value = readProperty(entity, field.getName());
            return isValidValue(value) && isDateRangeValid(entity, field, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private boolean isValidValue(Object value) {
        if (value == null) return false;
        if (value instanceof String text && !Utils.isNotEmpty(text)) return false;
        if (value instanceof java.math.BigDecimal number && number.signum() < 0) return false;
        return !(value instanceof Number number && !(value instanceof java.math.BigDecimal) && number.longValue() < 0);
    }

    private boolean isDateRangeValid(T entity, Field field, Object value) throws ReflectiveOperationException {
        if (!field.getName().equals("dateFin") || !(value instanceof java.time.LocalDate end)) return true;
        Object start = readProperty(entity, "dateDebut");
        return !(start instanceof java.time.LocalDate startDate) || !end.isBefore(startDate);
    }

    private Object readProperty(T entity, String fieldName) throws ReflectiveOperationException {
        String suffix = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        Method getter;
        try {
            getter = entity.getClass().getMethod("get" + suffix);
        } catch (NoSuchMethodException ex) {
            getter = entity.getClass().getMethod("is" + suffix);
        }
        return getter.invoke(entity);
    }

    private static boolean isStaticOrSynthetic(Field field) {
        return java.lang.reflect.Modifier.isStatic(field.getModifiers()) || field.getName().startsWith("$");
    }

    private static boolean isIdentifierField(Field field, String typeName) {
        String name = field.getName();
        return name.equalsIgnoreCase("id" + typeName) || name.equals("idAgence") || name.equals("idClient")
                || name.equals("idVehicule") || name.equals("idEmploye") || name.equals("idEquipement")
                || name.equals("idContrat") || name.equals("idReservation") || name.equals("idPaiement")
                || name.equals("idMaintenance");
    }

    private static boolean isValidationExemptField(Field field) {
        String name = field.getName();
        return name.startsWith("id") || name.equals("agence") || name.equals("reservations")
                || name.equals("maintenances") || name.equals("equipements") || name.equals("vehicules")
                || name.equals("employes") || name.equals("effectif") || name.equals("vehicule")
                || name.equals("client") || name.equals("reservation") || name.equals("contrat");
    }
}
