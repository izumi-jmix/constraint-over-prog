package org.izumi.jmix.dgrid161editor.screen.manufacturer;

import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.dgrid161editor.entity.Manufacturer;

@UiController("Manufacturer.browse")
@UiDescriptor("manufacturer-browse.xml")
@LookupComponent("manufacturersTable")
public class ManufacturerBrowse extends StandardLookup<Manufacturer> {
}