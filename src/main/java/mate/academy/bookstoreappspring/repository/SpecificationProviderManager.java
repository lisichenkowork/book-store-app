package mate.academy.bookstoreappspring.repository;

public interface SpecificationProviderManager<T> {

    SpecificationProvider<T> getSpecificationProvider(String key);
}
