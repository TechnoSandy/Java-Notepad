package com.learning.notpad;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import say.swing.JFontChooser;

public class NotpadSandy {
	static Locale locale = new Locale("en", "US");
	private static ResourceBundle BUNDLE;
	private static JTextArea textArea = new JTextArea();
	// private static final String VOICENAME = "kevin16";

	public static Locale getLocale() {
		return locale;
	}

	public static void setLocale(Locale locale) {
		NotpadSandy.locale = locale;
	}

	private JFrame frame;
	JCheckBox chckbxOn;
	JCheckBox chckbxOff;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				boolean visible = true;
				NotpadSandy window = null;
				try {
					if (args.length != 0) {
						if (!args[0].isEmpty() || args[0] != "") {
							try {
								BufferedReader br = new BufferedReader(new FileReader(args[0]));
								textArea.read(br, null);
								br.close();
								textArea.requestFocus();
//								window.frame.setTitle(args[0]);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						if (System.getProperty("java.version").startsWith("1.8.")) {
							window = new NotpadSandy();
							window.frame.setVisible(visible);
						} else {
							JOptionPane.showMessageDialog(null, "Please Install java version 1.8.xxxx ");
						}
					} else {
						if (System.getProperty("java.version").startsWith("1.8.")) {
							window = new NotpadSandy();
							window.frame.setVisible(visible);
						} else {
							JOptionPane.showMessageDialog(null, "Please Install java version 1.8.xxxx ");
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NotpadSandy() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Locale locale = getlocale();
		BUNDLE = ResourceBundle.getBundle("com.learning.notpad.messages", locale);
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		Image frameicon = new ImageIcon(this.getClass().getResource("/Notepad-Bloc-notes-icon.png")).getImage();
		frame = new JFrame();
		frame.setBounds(100, 100, 545, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setTitle(BUNDLE.getString("NotpadSandy.frame.title")); //$NON-NLS-1$
		frame.setIconImage(frameicon);

		JScrollPane scrollingArea = new JScrollPane();
		scrollingArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollingArea);

		// textArea = new JTextArea();
		scrollingArea.setViewportView(textArea);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(BUNDLE.getString("NotpadSandy.mnFile.text")); //$NON-NLS-1$
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem(BUNDLE.getString("NotpadSandy.mntmNew.text")); //$NON-NLS-1$
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Save", "Don't Save", "Cancel" };

				int button = JOptionPane.showOptionDialog(null, "Do you want to save changes to" + frame.getTitle(),
						"Notepad", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,

				null, options, options[0]);
				JFileChooser newFile = new JFileChooser();
				newFile.setDialogTitle("Save the file");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("text file", "txt", "text");
				newFile.setFileFilter(filter);

				if (button == 0) {

					// int option = newFile.showSaveDialog(frame);
					newFile.showSaveDialog(frame);
					saveAction(textArea, newFile);

				} else if (button == 1) {
					textArea.setText(null);
				} else if (button == 2) {
				}

			}
		});
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem(BUNDLE.getString("NotpadSandy.mntmOpen.text")); //$NON-NLS-1$
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(textArea);

			}

		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem(BUNDLE.getString("NotpadSandy.mntmSave.text")); //$NON-NLS-1$
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser save = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				save.setFileFilter(filter);
				int option = save.showSaveDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION) {
					saveAction(textArea, save);

				}

			}

		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem(BUNDLE.getString("NotpadSandy.mntmExit.text")); //$NON-NLS-1$
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		mnFile.add(mntmExit);

		JMenu mnFormat = new JMenu(BUNDLE.getString("NotpadSandy.mnFormat.text")); //$NON-NLS-1$
		menuBar.add(mnFormat);

		JMenuItem mntmFont = new JMenuItem(BUNDLE.getString("NotpadSandy.mntmFont.text")); //$NON-NLS-1$
		mntmFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JFontChooser is working 100 percent
				JFontChooser fontChooser = new JFontChooser();
				int result = fontChooser.showDialog(frame);
				if (result == JFontChooser.OK_OPTION) {
					Font font = fontChooser.getSelectedFont();
					textArea.setFont(font);
				}
			}
		});
		mnFormat.add(mntmFont);

		JMenu mnHelp = new JMenu(BUNDLE.getString("NotpadSandy.mnHelp.text")); //$NON-NLS-1$
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem(BUNDLE.getString("NotpadSandy.mntmAbout.text")); //$NON-NLS-1$
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About ab = About.getInstance();
				ab.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		JMenu mnNightmode = new JMenu(BUNDLE.getString("NotpadSandy.mnNightmode.text")); //$NON-NLS-1$
		menuBar.add(mnNightmode);

		chckbxOff = new JCheckBox(BUNDLE.getString("NotpadSandy.chckbxOff.text")); //$NON-NLS-1$
		chckbxOff.setMnemonic(KeyEvent.VK_O);
		chckbxOff.setSelected(true);
		chckbxOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxOff.setSelected(true);
				chckbxOn.setSelected(false);
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		mnNightmode.add(chckbxOff);
		chckbxOn = new JCheckBox(BUNDLE.getString("NotpadSandy.chckbxOn.text")); //$NON-NLS-1$
		chckbxOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxOff.setSelected(false);
				chckbxOn.setSelected(true);
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnNightmode.add(chckbxOn);
		JMenu mnLaunguage = new JMenu(BUNDLE.getString("NotpadSandy.mnLaunguage.launguage"));
		menuBar.add(mnLaunguage);

		String [] empty ={""};
		
		JMenuItem mntmHindi = new JMenuItem(BUNDLE.getString("NotpadSandy.mnLaunguage.launguage.hindi"));
		mntmHindi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale locale = new Locale("hi", "IN");
				setLocale(locale);
				int option = JOptionPane.showConfirmDialog(null,
						BUNDLE.getString("NotpadSandy.mnLaunguage.confirmationMessage"));
				if (option == JOptionPane.OK_OPTION) {
					frame.dispose();
					NotpadSandy.main(empty);
					SwingUtilities.updateComponentTreeUI(frame);
				}
			}
		});
		mnLaunguage.add(mntmHindi);
		JMenuItem mntmEnglish = new JMenuItem(BUNDLE.getString("NotpadSandy.mnLaunguage.launguage.english"));
		mntmEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale locale = new Locale("en", "US");
				setLocale(locale);
				int option = JOptionPane.showConfirmDialog(null,
						BUNDLE.getString("NotpadSandy.mnLaunguage.confirmationMessage"));
				if (option == JOptionPane.OK_OPTION) {
					frame.dispose();
					NotpadSandy.main(empty);
					SwingUtilities.updateComponentTreeUI(frame);
				}
			}
		});
		mnLaunguage.add(mntmEnglish);
		JMenu mnDictionary = new JMenu(BUNDLE.getString("NotpadSandy.dictionarybutton.dictionary"));
		mnDictionary.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					// when in eclipse use this 
					Runtime.getRuntime().exec("./dictionary/WordWeb/wwnotray.exe", null);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1);
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(mnDictionary);

		// http://java-demos.blogspot.in/2013/06/drag-and-drop-file-in-jtextarea.html
		enableDragAndDrop(textArea);

	}

	private void enableDragAndDrop(JTextArea textArea) {
		@SuppressWarnings("unused")
		DropTarget target = new DropTarget(textArea, new DropTargetListener() {
			public void dragEnter(DropTargetDragEvent e) {
			}

			public void dragExit(DropTargetEvent e) {
			}

			public void dragOver(DropTargetDragEvent e) {
			}

			public void dropActionChanged(DropTargetDragEvent e) {

			}

			public void drop(DropTargetDropEvent e) {
				try {
					// Accept the drop first, important!
					e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

					// Get the files that are dropped as java.util.List
					@SuppressWarnings("rawtypes")
					java.util.List list = (java.util.List) e.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);

					// Now get the first file from the list,
					File file = (File) list.get(0);
					textArea.read(new FileReader(file), null);

				} catch (Exception ex) {
				}
			}
		});
	}

	public void saveAction(JTextArea textArea, JFileChooser save) {
		try {
			File f = new File(save.getSelectedFile().getAbsolutePath().toString());
			System.out.println(save.getSelectedFile().getAbsolutePath().toString());
			if (!f.exists()) {
				WriteFile(textArea, save);
			} else {
				int userResponse = JOptionPane.showConfirmDialog(frame,
						"File already exist Do You want to override it ?");
				if (userResponse == JOptionPane.YES_OPTION) {
					WriteFile(textArea, save);
				} else {
					JOptionPane.showMessageDialog(frame, "File is not modified !!!");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void WriteFile(JTextArea textArea, JFileChooser save) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
		String[] line = textArea.getText().split("\\n");
		if (!textArea.equals(null)) {
			for (int i = 0; i < line.length; i++) {
				out.write(line[i]);
				out.newLine();
				out.flush();
			}

		}
		out.close();
	}

	public void openFile(JTextArea textArea) {
		JFileChooser open = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		open.setFileFilter(filter);
		// int option = open.showOpenDialog(frame);
		open.showOpenDialog(frame);
		if (!open.getSelectedFile().equals(null)) {
			JOptionPane.showMessageDialog(frame, "Selected File is : " + open.getSelectedFile().getName());
			frame.setTitle(open.getSelectedFile().getName());
			try {
				BufferedReader br = new BufferedReader(new FileReader(open.getSelectedFile().getPath()));
				textArea.read(br, null);
				br.close();
				textArea.requestFocus();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else {
			// Generally Jfile chooser will not let the user escape the
			// file it is compulsory to choose in jfile chooser
			JOptionPane.showMessageDialog(frame, "File Not Selected ");
		}
	}

}
