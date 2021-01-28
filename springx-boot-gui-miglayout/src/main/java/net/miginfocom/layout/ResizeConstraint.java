package net.miginfocom.layout;

import java.io.*;

/** A parsed constraint that specifies how an entity (normally column/row or component) can shrink or
 * grow compared to other entities.
 */
final class ResizeConstraint implements Externalizable
{
	static final Float WEIGHT_100 = 100f;

	/** How flexible the entity should be, relative to other entities, when it comes to growing. <code>null</code> or
	 * zero mean it will never grow. An entity that has twice the growWeight compared to another entity will get twice
	 * as much of available space.
	 * <p>
	 * "grow" are only compared within the same "growPrio".
	 */
	Float grow = null;

	/** The relative priority used for determining which entities gets the extra space first.
	 */
	int growPrio = 100;

	Float shrink = WEIGHT_100;

	int shrinkPrio = 100;

	public ResizeConstraint()   // For Externalizable
	{
	}

	ResizeConstraint(int shrinkPrio, Float shrinkWeight, int growPrio, Float growWeight)
	{
		this.shrinkPrio = shrinkPrio;
		this.shrink = shrinkWeight;
		this.growPrio = growPrio;
		this.grow = growWeight;
	}

	// ************************************************
	// Persistence Delegate and Serializable combined.
	// ************************************************

	private Object readResolve() throws ObjectStreamException
	{
		return LayoutUtil.getSerializedObject(this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		if (getClass() == ResizeConstraint.class)
			LayoutUtil.writeAsXML(out, this);
	}
}
