package com.anosym.nyundo.util;

import static com.anosym.nyundo.util.UIComponentId.fromCanonicalId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Deque;
import org.junit.Test;

/**
 *
 * @author mochieng
 */
public class UIComponentIdTest {

    @Test
    public void testParentQueue() {
        final UIComponentId one = new UIComponentId("id0");
        final UIComponentId two = new UIComponentId("id1");
        two.setParentId(one);
        final UIComponentId three = new UIComponentId("id2");
        three.setParentId(two);
        final Deque<UIComponentId> stack = three.getAncestryStack();

        assertThat(stack, hasSize(3));
        assertThat(stack.pop(), is(one));
        assertThat(stack.pop(), is(two));
        assertThat(stack.pop(), is(three));
    }

    @Test
    public void testParentQueueNotInOrder() {
        final UIComponentId one = new UIComponentId("id0");
        final UIComponentId two = new UIComponentId("id1");
        two.setParentId(one);
        final UIComponentId three = new UIComponentId("id2");
        three.setParentId(one);
        final Deque<UIComponentId> stack = three.getAncestryStack();

        assertThat(stack, hasSize(2));
        assertThat(stack.pop(), is(one));
        assertThat(stack.pop(), is(three));
    }

    @Test
    public void testCanonicalId() {
        final UIComponentId one = new UIComponentId("id0");
        final UIComponentId two = new UIComponentId("id1");
        two.setParentId(one);
        final UIComponentId three = new UIComponentId("id2");
        three.setParentId(two);
        final String canonicalId = three.getCanonicalId();
        final String expected = "id0:id1:id2";

        assertThat(canonicalId, is(expected));
    }

    @Test
    public void testFromCanonicalIdIsRelativeComponentId() {
        final String canonicalId = "id0:id1:id2";
        UIComponentId componentId = fromCanonicalId(canonicalId);

        assertThat(componentId.isAbsolute(), is(false));
    }

    @Test
    public void testFromCanonicalIdIsAbsoluteComponentId() {
        final String canonicalId = ":id0:id1:id2";
        UIComponentId componentId = fromCanonicalId(canonicalId);

        assertThat(componentId.isAbsolute(), is(true));
    }

    @Test
    public void testFromCanonicalIdParentComponentId() {
        final String canonicalId = ":id0:id1:id2";
        final UIComponentId parentComponentId = new UIComponentId(
                new UIComponentId(null, "id0", true), "id1", true);
        UIComponentId componentId = fromCanonicalId(canonicalId);

        assertThat(componentId.getParentId(), is(parentComponentId));
    }

    @Test(expected = IllegalStateException.class)
    public void testFromParentIdMustBeAbsoluteForChildIdToBeAbsolute() {
        final UIComponentId parent = new UIComponentId("id99");
        final UIComponentId child = new UIComponentId(parent, "id910", true);
    }

}
