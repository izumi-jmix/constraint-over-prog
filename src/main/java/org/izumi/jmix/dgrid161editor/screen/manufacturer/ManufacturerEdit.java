package org.izumi.jmix.dgrid161editor.screen.manufacturer;

import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.dgrid161editor.entity.Manufacturer;

@UiController("Manufacturer.edit")
@UiDescriptor("manufacturer-edit.xml")
@EditedEntityContainer("manufacturerDc")
public class ManufacturerEdit extends StandardEditor<Manufacturer> {
}