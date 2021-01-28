package net.miginfocom.swing;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;

/**
 */
public final class SwingContainerWrapper extends SwingComponentWrapper implements ContainerWrapper
{
	/** Debug color for cell outline.
	 */
	private static final Color DB_CELL_OUTLINE = new Color(255, 0, 0);

	public SwingContainerWrapper(Container c)
	{
		super(c);
	}

	@Override
	public ComponentWrapper[] getComponents()
	{
		Container c = (Container) getComponent();
		ComponentWrapper[] cws = new ComponentWrapper[c.getComponentCount()];
		for (int i = 0; i < cws.length; i++)
			cws[i] = new SwingComponentWrapper(c.getComponent(i));
		return cws;
	}

	@Override
	public int getComponentCount()
	{
		return ((Container) getComponent()).getComponentCount();
	}

	@Override
	public Object getLayout()
	{
		return ((Container) getComponent()).getLayout();
	}

	@Override
	public final boolean isLeftToRight()
	{
		return ((Container) getComponent()).getComponentOrientation().isLeftToRight();
	}

	@Override
	public final void paintDebugCell(int x, int y, int width, int height)
	{
		Component c = (Component) getComponent();
		if (c.isShowing() == false)
			return;

		Graphics2D g = (Graphics2D) c.getGraphics();
		if (g == null)
			return;

		g.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10f, new float[] {2f, 3f}, 0));
		g.setPaint(DB_CELL_OUTLINE);
		g.drawRect(x, y, width - 1, height - 1);
	}

	@Override
	public int getComponentType(boolean disregardScrollPane)
	{
		return TYPE_CONTAINER;
	}

	// Removed for 2.3 because the parent.isValid() in MigLayout will catch this instead.
	@Override
	public int getLayoutHashCode()
	{
		long n = System.nanoTime();
		int h = super.getLayoutHashCode();

		if (isLeftToRight())
			h += 416343;

		return 0;
	}
}
