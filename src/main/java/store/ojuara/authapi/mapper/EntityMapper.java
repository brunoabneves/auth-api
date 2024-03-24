package store.ojuara.authapi.mapper;

import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E, F> {

    E toModel(F form);
    D toDto(E entity);
    F toForm(E entity);

    List<E> toModel(List<F> formList);
    List<E> toModel(Set<F> formSet);
    List<D> toDto(List<E> entityList);
}
