package net.miginfocom.layout;

/**
 */
public abstract class UnitConverter
{
	/** Value to return if this converter can not handle the <code>unit</code> sent in as an argument
	 * to the convert method.
	 */
	public static final int UNABLE = -87654312;

	/** Converts <code>value</code> to pixels.
	 * @param value The value to be converted.
	 * @param unit The unit of <code>value</code>. Never <code>null</code> and at least one character.
	 * @param refValue Some reference value that may of may not be used. If the unit is percent for instance this value
	 * is the value to take the percent from. Usually the size of the parent component in the appropriate dimension.
	 * @param isHor If the value is horizontal (<code>true</code>) or vertical (<code>false</code>).
	 * @param parent The parent of the target component that <code>value</code> is to be applied to.
	 * Might for instance be needed to get the screen that the component is on in a multi screen environment.
	 * <p>
	 * May be <code>null</code> in which case a "best guess" value should be returned.
	 * @param comp The component, if applicable, or <code>null</code> if none.
	 * @return The number of pixels if <code>unit</code> is handled by this converter, <code>UnitConverter.UNABLE</code> if not.
	 */
	public abstract int convertToPixels(float value, String unit, boolean isHor, float refValue, ContainerWrapper parent, ComponentWrapper comp);
}
