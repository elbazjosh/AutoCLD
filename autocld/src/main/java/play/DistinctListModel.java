package play;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.swing.DefaultListModel;

public class DistinctListModel<E> extends DefaultListModel<E> {
    public DistinctListModel(Collection<E> data) {
        for (E e: new LinkedHashSet<E>(data)) {
            addElement(e);
        }
    }
}