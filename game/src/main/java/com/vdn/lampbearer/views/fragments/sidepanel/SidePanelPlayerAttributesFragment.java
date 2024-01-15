package com.vdn.lampbearer.views.fragments.sidepanel;

import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.entites.Printable;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.AttachedComponent;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hexworks.zircon.api.ComponentDecorations.margin;

/**
 * UI-компонент отображающий атрибуты
 */
@Slf4j
public class SidePanelPlayerAttributesFragment implements Fragment {

    private final VBox attributesVbox;
    private final List<Printable> printableAttributes;
    private final List<AttachedComponent> attributesFragments = new ArrayList<>();


    public SidePanelPlayerAttributesFragment(Collection<Attribute> attributes) {
        printableAttributes = attributes.stream()
                .filter(a -> a instanceof Printable)
                .map(a -> (Printable) a)
                .collect(Collectors.toList());

        Optional<Integer> maxAttributeWidth = printableAttributes.stream()
                .map(a -> a.toComponent().getWidth())
                .max(Integer::compareTo);

        attributesVbox = Components.vbox()
                .withPreferredSize(maxAttributeWidth.get() + 1, printableAttributes.size())
                .withDecorations(margin(0, 0, 0, 1))
                .build();
    }


    @NotNull
    @Override
    public Component getRoot() {
        addAttributesToList();
        return attributesVbox;
    }


    public void updateContent() {
        attributesFragments.forEach(AttachedComponent::detach);
        attributesFragments.clear();
        addAttributesToList();
    }


    private void addAttributesToList() {
        for (Printable attribute : printableAttributes) {
            AttachedComponent attachedComponent = attributesVbox
                    .addComponent(attribute.toComponent());
            attributesFragments.add(attachedComponent);
        }
    }
}
