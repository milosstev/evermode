package helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 	Ova klasa pravi zajedni�ki fajl otvaranja i fajl �uvanja dijaloga
 	za normalan rad i za Java Web Start.
 */
public abstract class FileHelper 
{

	/**
	    Uzima uslugu koja odgovara na�inu na koji ovaj program radi.
	    @return vra�a uslugu za lokalne dijaloge ili za Java Web Start
	*/
	public static synchronized FileHelper getInstance(File initialDirectory) {
		if (service != null)
			return service;
		try {
			service = new JFileChooserService(initialDirectory);
			return service;
		} catch (SecurityException exception) {
			// that happens when we run under Web Start
		}

		return null;
	}

	/**
	    Dobija otvoren objekat koji sa�ima tok i ime datoteke koju je korisnik izabrao.
	    @param defaultDirectory je podrazumijevani direktorij za biranje fajla
	    @param defaultFile je podrazumijevani fajl za biranje fajla
	    @param extensions je ekstenzija fajla
	    @return vra�a otvoren objekat za selektovanje fajla
	 */
	public abstract Open open(String defaultDirectory, String defaultFile,
			ExtensionHelper extensions) throws IOException;

	
	/**
	    Dobija sa�uvan objekat koji sa�ima tok i ime fajla koje je korisnik izabrao ili �e izabrati.
	    @param defaultDirectory je podrazumijevani direktorij za biranje fajla
	    @param defaultFile je podrazumijevani fajl za biranje fajla
	    @param extensions je ekstenzija fajla
	    @param removeExtension je ekstenzija za uklanjanje iz podrazumijevanog imena fajla
	    @param addExtension je ekstenzija za dodavanje imenu fajla
	    @return vra�a sa�uvan objekat za izabrani fajl
	*/
	public abstract Save save(String defaultDirectory, String defaultFile,
			ExtensionHelper extensions, String removeExtension,
			String addExtension) throws IOException;

	private static boolean webStart = false;
	private static FileHelper service;

	/**
	 	Otvoren objekat sa�ima tok i ime fajla koju je korisnik izabrao za otvaranje.
	*/
	public interface Open {

		/**
	       Dobija ulazni tok odgovaraju�i odabiru korisnika
	       @return vra�a ulazni tok   
	    */
		InputStream getInputStream() throws IOException;

		 /**
	       Dobija ime fajla koji je korisnik selektovao
	       @return vra�a ime fajla  
	     */
		String getName() throws IOException;
	}

	/**
	    Sa�uvani objekat sa�ima tok i ime fajla koji je korisnik izabrao za �uvanje.
	*/
	public interface Save 
	{

		/**
	       Dobija izlazni tok odgovaraju�i izboru korisnikova.
	       @return vra�a izlazni tok   
	    */
		OutputStream getOutputStream() throws IOException;

		/**
	       Dobija ime fajla koji koristnik izabrao.
	       @return vra�a ime fajla, ili ni�ta ako je dijalog fajla prikazan samo 
	       kada je izlazni tok zatvoren  
	    */
		String getName() throws IOException;
	}

	/**
	    Ova klasa implementira  FileService with a JFileChooser
	*/
	private static class JFileChooserService extends FileHelper {
		public JFileChooserService(File initialDirectory) {
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(initialDirectory);
		}

		public FileHelper.Open open(String defaultDirectory,
				String defaultFile, ExtensionHelper filter)
				throws FileNotFoundException {
			fileChooser.resetChoosableFileFilters();
			fileChooser.setFileFilter(filter);
			if (defaultDirectory != null)
				fileChooser.setCurrentDirectory(new File(defaultDirectory));
			if (defaultFile == null)
				fileChooser.setSelectedFile(null);
			else
				fileChooser.setSelectedFile(new File(defaultFile));
			int response = fileChooser.showOpenDialog(null);
			if (response == JFileChooser.APPROVE_OPTION)
				return new Open(fileChooser.getSelectedFile());
			else
				return new Open(null);
		}

		public FileHelper.Save save(String defaultDirectory,
				String defaultFile, ExtensionHelper filter,
				String removeExtension, String addExtension)
				throws FileNotFoundException {
			fileChooser.resetChoosableFileFilters();
			fileChooser.setFileFilter(filter);
			if (defaultDirectory == null)
				fileChooser.setCurrentDirectory(new File("."));
			else
				fileChooser.setCurrentDirectory(new File(defaultDirectory));

			if (defaultFile != null) {
				File f = new File(editExtension(defaultFile, removeExtension,
						addExtension));
				fileChooser.setSelectedFile(f);
			} else
				fileChooser.setSelectedFile(new File(""));
			int response = fileChooser.showSaveDialog(null);
			if (response == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				if (addExtension != null && f.getName().indexOf(".") < 0) // no
																			// extension
																			// supplied
					f = new File(f.getPath() + addExtension);
				if (!f.exists())
					return new Save(f);

				ResourceBundle editorResources = ResourceBundle
						.getBundle("EditorStrings");
				int result = JOptionPane.showConfirmDialog(null,
						editorResources.getString("dialog.overwrite"), null,
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
					return new Save(f);
			}

			return new Save(null);
		}

		public class Open implements FileHelper.Open {
			public Open(File f) throws FileNotFoundException {
				if (f != null) {
					name = f.getPath();
					in = new FileInputStream(f);
				}
			}

			public String getName() {
				return name;
			}

			public InputStream getInputStream() {
				return in;
			}

			private String name;
			private InputStream in;
		}

		public class Save implements FileHelper.Save {
			public Save(File f) throws FileNotFoundException {
				if (f != null) {
					name = f.getPath();
					out = new FileOutputStream(f);
				}
			}

			public String getName() {
				return name;
			}

			public OutputStream getOutputStream() {
				return out;
			}

			private String name;
			private OutputStream out;
		}

		public boolean isWebStart() {
			return false;
		}

		private JFileChooser fileChooser;
	}

	/**
	   Ure�uje putanju fajla i to tako da se zavr�ava �eljenom ekstenzijom.
	   @param original je fajl koji se koristi kao po�etna ta�ka
	   @param toBeRemoved je ekstenzija koja �e biti uklonjena prije dodavanja �eljene ekstenzije.
	   Koristi null ako ni�ta ne treba ukloniti.
	   @param desired je �eljena ekstenzija (npr. ".png"),ili odvojena | lista ekstenzija
	   @return vra�a original ako on ve� ima �eljenu ekstenzija
	   ili novi fajl sa ure�enom putanjom fajla. 
	 */
	public static String editExtension(String original, String toBeRemoved,
			String desired) {
		if (original == null)
			return null;
		int n = desired.indexOf('|');
		if (n >= 0)
			desired = desired.substring(0, n);
		String path = original;
		if (!path.toLowerCase().endsWith(desired.toLowerCase())) {
			if (toBeRemoved != null
					&& path.toLowerCase().endsWith(toBeRemoved.toLowerCase()))
				path = path.substring(0, path.length() - toBeRemoved.length());
			path = path + desired;
		}
		return path;
	}
}
