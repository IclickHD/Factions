package io.github.iclickhd.factions.utils;

import java.util.Iterator;

import org.spongepowered.api.text.Text;

public class TextUtils {
    public static Text join(final Iterable<Text> iterable, final Text separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    public static Text join(final Iterator<Text> iterator, final Text separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return Text.EMPTY;
        }
        final Text first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }

        
        Text.Builder builder = Text.builder();
        if (first != null) {
        	builder.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
            	builder.append(separator);
            }
            final Text text = iterator.next();
            if (text != null) {
            	builder.append(text);
            }
        }
        return builder.build();
    }
}
