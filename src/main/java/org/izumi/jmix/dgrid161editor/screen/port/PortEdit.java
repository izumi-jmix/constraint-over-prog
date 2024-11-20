package org.izumi.jmix.dgrid161editor.screen.port;

import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.dgrid161editor.entity.Port;

@UiController("Port.edit")
@UiDescriptor("port-edit.xml")
@EditedEntityContainer("portDc")
public class PortEdit extends StandardEditor<Port> {
}