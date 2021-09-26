package nl.knokko.customitems.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class CollectionHelper {

    public static <T, R> Optional<T> find(Collection<T> collection, Function<T, R> extract, R value) {
        return collection.stream().filter(candidate -> extract.apply(candidate).equals(value)).findFirst();
    }

    public static byte[] arrayCopy(byte[] original) {
        if (original == null) return null;
        return Arrays.copyOf(original, original.length);
    }
}
