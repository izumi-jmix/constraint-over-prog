package org.izumi.jmix.dgrid161editor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import io.jmix.core.DevelopmentException;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.Facet;
import io.jmix.ui.component.Fragment;
import io.jmix.ui.component.Frame;
import io.jmix.ui.component.HasComponents;
import io.jmix.ui.component.HasSubParts;
import io.jmix.ui.screen.FrameOwner;
import io.jmix.ui.screen.Install;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.ScreenFragment;
import io.jmix.ui.screen.Target;
import io.jmix.ui.screen.UiControllerUtils;
import io.jmix.ui.sys.ControllerDependencyInjector;
import io.jmix.ui.sys.UiControllerReflectionInspector;
import io.jmix.ui.sys.UiDescriptorUtils;
import io.jmix.ui.sys.ValuePathHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.jmix.ui.screen.UiControllerUtils.getScreenData;
import static io.jmix.ui.sys.ValuePathHelper.pathPrefix;

@Component(ActionsControllerDependencyInjector.BEAN_NAME)
public class ActionsControllerDependencyInjector implements ControllerDependencyInjector {
    public static final String BEAN_NAME = "uicomponents_ActionsControllerDependencyInjector";
    private static final String INSTALL_SUBJECT_ENABLED_RULE = "enabledRule";

    private final UiControllerReflectionInspector reflectionInspector;

    @Autowired
    public ActionsControllerDependencyInjector(UiControllerReflectionInspector reflectionInspector) {
        this.reflectionInspector = reflectionInspector;
    }

    @Override
    public void inject(InjectionContext context) {
        final var frameClass = context.getFrameOwner().getClass();
        final var screenIntrospectionData = reflectionInspector.getScreenIntrospectionData(frameClass);

        refreshActionsState(context.getFrameOwner(), screenIntrospectionData);
    }

    protected void refreshActionsState(FrameOwner frameOwner,
                                       UiControllerReflectionInspector.ScreenIntrospectionData introspectionData) {
        final var frame = UiControllerUtils.getFrame(frameOwner);
        final var enabledRuleMethodsStream = introspectionData.getInstallMethods().stream()
                .filter(this::isInstallToEnabledRule);

        final var toToMethods = enabledRuleMethodsStream.collect(Collectors.toMap(
                annotatedMethod -> annotatedMethod.getAnnotation().to(),
                List::of
        ));

        toToMethods.forEach((to, methods) -> {
            final var anyMethod = methods.iterator().next();
            final var annotation = anyMethod.getAnnotation();
            final var targetInstance = getInstallTargetInstance(frameOwner, annotation, frame);
            if (targetInstance != null && targetInstance instanceof BaseAction) {
                final var action = (BaseAction) targetInstance;
                action.refreshState();
            }
        });
    }

    @Nullable
    protected Object getInstallTargetInstance(FrameOwner frameOwner, Install annotation, Frame frame) {
        Object targetInstance;
        String target = UiDescriptorUtils.getInferredProvideId(annotation);
        if (Strings.isNullOrEmpty(target)) {

            switch (annotation.target()) {
                // if kept default value
                case COMPONENT:
                case CONTROLLER:
                    targetInstance = frameOwner;
                    break;

                case FRAME:
                    targetInstance = frame;
                    break;

                case PARENT_CONTROLLER:
                    if (frameOwner instanceof Screen) {
                        throw new DevelopmentException(
                                String.format("Screen %s cannot use @Install with target = PARENT_CONTROLLER",
                                        frame.getId())
                        );
                    }
                    targetInstance = ((ScreenFragment) frameOwner).getHostController();
                    break;

                case DATA_CONTEXT:
                    targetInstance = getScreenData(frameOwner).getDataContext();
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported @Install target " + annotation.target());
            }
        } else if (annotation.target() == Target.DATA_LOADER) {
            targetInstance = getScreenData(frameOwner).getLoader(target);
        } else if (annotation.target() == Target.DATA_CONTAINER) {
            targetInstance = getScreenData(frameOwner).getContainer(target);
        } else {
            targetInstance = findMethodTarget(frame, target);
        }
        return targetInstance;
    }

    private boolean isInstallToEnabledRule(UiControllerReflectionInspector.AnnotatedMethod<Install> annotatedMethod) {
        final var annotation = annotatedMethod.getAnnotation();
        return INSTALL_SUBJECT_ENABLED_RULE.equals(annotation.subject());
    }

    @Nullable
    private Object findMethodTarget(Frame frame, String target) {
        String[] elements = ValuePathHelper.parse(target);
        if (elements.length == 1) {
            Object part = frame.getSubPart(target);
            if (part != null) {
                return part;
            }

            io.jmix.ui.component.Component component = frame.getComponent(target);
            if (component != null) {
                return component;
            }

            return frame.getFacet(target);
        } else if (elements.length > 1) {
            String id = elements[elements.length - 1];

            io.jmix.ui.component.Component component = frame.getComponent(pathPrefix(elements));

            if (component != null) {
                if (component instanceof HasSubParts) {
                    Object part = ((HasSubParts) component).getSubPart(id);
                    if (part != null) {
                        return part;
                    }
                }

                if (component instanceof HasComponents) {
                    io.jmix.ui.component.Component childComponent = ((HasComponents) component).getComponent(id);
                    if (childComponent != null) {
                        return childComponent;
                    }
                }

                if (component instanceof Fragment) {
                    Facet facet = ((Fragment) component).getFacet(id);
                    if (facet != null) {
                        return facet;
                    }
                }
            }

            Facet facet = frame.getFacet(pathPrefix(elements));
            if (facet instanceof HasSubParts) {
                return ((HasSubParts) facet).getSubPart(id);
            }
        }

        return null;
    }
}
