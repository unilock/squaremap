package xyz.jpenilla.squaremap.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.ApiStatus;
import xyz.jpenilla.squaremap.api.temporary.ComponentEncoder;

/**
 * Safely encodes {@link Component Components} as HTML text.
 *
 * <p>Mostly useful for marker tooltips.</p>
 *
 * @see Squaremap#htmlComponentSerializer()
 * @since 1.2.0
 */
@DefaultQualifier(NonNull.class)
public interface HtmlComponentSerializer extends ComponentEncoder<Component, String> {

    /**
     * Create a new {@link HtmlComponentSerializer} using the provided {@link ComponentFlattener}.
     *
     * @param flattener component flattener
     * @return serializer
     */
    static HtmlComponentSerializer withFlattener(final ComponentFlattener flattener) {
        return ProviderHolder.HTML_SERIALIZER.create(flattener);
    }

    @ApiStatus.Internal
    interface Provider {
        HtmlComponentSerializer create(ComponentFlattener flattener);
    }

}
