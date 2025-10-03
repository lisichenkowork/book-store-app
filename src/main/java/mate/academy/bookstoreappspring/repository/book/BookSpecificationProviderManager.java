package mate.academy.bookstoreappspring.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.exception.ProviderNotFoundException;
import mate.academy.bookstoreappspring.repository.SpecificationProvider;
import mate.academy.bookstoreappspring.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager<Book> implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> providers;


    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return providers.stream().filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new ProviderNotFoundException("Provider is not found, key " + key + " doesn't exist"));
    }
}
