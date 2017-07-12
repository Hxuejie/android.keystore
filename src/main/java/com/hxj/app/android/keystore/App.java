package com.hxj.app.android.keystore;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.hxj.app.android.keystore.ui.MainFrame;

/**
 * Hello world!
 * 
 */
public class App {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
//		KeyStore keyStore = new KeyStore("F:\\daer\\sulian.android.keystore.jks");
//		keyStore.read("sulian170519");

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new MainFrame().setVisible(true);
	}
}
