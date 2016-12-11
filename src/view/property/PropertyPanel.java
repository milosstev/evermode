package view.property;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/**
	Komponenta ispunjena editorima za sve osobine ure�ivanja nekog objekta.
*/
public class PropertyPanel extends JPanel 
{

	@SuppressWarnings("unchecked")
	/**
	    Konstrui�e osobine lista koji pokazuje sposobnost ure�ivanja zadatog objekta.
	    @param object je objekat �ije osobine se edituju
	    @param parent je roditeljska komponenta
    */
	public PropertyPanel(Object bean, Component parent) 
	{
		this.parent = parent;
		try {
			BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] descriptors = (PropertyDescriptor[]) info
					.getPropertyDescriptors().clone();
			Arrays.sort(descriptors, new Comparator() {
				public int compare(Object o1, Object o2) {
					PropertyDescriptor d1 = (PropertyDescriptor) o1;
					PropertyDescriptor d2 = (PropertyDescriptor) o2;
					Integer p1 = (Integer) d1.getValue("priority");
					Integer p2 = (Integer) d2.getValue("priority");
					if (p1 == null && p2 == null)
						return 0;
					if (p1 == null)
						return 1;
					if (p2 == null)
						return -1;
					return p1.intValue() - p2.intValue();
				}
			});
			setLayout(new FormLayout());
			for (int i = 0; i < descriptors.length; i++) {
				PropertyEditor editor = getEditor(bean, descriptors[i]);
				if (editor != null) {
					add(new JLabel(descriptors[i].getName()));
					add(getEditorComponent(editor));
				}
			}
		} catch (IntrospectionException exception) {
			exception.printStackTrace();
		}
	}

	/**
	    Dobija osobine editora za dati posjed i povezuje ih tako da a�urira
	    dati objekat.
	    @param bean je objekat �ije osobine se edituju(ure�uju)
	    @param descriptor je indentifikator posjeda koji se edituje
	    @return vra�a svojstvo editora koji ure�uje podu�je sa datim identifikatorom
	    i a�urira dati objekat
	*/
	public PropertyEditor getEditor(final Object bean,
			PropertyDescriptor descriptor) 
	{
		try 
		{
			Method getter = descriptor.getReadMethod();
			if (getter == null)
				return null;
			final Method setter = descriptor.getWriteMethod();
			if (setter == null)
				return null;
			Class type = descriptor.getPropertyType();
			final PropertyEditor editor;
			Class editorClass = descriptor.getPropertyEditorClass();
			if (editorClass == null && editors.containsKey(type))
				editorClass = (Class) editors.get(type);
			if (editorClass != null)
				editor = (PropertyEditor) editorClass.newInstance();
			else
				editor = PropertyEditorManager.findEditor(type);
			if (editor == null)
				return null;

			Object value = getter.invoke(bean, new Object[] {});
			editor.setValue(value);
			editor.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent event) {
					try {
						setter.invoke(bean, new Object[] { editor.getValue() });
						fireStateChanged(null);
					} catch (IllegalAccessException exception) {
						exception.printStackTrace();
					} catch (InvocationTargetException exception) {
						exception.printStackTrace();
					}
				}
			});
			return editor;
		} catch (InstantiationException exception) {
			exception.printStackTrace();
			return null;
		} catch (IllegalAccessException exception) {
			exception.printStackTrace();
			return null;
		} catch (InvocationTargetException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	    Obuhvata podru�je izmjene u komponentu.
	    @param editor je editor koji se obuhvata
	    @return vra�a dugme (ako je naru�eni editor), kombinovani okvir (ako editor ima oznake),
	    ili tekstualno polje (u drugom slu�aju)
	*/   
	public Component getEditorComponent(final PropertyEditor editor) {
		String[] tags = editor.getTags();
		String text = editor.getAsText();
		if (editor.supportsCustomEditor()) {
			return editor.getCustomEditor();
		} else if (tags != null) {
			// make a combo box that shows all tags
			final JComboBox comboBox = new JComboBox(tags);
			comboBox.setSelectedItem(text);
			comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					if (event.getStateChange() == ItemEvent.SELECTED)
						editor.setAsText((String) comboBox.getSelectedItem());
				}
			});
			return comboBox;
		} else {
			final JTextField textField = new JTextField(text, 10);
			textField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					try {
						editor.setAsText(textField.getText());
					} catch (IllegalArgumentException exception) {
					}
				}

				public void removeUpdate(DocumentEvent e) {
					try {
						editor.setAsText(textField.getText());
					} catch (IllegalArgumentException exception) {
					}
				}

				public void changedUpdate(DocumentEvent e) {
				}
			});
			return textField;
		}
	}

	/**
	    Formatira tekst za dugme da otvara preporu�eni editor.
	    @param text je svojstvena vrijednost kao tekst
	    @return vra�a tekst koji se stavlja na dugme
    */
	private static String buttonText(String text) {
		if (text == null || text.equals(""))
			return " ";
		if (text.length() > MAX_TEXT_LENGTH)
			return text.substring(0, MAX_TEXT_LENGTH) + "...";
		return text;
	}

	 /**
	    Dodaje promjene listener-a(oslu�kiva�a) na listu listenera(oslu�kiva�a).
	    @param listener je oslu�kiva� koji se dodaje
     */
	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	/**
	    Obavje�tava sve oslu�kiva�e o promijenjenom stanju.
	    @param event je doga�aj za oslu�kivanje
    */
	private void fireStateChanged(ChangeEvent event) {
		for (int i = 0; i < changeListeners.size(); i++) {
			ChangeListener listener = (ChangeListener) changeListeners.get(i);
			listener.stateChanged(event);
		}
	}

	private ArrayList changeListeners = new ArrayList();
	private Component parent;

	private static Map editors;

	static {
		editors = new HashMap();
	}

	private static final int WIDTH = 100;
	private static final int HEIGHT = 25;
	private static final int MAX_TEXT_LENGTH = 15;
}
