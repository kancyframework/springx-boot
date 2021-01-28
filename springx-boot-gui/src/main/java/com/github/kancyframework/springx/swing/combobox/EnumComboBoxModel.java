package com.github.kancyframework.springx.swing.combobox;

import java.util.*;

/**
 * @author joshy
 * @author Karl Schaefer
 */
public class EnumComboBoxModel<E extends Enum<E>> extends ListComboBoxModel<E> {
    private static final long serialVersionUID = 2176566393195371004L;
    
    private final Map<String, E> valueMap;
    private final Class<E> enumClass;

    /**
     * Creates an {@code EnumComboBoxModel} for the enum represent by the
     * {@code Class} {@code en}.
     * 
     * @param en
     *            the enum class type
     * @throws IllegalArgumentException
     *             if the {@code Enum.toString} returns the same value for more
     *             than one constant
     */
    public EnumComboBoxModel(Class<E> en) {
        super(new ArrayList<E>(EnumSet.allOf(en)));
        
        //we could size these, probably not worth it; enums are usually small 
        valueMap = new HashMap<String, E>();
        enumClass = en;
        
        Iterator<E> iter = data.iterator();
        
        while (iter.hasNext()) {
            E element = iter.next();
            String s = element.toString();
            
            if (valueMap.containsKey(s)) {
                throw new IllegalArgumentException(
                        "multiple constants map to one string value");
            }
            
            valueMap.put(s, element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setSelectedItem(Object anItem) {
        E input = null;
        
        if (enumClass.isInstance(anItem)) {
            input = (E) anItem;
        } else {
            input = valueMap.get(anItem);
        }
        
        if (input != null || anItem == null) {
            selected = input;
        }
        
        this.fireContentsChanged(this, 0, getSize());
    }
}