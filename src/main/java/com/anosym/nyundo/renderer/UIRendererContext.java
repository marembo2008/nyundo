package com.anosym.nyundo.renderer;

import com.anosym.nyundo.component.UIComponent;
import com.anosym.nyundo.element.UIElement;
import com.anosym.nyundo.util.UIComponentId;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * May contain information relevant to the current rendering.
 *
 * @author mochieng
 */
public interface UIRendererContext {

    /**
     * The current component, is the parent of the component to be rendered next.
     *
     * @return
     */
    @Nullable
    UIComponent getCurrentComponent();

    /**
     * The current element, is the rendered parent of the next element rendered.
     *
     * @return
     */
    @Nullable
    UIElement getCurrentElement();

    /**
     * The root component.
     *
     * @return
     */
    @Nullable
    UIComponent getRootComponent();

    /**
     * The root element.
     *
     * @return
     */
    @Nullable
    UIElement getRootElement();

    /**
     * Searches a component by id. If this id starts by ':' then it is considered an absolute id of the component, and
     * the search begins from the root component.
     *
     * @param id
     *
     * @return
     */
    @Nullable
    UIComponent findComponent(@Nonnull final String id);

    /**
     * Searches a component by id. If this id starts by ':' then it is considered an absolute id of the component, and
     * the search begins from the root component.
     *
     * @param id
     *
     * @return
     */
    @Nullable
    UIComponent findComponent(@Nonnull final UIComponentId componentId);

    /**
     * Called by the current renderer to continue the rendering of child component of the current uicomponent.
     *
     * Provided the child content of the current UIComponent is an UIComponent, UIRenderers must delegate to this call,
     * even if the same UIRenderer is responsible for the actual decoding. If an UIRenderer fails to delegate its
     * rendering of the component, hierarchy chain will fail.
     *
     * <pre>
     *  <code>
     *   public class MyRenderer implements UIRenderer{
     *     ....
     *     public UIElement render(final UIComponent component, final UIRendererContext context) {
     *      UIElement compElem = decodeComponent(component);
     *      for(UIComponent child: component.children){
     *        UIElement childEleme = context.render(child);
     *      }
     *      ...
     *      return compElem;
     *     }
     *   }
     *  </code>
     * </pre>
     *
     * @param component the component to render to an UIElement
     */
    UIElement render(@Nonnull final UIComponent component);

}
