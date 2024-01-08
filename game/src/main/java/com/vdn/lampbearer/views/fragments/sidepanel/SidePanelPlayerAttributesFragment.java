package com.vdn.lampbearer.views.fragments.sidepanel;

import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.entites.Printable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UI-компонент отображающий атрибуты
 */
@Slf4j
@RequiredArgsConstructor
public class SidePanelPlayerAttributesFragment implements Fragment {

    private final Collection<Attribute> attributes;


    @NotNull
    @Override
    public Component getRoot() {
        List<Printable> printableAttributes = attributes.stream()
                .filter(a -> a instanceof Printable)
                .map(a -> (Printable) a)
                .collect(Collectors.toList());

        Optional<Integer> maxWidth = printableAttributes.stream()
                .map(a -> a.toComponent().getWidth())
                .max(Integer::compareTo);

        VBox attributes = Components.vbox()
                .withPreferredSize(maxWidth.get(), printableAttributes.size())
                .build();

        for (Printable attribute : printableAttributes) {
            attributes.addComponent(attribute.toComponent());
        }

        return attributes;
    }
}
